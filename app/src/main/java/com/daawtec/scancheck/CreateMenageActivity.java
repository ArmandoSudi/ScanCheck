package com.daawtec.scancheck;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.SiteDistribution;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateMenageActivity extends AppCompatActivity {

    private static final String TAG = "CreateMenageActivity";

    TextView mDateIdentificationTV, mDateAffectationTV;
    EditText mNomResponsableET, mAgeResponsableET, mTailleMenageET;
    Spinner mSexeSP, mRecoSP, mSiteDistributionSP;
    ImageButton mDateIdentificationIBT, mDateAffectationIBT;
    Button mSaveMenageBT;

    String mSexe, mRecoCode, mSiteDistributionCode;
    Date mDateAffecation, mDateIdentification;

    private Calendar mCalendar = Calendar.getInstance();

    ScanCheckDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menage);

        db = ScanCheckDB.getDatabase(this);

        initView();
    }

    public void initView() {
        mDateIdentificationTV = findViewById(R.id.date_identification_tv);
        mDateAffectationTV = findViewById(R.id.date_affectation_tv);
        mNomResponsableET = findViewById(R.id.nom_responsable_et);
        mAgeResponsableET = findViewById(R.id.age_responsable_et);
        mTailleMenageET = findViewById(R.id.taille_menage_et);

        final DatePickerDialog.OnDateSetListener dateAffectationListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(mDateAffectationTV);
                mDateAffecation = mCalendar.getTime();
            }
        };

        final DatePickerDialog.OnDateSetListener dateIdentificationListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(mDateIdentificationTV);
                mDateIdentification  = mCalendar.getTime();
            }
        };

        mDateAffectationIBT = findViewById(R.id.date_affecation_bt);
        mDateAffectationIBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateMenageActivity.this, dateAffectationListener, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mDateIdentificationIBT = findViewById(R.id.date_identification_bt);
        mDateIdentificationIBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateMenageActivity.this, dateIdentificationListener, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mSaveMenageBT = findViewById(R.id.save_menage_bt);
        mSaveMenageBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                saveMenage();
            }
        });

        mSexeSP = findViewById(R.id.sexe_sp);
        mSexeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSexe = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mRecoSP = findViewById(R.id.relais_communautaire_sp);
        mRecoSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RelaisCommunautaire reco = (RelaisCommunautaire) parent.getItemAtPosition(position);
                mRecoCode = reco.codeReco;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        populateReco();

        mSiteDistributionSP = findViewById(R.id.site_distribution_sp);
        mSiteDistributionSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SiteDistribution sd = (SiteDistribution) parent.getItemAtPosition(position);
                mSiteDistributionCode = sd.codeSD;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void updateLabel(TextView view) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        view.setText(sdf.format(mCalendar.getTime()));
    }

    public void populateReco(){
        (new AsyncTask<Void, Void, List<RelaisCommunautaire>>(){
            @Override
            protected void onPostExecute(List<RelaisCommunautaire> relaisCommunautaires) {
                super.onPostExecute(relaisCommunautaires);

                mRecoSP.setAdapter(new ArrayAdapter<RelaisCommunautaire>(
                        CreateMenageActivity.this,
                        android.R.layout.simple_spinner_item,
                        relaisCommunautaires));
            }

            @Override
            protected List<RelaisCommunautaire> doInBackground(Void... voids) {
                return db.getIRelaisCommunautaireDao().all();
            }
        }).execute();
    }

    public void saveMenage(){
        Log.e(TAG, "saveMenage: DATE AFFECATION: " + mDateAffecation );
        Log.e(TAG, "saveMenage: DATE IDENTIFICATION: " + mDateIdentification );
    }
}
