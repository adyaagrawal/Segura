package com.example.segura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signin extends AppCompatActivity {
    TextView signup;
    EditText email,pass;
    Button signin;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signup=findViewById(R.id.textView2);
        email=findViewById(R.id.editTextTextPersonName2);
        pass=findViewById(R.id.editTextTextPersonName3);
        signin=findViewById(R.id.button);
        firebaseAuth=FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signin.this,Signup.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emails=email.getText().toString().trim();
                final String passs=pass.getText().toString().trim();

                if(emails.isEmpty()){
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emails).matches()){
                    email.setError("Enter valid email");
                    email.requestFocus();
                    return;
                }
                if(passs.isEmpty()){
                    pass.setError("Email is required");
                    pass.requestFocus();
                    return;
                }
                if(passs.length()<6){
                    pass.setError("Password length should be a minimum of 6");
                    pass.requestFocus();
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(emails,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Signin.this,"Sign in successful",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Signin.this,Home.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(Signin.this,"Failed to log in. Please check your credentials.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}