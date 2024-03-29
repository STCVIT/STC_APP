package `in`.stcvit.stcapp

import `in`.stcvit.stcapp.databinding.ActivityViewResourceBinding
import `in`.stcvit.stcapp.model.Domain
import `in`.stcvit.stcapp.ui.resources.ViewPagerAdapter
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import com.google.android.material.appbar.AppBarLayout
import java.util.*
import kotlin.math.abs

private enum class State {
    EXPANDED, COLLAPSED, IDLE
}

class ViewResourceActivity : AppCompatActivity() {
    private val context: Context = this
    lateinit var binding: ActivityViewResourceBinding
    private lateinit var domainModel: Domain
    private val safeArgs: ViewResourceActivityArgs by navArgs()
    private var state: State? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        domainModel = safeArgs.domain
        setTheme(domainModel.style)
        binding = ActivityViewResourceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bindAppBar()
        showData()
    }

    private fun showData() {
        val viewPagerAdapter =
            ViewPagerAdapter(
                supportFragmentManager,
                domainModel.domain.lowercase(Locale.getDefault())
            )
        binding.apply {
            viewPager.adapter = viewPagerAdapter
            tabLayout.setupWithViewPager(binding.viewPager)
            collapsingToolbarLayout.title = domainModel.domain.uppercase(Locale.getDefault())
        }
    }

    private fun ActivityViewResourceBinding.bindAppBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarTitle.text = domainModel.domain.uppercase(Locale.getDefault())
        toolbarImage.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                domainModel.drawable
            )
        )
        appBarLayout.addOnOffsetChangedListener({ _, verticalOffset ->
            when {
                verticalOffset == 0 -> {
                    if (state != State.EXPANDED) {
                        toolbarImage.animation = AnimationUtils.loadAnimation(
                            this@ViewResourceActivity,
                            android.R.anim.fade_in
                        )
                        toolbarImage.postOnAnimation {
                            toolbarImage.visibility = View.VISIBLE
                            toolbarTitle.visibility = View.VISIBLE
                            toolbarDescription.visibility = View.VISIBLE
                        }
                    }
                    state = State.EXPANDED
                }
                abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                    state = State.COLLAPSED
                }
                else -> {
                    if (state != State.IDLE) {
                        toolbarImage.animation = AnimationUtils.loadAnimation(
                            this@ViewResourceActivity,
                            android.R.anim.fade_out
                        )
                        toolbarImage.postOnAnimation {
                            toolbarImage.visibility = View.INVISIBLE
                            toolbarTitle.visibility = View.INVISIBLE
                            toolbarDescription.visibility = View.INVISIBLE
                        }
                    }
                    state = State.IDLE
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    override fun onBackPressed() {
        when (binding.viewPager.currentItem) {
            0 -> finish()
            else -> binding.viewPager.currentItem = 0
        }
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_out_left)
    }
}