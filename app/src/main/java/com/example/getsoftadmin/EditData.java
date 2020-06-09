package com.example.getsoftadmin;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditData {

    static FirebaseStorage img= FirebaseStorage.getInstance();
    static final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Game");


    static void deleteKey(final String id, final Context c, final String imag)
    {

        databaseReference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                StorageReference photoref=img.getReference("game_images").child(id+"/"+imag);

                Toast.makeText(c, ""+photoref, Toast.LENGTH_SHORT).show();
                photoref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(c, "imagedeleted", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(c, "not deleted "+e, Toast.LENGTH_SHORT).show();

                    }
                });
                img.getReference("game_images_thumb").child(id+"/"+imag).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(c,"deleted", Toast.LENGTH_SHORT).show();
                    }
                });

            }




        });


    }
    public static void converttourl(final String id, final Context c, final String imag){
            StorageReference photoref=img.getReference("game_images_thumb").child(id+"/"+imag);
            photoref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    databaseReference.child(id).child("thumbnail").setValue(""+uri);
                }
            });


    }
}
