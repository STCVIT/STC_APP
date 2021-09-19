package com.mstc.mstcapp.ui.explore

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mstc.mstcapp.ui.explore.about.BoardMemberFragment
import com.mstc.mstcapp.ui.explore.event.EventFragment
import com.mstc.mstcapp.ui.explore.project.ProjectFragment

class ViewPagerAdapter(fm: FragmentManager) :

    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int) =
        when (position) {
            0 -> BoardMemberFragment()
            1 -> ProjectFragment()
            else -> EventFragment()
        }

    override fun getCount() = 3

    override fun getPageTitle(position: Int) =
        when (position) {
            0 -> "About"
            1 -> "Projects"
            else -> "Events"
        }
}
