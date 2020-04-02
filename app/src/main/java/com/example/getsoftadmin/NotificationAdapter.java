package com.example.getsoftadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    public NotificationAdapter(Context c,List<NotificationDetail> l) {
        this.c=c;
        this.l=l;
    }
    Context c;
    List<NotificationDetail> l;


    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.notificationview,null,false);
        NotificationAdapter.ViewHolder rv=new NotificationAdapter.ViewHolder(view);
        return rv;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationDetail nd=l.get(position);
        final String requesteduser=nd.getRequestedUser();
        holder.name.setText(nd.getName());

        holder.requesteduser.setText("-by "+nd.getRequestedUser());
        holder.requesteduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, ""+requesteduser, Toast.LENGTH_SHORT).show();
            }
        });
        holder.nc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,requesteduser;
        MaterialCardView nc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.requestedgame);
            requesteduser=itemView.findViewById(R.id.requesteduser);
            nc=itemView.findViewById(R.id.notificationcard);
        }
    }
}
