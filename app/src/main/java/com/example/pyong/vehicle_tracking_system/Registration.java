package com.example.pyong.vehicle_tracking_system;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.concurrent.TimeUnit;



public class Registration extends AppCompatActivity {
    EditText phoneEditText, codeEditText;
    Button verifyButton;
    TextView tryAgainTextView;
    DatabaseReference myDataBaseRef;
    ProgressBar progressBar;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public static FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private int btntype =0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        //check if current user is logged
        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(Registration.this, Vehicle_registration.class);
            startActivity(intent);
            finish();
        }



        setContentView(R.layout.activity_registration);
        Firebase.setAndroidContext(this);
        phoneEditText = (EditText) findViewById(R.id.editText_phone);
        codeEditText = (EditText) findViewById(R.id.editText_code);
        verifyButton = (Button) findViewById(R.id.button_verify);
        tryAgainTextView = (TextView) findViewById(R.id.textView_tryAgain);
        myDataBaseRef = FirebaseDatabase.getInstance().getReference();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        tryAgainTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeEditText.setVisibility(View.INVISIBLE);
                tryAgainTextView.setVisibility(View.INVISIBLE);
                phoneEditText.setEnabled(true);
                verifyButton.setEnabled(true);
                verifyButton.setText("Verify");
                btntype = 0;

            }
        });


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btntype == 0){
                    phoneEditText.setEnabled(false);
                    verifyButton.setEnabled(false);

                    String phoneNumber = phoneEditText.getText().toString();

                    

                    //check phone number
                    if (phoneNumber.isEmpty()){
                        Toast.makeText(Registration.this, "Phone Number field cannot be empty", Toast.LENGTH_SHORT).show();
                        phoneEditText.setEnabled(true);
                        verifyButton.setEnabled(true);
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                Registration.this,               // Activity (for callback binding)
                                mCallbacks);        // OnVerificationStateChangedCallbacks

                    }

                }else{
                    verifyButton.setEnabled(false);
                    codeEditText.setVisibility(View.VISIBLE);
                    tryAgainTextView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    String verificationCode = codeEditText.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Registration.this, "There were some errors please verify your phone number or internet connection", Toast.LENGTH_LONG).show();
                phoneEditText.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
                verifyButton.setEnabled(true);

            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                btntype = 1; //change the button functionality
                verifyButton.setText("Verify code");
                codeEditText.setVisibility(View.VISIBLE);
                tryAgainTextView.setVisibility(View.VISIBLE);
                verifyButton.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);

                // ...
            }
        };

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(Registration.this,Vehicle_registration.class);
                            startActivity(intent);
                            finish();

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(Registration.this, "Wrong Verification code", Toast.LENGTH_SHORT).show();
                            verifyButton.setEnabled(false);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
