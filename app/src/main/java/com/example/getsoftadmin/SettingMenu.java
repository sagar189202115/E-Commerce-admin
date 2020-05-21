package com.example.getsoftadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class SettingMenu extends AppCompatActivity {
Button genreadd;
EditText genrename;
DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("GameGenre").child("List");
List<String> listgenre;
    ListView genrelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_menu);
        listgenre=new ArrayList<>();
        genreadd=findViewById(R.id.addgenre);
        genrename=findViewById(R.id.genrename);
        genrelist=findViewById(R.id.genrelist);
        genreadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String name=genrename.getText().toString();
                if(!(name.equals(""))){
                    Query query=databaseReference;
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> genreid=new ArrayList<>();
                            ArrayList<String> genrel=new ArrayList<>();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                genreid.add(ds.getKey());
                                genrel.add(""+ds.getValue());
                            }
                            if(!(genrel.contains(name))){
                                int j;
                                for (j = 0; TRUE; j++) {
                                    if (!(genreid.contains(String.valueOf(j)))) {

                                        break;
                                    }
                                }
                                 insert(j);
                            }
                            else
                            {
                                genrename.setError("Already Exist");
                            }

                        }

                        private void insert(int j) {
                            databaseReference.child(""+j).setValue(name);
                            Toast.makeText(SettingMenu.this, "Added", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        Query query=databaseReference;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(!(listgenre.isEmpty())){
                    listgenre.clear();
                }
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(!(ds.getValue().equals("Select")))
                        listgenre.add(""+ds.getValue());
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(SettingMenu.this,R.layout.support_simple_spinner_dropdown_item,listgenre);
                    genrelist.setAdapter(arrayAdapter);
                    genrelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                            Query q= databaseReference.orderByValue().equalTo((String)genrelist.getItemAtPosition(position));
                            q.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                    }
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                        ds.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SettingMenu.this, "removed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            return true;
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
