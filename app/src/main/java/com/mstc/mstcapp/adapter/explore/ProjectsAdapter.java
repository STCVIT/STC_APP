package com.mstc.mstcapp.adapter.explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mstc.mstcapp.R;
import com.mstc.mstcapp.model.explore.ProjectsModel;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {

    private List<ProjectsModel> list;
    private Context context;

    public ProjectsAdapter(Context context, List<ProjectsModel> items) {
        this.context = context;
        list = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = list.get(position);
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getLink());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView image;
        public final TextView title;
        public final TextView description;
        public ProjectsModel mItem;

        public ViewHolder(View view) {
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
}