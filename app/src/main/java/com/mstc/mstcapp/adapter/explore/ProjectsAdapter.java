package com.mstc.mstcapp.adapter.explore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_project, parent, false);

        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
            return new ItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

//        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder)
            populateItemRows((ItemViewHolder) holder, position);
        else if (holder instanceof LoadingViewHolder)
            showLoadingView((LoadingViewHolder) holder, position);
    }


    private void showLoadingView(LoadingViewHolder holder, int position) {

    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getDescription());
        new Thread(() -> holder.image.post(() -> {
            String pic = list.get(position).getImage();
            try {
                byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                Bitmap picture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.image.setImageBitmap(picture);
            } catch (Exception e) {
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


        //        public final RelativeLayout relativeLayout;
//        public final ImageButton imageButton;
        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.details);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + description.getText() + "'";
        }
    }

//    public class ItemViewHolder extends RecyclerView.ViewHolder {
//        public final CardView cardView;
//        public final ImageView image;
//        public final TextView status;
//        public final TextView title;
//        public final TextView description;
//
//        public ItemViewHolder(View view) {
//            super(view);
//            image = view.findViewById(R.id.image);
//            status = view.findViewById(R.id.status);
//            title = view.findViewById(R.id.title);
//            description = view.findViewById(R.id.description);
//            cardView = view.findViewById(R.id.cardView);
//        }
//
//        @NonNull
//        @Override
//        public String toString() {
//            return super.toString() + " '" + description.getText() + "'";
//        }
//    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}