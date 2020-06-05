package com.example.getsoftadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {
    EditText name;
    FloatingActionButton signin;
    String usernum;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        database=FirebaseDatabase.getInstance();
        usernum=getIntent().getStringExtra("number");
        name=findViewById(R.id.name);
        signin=findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username=name.getText().toString();
                if(!username.equals("")){
                    insert(username);
                }
            }
        });
    }
    private void insert(String user_name)
    {
        DatabaseReference new_data = database.getReference("Admins/Users").push();
        new_data.child("Name").setValue(user_name);
        new_data.child("Mobile").setValue(usernum);
        new_data.child("id").setValue(new_data.getKey());
        UserSession userSession = new UserSession(RegisterUser.this);
        userSession.setData(usernum, user_name,new_data.getKey());
        finish();
        startActivity(new Intent(RegisterUser.this, Homepage.class));

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exit_builder;
        exit_builder=new AlertDialog.Builder(RegisterUser.this);
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
