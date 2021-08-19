package com.mstc.mstcapp

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mstc.mstcapp.databinding.ActivityViewResourceBinding
import com.mstc.mstcapp.model.Domain
import com.mstc.mstcapp.ui.resources.ViewPagerAdapter
import java.util.*

class ViewResourceActivity : AppCompatActivity() {
    private val context: Context = this
    lateinit var binding: ActivityViewResourceBinding
    private lateinit var domainModel: Domain
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        domainModel = intent.getSerializableExtra("domain") as Domain
        setTheme(domainModel.style)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_view_resource
        ) as ActivityViewResourceBinding

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
            toolbarImage.post {
                run {
                    toolbarImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            domainModel.drawable
                        )
                    )
                }
            }
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
        overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_out_left)
    }
}