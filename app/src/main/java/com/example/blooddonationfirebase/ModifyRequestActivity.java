package com.example.blooddonationfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationfirebase.models.Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

    private AppCompatImageButton clear_imgBtn;
    private TextView task_tv;
    private FloatingActionButton saveRequest_fab;
    private TextInputEditText patientName_et, location_et, date_et, unit_et, phone_et, note_et;
    private RadioButton male_radio, female_radio, other_radio;
    private Spinner bloodGroup_spinner;
    private ProgressDialog pd;

    private String id, userId, patientName, gender, bloodGroup, location, date, unit, phone, note, postedOn;

    String[] bloodGroupChoices = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_request);

        initializeViews();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        if (id != null) {
            setData(id);
        }
        clear_imgBtn.setOnClickListener(view -> {
            onBackPressed();
        });

        bloodGroup_spinner.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroupChoices);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroup_spinner.setAdapter(aa);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        };
        date_et.setOnClickListener(v -> new DatePickerDialog(ModifyRequestActivity.this, date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        saveRequest_fab.setOnClickListener(v -> {
            createOrUpdateRequest();
        });
    }

    private void setData(String id) {
        task_tv.setText("Update request");
    }

    private void createOrUpdateRequest() {
//        Toast.makeText(this, "Create or Update Request", Toast.LENGTH_SHORT).show();

//        pd = new ProgressDialog(ModifyRequestActivity.this);
//        pd.setTitle("Posting New Request...");
//        pd.show();

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
//                    pd.dismiss();
                    Toast.makeText(ModifyRequestActivity.this, "Request Created", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(ModifyRequestActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                })
                .addOnFailureListener(e -> {
//                    pd.dismiss();
                    Toast.makeText(ModifyRequestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        Toast.makeText(getApplicationContext(), bloodGroup[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void updateDate() {
        String dateFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        date_et.setText(sdf.format(myCalendar.getTime()));
    }

//    private TextWatcher addTextWatcher = new TextWatcher() {
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            patientName = patientName_et.getText().toString();
//            location = location_et.getText().toString();
//            date = date_et.getText().toString();
//            unit = unit_et.getText().toString();
//            note = note_et.getText().toString();
//
//            saveRequest_fab.setEnabled(!patientName.isEmpty() && !location.isEmpty()
//                    && !date.isEmpty() && !unit.isEmpty() && !note.isEmpty());
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) { }
//    };

    private void initializeViews() {
        task_tv = findViewById(R.id.task_tv);
        clear_imgBtn = findViewById(R.id.clear_imgBtn);

        patientName_et = findViewById(R.id.patientName_et);

        male_radio = findViewById(R.id.male_radio);
        female_radio = findViewById(R.id.female_radio);
        other_radio = findViewById(R.id.other_radio);

        bloodGroup_spinner = findViewById(R.id.bloodGroup_spinner);

        location_et = findViewById(R.id.location_et);
        date_et = findViewById(R.id.date_et);
        unit_et = findViewById(R.id.unit_et);
        phone_et = findViewById(R.id.phone_et);
        note_et = findViewById(R.id.note_et);

        saveRequest_fab = findViewById(R.id.saveRequest_fab);
    }
}