package com.example.blooddonationfirebase.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blooddonationfirebase.R;
import com.example.blooddonationfirebase.RequestDetailsActivity;
import com.example.blooddonationfirebase.models.Request;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestCardViewHolder> {

    private Context context;
    private List<Request> requestList;

    public RequestAdapter(Context context, List<Request> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
        return new RequestCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestCardViewHolder holder, int position) {
        holder.name_tv.setText(requestList.get(position).getPatientName());
        holder.location_tv.setText(requestList.get(position).getLocation());
        holder.bloodGroup_tv.setText(requestList.get(position).getBloodGroup());
        holder.unit_tv.setText(requestList.get(position).getUnit() + " Unit(s)");
        holder.neededWithin_tv.setText("Within " + requestList.get(position).getNeededWithin());
        holder.timeAgo_tv.setText("Posted on " + requestList.get(position).getPostedOn());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RequestDetailsActivity.class);
            intent.putExtra("id", String.valueOf(requestList.get(position).getId()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class RequestCardViewHolder extends RecyclerView.ViewHolder {

        private TextView bloodGroup_tv, name_tv, unit_tv, location_tv, timeAgo_tv, neededWithin_tv;

        public RequestCardViewHolder(@NonNull View itemView) {
            super(itemView);
            initializeViews(itemView);
        }

        public void initializeViews(View itemView) {
            bloodGroup_tv = itemView.findViewById(R.id.bloodGroup_tv);
            name_tv = itemView.findViewById(R.id.name_tv);
            unit_tv = itemView.findViewById(R.id.unit_tv);
            location_tv = itemView.findViewById(R.id.location_tv);
            timeAgo_tv = itemView.findViewById(R.id.timeAgo_tv);
            neededWithin_tv = itemView.findViewById(R.id.neededWithin_tv);
        }
    }
}
