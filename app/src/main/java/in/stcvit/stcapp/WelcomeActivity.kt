package `in`.stcvit.stcapp

import `in`.stcvit.stcapp.databinding.ActivityWelcomeBinding
import `in`.stcvit.stcapp.ui.WelcomeFragment
import `in`.stcvit.stcapp.util.Constants
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

class WelcomeActivity : AppCompatActivity() {
    private val context: Context = this

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var pagerAdapter: WelcomePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences =
            context.getSharedPreferences(Constants.STC_SHARED_PREFERENCES, MODE_PRIVATE)
        pagerAdapter = WelcomePagerAdapter(supportFragmentManager)
        binding.apply {
            viewPager.adapter = pagerAdapter
            next.setOnClickListener {
                if (viewPager.currentItem == pagerAdapter.count - 1) {
                    sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
                    startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                    finish()
                } else
                    ++viewPager.currentItem
            }
            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                }

                override fun onPageSelected(position: Int) {
                    next.text = getString(
                        when (position) {
                            pagerAdapter.count - 1 -> R.string.continue_btn
                            else -> R.string.next_btn
                        }
                    )
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem > 0)
            --binding.viewPager.currentItem
        else
            super.onBackPressed()
    }

    private inner class WelcomePagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int = 3

        override fun getItem(position: Int): Fragment = WelcomeFragment(position)

    }
}