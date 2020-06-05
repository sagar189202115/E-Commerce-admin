package com.example.getsoftadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {
    EditText otp;
    FloatingActionButton verifyotp;
    String code;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    String num="";
    int check;
    TextView resendotp;
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        otp=findViewById(R.id.otp);
        resendotp=findViewById(R.id.resendotp);
        num=getIntent().getStringExtra("num");
        if(!(num.equals(""))){
            sendOtp("+91"+num);
        }
        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            sendOtp("+91"+num);

            }
        });
        mAuth = FirebaseAuth.getInstance();
        verifyotp=findViewById(R.id.verifyotp);
        database=FirebaseDatabase.getInstance();
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyotp.startAnimation(new AlphaAnimation(1f,0.7f));

                String otp1 = otp.getText().toString().trim();
                if("".equals(otp1)||otp1.length()!=6){
                    Toast.makeText(VerifyPhone.this, "OTP must be Six digit", Toast.LENGTH_SHORT).show();
                }
                else{
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(code,otp1);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });



    }





    private void sendOtp(String mobileNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        otp.setText(phoneAuthCredential.getSmsCode());
                        Toast.makeText(VerifyPhone.this, "verification completed", Toast.LENGTH_SHORT).show();
                        check();

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(VerifyPhone.this, "Verification failed"+e, Toast.LENGTH_LONG).show();
                        
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        Toast.makeText(VerifyPhone.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                        code = s;
                    }
                });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //do something when task successful
                            Toast.makeText(VerifyPhone.this, "otp verified", Toast.LENGTH_SHORT).show();
                            verifyotp.setVisibility(View.GONE);
                            otp.setVisibility(View.GONE);
                            check();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyPhone.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void check() {


        DatabaseReference data = database.getReference("Admins/Users");
        Query query = data.orderByChild("Mobile").equalTo(num);
        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    model = ds.getValue(Model.class);
                }
                if (dataSnapshot.exists()) {
                    if (num.equals(model.getMobile())) {
                        UserSession userSession = new UserSession(VerifyPhone.this);
                        userSession.setData(model.getMobile(),model.getName(),model.getId());
                        finish();
                        startActivity(new Intent(VerifyPhone.this, Homepage.class));
                    }
                }
                else
                {
                    Intent intent=new Intent(VerifyPhone.this,RegisterUser.class);
                    intent.putExtra("number",num);
                    finish();
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VerifyPhone.this, "canceled"+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exit_builder;
        exit_builder=new AlertDialog.Builder(VerifyPhone.this);
        exit_builder.setTitle("Exit!");
        exit_builder.setMessage("Do You Really Want To Exit");
        exit_builder.setCancelable(true);
        exit_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();

            }
        });
        exit_builder.setNegativeButton("No",null);
        exit_builder.create().show();
    }
    //    public void login(View view) {
//        finish();
//        startActivity(new Intent(getApplicationContext(),UserLogin.class));
//    }
}
