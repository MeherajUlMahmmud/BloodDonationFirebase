package com.example.blooddonationfirebase.main_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.blooddonationfirebase.R;
import com.example.blooddonationfirebase.models.Donor;
import com.example.blooddonationfirebase.models.Request;
import com.example.blooddonationfirebase.utils.DonorAdapter;
import com.example.blooddonationfirebase.utils.RequestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class DonorsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView donor_recycler;

    private ProgressDialog pd;

    public DonorsFragment() {
        // Required empty public constructor
    }

    public static DonorsFragment newInstance(String param1, String param2) {
        DonorsFragment fragment = new DonorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donors, container, false);
        donor_recycler = view.findViewById(R.id.donor_recycler);
        donor_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        getAllDonors(view);

        return view;
    }

    public void getAllDonors(View view) {
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference requestsCollectionRef = db
                .collection("profiles");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query donorQuery = requestsCollectionRef
                .whereNotEqualTo("id", user.getUid());

        donorQuery.get()
                .addOnCompleteListener(task -> {
                    pd.dismiss();

//                    System.out.println("Fetched");
                    List<Donor> donorList = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult()) {
                        Donor donor = new Donor();
                        donor.setId(doc.getString("id"));
                        donor.setName(doc.getString("name"));
                        donor.setGender(doc.getString("gender"));
                        donor.setBloodGroup(doc.getString("bloodGroup"));
                        donor.setPhone(doc.getString("phone"));
                        donor.setLocation(doc.getString("location"));
                        donor.setLastDonation(doc.getString("lastDonation"));
                        donor.setAvailable(doc.getString("available"));

                        donorList.add(donor);
//                        System.out.println("added");
                    }
                    donor_recycler.setAdapter(new DonorAdapter(getContext(), donorList));
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}