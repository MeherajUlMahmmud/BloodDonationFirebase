package com.example.blooddonationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationfirebase.main_fragments.DonorsFragment;
import com.example.blooddonationfirebase.main_fragments.HomeFragment;
import com.example.blooddonationfirebase.main_fragments.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private BottomNavigationView bottomNav_view;
    private FloatingActionButton newRequest_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        bottomNav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        newRequest_fab.setOnClickListener(view -> {
            Intent newRequest_intent = new Intent(MainActivity.this, ModifyRequestActivity.class);
            startActivity(newRequest_intent);
        });

        toolbar = getSupportActionBar();
        toolbar.setTitle("Requests");
        loadFragment(new HomeFragment());
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settings_intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings_intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        bottomNav_view = findViewById(R.id.bottomNav_view);
        newRequest_fab = findViewById(R.id.newRequest_fab);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Requests");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_donors:
                    toolbar.setTitle("Donors");
                    fragment = new DonorsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.navHostFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}