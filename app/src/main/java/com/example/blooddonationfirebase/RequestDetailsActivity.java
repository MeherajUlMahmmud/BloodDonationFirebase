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
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationfirebase.home_utils.HomeAdapter;
import com.example.blooddonationfirebase.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestDetailsActivity extends AppCompatActivity {

    private String id;
    private TextView patientName_tv, postedOn_tv, phone_tv, location_tv, bloodGroup_tv, neededWithin_tv, note_tv, user_tv;
    private Button showOnMap_btn, contact_btn;
    private Request req;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        getSupportActionBar().setTitle("Request Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        pd = new ProgressDialog(RequestDetailsActivity.this);
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.show();

        fetchData(id);

        contact_btn.setOnClickListener(v -> {
            Intent intent12 = new Intent(Intent.ACTION_DIAL);
            intent12.setData(Uri.parse("tel:" + req.getPhone()));
            startActivity(intent12);
        });

        showOnMap_btn.setOnClickListener(v -> {
            String map = "http://maps.google.co.in/maps?q=" + req.getLocation();
            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
            startActivity(intent1);
        });
    }

    private void fetchData(String id) {
        db.collection("requests").document(id).get()
                .addOnCompleteListener(task -> {
                    req = new Request();

                    req.setId(task.getResult().getString("id"));
                    req.setPatientName(task.getResult().getString("patientName"));
                    req.setGender(task.getResult().getString("gender"));
                    req.setBloodGroup(task.getResult().getString("bloodGroup"));
                    req.setLocation(task.getResult().getString("location"));
                    req.setNeededWithin(task.getResult().getString("neededWithin"));
                    req.setUnit(task.getResult().getString("unit"));
                    req.setNote(task.getResult().getString("note"));
                    req.setPostedOn(task.getResult().getString("postedOn"));
                    req.setPhone(task.getResult().getString("phone"));
                    req.setUserId(task.getResult().getString("userId"));

                    showData(req);

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RequestDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showData(Request req) {
        patientName_tv.setText(req.getPatientName());
        postedOn_tv.setText(req.getPostedOn());
        phone_tv.setText(req.getPhone());
        location_tv.setText(req.getLocation());
        bloodGroup_tv.setText(req.getBloodGroup());
        neededWithin_tv.setText(req.getNeededWithin());
        note_tv.setText(req.getNote());
        phone_tv.setText(req.getPhone());

        db.collection("profiles").document(req.getUserId()).get()
                .addOnCompleteListener(task -> {
                    pd.dismiss();
                    user_tv.setText(task.getResult().getString("name"));
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(RequestDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

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

    private void initializeViews() {
        patientName_tv = findViewById(R.id.patientName_tv);
        postedOn_tv = findViewById(R.id.postedOn_tv);
        phone_tv = findViewById(R.id.phone_tv);
        location_tv = findViewById(R.id.location_tv);
        bloodGroup_tv = findViewById(R.id.bloodGroup_tv);
        neededWithin_tv = findViewById(R.id.neededWithin_tv);
        note_tv = findViewById(R.id.note_tv);
        user_tv = findViewById(R.id.user_tv);

        contact_btn = findViewById(R.id.contact_btn);
        showOnMap_btn = findViewById(R.id.showOnMap_btn);
    }
}