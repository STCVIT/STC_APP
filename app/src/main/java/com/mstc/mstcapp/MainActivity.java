package com.mstc.mstcapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mstc.mstcapp.ui.ProjectIdeaFragment;
import com.mstc.mstcapp.util.Constants;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final HashMap<String, Boolean> fetchedData = new HashMap<>();
    public static boolean isAppRunning = false;
    public static int feed_position = 0;
    public static boolean isHome = false;
    private final Context context = this;
    private final int[] ids = {R.id.home, R.id.resources, R.id.explore};
    int exitCount = 0;
    private NavController navController;
    private DrawerLayout drawerLayout;

    public static int getFeed_position() {
        return feed_position;
    }

    public static void setFeed_position(int feed_position) {
        MainActivity.feed_position = feed_position;
    }

    public static boolean isFetchedData(String domain) {
        if (fetchedData.containsKey(domain))
            return fetchedData.get(domain);
        else
            return false;
    }

    public static void setFetchedData(String domain) {
        fetchedData.put(domain, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        drawerLayout = findViewById(R.id.drawerLayout);

        drawerLayout.findViewById(R.id.share).setOnClickListener(v -> share());
        drawerLayout.findViewById(R.id.feedback).setOnClickListener(v -> openURL("market://details?id=" + context.getPackageName()));
        drawerLayout.findViewById(R.id.idea).setOnClickListener(v -> openIdeaDialog());

        drawerLayout.findViewById(R.id.instagram).setOnClickListener(v -> openURL(Constants.INSTAGRAM_URL));
        drawerLayout.findViewById(R.id.facebook).setOnClickListener(v -> openURL(Constants.FACEBOOK_URL));
        drawerLayout.findViewById(R.id.linkedin).setOnClickListener(v -> openURL(Constants.LINKEDIN_URL));
        drawerLayout.findViewById(R.id.github).setOnClickListener(v -> openURL(Constants.GITHUB_URL));

        drawerLayout.findViewById(R.id.privacy_policy).setOnClickListener(v -> openURL(Constants.PRIVACY_URL));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);

        toggle.syncState();
        toolbar.setNavigationIcon(ContextCompat.getDrawable(context, R.drawable.ic_navigation));

        findViewById(R.id.home).setOnClickListener(v -> {
            if (isHome) feed_position = 0;
            selectTab(ids[0]);
            navController.popBackStack();
            navController.navigate(R.id.navigation_home);
        });

        findViewById(R.id.resources).setOnClickListener(v -> {
            selectTab(ids[1]);
            navController.popBackStack();
            navController.navigate(R.id.navigation_resources);
        });

        findViewById(R.id.explore).setOnClickListener(v -> {
            selectTab(ids[2]);
            navController.popBackStack();
            navController.navigate(R.id.navigation_explore);
        });
    }

    private void share() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        Random rand = new Random();

        String[] messages = {"Are you someone who still cannot find the material to start your journey to become a proficient developer?\n",
                "On the lookout for study material?\n",
                "Budding developers! Still on the lookout for study material?\n"
        };
        String message = messages[rand.nextInt(3)] +
                "\nDon’t worry STC has got you covered!\n" +
                "\nDownload the STC app for latest resources and guidelines from \n" + Constants.PLAY_STORE_URL +
                "\n\nGuess what, it is FREE!";
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share Using"));
    }


    private void openIdeaDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ProjectIdeaFragment projectIdeaFragment = ProjectIdeaFragment.newInstance();
        projectIdeaFragment.show(fm, "projectFragment");
    }

    public void openURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        } else if (navController.getCurrentDestination().getId() != R.id.navigation_home) {
            selectTab(ids[0]);
            navController.popBackStack();
            navController.navigate(R.id.navigation_home);
        } else {
            exitCount++;
            View view = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(context, view, "Press back again to exit",
                    BaseTransientBottomBar.LENGTH_SHORT);
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    exitCount = 0;
                }
            });
            snackbar.show();
            if (exitCount == 2) {
                super.onBackPressed();
            }
        }
    }

    private void selectTab(int id) {
        for (int j : ids) {
            if (j != id) setUnselected(j);
        }
        setSelected(id);
    }

    private void setUnselected(int id) {
        Chip chip = findViewById(id);
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
        chip.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        chip.setChipIconTint(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.textColorPrimary)));
    }

    private void setSelected(int id) {
        Chip chip = findViewById(id);
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorTertiaryBlue)));
        chip.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        chip.setChipIconTint(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
    }
}