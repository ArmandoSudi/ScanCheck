package com.daawtec.scancheck;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.GPSTracker;
import com.daawtec.scancheck.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateMenageActivity extends AppCompatActivity {

    private static final String TAG = "CreateMenageActivity";

    TextView mDateIdentificationTV, mCodeMacaronTV, mLatitudeTV, mLongitudeTV;
    EditText mNomResponsableET, mPrenomResponsableET, mVillageET, mTailleMenageET, mRecoPrenomET, mRecoNomET, mNombreCouchetteET;
    Spinner mSexeSP;
    Button mSaveMenageBT, mGpsBT;
    Spinner mSiteDistributionSP;

    String mSexe;
    Date mDateIdentification;
    double mLatitude=0.0, mLongitude=0.0;

    private Calendar mCalendar = Calendar.getInstance();

    ScanCheckDB db;
    String qrCode, mCodeSD, mCodeAgentIT;
    GPSAsyncTask gpsAsyncTask;

    boolean isAgentIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menage);

        db = ScanCheckDB.getDatabase(this);

        Intent intent = getIntent();
        qrCode = intent.getStringExtra(Constant.CODE_QR);
        mCodeAgentIT = intent.getStringExtra(Constant.KEY_CODE_AGENT_IT);

        if (mCodeAgentIT != null) isAgentIT = true;

        //Return if the macaron is already affected to another menage
        checkMacaron(qrCode);

        if (qrCode == null) {
            Toast.makeText(this, "Code Macaron non disponible", Toast.LENGTH_SHORT).show();
            finish();
        }

        initView(qrCode);

    }

    public void initView(String codeMacaron) {
        mDateIdentificationTV = findViewById(R.id.date_identification_tv);
        mNomResponsableET = findViewById(R.id.nom_responsable_et);
        mPrenomResponsableET = findViewById(R.id.prenom_responsable_et);
        mVillageET = findViewById(R.id.age_responsable_et);
        mRecoNomET = findViewById(R.id.reco_nom_et);
        mRecoPrenomET = findViewById(R.id.reco_prenom_et);
        mTailleMenageET = findViewById(R.id.taille_menage_et);
        mNombreCouchetteET = findViewById(R.id.nombre_couchete_et);

        mCodeMacaronTV = findViewById(R.id.code_macaron_tv);
        mCodeMacaronTV.setText(qrCode + "");
        mLongitudeTV = findViewById(R.id.longitude_tv);
        mLatitudeTV = findViewById(R.id.latitude_tv);

        if (isAgentIT){
            mVillageET.setVisibility(View.GONE);
            mRecoNomET.setVisibility(View.GONE);
            mRecoPrenomET.setVisibility(View.GONE);
            mTailleMenageET.setVisibility(View.GONE);
        }

        mSaveMenageBT = findViewById(R.id.save_menage_bt);
        mSaveMenageBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectMenage();
            }
        });

        mGpsBT = findViewById(R.id.coordonee_gps_bt);
        mGpsBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (CreateMenageActivity.this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || CreateMenageActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(
                                new String[]{
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION,},
                                Constant.REQUEST_CODE_ASK_PERMISSIONS
                        );
                    } else {
                        gpsAsyncTask = new GPSAsyncTask(CreateMenageActivity.this);
                        gpsAsyncTask.execute();
                    }
                }
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

        mSiteDistributionSP = findViewById(R.id.sd_sp);
        mSiteDistributionSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SiteDistribution sd = (SiteDistribution) parent.getItemAtPosition(position);
                mCodeSD = sd.codeSD;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadSD();

    }

    public void collectMenage(){

        String nomResponsable = mNomResponsableET.getText().toString();
        String prenomResponsable = mPrenomResponsableET.getText().toString();
        String village = mVillageET.getText().toString();
        String recoNom = mRecoNomET.getText().toString();
        String recoPrenom = mRecoPrenomET.getText().toString();
        int tailleMenage = Utils.stringToInt(mTailleMenageET.getText().toString()) ;
        int nombreCouchette = Utils.stringToInt(mNombreCouchetteET.getText().toString());

        String codeMenage = Utils.getTimeStamp();

        boolean isValid = true;

        // Validation Menage
        if (nomResponsable.equals("")){ isValid = false;}

        if (tailleMenage == 0){ isValid = false;}
        if (mCodeSD == null) isValid = false;
        if (mLatitude == 0.0) isValid = false;
        if (mLongitude == 0.0) isValid = false;

        if (!isAgentIT) {
            if (village.equals("")) { isValid = false; }
            if (recoNom.equals("")) isValid = false;
            if (recoPrenom.equals("")) isValid = false;
        }

        if (isValid){

            mDateIdentification = mCalendar.getTime();

            int nombreMILD = Utils.computeMildNumber(tailleMenage);
            String codeMacaron = qrCode;
            Menage menage = new Menage(codeMenage, nomResponsable + " " + prenomResponsable, mSexe, village, tailleMenage,
                    mDateIdentification, mCodeSD, nombreMILD, mLatitude, mLongitude, codeMacaron, false);
            menage.recoNom = recoNom;
            menage.recoPrenom = recoPrenom;
            menage.nombreCouchette = nombreCouchette;

            saveMenage(menage);

        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveMenage(final Menage menage){
        (new AsyncTask<Void, Void, long[]>(){
            @Override
            protected void onPostExecute(long[] results) {
                super.onPostExecute(results);
                if (results[0] > 0) {
                    Toast.makeText(CreateMenageActivity.this, "Menage enregistré", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                } else {
                    Toast.makeText(CreateMenageActivity.this, "Erreur lors de l'enregistrement de Macaron", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected long[] doInBackground(Void... voids) {
                long[] results = db.getIMenageDao().insert(menage);
                if (results[0] > 0) {
                    db.getIMacaronDao().updateMacaronState(true, menage.codeMacaron);
                }
                return results;
            }
        }).execute();
    }

    /**
     * Verifie si le macaron de ce code macaron est deja affecte a un reco, si oui, continuer l'enregistrement du
     * @param codeMacaron
     */
    public void checkMacaron(final String codeMacaron){
        (new AsyncTask<Void, Void, Macaron>(){
            @Override
            protected void onPostExecute(Macaron macaron) {
                super.onPostExecute(macaron);
                if (macaron != null){
                    if (macaron.isAffected) {
                        Toast.makeText(CreateMenageActivity.this, "Macaron déjà affecté", Toast.LENGTH_SHORT).show();
                        CreateMenageActivity.this.finish();
                    }

                } else {
                    Toast.makeText(CreateMenageActivity.this, "Créer le ménage pour le macaron scanné", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Macaron doInBackground(Void... voids) {
                return db.getIMacaronDao().get(codeMacaron);
            }
        }).execute();
    }

    public void loadSD(){
        (new AsyncTask<Void, Void, List<SiteDistribution>>(){
            @Override
            protected void onPostExecute(List<SiteDistribution> siteDistributions) {
                super.onPostExecute(siteDistributions);

                if (siteDistributions != null) {
                    if (siteDistributions.size() > 0) {
                        siteDistributions.add(0, new SiteDistribution());
                        mSiteDistributionSP.setAdapter(new ArrayAdapter<>(
                                CreateMenageActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                siteDistributions
                        ));
                    }
                }
            }

            @Override
            protected List<SiteDistribution> doInBackground(Void... voids) {
                return db.getISiteDistributionDao().all();
            }
        }).execute();

    }

    class GPSAsyncTask extends AsyncTask<Void, Object, Object> {

        private Context context;
        private ProgressDialog progressDialog;
        private double latitude;
        private double longitude;

        private GPSTracker gps;

        public GPSAsyncTask(Context context) {
            this.context = context;
            this.gps = new GPSTracker(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(
                    context,
                    "Rechercher des coordonnées GPS",
                    "Veuillez patientez pendant la recherche...",
                    true,
                    true,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(true);
                        }
                    }
            );

            if (!gps.canGetLocation()) {
                gps.showSettingsAlert();
            }
        }

        @Override
        protected Object doInBackground(Void... voids) {
            if (gps.canGetLocation()) {
                gps.getLocation();
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                publishProgress("Longitude: " + longitude + " et Latitude: " + latitude);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.dismiss();

            Log.e(TAG, "onPostExecute : " + "LATITUDE : " + latitude + " LONGITUDE : " + longitude  );
            mLatitude = latitude;
            mLongitude = longitude;

            mLatitudeTV.setText(Utils.getDecimalFormat().format(latitude) + " degré");
            mLongitudeTV.setText(Utils.getDecimalFormat().format(longitude) + " degré");

        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);

            progressDialog.setMessage(String.valueOf(values[0]));
        }

        @Override
        protected void onCancelled(Object o) {
            super.onCancelled(o);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Log.e(TAG, "onCancelled: " + "LATITUDE : " + latitude + " LONGITUDE : " + longitude  );
        }
    }
}
