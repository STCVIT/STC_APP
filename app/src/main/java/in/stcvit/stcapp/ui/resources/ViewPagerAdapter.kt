package `in`.stcvit.stcapp.ui.resources

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import `in`.stcvit.stcapp.ui.resources.details.DetailsFragment
import `in`.stcvit.stcapp.ui.resources.resource.ResourceFragment
import `in`.stcvit.stcapp.ui.resources.roadmap.RoadmapFragment

class ViewPagerAdapter(fm: FragmentManager, private val domain: String) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DetailsFragment(domain)
            1 -> RoadmapFragment(domain)
            else -> ResourceFragment(domain)
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Details"
            1 -> "Roadmap"
            else -> "Resources"
        }
    }
}
