package com.example.pyong.vehicle_tracking_system;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import android.Manifest;

import static com.example.pyong.vehicle_tracking_system.Registration.mAuth;

public class userprofile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView textviewName, textviewtPhone, textviewManufacturer,
            textviewPlateNumber, textviewColour, textviewModel;
    Button connect_btn;
    ArrayList<String> userDataList = new ArrayList<>();
    private final static int REQUEST_CODE_PERMISSION_SEND_SMS =123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //get user Data
        getUserInfo();
        getuserData();

        //sendsms

        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.userprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit) {
            // Handle the camera action
            Intent intent = new Intent(userprofile.this, Editvehicleinfo.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_share) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                    if (userDataList.size() >= 6) {
                        getuserData();
                        textviewColour.setText(String.valueOf(userDataList.get(0)));
                        textviewtPhone.setText(String.valueOf(userDataList.get(1)));
                        textviewModel.setText(String.valueOf(userDataList.get(2)));
                        textviewName.setText(String.valueOf(userDataList.get(3)));
                        textviewPlateNumber.setText(String.valueOf(userDataList.get(4)));
                        textviewManufacturer.setText(String.valueOf(userDataList.get(5)));
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


    private void getuserData() {
        textviewName = (TextView) findViewById(R.id.textViewgetName);
        textviewColour = (TextView) findViewById(R.id.textViewgetcolour);
        textviewManufacturer = (TextView) findViewById(R.id.textViewgetmanufacture);
        textviewModel = (TextView) findViewById(R.id.textViewgetmodel);
        textviewtPhone = (TextView) findViewById(R.id.textViewgetphone);
        textviewPlateNumber = (TextView) findViewById(R.id.textViewgetplate);
        connect_btn = (Button) findViewById(R.id.buttom_connect);

    }

    protected void sendSMSMessage() {
        String phone = "+27659943377";
        String message = "Hello world";

        try {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(userprofile.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(userprofile.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission){
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_PERMISSION_SEND_SMS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    connect_btn.setEnabled(true);
                }
                break;
        }
    }


}
