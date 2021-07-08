package com.mstc.mstcapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mstc.mstcapp.R;
import com.mstc.mstcapp.model.DomainModel;

import java.util.ArrayList;

public class DomainAdapter extends RecyclerView.Adapter<DomainAdapter.ResourceViewHolder> {

    private final ArrayList<DomainModel> list;
    private final Context context;

    public DomainAdapter(Context context, ArrayList<DomainModel> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ResourceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_domain, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getDomain());
        holder.image.setImageDrawable(ContextCompat.getDrawable(context, list.get(position).getDrawable()));

        final ContextThemeWrapper wrapper;
        if (position % 3 == 0)
            wrapper = new ContextThemeWrapper(context, R.style.resources_red);
        else if (position % 3 == 1)
            wrapper = new ContextThemeWrapper(context, R.style.resources_blue);
        else
            wrapper = new ContextThemeWrapper(context, R.style.resources_yellow);
        final Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.resource_background, wrapper.getTheme());
        holder.background.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ResourceViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout background;
        ImageView image;
        TextView textView;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.domain);
            image = itemView.findViewById(R.id.image);
            background = itemView.findViewById(R.id.background);
        }
    }
}
