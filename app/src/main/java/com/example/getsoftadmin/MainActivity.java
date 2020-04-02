package com.example.getsoftadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.TimeAnimator;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.icu.text.TimeZoneNames;
import android.icu.util.TimeUnit;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.OpenableColumns;
import android.text.format.Time;
import android.text.method.DateTimeKeyListener;
import android.text.method.TimeKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.lang.Boolean.TRUE;
import static java.time.ZoneOffset.UTC;

public class MainActivity extends AppCompatActivity {
TextView text;
List list;
    Uri uri;
    StorageReference mStorageRef;
    ImageView imageView;
    ProgressBar progressBar;
    UploadTask uploadTask;
    String imgname;
    EditText gname,gprice,gdesc;
    DatabaseReference data;
    SimpleDateFormat time=new SimpleDateFormat("hh:mm a");
    SimpleDateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy");
    Date today;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imageView = findViewById(R.id.image);
        progressBar=findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.INVISIBLE);
        gname=findViewById(R.id.gname);
        gprice=findViewById(R.id.gprice);
        gdesc=findViewById(R.id.gdesc);
        list=new ArrayList();
        Button btn=findViewById(R.id.submit);
        time.setTimeZone(TimeZone.getDefault());
        dateformat.setTimeZone(TimeZone.getDefault());


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageUpload();
            }
        });


    }

    private void imageUpload() {
        String url=""+uri;
        if (!(gname.getText().toString().equals("") || gdesc.getText().toString().equals("") || gprice.getText().toString().equals(""))) {
            if (!(url.equals("null"))) {
                StorageReference riversRef = mStorageRef.child("game_images/"+imgname);
                uploadTask = riversRef.putFile(uri);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageURI(null);
                            Query q = FirebaseDatabase.getInstance().getReference().child("Game");

                            q.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        list.add(ds.getKey());
                                    }
                                    int j;
                                    for (j = 1; TRUE; j++) {
                                        if (!(list.contains(String.valueOf(j)))) {
                                            break;
                                        }
                                    }
                                    insertData(j);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }


                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Upload Failed" + e, Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        int e = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(e);


                    }
                });
            } else {
                Toast.makeText(this, "please select image", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Every field is madatory", Toast.LENGTH_SHORT).show();
        }


    }

    private void insertData(final int id) {
        mStorageRef.child("game_images/"+imgname).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                today=Calendar.getInstance().getTime();
                data=FirebaseDatabase.getInstance().getReference("Game").child(String.valueOf(id));

                data.child("Name").setValue(gname.getText().toString());
                data.child("id").setValue(""+id);
                data.child("price").setValue(gprice.getText().toString());
                data.child("img").setValue(uri.toString());
                data.child("desc").setValue(gdesc.getText().toString());
                data.child("time").setValue(time.format(today));
                data.child("date").setValue(dateformat.format(today));
                data.child("desc").setValue(gdesc.getText().toString());
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(MainActivity.this, "canceled", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed "+e, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //image
    public void select(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 101);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            //to get image name when selecting from gallery from here
            Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
            returnCursor.moveToFirst();
            int nameIndex =returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            //to here
            imageView.setImageURI(uri);
            imgname=returnCursor.getString(nameIndex);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
