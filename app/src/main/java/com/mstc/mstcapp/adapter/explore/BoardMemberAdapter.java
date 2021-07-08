package com.mstc.mstcapp.adapter.explore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mstc.mstcapp.R;
import com.mstc.mstcapp.model.explore.BoardMemberModel;
import com.mstc.mstcapp.util.Functions;

import java.util.List;

public class BoardMemberAdapter extends RecyclerView.Adapter<BoardMemberAdapter.BoardViewHolder> {

    private final Context context;
    private List<BoardMemberModel> list;

    public BoardMemberAdapter(Context context, List<BoardMemberModel> items) {
        this.context = context;
        list = items;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board_member, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BoardViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.position.setText(list.get(position).getPosition());
        holder.phrase.setText(list.get(position).getPhrase());

        new Thread(() -> holder.photo.post(() -> {
            String pic = list.get(position).getPhoto();
            try {
                byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                Bitmap picture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.photo.setImageBitmap(picture);
            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();

        if (position % 3 == 0)
            holder.photo.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTertiaryRed));
        else if (position % 3 == 1)
            holder.photo.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTertiaryBlue));
        else
            holder.photo.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTertiaryYellow));
        holder.mView.setOnClickListener(v -> Functions.openURL(v, context, list.get(position).getLink()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<BoardMemberModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {
        public final ImageView photo;
        public final TextView name;
        public final TextView position;
        public final TextView phrase;
        public View mView;

        public BoardViewHolder(View view) {
            super(view);
            mView = view;
            photo = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            position = view.findViewById(R.id.position);
            phrase = view.findViewById(R.id.phrase);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + position.getText() + "'";
        }
    }
}