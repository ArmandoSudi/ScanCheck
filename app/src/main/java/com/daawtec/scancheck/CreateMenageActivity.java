package com.daawtec.scancheck;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateMenageActivity extends AppCompatActivity {

    private static final String TAG = "CreateMenageActivity";

    TextView mDateIdentificationTV;
    EditText mNomResponsableET, mAgeResponsableET, mTailleMenageET, mNumeroMacaronET;
    Spinner mSexeSP;
    ImageView mDateIdentificationIV;
    Button mSaveMenageBT;

    String mSexe;
    Date mDateIdentification;

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
        mNomResponsableET = findViewById(R.id.nom_responsable_et);
        mAgeResponsableET = findViewById(R.id.age_responsable_et);
        mTailleMenageET = findViewById(R.id.taille_menage_et);
        mNumeroMacaronET = findViewById(R.id.numero_macaron_et);

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

        mDateIdentificationIV = findViewById(R.id.date_identification_bt);
        mDateIdentificationIV.setOnClickListener(new View.OnClickListener() {
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
                collectMenage();
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

//                mRecoSP.setAdapter(new ArrayAdapter<RelaisCommunautaire>(
//                        CreateMenageActivity.this,
//                        android.R.layout.simple_spinner_item,
//                        relaisCommunautaires));
            }

            @Override
            protected List<RelaisCommunautaire> doInBackground(Void... voids) {
                return db.getIRelaisCommunautaireDao().all();
            }
        }).execute();
    }

    public void collectMenage(){
        String nomResponsable = mNomResponsableET.getText().toString();
        int ageResponsable = Utils.stringToInt(mAgeResponsableET.getText().toString());
        String tailleMenage = mTailleMenageET.getText().toString();
        String numeroMacaron = mNumeroMacaronET.getText().toString();
        String codeMenage = Utils.getTimeStamp();

        boolean isValid = true;

        if (nomResponsable.equals("")){ isValid = false;}
        if (ageResponsable ==0 ) { isValid = false; }
        if (tailleMenage.equals("")){ isValid = false;}
        if(numeroMacaron.equals("")){ isValid = false;}
        if (mDateIdentification == null) { isValid = false; }

        if (isValid){
            Menage menage = new Menage(codeMenage, nomResponsable, mSexe, ageResponsable, tailleMenage, mDateIdentification, numeroMacaron);
            saveMenage(menage);
        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }

    }

    public void saveMenage(final Menage menage){
        (new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(CreateMenageActivity.this, "Menage enregistre", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateMenageActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                db.getIMenageDao().insert(menage);
                return null;
            }
        }).execute();
    }
}
