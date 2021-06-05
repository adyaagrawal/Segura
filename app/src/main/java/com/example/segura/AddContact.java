package com.example.segura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddContact extends AppCompatActivity {
    EditText name;
    EditText phone;
    Button button;
    String names,phones,uid;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase mdatabase;
    DatabaseReference det;
    int sizen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        name=findViewById(R.id.editTextTextPersonName);
        phone=findViewById(R.id.editTextTextPersonName2);
        button=findViewById(R.id.button2);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        uid=user.getUid();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names=name.getText().toString().trim();
                phones=phone.getText().toString().trim();
                if(names.isEmpty()){
                    name.setError("Name is required");
                    name.requestFocus();
                    return;
                }
                if(phones.isEmpty()){
                    phone.setError("Phone is required");
                    phone.requestFocus();
                    return;
                }
                database();
            }
        });
    }

    private void database() {
        mdatabase=FirebaseDatabase.getInstance();
        det=mdatabase.getReference();
        contact contact=new contact(names,phones);
        List<contact> list= new ArrayList<contact>();
        list.add(contact);

        mdatabase.getReference().child("User").child(uid).child("contact").setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddContact.this,"Contact added",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddContact.this,Home.class));
            }
        });

    }
}