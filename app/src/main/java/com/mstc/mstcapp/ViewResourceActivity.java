package com.mstc.mstcapp;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.mstc.mstcapp.model.DomainModel;
import com.mstc.mstcapp.ui.resources.ViewPagerAdapter;

import java.util.Objects;

public class ViewResourceActivity extends AppCompatActivity {
    private final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DomainModel domain = (DomainModel) getIntent().getSerializableExtra("domain");

        if (domain == null) {
            Toast.makeText(context, "Could not load domain! Please try again...", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            setTheme(domain.getColor());

            setContentView(R.layout.activity_view_resource);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(context, R.drawable.ic_back));

            TextView toolbar_title = findViewById(R.id.toolbar_title);
            toolbar_title.setText(domain.getDomain().toUpperCase());

            TextView toolbar_description = findViewById(R.id.toolbar_description);
            toolbar_description.setText(R.string.choose_domain_helper_text);

            ImageView toolbar_image = findViewById(R.id.toolbar_image);
            toolbar_image.setImageDrawable(ContextCompat.getDrawable(context, domain.getDrawable()));

            TabLayout tabLayout = findViewById(R.id.tabLayout);
            ViewPager viewPager = findViewById(R.id.viewPager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), domain.getDomain().toLowerCase());
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
            collapsingToolbarLayout.setTitle(domain.getDomain().toUpperCase());
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}