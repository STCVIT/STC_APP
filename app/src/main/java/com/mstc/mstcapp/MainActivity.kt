package com.mstc.mstcapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mstc.mstcapp.databinding.ActivityMainBinding
import com.mstc.mstcapp.ui.ProjectIdeaFragment
import com.mstc.mstcapp.util.Constants
import com.mstc.mstcapp.util.Functions.Companion.openURL
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var isHome = false
    val context: Context = this
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        binding.drawerLayout.apply {
            findViewById<View>(R.id.share)
                .setOnClickListener { share() }
            findViewById<View>(R.id.feedback)
                .setOnClickListener {
                    openURL(
                        context,
                        "market://details?id=" + context.packageName
                    )
                }
            findViewById<View>(R.id.idea)
                .setOnClickListener { openIdeaDialog() }
            findViewById<View>(R.id.instagram)
                .setOnClickListener { openURL(context, Constants.INSTAGRAM_URL) }
            findViewById<View>(R.id.facebook)
                .setOnClickListener { openURL(context, Constants.FACEBOOK_URL) }
            findViewById<View>(R.id.linkedin)
                .setOnClickListener { openURL(context, Constants.LINKEDIN_URL) }
            findViewById<View>(R.id.github)
                .setOnClickListener { openURL(context, Constants.GITHUB_URL) }
            findViewById<View>(R.id.privacy_policy)
                .setOnClickListener { openURL(context, Constants.PRIVACY_URL) }
        }
//        val navBuilder = NavOptions.Builder()
//        navBuilder
//            .setEnterAnim(android.R.anim.slide_in_left)
//            .setExitAnim(android.R.anim.fade_out)
//            .setExitAnim(android.R.anim.fade_out)
//            .setPopEnterAnim(R.anim.slide_out_left)
//            .setPopExitAnim(R.anim.slide_in_right)

        binding.apply {
            selectTab(home)
            home.setOnClickListener {
                selectTab(home)
                if (navController.currentDestination?.id != R.id.navigation_home)
                    navController.popBackStack()
            }

            resources.setOnClickListener {
                selectTab(resources)
                if (navController.currentDestination?.id != R.id.navigation_home)
                    navController.popBackStack()
                navController.navigate(R.id.navigation_resources)
            }

            explore.setOnClickListener {
                selectTab(explore)
                if (navController.currentDestination?.id != R.id.navigation_home)
                    navController.popBackStack()
                navController.navigate(R.id.navigation_explore)
            }

            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            toolbar.navigationIcon = ContextCompat.getDrawable(
                context,
                R.drawable.ic_navigation
            )
        }
    }


    var exitCount: Int = 0
    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.navigation_home) {
            exitCount++
            val view = findViewById<View>(android.R.id.content)
            Snackbar.make(
                context, view, "Press back again to exit",
                BaseTransientBottomBar.LENGTH_SHORT
            ).addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar, event: Int) {
                    exitCount = 0
                }
            }).show()
            if (exitCount == 2) {
                super.onBackPressed()
            }
        } else {
            selectTab(binding.home)
            navController.popBackStack()
        }
    }

    private fun selectTab(chip: Chip) {
        for (j in arrayOf(binding.home, binding.resources, binding.explore)) {
            setUnselected(j)
        }
        setSelected(chip)
    }

    private fun setUnselected(chip: Chip) {
        chip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        chip.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
        chip.chipIconTint =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.textColorPrimary))
    }

    private fun setSelected(chip: Chip) {
        chip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorTertiaryBlue))
        chip.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        chip.chipIconTint =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary))
    }

    private fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        val rand = Random()
        val messages =
            arrayOf(
                "Are you someone who still cannot find the material to start your journey to become a proficient developer?\n",
                "On the lookout for study material?\n",
                "Budding developers! Still on the lookout for study material?\n"
            )
        val message = """
            ${messages[rand.nextInt(3)]}
            Don’t worry STC has got you covered!
            
            Download the STC app for latest resources and guidelines from 
            ${Constants.PLAY_STORE_URL}
            
            Guess what, it is FREE!
            """.trimIndent()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(intent, "Share Using"))
    }

    private fun openIdeaDialog() {
        val fm = supportFragmentManager
        val projectIdeaFragment: ProjectIdeaFragment = ProjectIdeaFragment.newInstance()
        projectIdeaFragment.show(fm, "projectFragment")
    }


    companion object {
        private val fetchedData = HashMap<String, Boolean>()
        fun isFetchedData(domain: String): Boolean {
            return if (fetchedData.containsKey(domain))
                fetchedData[domain] == true
            else false
        }

        fun setFetchedData(domain: String) {
            fetchedData[domain] = true
        }
    }
}