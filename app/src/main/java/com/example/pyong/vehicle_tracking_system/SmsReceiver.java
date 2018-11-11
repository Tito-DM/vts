package com.example.pyong.vehicle_tracking_system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.pyong.vehicle_tracking_system.Registration.mAuth;

public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();
    public static String hdwMessage = "";
    public static String latitude = "", longitude = "";

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            // Retrieve the SMS Messages received

            Object[] sms = (Object[]) bundle.get("pdus");

            // For every SMS message received
            for (int i = 0; i < sms.length; i++) {
                // Convert Object array
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String phone = smsMessage.getOriginatingAddress();
                hdwMessage = smsMessage.getMessageBody().toString();
                try {
                    Toast.makeText(context, phone + ": " + hdwMessage, Toast.LENGTH_SHORT).show();
                    //check the phone number

                    if (phone == "myphone"){
                        String []coordinates = hdwMessage.split(",");
                        latitude = coordinates[0];
                        longitude = coordinates[1];
                    }

                }
                catch (Exception e){
                    Toast.makeText(context, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }


                mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


                    try {
                        ref.child("Users").child(userId).child("latitude").setValue(latitude);
                        ref.child("Users").child(userId).child("longitude").setValue(longitude);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Data saved", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}

