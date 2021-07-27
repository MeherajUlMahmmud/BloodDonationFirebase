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
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationfirebase.MainActivity;
import com.example.blooddonationfirebase.R;
import com.example.blooddonationfirebase.home_utils.HomeAdapter;
import com.example.blooddonationfirebase.models.Request;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView name_tv;
    private RecyclerView request_recycler;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog pd;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        request_recycler = view.findViewById(R.id.request_recycler);
        request_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        getAllRequests(view);

//        name_tv = view.findViewById(R.id.name);
//
//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
//        if (signInAccount != null) {
//            name_tv.setText(new StringBuilder().append("Hi ").append(signInAccount.getDisplayName()).toString());
//        }

        return view;
    }

    public void getAllRequests(View view) {
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("requests").get()
                .addOnCompleteListener(task -> {
                    pd.dismiss();

                    System.out.println("Fetched");
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
                        System.out.println("added");
                    }
                    request_recycler.setAdapter(new HomeAdapter(getContext(), reqList));
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}