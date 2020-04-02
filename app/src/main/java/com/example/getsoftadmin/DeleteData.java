package com.example.getsoftadmin;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteData {
    static void deleteKey(String id, final Context c)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Game");
        databaseReference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(c, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
