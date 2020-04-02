package com.example.getsoftadmin;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.example.getsoftadmin.App.CHANNEL_1_ID;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private NavigationView nab;
    private DrawerLayout drawer;
    private RecyclerView recyclerView;
    private ArrayList<GameDetails> gamelist;
    private GameAdapter gameAdapter;
    private DatabaseReference db=FirebaseDatabase.getInstance().getReference();
    private final Query query = db.child("Game");
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        PeriodicWorkRequest periodicWorkRequest=new PeriodicWorkRequest.Builder(
                MyPeriodicWork.class,15, TimeUnit.MINUTES
        ).build();

        WorkManager.getInstance().enqueue(periodicWorkRequest);
        toolbar=findViewById(R.id.tb);
        nab =findViewById(R.id.navbtm);
        drawer=findViewById(R.id.drawer);
        gamelist=new ArrayList<>();

//        fab=findViewById(R.id.add_games);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this,MainActivity.class));
//            }
//        });
        recyclerView=findViewById(R.id.gamerecycler);
        final SwipeRefreshLayout swipeRefreshLayout=findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setBackgroundColor(Color.red(R.color.maintheme));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(gamelist!=null)
                        {
                            gamelist.clear();
                        }
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            gamelist.add(ds.getValue(GameDetails.class));

                        }
                        recyclerView.setAdapter(gameAdapter);
                        swipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                });

            }
        });

        gameAdapter=new GameAdapter(getApplicationContext(),gamelist);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(gameAdapter);

        //navigation trigger
        ActionBarDrawerToggle at=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        at.syncState();
        drawer.addDrawerListener(at);
        nab.setNavigationItemSelectedListener(this);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(gamelist!=null)
                {
                    gamelist.clear();
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    gamelist.add(ds.getValue(GameDetails.class));

                }
                gameAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        broadcastReceiver= new ResponseBroadcastReceiver();
//        IntentFilter intentFilter= new IntentFilter();
//        intentFilter.addAction(BackgroundService.ACTION);
//        registerReceiver(broadcastReceiver,intentFilter);
//
//        scheduleAlarm();
        //bg service



    }
//    private void scheduleAlarm() {
//        Intent toastIntent= new Intent(getApplicationContext(),ToastBroadcastReceiver.class);
//        PendingIntent toastAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        long startTime=System.currentTimeMillis(); //alarm starts immediately
//        AlarmManager backupAlarmMgr=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//        backupAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime,500,toastAlarmIntent); // alarm will repeat after every 15 minutes
//    }







    //main




////navbar

@Override
public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    int id=menuItem.getItemId();

    switch(id)
    {
        case R.id.allgames:
            Toast.makeText(this, "Games", Toast.LENGTH_SHORT).show();
            break;
        case R.id.profile:
            Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
            break;
        case R.id.requestgame:
            Toast.makeText(this, "request", Toast.LENGTH_SHORT).show();
            break;


    }

    drawer.closeDrawers();
    return false;
}

    public void openNotificationPage(View view) {
        startActivity(new Intent(this,NotificationPage.class));
    }

    //recycler grid
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includeEdge;
    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }
}
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(2, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exit_builder;
        exit_builder=new AlertDialog.Builder(this);
        exit_builder.setTitle("Exit!");
        exit_builder.setMessage("Do You Really Want To Exit");
        exit_builder.setCancelable(true);
        exit_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HomeActivity.this.finishAffinity();

            }
        });
        exit_builder.setNegativeButton("No",null);
        exit_builder.create().show();

        }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
//        IntentFilter intentFilter= new IntentFilter();
//        intentFilter.addAction(BackgroundService.ACTION);
//        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(broadcastReceiver);
    }


}
