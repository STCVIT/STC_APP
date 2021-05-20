package com.mstc.mstcapp.ui.explore;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mstc.mstcapp.ui.explore.about.AboutFragment;
import com.mstc.mstcapp.ui.explore.event.EventsFragment;
import com.mstc.mstcapp.ui.explore.project.ProjectFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new AboutFragment();
        else if (position == 1)
            return new ProjectFragment();
        else
            return new EventsFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "About";
        else if (position == 1)
            return "Projects";
        else
            return "Events";
    }

}
