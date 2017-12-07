package com.idit.gasomovil;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by viper on 06/12/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HisyoryViewHolder>{

    private List<HistoryModel> list;

    public HistoryAdapter(List<HistoryModel> list) {
        this.list = list;
    }

    @Override
    public HisyoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HisyoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final HisyoryViewHolder holder, int position) {

        HistoryModel history = list.get(position);

        holder.textName.setText(history.name);
        holder.textDate.setText(String.valueOf(history.timestamp));
        holder.textPrice.setText(String.valueOf(history.price));
        holder.textScore.setRating(history.score);

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(holder.getAdapterPosition(), 0, 0, "Eliminar");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HisyoryViewHolder extends RecyclerView.ViewHolder{

        TextView textName, textDate, textPrice;
        RatingBar textScore;

        public HisyoryViewHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.text_name);
            textDate = itemView.findViewById(R.id.text_date);
            textPrice = itemView.findViewById(R.id.text_price);
            textScore = itemView.findViewById(R.id.myRatingBar_history);
        }
    }
}
