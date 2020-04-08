package com.example.getsoftadmin;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteData {

    static FirebaseStorage img= FirebaseStorage.getInstance();


    static void deleteKey(String id, final Context c, final String imag)
    {

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Game");
        databaseReference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                StorageReference photoref=img.getReference("game_images/"+imag);
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

            }




        });


    }
}
