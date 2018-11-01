package com.example.pyong.vehicle_tracking_system;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.example.pyong.vehicle_tracking_system.Registration.mAuth;

public class Vehicle_registration extends AppCompatActivity {
    EditText editTextName, editTextPhone, editTextManufacturer,
            editTextPlateNumber, editTextColour, editTextModel;
    Button saveDataBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration);
        String name = "";

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(Vehicle_registration.this, userprofile.class);
            startActivity(intent);
            finish();
        }



        //get user Data
        editTextName = (EditText) findViewById(R.id.editText_name);
        editTextPhone = (EditText) findViewById(R.id.editText_hdwPhone);
        editTextManufacturer = (EditText) findViewById(R.id.editText_vehicle_manufacturer);
        editTextPlateNumber = (EditText) findViewById(R.id.editText_plate_number);
        editTextColour = (EditText) findViewById(R.id.editText_color);
        editTextModel = (EditText) findViewById(R.id.editText_model);
        saveDataBtn = (Button) findViewById(R.id.button_save);
        Firebase.setAndroidContext(this);

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

                    //save info to a hash
                    newPost.put("name", name);
                    newPost.put("hdw_phone", phone);
                    newPost.put("vehicle_manufacture", manufacture);
                    newPost.put("plate_number", plate_number);
                    newPost.put("colour", colour);
                    newPost.put("model", model);

                    //save to the database
                    current_user_db.setValue(newPost);
                    Toast.makeText(Vehicle_registration.this, "Data Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Vehicle_registration.this, userprofile.class);
                    startActivity(intent);
                    finish();
                }

            }
        });



    }
}
