package com.example.getsoftadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {
TextView text;
List<String> list;
    Uri uri;
    StorageReference mStorageRef;
    ImageView imageView;
    ProgressBar progressBar;
    UploadTask uploadTask;
    String imgname;
    EditText gname,gprice,gdesc;
    DatabaseReference data;
    SimpleDateFormat time=new SimpleDateFormat("hh:mm a");
    SimpleDateFormat dateformat=new SimpleDateFormat("dd MMM, yy");
    Date today;
    List<String> checkedcategorylist;
    List<String> categorylist;
    ChipGroup grp;
    Spinner clist;

    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Games");
    Query q =databaseReference.child("OptionId").child("LastId");




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
        categorylist=new ArrayList<>();
        clist=findViewById(R.id.list);
        checkedcategorylist=new ArrayList<>();
        getGenre();

        clist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item=clist.getSelectedItem().toString();
                if(clist.getItemAtPosition(0)!=item&&!(checkedcategorylist.contains(item))&&!(clist.getItemAtPosition(categorylist.size()-1).equals(item))){
                checkedcategorylist.add(item);
                addchip(item);}
                else if(item.equals(clist.getItemAtPosition(categorylist.size()-1))){
                    clist.setSelection(0);
                    startActivity(new Intent(getApplicationContext(),SettingMenu.class));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button btn=findViewById(R.id.submit);
        time.setTimeZone(TimeZone.getDefault());
        dateformat.setTimeZone(TimeZone.getDefault());
        grp=findViewById(R.id.chgrp);





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, ""+checkedcategorylist, Toast.LENGTH_SHORT).show();
//                for(int i=0;i<grp.getChildCount();i++){
//                    View checkedchip=grp.getChildAt(i);
//                    checkedcategorylist.add(checkedchip.);
//                }

                imageUpload();
            }
        });


    }

    private void getGenre() {
        FirebaseDatabase.getInstance().getReference("GameGenre/List").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(categorylist.isEmpty())){
                    categorylist.clear();
                }
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    categorylist.add(ds.getValue().toString());
                }

                categorylist.add("Add More..");
                clist.setAdapter(new ArrayAdapter<>(MainActivity.this,R.layout.support_simple_spinner_dropdown_item,categorylist));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void addchip(final String a) {
        LayoutInflater inflater=LayoutInflater.from(MainActivity.this);

                Chip chip=(Chip)inflater.inflate(R.layout.singlechip,null,false);
                chip.setText(a);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkedcategorylist.remove(a);
                        grp.removeView(v);
                    }
                });
                grp.addView(chip,grp.getChildCount()-1);




//        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("GameGenre");
//        databaseReference.child("List").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds:dataSnapshot.getChildren()){
//                    categorylist.add(ds.getValue().toString());
//                }
//                Toast.makeText(MainActivity.this, ""+categorylist, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void imageUpload() {
        String url=""+uri;
        if (!(gname.getText().toString().equals("") || gdesc.getText().toString().equals("") || gprice.getText().toString().equals("")|| checkedcategorylist.isEmpty())) {
            if (!(url.equals("null"))) {
                StorageReference riversRef = mStorageRef.child("game_images/"+imgname);
                uploadTask = riversRef.putFile(uri);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                            imageView.setImageURI(null);

                            q.addListenerForSingleValueEvent(new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String j;
                                   j=""+dataSnapshot.getValue();


                                    insertData(Integer.parseInt(j)+1);
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
                data.child("imgurl").setValue(uri.toString());
                data.child("i").setValue(imgname);
                data.child("desc").setValue(gdesc.getText().toString());
                data.child("time").setValue(time.format(today));
                data.child("date").setValue(dateformat.format(today));
                data.child("desc").setValue(gdesc.getText().toString());
                for(String tags:checkedcategorylist){
                    data.child("tags").child(String.valueOf(checkedcategorylist.indexOf(tags))).setValue(tags);
                }
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                databaseReference.child("OptionId").child("LastId").setValue(id);
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

    @Override
    protected void onResume() {
        super.onResume();
        getGenre();

    }
}
