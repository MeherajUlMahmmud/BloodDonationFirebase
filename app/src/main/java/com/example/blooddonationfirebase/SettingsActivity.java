package com.example.blooddonationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private TextView contact_tv, terms_tv, logout_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();

        contact_tv.setOnClickListener(v -> {
        Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
        });

        terms_tv.setOnClickListener(v -> {
        Toast.makeText(this, "Terms and Conditions", Toast.LENGTH_SHORT).show();
        });

        logout_tv.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void initializeViews() {
        contact_tv = findViewById(R.id.contact_tv);
        terms_tv = findViewById(R.id.terms_tv);
        logout_tv = findViewById(R.id.logout_tv);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}