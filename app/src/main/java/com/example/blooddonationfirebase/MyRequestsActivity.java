package com.example.blooddonationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.blooddonationfirebase.utils.RequestAdapter;
import com.example.blooddonationfirebase.models.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MyRequestsActivity extends AppCompatActivity {

    private RecyclerView request_recycler;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);
        getSupportActionBar().setTitle("My Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        request_recycler = findViewById(R.id.request_recycler);
        request_recycler.setLayoutManager(new LinearLayoutManager(MyRequestsActivity.this));

        getAllRequests();
    }

    private void getAllRequests() {
        pd = new ProgressDialog(MyRequestsActivity.this);
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference requestsCollectionRef = db
                .collection("requests");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query requestQuery = requestsCollectionRef
                .whereEqualTo("userId", user.getUid())
                .orderBy("neededWithin", Query.Direction.ASCENDING);

        requestQuery.get()
                .addOnCompleteListener(task -> {
                    pd.dismiss();

//                    System.out.println("Fetched");
                    List<Request> reqList = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult()) {
                        Request req = new Request();
                        req.setId(doc.getString("id"));
                        req.setPatientName(doc.getString("patientName"));
                        req.setGender(doc.getString("gender"));
                        req.setBloodGroup(doc.getString("bloodGroup"));
                        req.setLocation(doc.getString("location"));
                        req.setNeededWithin(doc.getString("neededWithin"));
                        req.setUnit(doc.getString("unit"));
                        req.setNote(doc.getString("note"));
                        req.setPostedOn(doc.getString("postedOn"));

                        reqList.add(req);
//                        System.out.println("added");
                    }
                    request_recycler.setAdapter(new RequestAdapter(MyRequestsActivity.this, reqList));
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(MyRequestsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(MyRequestsActivity.this, MainActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}