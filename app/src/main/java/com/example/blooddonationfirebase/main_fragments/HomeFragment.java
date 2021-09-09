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
import com.example.blooddonationfirebase.utils.RequestAdapter;
import com.example.blooddonationfirebase.models.Request;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView request_recycler;

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

        return view;
    }

    public void getAllRequests(View view) {
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference requestsCollectionRef = db
                .collection("requests");

        Query requestQuery = requestsCollectionRef
                .orderBy("neededWithin", Query.Direction.ASCENDING);

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                    request_recycler.setAdapter(new RequestAdapter(getContext(), reqList));
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}