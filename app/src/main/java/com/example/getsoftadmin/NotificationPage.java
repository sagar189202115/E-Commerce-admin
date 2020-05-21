package com.example.getsoftadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private RecyclerView recyclerView;
private NotificationAdapter notificationAdapter;
private List<NotificationDetail> notificationList;
    private NavigationView nab;
    private DrawerLayout drawer;
    private DatabaseReference db= FirebaseDatabase.getInstance().getReference("Admins");
    private final Query query = db.child("Notifications").child("GameRequest");
   private Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);
        FirebaseDatabase.getInstance().getReference("Request").removeValue();
       tb=findViewById(R.id.toolbar);
        nab =findViewById(R.id.navbtn);
        drawer=findViewById(R.id.drawer);
        recyclerView=findViewById(R.id.notificationrecycler);
        notificationList=new ArrayList<>();
        notificationAdapter=new NotificationAdapter(getApplicationContext(),notificationList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.addItemDecoration(new NotificationPage.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        ActionBarDrawerToggle at=new ActionBarDrawerToggle(this,drawer,tb,R.string.open,R.string.close);
        at.syncState();
        drawer.addDrawerListener(at);
        nab.setNavigationItemSelectedListener(this);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(notificationList!=null)
                {
                    notificationList.clear();
                }

                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    notificationList.add(ds.getValue(NotificationDetail.class));
                }
                recyclerView.setAdapter(notificationAdapter);
                notificationAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
}
