package com.example.blooddonationfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationfirebase.models.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class ModifyRequestActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    final Calendar myCalendar = Calendar.getInstance();

    private FloatingActionButton saveRequest_fab;
    private TextInputLayout patientName_til, location_til, date_til, unit_til, phone_til, note_til;
    private TextInputEditText patientName_et, location_et, date_et, unit_et, phone_et, note_et;
    private RadioButton male_radio, female_radio, other_radio;
    private Spinner bloodGroup_spinner;
    private ProgressDialog pd;
    private Request req;
    ArrayAdapter aa;

    String[] bloodGroupChoices = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    private String id, userId, patientName, gender, bloodGroup, location, date, unit, phone, note, postedOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_request);

        Intent intent = getIntent();
        id = intent.getStringExtra("reqId");

        if (id != null) {
            getSupportActionBar().setTitle("Update Request");
        } else {
            getSupportActionBar().setTitle("Post New Request");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViews();

        if (id != null) {
            setData(id);
        }

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ModifyRequestActivity.this, date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        date_et.setOnClickListener(v -> datePickerDialog.show());

        saveRequest_fab.setOnClickListener(v -> {
            if (id != null) {
                updateRequest(id);
            } else {
                createRequest();
            }
        });
    }

    private void setData(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                    Toast.makeText(ModifyRequestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showData(Request req) {
        patientName_et.setText(req.getPatientName());

        if (req.getGender().equals("Male")) {
            male_radio.setChecked(true);
        } else if (req.getGender().equals("Female")) {
            female_radio.setChecked(true);
        } else if (req.getGender().equals("Other")) {
            other_radio.setChecked(true);
        }

        bloodGroup_spinner.setSelection(aa.getPosition(req.getBloodGroup()));

        location_et.setText(req.getLocation());
        date_et.setText(req.getNeededWithin());
        unit_et.setText(req.getUnit());
        phone_et.setText(req.getPhone());
        note_et.setText(req.getNote());
    }

    private void updateRequest(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference requestRef = db
                .collection("requests")
                .document(req.getId());

        patientName = patientName_et.getText().toString();

        if (male_radio.isChecked()) {
            gender = "Male";
        } else if (female_radio.isChecked()) {
            gender = "Female";
        } else if (other_radio.isChecked()) {
            gender = "Other";
        }
        bloodGroup = bloodGroup_spinner.getSelectedItem().toString();
        location = location_et.getText().toString();
        date = date_et.getText().toString();
        unit = unit_et.getText().toString();
        phone = phone_et.getText().toString();
        note = note_et.getText().toString();

        Request r = new Request(req.getId(), req.getUserId(), patientName,
                gender, bloodGroup, location, date, unit, phone, note, req.getPostedOn());

        requestRef.set(r).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ModifyRequestActivity.this, "Request Updated", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(ModifyRequestActivity.this, RequestDetailsActivity.class);
                    i.putExtra("id", id);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    makeSnackBarMessage("Failed to update.");
                }
            }
        });
    }

    private void createRequest() {
        if (!checkForErrors()) {
            pd = new ProgressDialog(ModifyRequestActivity.this);
            pd.setTitle("Processing...");
            pd.show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            id = UUID.randomUUID().toString();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userId = user.getUid();
            patientName = patientName_et.getText().toString();

            if (male_radio.isChecked()) {
                gender = "Male";
            } else if (female_radio.isChecked()) {
                gender = "Female";
            } else if (other_radio.isChecked()) {
                gender = "Other";
            }
            bloodGroup = bloodGroup_spinner.getSelectedItem().toString();
            location = location_et.getText().toString();
            date = date_et.getText().toString();
            unit = unit_et.getText().toString();
            phone = phone_et.getText().toString();
            note = note_et.getText().toString();

            String pattern = "dd/MM/yyyy";
            String dateInString = new SimpleDateFormat(pattern).format(new Date());
            postedOn = dateInString;

            Map<String, Object> doc = new HashMap<>();
            doc.put("id", id);
            doc.put("userId", userId);
            doc.put("patientName", patientName);
            doc.put("gender", gender);
            doc.put("bloodGroup", bloodGroup);
            doc.put("location", location);
            doc.put("neededWithin", date);
            doc.put("unit", unit);
            doc.put("phone", phone);
            doc.put("note", note);
            doc.put("postedOn", postedOn);

            db.collection("requests").document(id).set(doc)
                    .addOnCompleteListener(task -> {
                        pd.dismiss();
                        Toast.makeText(ModifyRequestActivity.this, "Request Created", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(ModifyRequestActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    })
                    .addOnFailureListener(e -> {
                        pd.dismiss();
                        Toast.makeText(ModifyRequestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private boolean checkForErrors() {
        patientName = patientName_et.getText().toString();
        location = location_et.getText().toString();
        date = date_et.getText().toString();
        unit = unit_et.getText().toString();
        phone = phone_et.getText().toString();
        note = note_et.getText().toString();

        if (patientName.isEmpty()) {
            patientName_til.setError("Enter the patient's name");
            return true;
        } else {
            patientName_til.setError(null);
        }

        if (location.isEmpty()) {
            location_til.setError("Enter the location");
            return true;
        } else {
            location_til.setError(null);
        }

        if (date.isEmpty()) {
            date_til.setError("Enter a date");
            return true;
        } else {
            date_til.setError(null);
        }

        if (unit.isEmpty()) {
            unit_til.setError("Enter the number of unit(s)");
            return true;
        } else {
            unit_til.setError(null);
        }

        if (phone.isEmpty()) {
            phone_til.setError("Enter a contact number");
            return true;
        } else {
            phone_til.setError(null);
        }

        if (note.isEmpty()) {
            note_til.setError("Write a note please");
            return true;
        } else {
            note_til.setError(null);
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getApplicationContext(), bloodGroup[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void makeSnackBarMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    private void updateDate() {
        String dateFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        date_et.setText(sdf.format(myCalendar.getTime()));
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
        patientName_et = findViewById(R.id.patientName_et);
        location_et = findViewById(R.id.location_et);
        date_et = findViewById(R.id.date_et);
        unit_et = findViewById(R.id.unit_et);
        phone_et = findViewById(R.id.phone_et);
        note_et = findViewById(R.id.note_et);

        male_radio = findViewById(R.id.male_radio);
        female_radio = findViewById(R.id.female_radio);
        other_radio = findViewById(R.id.other_radio);

        bloodGroup_spinner = findViewById(R.id.bloodGroup_spinner);

        patientName_til = findViewById(R.id.patientName_til);
        location_til = findViewById(R.id.location_til);
        date_til = findViewById(R.id.date_til);
        unit_til = findViewById(R.id.unit_til);
        phone_til = findViewById(R.id.phone_til);
        note_til = findViewById(R.id.note_til);

        saveRequest_fab = findViewById(R.id.saveRequest_fab);
    }
}