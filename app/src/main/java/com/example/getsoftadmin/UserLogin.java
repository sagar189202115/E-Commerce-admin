package com.example.getsoftadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class UserLogin extends AppCompatActivity {
    EditText unumber,upass;
    FloatingActionButton login;
    FirebaseDatabase database;
    String code="";
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        unumber=findViewById(R.id.phone);
        userSession=new UserSession(this);
        login=findViewById(R.id.login);
        database=FirebaseDatabase.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(UserLogin.this,VerifyPhone.class);
                    intent.putExtra("num",unumber.getText().toString());
                    finish();
                    startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exit_builder;
        exit_builder=new AlertDialog.Builder(UserLogin.this);
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
}
