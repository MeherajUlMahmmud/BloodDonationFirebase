package com.example.blooddonationfirebase.main_fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationfirebase.EditProfileActivity;
import com.example.blooddonationfirebase.MyRequestsActivity;
import com.example.blooddonationfirebase.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private ImageView profile_iv, available_iv, unavailable_iv;
    private TextView name_tv, bloodGroup_tv, available_tv, unavailable_tv, gender_tv, phone_tv, location_tv, lastDonation_tv;
    private Button editProfile_btn, myRequests_btn;

    private String bloodGroup, available, gender, phone, location, lastDonation;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog pd;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeViews(view);

        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.show();

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (signInAccount != null) {
            name_tv.setText(signInAccount.getDisplayName());
            Uri personPhoto = signInAccount.getPhotoUrl();
            Picasso.get().load(personPhoto).into(profile_iv);
        }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("profiles").document(user.getUid()).get()
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
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        editProfile_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        myRequests_btn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MyRequestsActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void initializeViews(View view) {
        profile_iv = view.findViewById(R.id.profile_iv);
        name_tv = view.findViewById(R.id.name_tv);
        editProfile_btn = view.findViewById(R.id.editProfile_btn);

        bloodGroup_tv = view.findViewById(R.id.bloodGroup_tv);

        available_iv = view.findViewById(R.id.available_iv);
        unavailable_iv = view.findViewById(R.id.unavailable_iv);
        available_tv = view.findViewById(R.id.available_tv);
        unavailable_tv = view.findViewById(R.id.unavailable_tv);

        gender_tv = view.findViewById(R.id.gender_tv);
        phone_tv = view.findViewById(R.id.phone_tv);
        location_tv = view.findViewById(R.id.location_tv);
        lastDonation_tv = view.findViewById(R.id.lastDonation_tv);

        myRequests_btn = view.findViewById(R.id.myRequests_btn);
    }
}