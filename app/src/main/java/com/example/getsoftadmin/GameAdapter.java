package com.example.getsoftadmin;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    public GameAdapter(Context context, ArrayList<GameDetails> gameDetails) {
        this.context = context;
        this.gameDetails = gameDetails;
    }
    Context context;

    ArrayList<GameDetails> gameDetails;
    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.productview,null,true);
        GameAdapter.ViewHolder rv=new GameAdapter.ViewHolder(view);

        return rv;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final GameDetails game=gameDetails.get(position);
        holder.title.setText(game.getName());
        holder.price.setText(game.getPrice());
        holder.desc.setText(game.getDesc());
        holder.time.setText(game.getTime());
        holder.date.setText(game.getDate());
        Glide.with(context).load(game.getImgurl()).into(holder.image);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+game.getTags(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.optionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,holder.optionButton);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id=item.getItemId();
                        switch(id){
                            case R.id.about:
                                Toast.makeText(context, "about", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.setting:
                                Toast.makeText(context, "setting", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.delete:
                                DeleteData.deleteKey(game.getId(),context,game.getI());
                                break;
                            default:
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });



//                        if (sr.toString().equals(demo)) {

    }

    @Override
    public int getItemCount() {
        return gameDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,date,time;
        TextView price;
        TextView desc;
        ImageView image;
        MaterialCardView card;
        ImageButton optionButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.gametitle);
            time=itemView.findViewById(R.id.time);
            date=itemView.findViewById(R.id.date);
            price=itemView.findViewById(R.id.gameprice);
            desc=itemView.findViewById(R.id.gamedesc);
            image=itemView.findViewById(R.id.gameimg);
            card=itemView.findViewById(R.id.card);
            optionButton=itemView.findViewById(R.id.optionmenu);
        }
    }
}
