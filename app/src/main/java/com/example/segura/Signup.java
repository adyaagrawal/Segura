package com.example.segura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Signup extends AppCompatActivity {
    TextView signin;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    EditText email,pass,phone,name;
    Button signup;
    DatabaseReference det;
    String uid;
    FirebaseUser user;
    String emails;String passs;String names;String phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signin=findViewById(R.id.textView4);
        name=findViewById(R.id.editTextTextPersonName7);
        email=findViewById(R.id.editTextTextPersonName4);
        phone=findViewById(R.id.editTextTextPersonName5);
        pass=findViewById(R.id.editTextTextPersonName6);
        signup=findViewById(R.id.signup);
        firebaseAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        det=database.getReference();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,Signin.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emails=email.getText().toString().trim();
                passs=pass.getText().toString().trim();
                names=name.getText().toString().trim();
                phones=phone.getText().toString().trim();

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
                if(names.isEmpty()){
                    name.setError("Name is required");
                    name.requestFocus();
                    return;
                }
                if(phones.isEmpty()){
                    phone.setError("Phone number is required");
                    phone.requestFocus();
                    return;
                }
                if (!Patterns.PHONE.matcher(phones).matches()){
                    phone.setError("Enter valid phone number");
                    phone.requestFocus();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(emails,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user=firebaseAuth.getCurrentUser();
                            uid=user.getUid();
                            Log.d("signup1","signup done");
                            Toast.makeText(Signup.this,"Sign up successful. Login to app",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this,Signin.class));
                            adddatabase();
                        }
                        else{
                            Toast.makeText(Signup.this,"User registration failed. Try again!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void adddatabase() {
        User user=new User(emails,names,phones);
        database.getReference().child("User").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("signup1","db updated");

            }
        });
    }
}