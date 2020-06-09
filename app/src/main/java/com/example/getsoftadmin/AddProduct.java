package com.example.getsoftadmin;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AddProduct extends AppCompatActivity {
TextView text;
List<String> list;
    Uri uri;
    StorageReference mStorageRef;
    ImageView imageView;
    ProgressBar progressBar;
    UploadTask uploadTask,uploadthumb;
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
        setContentView(R.layout.add_product);
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

                try {
                    imageUpload();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
                clist.setAdapter(new ArrayAdapter<>(AddProduct.this,R.layout.support_simple_spinner_dropdown_item,categorylist));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void addchip(final String a) {
        LayoutInflater inflater=LayoutInflater.from(AddProduct.this);

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

    private void imageUpload() throws FileNotFoundException {
        String url=""+uri;
        InputStream stream=new FileInputStream(new File(compressImage(url)));
        if (!(gname.getText().toString().equals("") || gdesc.getText().toString().equals("") || gprice.getText().toString().equals("")|| checkedcategorylist.isEmpty())) {
            if (!(url.equals("null"))) {
                final String id=""+System.currentTimeMillis();
                StorageReference riversRef = mStorageRef.child("game_images/"+id+"/"+imgname);
                StorageReference rivers = mStorageRef.child("game_images_thumb/"+id+"/"+imgname);
                uploadTask = riversRef.putFile(uri);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(AddProduct.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                            imageView.setImageURI(null);

                            insertData(id);
                        }


                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProduct.this, "Upload Failed" + e, Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        int e = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        Toast.makeText(AddProduct.this, ""+taskSnapshot.getTotalByteCount()*1024.0, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(e);


                    }
                });
                uploadthumb = rivers.putStream(stream);
                uploadthumb.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(AddProduct.this, "Thumbnail uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(AddProduct.this, "Caanceled", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddProduct.this, ""+e, Toast.LENGTH_SHORT).show();
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

    private void insertData(final String id) {


                today=Calendar.getInstance().getTime();
                data=FirebaseDatabase.getInstance().getReference("Game").child(id);

                data.child("Name").setValue(gname.getText().toString());
                data.child("id").setValue(""+id);
                data.child("price").setValue(gprice.getText().toString());
                data.child("thumbnail").setValue(imgname);
                data.child("desc").setValue(gdesc.getText().toString());
                data.child("time").setValue(time.format(today));
                data.child("date").setValue(dateformat.format(today));
                data.child("desc").setValue(gdesc.getText().toString());
                for(String tags:checkedcategorylist){
                    data.child("tags").child(String.valueOf(checkedcategorylist.indexOf(tags))).setValue(tags);
                }
                Toast.makeText(AddProduct.this, "success", Toast.LENGTH_SHORT).show();
                gname.setText("");
                gdesc.setText("");
                gprice.setText("");
                grp.clearCheck();
                databaseReference.child("OptionId").child("LastId").setValue(id);

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
            int size =returnCursor.getColumnIndex(OpenableColumns.SIZE);

            //to here
            returnCursor.moveToFirst();
            String actualsize=returnCursor.getString(size);
            int s= Integer.parseInt( actualsize)/1024;
            Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();

            imageView.setImageURI(uri);
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

    //for compressing image

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    public String getFilename() {
        File file = new File(""+this.getFilesDir(), "thumbnail/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        imgname=System.currentTimeMillis()+".jpg";
        String uriSting = (file.getAbsolutePath() + "/" + imgname);
        return uriSting;

    }
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }
}
