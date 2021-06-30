package com.example.segura;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {
    FloatingActionButton button,send;
    FirebaseDatabase mdatabase;
    DatabaseReference dat;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String uid;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Double lat,log;
    FirebaseRecyclerAdapter<contact,ContactViewHolder> Adapter;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    String phonesms,message,city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.nav_home);
        button=findViewById(R.id.floatingActionButton2);
        send=findViewById(R.id.floatingActionButton);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        uid=user.getUid();
        mdatabase=FirebaseDatabase.getInstance();
        dat=mdatabase.getReference("User");
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        } else {
            LocationManager locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission") Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try{
                lat=location.getLatitude();
                log=location.getLongitude();
                city=hereLocation(lat,log);
                Log.d("check",city);

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(Home.this,"Not found",Toast.LENGTH_SHORT).show();
            }
        }

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_loc:
                        Intent intent=new Intent(getApplicationContext(),locActivity.class);
                        intent.putExtra("latitude",lat);
                        intent.putExtra("longitude",log);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_tips:
                        startActivity(new Intent(getApplicationContext(), Tips.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        mdatabase.getReference().child("User").child(uid).child("contact").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> hashMap= (HashMap<String, String>) snapshot.child("0").getValue();
                phonesms=hashMap.get("phone");
                Log.d("check",snapshot.getValue().toString());
                Log.d("check",snapshot.child("0").getValue().toString());
                //Log.d("check",c1.getPhone());
                Log.d("check",hashMap.get("phone"));
                //Log.d("check",c.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,AddContact.class));
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadContacts();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("check","send fn");
                if(city!=null){
                    Log.d("check","city not null");
                    sendSMSMessagePermission();
                }
            }
        });
    }
    private String hereLocation(double lat,double lon) {
        String cityname="";
        String s1="",s2="";

        Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());
        List<Address> adresses;
        try {
            adresses = geocoder.getFromLocation(lat,lon, 1);
            if(adresses.size()>0){
                for(Address adr:adresses){
                    s1=adr.getAddressLine(0);
                    }}}
        catch(IOException e){
            e.printStackTrace();
        }
        return s1;
    }
    private void loadContacts() {
        Adapter=new FirebaseRecyclerAdapter<contact, ContactViewHolder>(contact.class,
                R.layout.contactcard,
                ContactViewHolder.class,
                dat.child(uid).child("contact").orderByKey()
        ) {
            @Override
            protected void populateViewHolder(ContactViewHolder viewholder, contact c, int i) {
                viewholder.name.setText(c.getName());
                viewholder.phone.setText(c.getPhone());
            }
        };
        recyclerView.setAdapter(Adapter);
    }

    protected void sendSMSMessagePermission() {
        Log.d("check","inside send sms permission");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            sendSMSMessage();
        }
    }

    protected void sendSMSMessage() {
        Log.d("check","inside send sms");
        SmsManager smsManager = SmsManager.getDefault();
        message="Help! I am in danger! Here is my location:"+ city;
        smsManager.sendTextMessage(phonesms, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSMessage();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
}