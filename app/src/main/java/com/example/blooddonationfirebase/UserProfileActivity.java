package com.example.blooddonationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView profile_iv, available_iv, unavailable_iv;
    private TextView name_tv, bloodGroup_tv, available_tv, unavailable_tv, gender_tv, phone_tv, location_tv, lastDonation_tv;
    private Button call_btn;

    private String id, bloodGroup, available, gender, phone, location, lastDonation;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("Donor Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        pd = new ProgressDialog(UserProfileActivity.this);
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.show();

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(UserProfileActivity.this);
        if (signInAccount != null) {
            name_tv.setText(signInAccount.getDisplayName());
            Uri personPhoto = signInAccount.getPhotoUrl();
            Picasso.get().load(personPhoto).into(profile_iv);
        }

        db.collection("profiles").document(id).get()
                .addOnCompleteListener(task -> {
                    pd.dismiss();

                    bloodGroup = task.getResult().getString("bloodGroup");
                    available = task.getResult().getString("available");
                    gender = task.getResult().getString("gender");
                    phone = task.getResult().getString("phone");
                    location = task.getResult().getString("location");
                    lastDonation = task.getResult().getString("lastDonation");

                    if(!bloodGroup.isEmpty()) {
                        bloodGroup_tv.setVisibility(View.VISIBLE);
                        bloodGroup_tv.setText(task.getResult().getString("bloodGroup") + " Donor");
                    }
                    if(!available.isEmpty()) {
                        if(available.equals("Yes")) {
                            available_iv.setVisibility(View.VISIBLE);
                            unavailable_iv.setVisibility(View.GONE);
                            available_tv.setVisibility(View.VISIBLE);
                            unavailable_tv.setVisibility(View.GONE);
                        }
                        else if(available.equals("No")) {
                            available_iv.setVisibility(View.GONE);
                            unavailable_iv.setVisibility(View.VISIBLE);
                            available_tv.setVisibility(View.GONE);
                            unavailable_tv.setVisibility(View.VISIBLE);
                        }
                    }
                    if(!gender.isEmpty()) {
                        gender_tv.setVisibility(View.VISIBLE);
                        gender_tv.setText(task.getResult().getString("gender"));
                    }
                    if(!phone.isEmpty()) {
                        phone_tv.setVisibility(View.VISIBLE);
                        phone_tv.setText(task.getResult().getString("phone"));
                    }
                    if(!location.isEmpty()) {
                        location_tv.setVisibility(View.VISIBLE);
                        location_tv.setText(task.getResult().getString("location"));
                    }
                    if(!lastDonation.isEmpty()) {
                        lastDonation_tv.setVisibility(View.VISIBLE);
                        lastDonation_tv.setText(task.getResult().getString("lastDonation"));
                    }

                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(UserProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        if(phone == null) {
            call_btn.setVisibility(View.INVISIBLE);
        }

        call_btn.setOnClickListener(v -> {
            Intent intent12 = new Intent(Intent.ACTION_DIAL);
            intent12.setData(Uri.parse("tel:" + phone));
            startActivity(intent12);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeViews() {
        profile_iv = findViewById(R.id.profile_iv);
        name_tv = findViewById(R.id.name_tv);

        bloodGroup_tv = findViewById(R.id.bloodGroup_tv);

        available_iv = findViewById(R.id.available_iv);
        unavailable_iv = findViewById(R.id.unavailable_iv);
        available_tv = findViewById(R.id.available_tv);
        unavailable_tv = findViewById(R.id.unavailable_tv);

        gender_tv = findViewById(R.id.gender_tv);
        phone_tv = findViewById(R.id.phone_tv);
        location_tv = findViewById(R.id.location_tv);
        lastDonation_tv = findViewById(R.id.lastDonation_tv);

        call_btn = findViewById(R.id.call_btn);
    }
}