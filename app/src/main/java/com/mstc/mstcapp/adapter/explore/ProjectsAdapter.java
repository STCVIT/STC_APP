package com.mstc.mstcapp.adapter.explore;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mstc.mstcapp.R;
import com.mstc.mstcapp.model.explore.ProjectsModel;
import com.mstc.mstcapp.util.Functions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<ProjectsModel> list;
    private Context context;

    public ProjectsAdapter(Context context, List<ProjectsModel> items) {
        this.context = context;
        list = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
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

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null)
            return VIEW_TYPE_LOADING;
        else
            return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void populateItemRows(@NotNull ItemViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getDescription());
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
            holder.image.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTertiaryRed));
        else if (position % 3 == 1)
            holder.image.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTertiaryBlue));
        else
            holder.image.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTertiaryYellow));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ProjectsModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView image;
        public final TextView title;
        public final TextView description;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.details);
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