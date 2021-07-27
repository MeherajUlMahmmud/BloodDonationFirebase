package com.example.blooddonationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blooddonationfirebase.main_fragments.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {


    final Calendar myCalendar = Calendar.getInstance();

    private FloatingActionButton save_fab;
    private TextInputEditText phone_et, location_et, lastDonation_et;
    private RadioGroup editProfileGender_rg;
    private RadioButton male_radio, female_radio, other_radio, yes_radio, no_radio;
    private Spinner bloodGroup_spinner;
    private ProgressDialog pd;

    String[] bloodGroupChoices = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    private String userId, name, phone, gender, available, bloodGroup, location, lastDonation;
    ArrayAdapter aa;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();

        bloodGroup_spinner.setOnItemSelectedListener(this);

        aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroupChoices);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroup_spinner.setAdapter(aa);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        };
        lastDonation_et.setOnClickListener(v -> new DatePickerDialog(EditProfileActivity.this, date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        updateFields();

        save_fab.setOnClickListener(v -> {
            updateUser();
        });
    }

    private void updateFields() {
        pd = new ProgressDialog(EditProfileActivity.this);
        pd.setTitle("Loading...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("profiles").document(user.getUid()).get()
                .addOnCompleteListener(task -> {
                    pd.dismiss();

                    bloodGroup = task.getResult().getString("bloodGroup");
                    gender = task.getResult().getString("gender");
                    phone = task.getResult().getString("phone");
                    location = task.getResult().getString("location");
                    lastDonation = task.getResult().getString("lastDonation");

                    if (!bloodGroup.isEmpty()) {
                        bloodGroup_spinner.setSelection(aa.getPosition(bloodGroup));
                    }

                    if (!gender.isEmpty()) {
                        if (gender == "Male") {
                            editProfileGender_rg.check(editProfileGender_rg.getChildAt(0).getId());
                        } else if (gender == "Female") {
                            editProfileGender_rg.check(editProfileGender_rg.getChildAt(1).getId());
                        } else if (gender == "Other") {
                            editProfileGender_rg.check(editProfileGender_rg.getChildAt(2).getId());
                        }
                    }

                    if (!phone.isEmpty()) {
                        phone_et.setText(task.getResult().getString("phone"));
                    }
                    if (!location.isEmpty()) {
                        location_et.setText(task.getResult().getString("location"));
                    }
                    if (!lastDonation.isEmpty()) {
                        lastDonation_et.setText(task.getResult().getString("lastDonation"));
                    }

                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void updateUser() {
        pd = new ProgressDialog(EditProfileActivity.this);
        pd.setTitle("Updating...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        name = user.getDisplayName();

        if (male_radio.isChecked()) {
            gender = "Male";
        } else if (female_radio.isChecked()) {
            gender = "Female";
        } else if (other_radio.isChecked()) {
            gender = "Other";
        }

        if (yes_radio.isChecked()) {
            available = "Yes";
        } else if (no_radio.isChecked()) {
            available = "No";
        }

        bloodGroup = bloodGroup_spinner.getSelectedItem().toString();
        location = location_et.getText().toString();
        lastDonation = lastDonation_et.getText().toString();
        phone = phone_et.getText().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", userId);
        doc.put("name", name);
        doc.put("gender", gender);
        doc.put("available", available);
        doc.put("bloodGroup", bloodGroup);
        doc.put("location", location);
        doc.put("lastDonation", lastDonation);
        doc.put("phone", phone);

        db.collection("profiles").document(userId).set(doc)
                .addOnCompleteListener(task -> {
                    pd.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getApplicationContext(), bloodGroup[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void updateDate() {
        String dateFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        lastDonation_et.setText(sdf.format(myCalendar.getTime()));
    }

    private void initializeViews() {
        phone_et = findViewById(R.id.phone_et);

        editProfileGender_rg = findViewById(R.id.editProfileGender_rg);
        male_radio = findViewById(R.id.male_radio);
        female_radio = findViewById(R.id.female_radio);
        other_radio = findViewById(R.id.other_radio);
        yes_radio = findViewById(R.id.yes_radio);
        no_radio = findViewById(R.id.no_radio);

        bloodGroup_spinner = findViewById(R.id.bloodGroup_spinner);

        location_et = findViewById(R.id.location_et);
        lastDonation_et = findViewById(R.id.lastDonation_et);

        save_fab = findViewById(R.id.save_fab);
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