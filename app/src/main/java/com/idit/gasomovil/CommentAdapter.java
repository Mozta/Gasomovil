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
 * Created by viper on 11/12/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{

    private List<CommentModel> list;

    public CommentAdapter(List<CommentModel> list) {
        this.list = list;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {

        CommentModel commentModel = list.get(position);

        holder.textName.setText(commentModel.key);
        holder.textComment.setText(commentModel.comment);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView textName, textComment;
        RatingBar textScore;

        public CommentViewHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.comment_username);
            textComment = itemView.findViewById(R.id.comment);
        }
    }
}
