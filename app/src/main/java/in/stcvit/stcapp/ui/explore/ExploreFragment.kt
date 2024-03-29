package `in`.stcvit.stcapp.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import `in`.stcvit.stcapp.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {
    private var viewPagerAdapter: ViewPagerAdapter? = null
    lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        binding.apply {
            viewPager.adapter = viewPagerAdapter
            tabLayout.setupWithViewPager(binding.viewPager)
        }
    }

}