package com.example.blooddonationfirebase.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationfirebase.R;
import com.example.blooddonationfirebase.RequestDetailsActivity;
import com.example.blooddonationfirebase.UserProfileActivity;
import com.example.blooddonationfirebase.models.Donor;

import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorCardViewHolder> {

    private Context context;
    private List<Donor> donorList;

    public DonorAdapter(Context context, List<Donor> donorList) {
        this.context = context;
        this.donorList = donorList;
    }

    @NonNull
    @Override
    public DonorCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_card, parent, false);
        return new DonorCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorCardViewHolder holder, int position) {
        holder.name_tv.setText(donorList.get(position).getName());
        holder.location_tv.setText(donorList.get(position).getLocation());
        holder.bloodGroup_tv.setText(donorList.get(position).getBloodGroup());
        holder.phone_tv.setText(donorList.get(position).getPhone());
        if (donorList.get(position).getAvailable().equals("Yes")) {
            holder.available_tv.setText("Available");
        } else {
            holder.available_tv.setText("Unavailable");
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("id", String.valueOf(donorList.get(position).getId()));
            context.startActivity(intent);
        });

        if(donorList.get(position).getPhone().isEmpty()) {
            holder.call_imgBtn.setVisibility(View.INVISIBLE);
        }

        holder.call_imgBtn.setOnClickListener(v -> {
            Intent intent12 = new Intent(Intent.ACTION_DIAL);
            intent12.setData(Uri.parse("tel:" + donorList.get(position).getPhone()));
            context.startActivity(intent12);
        });
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public class DonorCardViewHolder extends RecyclerView.ViewHolder {

        private TextView bloodGroup_tv, name_tv, phone_tv, location_tv, available_tv;
        private ImageButton call_imgBtn;

        public DonorCardViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews(itemView);
        }

        public void initializeViews(View itemView) {
            bloodGroup_tv = itemView.findViewById(R.id.bloodGroup_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            phone_tv = itemView.findViewById(R.id.phone_tv);
            location_tv = itemView.findViewById(R.id.location_tv);
            available_tv = itemView.findViewById(R.id.available_tv);
            call_imgBtn = itemView.findViewById(R.id.call_imgBtn);
        }
    }
}
