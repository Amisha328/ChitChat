package com.amisha.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignInPage extends AppCompatActivity {
    EditText name,phone;
    Button verify;
    FirebaseAuth auth;
    String codeSent,otp;
    ProgressDialog pd;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String key="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
        name=findViewById(R.id.editText2);
        phone=findViewById(R.id.editText3);
        verify=findViewById(R.id.button2);
        auth=FirebaseAuth.getInstance();
        pd=new ProgressDialog(SignInPage.this);

        //remove toolbar
        getSupportActionBar().hide();

       // pref = getSharedPreferences("Username", Context.MODE_PRIVATE);
       // editor = pref.edit();


              verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (name.getText().toString().isEmpty()) {
                     name.setError("Required");
                 }
                 String phonenum = phone.getText().toString();
                 if (phonenum.isEmpty() || phonenum.length() < 10) {
                     phone.setError("Enter a valid mobile");
                     phone.requestFocus();
                     return;
                 }
                 otp = sendOtp();

             }
            });


    }
    //Generate Otp
    public String sendOtp()
    {
        String phoneNumber="+91"+(phone.getText().toString());
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,(Activity)SignInPage.this,mcallBack);
        pd.setMessage("Verifying");
        pd.show();

        return  codeSent;
    }
    //Register Callback for generate Otp
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        //OnVerificationStateChangedCallbacks, which contains implementations of the callback functions that handle the results of the request.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                validateOtp(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(SignInPage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent=s;
        }
    };

    //validate otp

    public void validateOtp(String otp)
    {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(codeSent,otp);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    pd.dismiss();
                    Toast.makeText(SignInPage.this, "Successfully Verified", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(SignInPage.this,MainActivity.class);
                    i.putExtra("name_key",name.getText().toString());
                    startActivity(i);
                    finish();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInPage.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
