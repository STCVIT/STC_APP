package com.mstc.mstcapp.ui.resources;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import com.mstc.mstcapp.R;
import com.mstc.mstcapp.adapter.DomainAdapter;
import com.mstc.mstcapp.model.DomainModel;
import com.mstc.mstcapp.util.ClickListener;
import com.mstc.mstcapp.util.RecyclerTouchListener;

import java.util.ArrayList;

public class DomainsFragment extends Fragment {

    private ArrayList<DomainModel> list;
    private RecyclerView recyclerView;

    public DomainsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resources, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        list = new ArrayList<>();
        int color1 = R.style.resources_red;
        int color2 = R.style.resources_blue;
        int color3 = R.style.resources_yellow;

        /* ADD A NEW RESOURCE HERE */
        list.add(new DomainModel("Android", R.drawable.ic_app_dev, color1));
        list.add(new DomainModel("Frontend", R.drawable.ic_frontend, color2));
        list.add(new DomainModel("Backend", R.drawable.ic_backend, color3));
        list.add(new DomainModel("Design", R.drawable.ic_design, color1));
        list.add(new DomainModel("Machine Learning", R.drawable.ic_ml, color2));
        list.add(new DomainModel("Competitive Coding", R.drawable.ic_cc, color3));

        DomainAdapter domainAdapter = new DomainAdapter(getContext(), list);
        recyclerView.setAdapter(domainAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                viewResource(list.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void viewResource(DomainModel domainModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("domain", domainModel);
        NavHostFragment.findNavController(DomainsFragment.this).navigate(R.id.action_navigation_resources_to_navigation_view_resource_activity, bundle);
    }
}