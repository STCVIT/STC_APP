package com.mstc.mstcapp.ui.resources;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mstc.mstcapp.ui.resources.detailsTab.DetailsFragment;
import com.mstc.mstcapp.ui.resources.resourceTab.ResourceTabFragment;
import com.mstc.mstcapp.ui.resources.roadmapTab.RoadmapTabFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final String domain;

    public ViewPagerAdapter(@NonNull FragmentManager fm, String domain) {
        super(fm);
        this.domain = domain;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new DetailsFragment(domain);
        else if (position == 1)
            return new RoadmapTabFragment(domain);
        else
            return new ResourceTabFragment(domain);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Details";
        else if (position == 1)
            return "Roadmap";
        else
            return "Resources";
    }

}
