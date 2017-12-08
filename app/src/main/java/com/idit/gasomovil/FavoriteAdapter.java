package com.idit.gasomovil;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by viper on 07/12/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{

    private List<FavoriteModel> list;

    public FavoriteAdapter(List<FavoriteModel> list) {
        this.list = list;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(final FavoriteViewHolder holder, int position) {

        FavoriteModel favorite = list.get(position);

        holder.textName.setText(favorite.name);
        holder.textPremium.setText(favorite.prices.get("premium").toString());
        holder.textMagna.setText(favorite.prices.get("magna").toString());
        holder.textDiesel.setText(favorite.prices.get("diesel").toString());
        holder.textScore.setRating(favorite.score);

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(holder.getAdapterPosition(), 0, 0, "Información");
                contextMenu.add(holder.getAdapterPosition(), 1, 0, "Eliminar");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder{

        TextView textName, textPremium, textMagna, textDiesel;
        RatingBar textScore;

        public FavoriteViewHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.text_name);
            textPremium = itemView.findViewById(R.id.premium_price_station);
            textMagna = itemView.findViewById(R.id.magna_price_station);
            textDiesel = itemView.findViewById(R.id.diesel_price_station);
            textScore = itemView.findViewById(R.id.myRatingBar_favorite);
        }
    }
}

