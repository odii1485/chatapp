package com.odii.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {
    CountryCodePicker mcountryCodePicker;
    EditText mnumber;
    android.widget.Button SendButton;
    String countrycode, phonenumber;
    FirebaseAuth firebaseAuth;


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    String codesent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcountryCodePicker = findViewById(R.id.countryCodeHolder);
        mnumber = findViewById(R.id.number);
        SendButton = findViewById(R.id.sendbutton);
        firebaseAuth = FirebaseAuth.getInstance();
        countrycode = mcountryCodePicker.getSelectedCountryCodeWithPlus();
        mcountryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode = mcountryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number;
                number = mnumber.getText().toString();
                if (number.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter phone number", Toast.LENGTH_SHORT).show();

                } else if (number.length() < 10) {
                    Toast.makeText(getApplicationContext(), "please enter valid number", Toast.LENGTH_SHORT).show();
                } else {
                    phonenumber = countrycode + number;
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                            .setPhoneNumber(phonenumber)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mcallbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });
        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(), "Code sent", Toast.LENGTH_SHORT).show();
                codesent = s;
                Intent intent = new Intent(MainActivity.this, otp_authetication.class);
                intent.putExtra("otp", codesent);
                startActivity(intent);
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, chatactivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}