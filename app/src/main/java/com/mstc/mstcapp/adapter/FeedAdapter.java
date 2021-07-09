package com.mstc.mstcapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mstc.mstcapp.R;
import com.mstc.mstcapp.model.FeedModel;
import com.mstc.mstcapp.util.Functions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<FeedModel> list;

    public FeedAdapter(Context context, List<FeedModel> items) {
        list = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
            return new ItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder)
            populateItemRows((ItemViewHolder) holder, position);
    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getLink());
        new Thread(() -> holder.image.post(() -> {
            try {
                byte[] decodedString = Base64.decode(list.get(position).getImage(), Base64.DEFAULT);
                Bitmap picture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.image.setImageBitmap(picture);
            } catch (Exception e) {
                holder.image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_error));
                e.printStackTrace();
            }
        })).start();
        holder.mView.setOnClickListener(v -> Functions.openURL(v, context, list.get(position).getLink()));
        if (position % 3 == 0)
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorTertiaryBlue));
        else if (position % 3 == 1)
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorTertiaryRed));
        else
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorTertiaryYellow));
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null)
            return VIEW_TYPE_LOADING;
        else
            return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<FeedModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title;
        public final TextView description;
        public final ImageView image;
        public final CardView cardView;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            image = view.findViewById(R.id.image);
            cardView = view.findViewById(R.id.cardView);
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}