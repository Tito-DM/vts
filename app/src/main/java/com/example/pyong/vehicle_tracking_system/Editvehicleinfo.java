package com.example.pyong.vehicle_tracking_system;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.pyong.vehicle_tracking_system.Registration.mAuth;

public class Editvehicleinfo extends AppCompatActivity {
    EditText editTextNameEdit, editTextPhoneEdit, editTextManufacturerEdit,
            editTextPlateNumberEdit, editTextColourEdit, editTextModelEdit;
    Button updateDataBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editvehicleinfo);

        //get user Data
        editTextNameEdit = (EditText) findViewById(R.id.editText_nameEdit );
        editTextPhoneEdit  = (EditText) findViewById(R.id.editText_hdwPhoneEdit);
        editTextManufacturerEdit  = (EditText) findViewById(R.id.editText_vehicle_manufacturerEdit );
        editTextPlateNumberEdit  = (EditText) findViewById(R.id.editText_plate_numberEdit );
        editTextColourEdit  = (EditText) findViewById(R.id.editText_colorEdit);
        editTextModelEdit  = (EditText) findViewById(R.id.editText_modelEdit );
        updateDataBtn  = (Button) findViewById(R.id.button_update);
        Firebase.setAndroidContext(this);
        updateDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                    try {
                        ref.child("Users").child(userId).child("name").setValue(editTextNameEdit.toString());
                        ref.child("Users").child(userId).child("colour").setValue(editTextColourEdit.toString());
                        ref.child("Users").child(userId).child("model").setValue(editTextModelEdit.toString());
                        ref.child("Users").child(userId).child("hdw_phone").setValue(editTextPhoneEdit.toString());
                        ref.child("Users").child(userId).child("plate_number").setValue(editTextPlateNumberEdit.toString());
                        ref.child("Users").child(userId).child("vehicle_manufacture").setValue(editTextManufacturerEdit.toString());
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            }
        });



    }
}
