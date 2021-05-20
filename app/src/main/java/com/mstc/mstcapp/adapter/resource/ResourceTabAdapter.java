package com.mstc.mstcapp.adapter.resource;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mstc.mstcapp.R;
import com.mstc.mstcapp.model.resources.ResourceModel;
import com.mstc.mstcapp.util.Constants;

import java.util.List;

public class ResourceTabAdapter extends RecyclerView.Adapter<ResourceTabAdapter.ResourceViewHolder> {

    private final Context context;
    private List<ResourceModel> list;

    public ResourceTabAdapter(Context context, List<ResourceModel> items) {
        this.context = context;
        list = items;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resource, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ResourceViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getDescription());
        holder.share.setOnClickListener(v -> shareResource(list.get(position).getTitle(), list.get(position).getLink()));
        holder.relativeLayout.setOnClickListener(v -> openURL(list.get(position).getLink()));
    }

    private void shareResource(String title, String link) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link + "\n\nHere is an article for you! This is an article on " + title + ".\nTo keep receiving amazing articles like these, check out the STC app here \n\n" + Constants.PLAY_STORE_URL);
        context.startActivity(Intent.createChooser(intent, "Share Using"));
    }

    public void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ResourceModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ResourceViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final TextView description;
        public final ImageButton share;
        public final RelativeLayout relativeLayout;

        public ResourceViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            share = view.findViewById(R.id.share);
            relativeLayout = view.findViewById(R.id.relativeLayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + description.getText() + "'";
        }
    }
}