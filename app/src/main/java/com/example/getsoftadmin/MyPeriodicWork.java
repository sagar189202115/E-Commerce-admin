package com.example.getsoftadmin;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import static com.example.getsoftadmin.App.CHANNEL_1_ID;
import static java.lang.Boolean.TRUE;

public class MyPeriodicWork extends Worker {
    private NotificationManagerCompat notificationManagerCompat;
    Notification builder;
    public MyPeriodicWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        DatabaseReference data= FirebaseDatabase.getInstance().getReference("Request");
        Query q=data.orderByChild("Name");
        notificationManagerCompat= NotificationManagerCompat.from(getApplicationContext());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {


                    Intent i = new Intent(getApplicationContext(), NotificationPage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_IMMUTABLE);

//                    builder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_1_ID)
//                           .setContentTitle("Notificaiton")
//                           .setContentText(ds.getValue().toString())
//                           .setSmallIcon(R.drawable.ic_games)
//                           .setContentIntent(pendingIntent)
//                           .build();
                    builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                            .setContentTitle(ds.child("RequestedUser").getValue() + " requested " + ds.child("Name").getValue())

                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build();
                    notificationManagerCompat.notify(1, builder);
                    ds.getRef().removeValue();
                }

//                    notificationManagerCompat.notify(1,builder);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), ""+databaseError, Toast.LENGTH_SHORT).show();

            }
        });
        return null;
    }


}
