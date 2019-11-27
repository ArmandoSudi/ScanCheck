package com.daawtec.scancheck;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.adapters.MembreMenageAdapter;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.MembreMenage;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.GPSTracker;
import com.daawtec.scancheck.utils.Utils;

import java.util.Calendar;
import java.util.List;

public class CreateMenageActivity extends AppCompatActivity {

    private static final String TAG = "CreateMenageActivity";

    TextView mDateIdentificationTV, mCodeMacaronTV, mLatitudeTV, mLongitudeTV, mTypeMenageLabelTV;
    EditText mNomResponsableET, mPrenomResponsableET, mVillageET, mTailleMenageET, mRecoPrenomET, mRecoNomET, mNombreCouchetteET, mCommentaireET;
    Spinner mSexeSP, mTypeMenage;
    Button mSaveMenageBT, mGpsBT;
    Spinner mSiteDistributionSP;
    RecyclerView mMembreMenageRV;
    LinearLayout mMembreMenageLinearLayout;

    String mSexe;
    String  mDateIdentification, mCodeTypeMenage;
    double mLatitude=0.0, mLongitude=0.0;

    private Calendar mCalendar = Calendar.getInstance();

    ScanCheckDB db;
    String qrCode, mCodeSD;
    GPSAsyncTask gpsAsyncTask;
    SharedPreferences mSharedPref;
    String mCodeAgent, codeTypeAgent;
    final String codeMenage = Utils.generateId();
    MembreMenageAdapter mMembreMenageAdapter;

    boolean isAgentIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menage);

        db = ScanCheckDB.getDatabase(this);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (Build.VERSION.SDK_INT >= 23) {
            if (CreateMenageActivity.this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || CreateMenageActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,},
                        Constant.REQUEST_CODE_ASK_PERMISSIONS
                );

            }
        }

        mCodeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_AGENT, null);
        codeTypeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_TYPE_AGENT, null);

        Intent intent = getIntent();
        qrCode = intent.getStringExtra(Constant.CODE_QR);

        mMembreMenageAdapter = new MembreMenageAdapter(this);

        if (codeTypeAgent.equals(Constant.IT_DENOMBREMENT)) {
            isAgentIT = true;
            setTitle("Enregistrer ménage spécial");
        } else {
            setTitle("Enregistrer ménage");
        }

        //Return if the macaron is already affected to another menage
        checkMacaron(qrCode);

        if (qrCode == null) {
            Toast.makeText(this, "Code Macaron non disponible", Toast.LENGTH_SHORT).show();
            finish();
        }

        initView(isAgentIT);

    }

    public void initView(boolean isAgentIt) {
        mDateIdentificationTV = findViewById(R.id.date_identification_tv);
        mNomResponsableET = findViewById(R.id.nom_responsable_et);
        mPrenomResponsableET = findViewById(R.id.prenom_responsable_et);
        mVillageET = findViewById(R.id.village_et);
        mRecoNomET = findViewById(R.id.reco_nom_et);
        mRecoPrenomET = findViewById(R.id.reco_prenom_et);
        mTailleMenageET = findViewById(R.id.taille_menage_et);
        mNombreCouchetteET = findViewById(R.id.nombre_couchete_et);
        mCommentaireET = findViewById(R.id.commentaire_et);
        mMembreMenageLinearLayout = findViewById(R.id.membre_menage_layout);

        mCodeMacaronTV = findViewById(R.id.code_macaron_tv);
        mCodeMacaronTV.setText(qrCode + "");
        mLongitudeTV = findViewById(R.id.longitude_tv);
        mLatitudeTV = findViewById(R.id.latitude_tv);
        mTypeMenageLabelTV = findViewById(R.id.type_menage_label);

        mTypeMenage = findViewById(R.id.type_menage_sp);

        mMembreMenageRV = findViewById(R.id.membre_menage_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMembreMenageRV.setLayoutManager(linearLayoutManager);
        mMembreMenageRV.setHasFixedSize(true);
        mMembreMenageRV.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
        mMembreMenageRV.setAdapter(mMembreMenageAdapter);

        Button addMembreMenage = findViewById(R.id.add_membre_menage_bt);
        addMembreMenage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMembreMenageDialog(CreateMenageActivity.this);
            }
        });

        if (isAgentIt){
            mRecoNomET.setVisibility(View.GONE);
            mRecoPrenomET.setVisibility(View.GONE);
            mTailleMenageET.setVisibility(View.GONE);
            mMembreMenageLinearLayout.setVisibility(View.GONE);
        } else {
            mTypeMenageLabelTV.setVisibility(View.GONE);
            mTypeMenage.setVisibility(View.GONE);
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

                        gpsAsyncTask = new GPSAsyncTask(CreateMenageActivity.this);
                        gpsAsyncTask.execute();

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

        mTypeMenage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCodeTypeMenage = Utils.getCodeTypeMenage((String)parent.getItemAtPosition(position));
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
        String commentaire = mCommentaireET.getText().toString();
        int tailleMenage = Utils.stringToInt(mTailleMenageET.getText().toString()) ;
        int nombreCouchette = Utils.stringToInt(mNombreCouchetteET.getText().toString());

        // Pour les menages traditionnels, nous derivons le type par rapport au nombre des membres du menage
        if (!isAgentIT){
            mCodeTypeMenage = getTypeMenage(tailleMenage);
        }

        boolean isValid = true;

        // Validation Menage
        if (nomResponsable.equals("")){ isValid = false;}
        if (mCodeSD == null) isValid = false;
        if (nombreCouchette < 0) isValid = false;

        if (!isAgentIT) {
            if (tailleMenage == 0){ isValid = false;}
            if (village.equals("")) { isValid = false; }
            if (recoNom.equals("")) isValid = false;
            if (recoPrenom.equals("")) isValid = false;
        }

        if (isValid){

            mDateIdentification = Utils.formatDate(mCalendar.getTime());

            int nombreMILD;
            if (isAgentIT){
                nombreMILD = nombreCouchette;
            } else {
                nombreMILD = Utils.computeMildNumber(tailleMenage);
            }

            String codeMacaron = qrCode;
            Menage menage = new Menage(codeMenage, nomResponsable, mSexe, village, tailleMenage,
                    mDateIdentification, mCodeSD, nombreMILD, mLatitude, mLongitude, null, false);
            menage.recoNom = recoNom;
            menage.recoPrenom = recoPrenom;
            menage.nombreCouchette = nombreCouchette;
            menage.codeAgentDenombrement = mCodeAgent;
            menage.codeTypeMenage = mCodeTypeMenage;
            menage.commentaire = commentaire;
            menage.codeMacaron = codeMacaron;
            menage.PrenomResponsable = prenomResponsable;

            saveMenage(menage);

        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveMenage(final Menage menage){
        (new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected void onPostExecute(Boolean results) {
                super.onPostExecute(results);
                if (results) {
                    Toast.makeText(CreateMenageActivity.this, "Menage enregistré", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                } else {
                    Toast.makeText(CreateMenageActivity.this, "Erreur lors de l'enregistrement de Macaron", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                Log.e(TAG, "doInBackground: " + menage.toString() );
                boolean isSuccess = false;
                try {
                    db.getIMenageDao().insert(menage);
                    isSuccess = true;
                }catch (Exception ex){
                    Log.e(TAG, "doInBackground: " + ex.getMessage() );
                    isSuccess = false;
                }

                if (isSuccess) {
                    db.getIMacaronDao().updateMacaronState(true, menage.codeMacaron);
                    if (mMembreMenageAdapter.getItemCount() > 0) {
                        long[] membreMenageIds = db.getIMembreMenageDao().insert(mMembreMenageAdapter.all());
                        Log.e(TAG, "doInBackground: membre menages : " + membreMenageIds[0]);
                    }

                }
                return isSuccess;
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
                Affectation affectation = db.getIAffectation().getAffectationByAgent(mCodeAgent);
                return db.getISiteDistributionDao().get(affectation.CodeAs);
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

    public String getTypeMenage(int nombrePersonne){
        if (nombrePersonne > 0 && nombrePersonne < 3){
            return "1001";
        } else if (nombrePersonne > 2 && nombrePersonne < 5) {
            return "1002";
        } else if (nombrePersonne > 4 && nombrePersonne < 7){
            return "1003";
        } else if (nombrePersonne > 6 && nombrePersonne < 9) {
            return "1004";
        } else if (nombrePersonne >= 9) {
            return "1005";
        }
        return null;
    }

    public void showAddMembreMenageDialog(Context context) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enregistrer le membre menage");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_create_membre_menage, null);
        builder.setView(customLayout);
        final EditText nomMembreET = customLayout.findViewById(R.id.nom_membre_menage_et);
        final EditText prenomMembreET = customLayout.findViewById(R.id.prenom_membre_menage_et);
        final Spinner sexeSP = customLayout.findViewById(R.id.sexe_sp);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nom = nomMembreET.getText().toString();
                String prenom = prenomMembreET.getText().toString();
                String sexe = (String) sexeSP.getSelectedItem();
                MembreMenage membreMenage = new MembreMenage();
                membreMenage.CodeMembreMenage = Utils.getTimeStamp();
                membreMenage.Nom = nom;
                membreMenage.Prenom = prenom;
                membreMenage.sexe = sexe;
                membreMenage.CodeMenage = codeMenage;
                getMembreMenage(membreMenage);

            }
        });

        builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // create and show the alert dialog
        if (mMembreMenageAdapter.getItemCount() < 9) {
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(context, "Nombre maximum des membres ménage atteint", Toast.LENGTH_SHORT).show();
        }

    }

    public Void getMembreMenage(MembreMenage membreMenage){
        if (mMembreMenageAdapter.getItemCount() < 9) {
            mMembreMenageAdapter.add(membreMenage);
            mMembreMenageAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Nombre maximum de membre ménage atteint", Toast.LENGTH_SHORT).show();
        }

        return null;
    }
}
