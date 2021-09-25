package com.mstc.mstcapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mstc.mstcapp.databinding.ActivityMainBinding
import com.mstc.mstcapp.util.Constants
import com.mstc.mstcapp.util.Functions.Companion.openURL
import java.util.*


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val context: Context = this
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        initDrawer()
        binding.apply {
            selectTab(bottomNav.home)
            bottomNav.home.setOnClickListener {
                selectTab(bottomNav.home)
                if (navController.currentDestination?.id != R.id.navigation_home)
                    navController.popBackStack().also {
                        toolbar.title = "Home"
                    }
            }
            bottomNav.resources.setOnClickListener {
                selectTab(bottomNav.resources)
                navController.apply {
                    if (currentDestination?.id != R.id.navigation_home)
                        popBackStack()
                    navigate(R.id.navigation_resources).also {
                        toolbar.title = "Resources"
                    }
                }
            }

            bottomNav.explore.setOnClickListener {
                selectTab(bottomNav.explore)
                navController.apply {
                    if (currentDestination?.id != R.id.navigation_home)
                        popBackStack()
                    navigate(R.id.navigation_explore).also {
                        toolbar.title = "Explore"
                    }
                }
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
            Snackbar.make(
                context, findViewById(android.R.id.content), "Press back again to exit",
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
            selectTab(binding.bottomNav.home)
            navController.popBackStack()
        }
    }

    private fun initDrawer() {
        binding.navigationDrawer.apply {
            share.setOnClickListener { share() }
            feedback.setOnClickListener { feedback() }
            idea.setOnClickListener {
                navController.navigate(R.id.projectIdeaDialog)
            }
            instagram.setOnClickListener { openURL(context, Constants.INSTAGRAM_URL) }
            facebook.setOnClickListener { openURL(context, Constants.FACEBOOK_URL) }
            linkedin.setOnClickListener { openURL(context, Constants.LINKEDIN_URL) }
            github.setOnClickListener { openURL(context, Constants.GITHUB_URL) }
            privacyPolicy.setOnClickListener { openURL(context, Constants.PRIVACY_URL) }
        }
    }

    private fun feedback() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Give us a feedback")
            .setMessage("This will redirect you to Google Play Store")
            .setPositiveButton("Rate App") { _, _ ->
                openURL(
                    context,
                    "market://details?id=" + context.packageName
                )
            }
            .setNegativeButton("Not Now") { _, _ -> }
            .show()
    }

    private fun selectTab(chip: Chip) {
        binding.bottomNav.apply {
            for (j in arrayOf(home, resources, explore)) {
                setUnselected(j)
            }
            setSelected(chip)
        }
    }

    private fun setUnselected(chip: Chip) {
        chip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.textColorPrimary))
            .also {
                chip.chipIconTint = it
                chip.setTextColor(it)
            }
    }

    private fun setSelected(chip: Chip) {
        chip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorTertiaryBlue))
        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary))
            .also {
                chip.setTextColor(it)
                chip.chipIconTint = it
            }
    }

    private fun share() {
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

        val intent = Intent(Intent.ACTION_SEND)
            .also {
                it.type = "text/plain"
                it.putExtra(Intent.EXTRA_TEXT, message)
            }
        startActivity(Intent.createChooser(intent, "Share Using"))
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
