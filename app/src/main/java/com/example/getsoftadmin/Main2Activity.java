package com.example.getsoftadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    private NavigationView nab;
    private DrawerLayout drawer;
    private RecyclerView recyclerView;
    private ArrayList<GameDetails> gamelist;
    private GameAdapter gameAdapter;
    private DatabaseReference db= FirebaseDatabase.getInstance().getReference();
    private final Query query = db.child("Game");
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);
        PeriodicWorkRequest periodicWorkRequest=new PeriodicWorkRequest.Builder(
                MyPeriodicWork.class,15, TimeUnit.MINUTES
        ).build();

        WorkManager.getInstance().enqueue(periodicWorkRequest);
        gamelist=new ArrayList<>();
        recyclerView=findViewById(R.id.gamerecycler);
        gameAdapter=new GameAdapter(getApplicationContext(),gamelist);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.addItemDecoration(new Main2Activity.GridSpacingItemDecoration(2, dpToPx(5), false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(gameAdapter);
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
        toolbar=findViewById(R.id.toolbar);
        nab =findViewById(R.id.nav_view);
        drawer=findViewById(R.id.drawer_layout);
        View g=nab.inflateHeaderView(R.layout.nav_header_main2);
        g.findViewById(R.id.textView);

        ActionBarDrawerToggle at=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        at.syncState();
        drawer.addDrawerListener(at);
        nab.setNavigationItemSelectedListener(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void openNotificationPage(View view) {
        startActivity(new Intent(this,NotificationPage.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

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
                Main2Activity.this.finishAffinity();

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
