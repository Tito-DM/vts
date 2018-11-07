package com.example.pyong.vehicle_tracking_system;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.pyong.vehicle_tracking_system.Registration.mAuth;

public class Editvehicleinfo extends AppCompatActivity {
    EditText editTextNameEdit, editTextPhoneEdit, editTextManufacturerEdit,
            editTextPlateNumberEdit, editTextColourEdit, editTextModelEdit;
    Button updateDataBtn, backBtn;
    ArrayList<String> userDataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editvehicleinfo);
        //get user info
        getUserInfo();

        //get user Data
        getuserData();

        Firebase.setAndroidContext(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Editvehicleinfo.this,userprofile.class);
                startActivity(i);
                finish();

            }
        });

        updateDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


                    try {
                        ref.child("Users").child(userId).child("name").setValue(editTextNameEdit.getText().toString());
                        ref.child("Users").child(userId).child("colour").setValue(editTextColourEdit.getText().toString());
                        ref.child("Users").child(userId).child("model").setValue(editTextModelEdit.getText().toString());
                        ref.child("Users").child(userId).child("hdw_phone").setValue(editTextPhoneEdit.getText().toString());
                        ref.child("Users").child(userId).child("plate_number").setValue(editTextPlateNumberEdit.getText().toString());
                        ref.child("Users").child(userId).child("vehicle_manufacture").setValue(editTextManufacturerEdit.getText().toString());
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                Toast.makeText(Editvehicleinfo.this, "Data Updated", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Editvehicleinfo.this,userprofile.class);
                startActivity(i);
                finish();

            }
        });


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
                    if (userDataList.size() >= 8)
                    {
                        getuserData();
                        editTextColourEdit.setText(String.valueOf(userDataList.get(0)));
                        editTextPhoneEdit.setText(String.valueOf(userDataList.get(1)));
                        editTextModelEdit.setText(String.valueOf(userDataList.get(4)));
                        editTextNameEdit.setText(String.valueOf(userDataList.get(5)));
                        editTextPlateNumberEdit.setText(String.valueOf(userDataList.get(6)));
                        editTextManufacturerEdit.setText(String.valueOf(userDataList.get(7)));
                    }


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

    private void getuserData(){
        editTextNameEdit = (EditText) findViewById(R.id.editText_nameEdit);
        editTextPhoneEdit = (EditText) findViewById(R.id.editText_hdwPhoneEdit);
        editTextManufacturerEdit = (EditText) findViewById(R.id.editText_vehicle_manufacturerEdit);
        editTextPlateNumberEdit = (EditText) findViewById(R.id.editText_plate_numberEdit);
        editTextColourEdit = (EditText) findViewById(R.id.editText_colorEdit);
        editTextModelEdit = (EditText) findViewById(R.id.editText_modelEdit);
        updateDataBtn = (Button) findViewById(R.id.button_update);
        backBtn = (Button) findViewById(R.id.button_back);

    }
}
