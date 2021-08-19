package com.mstc.mstcapp.ui.resources

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mstc.mstcapp.ui.resources.details.DetailsFragment
import com.mstc.mstcapp.ui.resources.resource.ResourceFragment
import com.mstc.mstcapp.ui.resources.roadmap.RoadmapFragment

class ViewPagerAdapter(fm: FragmentManager, private val domain: String) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DetailsFragment(domain)
            1 -> RoadmapFragment(domain)
            else -> ResourceFragment(domain)
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Details"
            1 -> "Roadmap"
            else -> "Resources"
        }
    }
}
