package com.mstc.mstcapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.mstc.mstcapp.databinding.ActivityViewResourceBinding
import com.mstc.mstcapp.model.Domain
import com.mstc.mstcapp.ui.resources.ViewPagerAdapter
import java.util.*
import kotlin.math.abs

private const val TAG = "ViewResourceActivity"

private enum class State {
    EXPANDED, COLLAPSED, IDLE
}

class ViewResourceActivity : AppCompatActivity() {
    private val context: Context = this
    lateinit var binding: ActivityViewResourceBinding
    private lateinit var domainModel: Domain
    private var state: State? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        domainModel = intent.getSerializableExtra("domain") as Domain
        setTheme(domainModel.style)
        binding = ActivityViewResourceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_back
                )
            )
            toolbarTitle.text = domainModel.domain.uppercase(Locale.getDefault())
            toolbarImage.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    domainModel.drawable
                )
            )
            Log.i(TAG, "onCreate: ${toolbar.title}  , ${collapsingToolbarLayout.title}")
            appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                when {
                    verticalOffset == 0 -> {
                        if (state != State.EXPANDED) {
                            Log.d(TAG, "Expanded")
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
                        if (state != State.COLLAPSED) {
                            Log.d(TAG, "Collapsed")
                        }
                        state = State.COLLAPSED
                    }
                    else -> {
                        if (state != State.IDLE) {
                            Log.d(TAG, "Idle")
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