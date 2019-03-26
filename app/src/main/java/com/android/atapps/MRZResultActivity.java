package com.android.atapps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import io.scanbot.mrzscanner.model.MRZRecognitionResult;

public class MRZResultActivity extends AppCompatActivity {
    public static final String EXTRA_documentCode = "documentCode";
    public static final String EXTRA_firstName = "firstName";
    public static final String EXTRA_lastName = "lastName";
    public static final String EXTRA_issuingStateOrOrganization = "issuingStateOrOrganization";
    public static final String EXTRA_departmentOfIssuance = "departmentOfIssuance";
    public static final String EXTRA_nationality = "nationality";
    public static final String EXTRA_dateOfBirth = "dateOfBirth";
    public static final String EXTRA_gender = "gender";
    public static final String EXTRA_dateOfExpiry = "dateOfExpiry";
    public static final String EXTRA_personalNumber = "personalNumber";
    public static final String EXTRA_travelDocTyp = "travelDocType";


    public static Intent newIntent(Context context, MRZRecognitionResult result) {
        Intent intent = new Intent(context, MRZResultActivity.class);
        intent.putExtra(EXTRA_documentCode, result.documentCode);
        intent.putExtra(EXTRA_firstName, result.firstName);
        intent.putExtra(EXTRA_lastName, result.lastName);
        intent.putExtra(EXTRA_issuingStateOrOrganization, result.issuingStateOrOrganization);
        intent.putExtra(EXTRA_nationality, result.nationality);
        intent.putExtra(EXTRA_dateOfBirth, result.dateOfBirth);
        intent.putExtra(EXTRA_gender, result.gender);
        intent.putExtra(EXTRA_dateOfExpiry, result.dateOfExpiry);
        intent.putExtra(EXTRA_personalNumber, result.personalNumber);
        intent.putExtra(EXTRA_travelDocTyp, result.travelDocType.name());
        return intent;
    }

    TextInputEditText et_fullname,et_passportNp,et_country,et_dob,et_gender,et_dateOfExpiry,et_issue,et_docType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrz_result);


        et_fullname = findViewById(R.id.et_fullname);
        et_passportNp = findViewById(R.id.et_passportNp);
        et_country = findViewById(R.id.et_country);
        et_dob = findViewById(R.id.et_dob);
        et_gender = findViewById(R.id.et_gender);
        et_dateOfExpiry = findViewById(R.id.et_dateOfExpiry);
        et_issue = findViewById(R.id.et_issue);
        et_docType = findViewById(R.id.et_docType);


        et_fullname.setText(getIntent().getStringExtra(EXTRA_lastName)+" "+getIntent().getStringExtra(EXTRA_firstName));
        et_passportNp.setText(getIntent().getStringExtra(EXTRA_documentCode));
        et_country.setText(getIntent().getStringExtra(EXTRA_nationality));
        et_dob.setText(getIntent().getStringExtra(EXTRA_dateOfBirth));
        et_gender.setText(getIntent().getStringExtra(EXTRA_gender));
        et_dateOfExpiry.setText(getIntent().getStringExtra(EXTRA_dateOfExpiry));
        et_issue.setText(getIntent().getStringExtra(EXTRA_issuingStateOrOrganization));
        et_docType.setText(getIntent().getStringExtra(EXTRA_travelDocTyp));
    }

}
