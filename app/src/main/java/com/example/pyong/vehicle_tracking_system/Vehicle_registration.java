package com.example.pyong.vehicle_tracking_system;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.pyong.vehicle_tracking_system.Registration.mAuth;
import static com.example.pyong.vehicle_tracking_system.SmsReceiver.latitude;
import static com.example.pyong.vehicle_tracking_system.SmsReceiver.longitude;


public class Vehicle_registration extends AppCompatActivity {
    EditText editTextName, editTextPhone, editTextManufacturer,
            editTextPlateNumber, editTextColour, editTextModel;
    Button saveDataBtn;
    ArrayList<String> userDataList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration);

        SharedPreferences pref = getSharedPreferences("", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){
            Intent intent = new Intent(this, userprofile.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }


        try {
            Firebase.setAndroidContext(this);
            //get user Data
            getUserInfo();

            if (userDataList.size() >= 8)
            {

                if (!(userDataList.get(0).isEmpty()) && mAuth.getCurrentUser() != null){

                    Intent intent = new Intent(Vehicle_registration.this, userprofile.class);
                    startActivity(intent);
                    finish();

                }
            }



            editTextName = (EditText) findViewById(R.id.editText_name);
            editTextPhone = (EditText) findViewById(R.id.editText_hdwPhone);
            editTextManufacturer = (EditText) findViewById(R.id.editText_vehicle_manufacturer);
            editTextPlateNumber = (EditText) findViewById(R.id.editText_plate_number);
            editTextColour = (EditText) findViewById(R.id.editText_color);
            editTextModel = (EditText) findViewById(R.id.editText_model);
            saveDataBtn = (Button) findViewById(R.id.button_save);

            saveDataBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //check if user is logged in
                    mAuth = FirebaseAuth.getInstance();
                    if (mAuth.getCurrentUser() != null){
                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                        Map  newPost = new HashMap();
                        String name = editTextName.getText().toString();
                        String phone = editTextPhone.getText().toString();
                        String manufacture = editTextManufacturer.getText().toString();
                        String plate_number = editTextPlateNumber.getText().toString();
                        String colour = editTextColour.getText().toString();
                        String model = editTextModel.getText().toString();

                        //check each field
                        if (name.isEmpty()){
                            Toast.makeText(Vehicle_registration.this, "Name field cannot be empty", Toast.LENGTH_SHORT).show();

                        }else if (phone.isEmpty()){
                            Toast.makeText(Vehicle_registration.this, "Phone number field cannot be empty", Toast.LENGTH_SHORT).show();
                        }else if (manufacture.isEmpty()){
                            Toast.makeText(Vehicle_registration.this, "manufacture field cannot be empty", Toast.LENGTH_SHORT).show();
                        }else if (plate_number.isEmpty()){
                            Toast.makeText(Vehicle_registration.this, "plate mumber field cannot be empty", Toast.LENGTH_SHORT).show();
                        }else if (colour.isEmpty()){
                            Toast.makeText(Vehicle_registration.this, "color field cannot be empty", Toast.LENGTH_SHORT).show();
                        }else if (model.isEmpty()){
                            Toast.makeText(Vehicle_registration.this, "model field cannot be empty", Toast.LENGTH_SHORT).show();
                        }else{
                            //save info to a hash
                            newPost.put("name", name);
                            newPost.put("hdw_phone", phone);
                            newPost.put("vehicle_manufacture", manufacture);
                            newPost.put("plate_number", plate_number);
                            newPost.put("colour", colour);
                            newPost.put("model", model);
                            newPost.put("latitude", "");
                            newPost.put("longitude", "");


                            //save to the database
                            current_user_db.setValue(newPost);
                            Toast.makeText(Vehicle_registration.this, "Data Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Vehicle_registration.this, userprofile.class);
                            startActivity(intent);
                            finish();

                        }

                    }

                }
            });



        } catch (Exception e) {

            e.printStackTrace();
        }






    }


    private void getUserInfo() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String userData = dataSnapshot.getValue(String.class);
                    userDataList.add(userData);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }





}
