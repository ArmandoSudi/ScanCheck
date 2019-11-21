package com.daawtec.scancheck;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Agent;
import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.entites.AgentDistribution;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.DivisionProvincialeSante;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.entites.TypeAgent;
import com.daawtec.scancheck.entites.TypeMenage;
import com.daawtec.scancheck.entites.ZoneSante;
import com.daawtec.scancheck.service.ScanCheckApi;
import com.daawtec.scancheck.service.ScanCheckApiInterface;

import com.daawtec.scancheck.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    ProgressDialog mProgressDialog;
    ProgressBar mLoadingPG;

    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;

    ScanCheckApiInterface scanCheckApiInterface;
    ScanCheckDB db;

    List<DivisionProvincialeSante> mDPSs = new ArrayList<>();
    List<ZoneSante> mZSs = new ArrayList<>();
    List<AirsSante> mASs = new ArrayList<>();
    List<SiteDistribution> mSDs = new ArrayList<>();
    List<AgentDenombrement> mAgents = new ArrayList<>();
    List<AgentDistribution> mAgentDistDemos = new ArrayList<>();
    List<AgentDenombrement> mAgentDenombrementDemos = new ArrayList<>();
    List<TypeAgent> mTypeAgents = new ArrayList<>();
    List<Agent> mAgentDemos = new ArrayList<>();
    List<Affectation> mAffectationDemos = new ArrayList<>();
    List<TypeAgent> mTypeAgentDemos = new ArrayList<>();
    List<TypeMenage> mTypeMenageDemos = new ArrayList<>();

    public static final int REQUEST_CODE_QR_SCAN = 101;
    boolean isAgentRegistered;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = ScanCheckDB.getDatabase(this);

        scanCheckApiInterface = ScanCheckApi.getService();
        mProgressDialog = new ProgressDialog(this);
        mLoadingPG = findViewById(R.id.loading_pb);

//        showProgressDiag(mProgressDialog);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        boolean isInitialized = mSharedPref.getBoolean(Constant.KEY_IS_INITIALIZED, false);


        if(!isInitialized){
            getInitialData();
        } else {
            isAgentRegistered = mSharedPref.getBoolean(Constant.KEY_IS_AGENT_REGISTERED, false);
            if (isAgentRegistered){
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (HomeActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || HomeActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || HomeActivity.this.checkSelfPermission(Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED
                || HomeActivity.this.checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.VIBRATE,
                                Manifest.permission.CAMERA},
                        Constant.REQUEST_CODE_ASK_PERMISSIONS
                );
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public void canInsert(){
        if (mDPSs.size() > 0 &&
                mZSs.size() > 0 &&
                mASs.size() > 0 &&
                mSDs.size() > 0 &&
                mTypeAgents.size() > 0 &&
                mAgentDenombrementDemos.size() > 0 &&
                mAgentDistDemos.size() > 0 &&
                mAffectationDemos.size() > 0 &&
                mAgentDemos.size() > 0 &&
                mTypeMenageDemos.size() > 0)
        {
            Log.i(TAG, "canInsert: INSERTING VALUES IN THE DATABASE");
            new InitDB(db).execute();
        }
    }

    public void getInitialData() {

        Log.d(TAG, "getInitialData: INITIALIZING DATA");
//        showProgressDiag(mProgressDialog);
//        mLoadingPG.setVisibility(View.VISIBLE);

        getDivisionSantes();
        getAiresSante();
        getZoneSante();
        getSiteDistributions();
        getAgentDenombrementDemos();
        getAgenDistributionsDemo();

        // Pour la demo de jeudi 21/11/2019
        getAffectationsDemos();
        getAgentDemos();
        getTypeAgent();
        getTypemenageDemo();

    }

    public class InitDB extends AsyncTask<Void, Void, Void>{

        private static final String TAG = "InitDB";

        ScanCheckDB db;

        public InitDB(ScanCheckDB db) {
            this.db = db;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDiag(mProgressDialog);
//            mLoadingPG.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.i(TAG, "onPostExecute: DATABASE INITIALIZED ");
            // pour ne pas initialiser la base des donnees lors de redemarrage ulterieurs
            mEditor.putBoolean(Constant.KEY_IS_INITIALIZED, true);
            mEditor.commit();
            hideProgressDiag(mProgressDialog);
//            mLoadingPG.setVisibility(View.INVISIBLE);

            // Go to set the parameter of the user for the first initialization of the data of the app
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.i(TAG, "doInBackground: INITIALIZING DATA");

            long[] dps_ids = db.getIDivisionProvincialSanteDao().insert(mDPSs);
            long[] zs_ids = db.getIZoneSanteDao().insert(mZSs);
            long[] as_ids = db.getIAirSanteDao().insert(mASs);
            long[] sd_ids = db.getISiteDistributionDao().insert(mSDs);
            long[] typeAgents = db.getITypeAgentDao().insert(mTypeAgents);
            long[] agents = db.getIAgentDenombrementDao().insert(mAgentDenombrementDemos);
            long [] agentDist = db.getIAgentDistributionDao().insert(mAgentDistDemos);
            long[] agent_ids = db.getIAgentDao().insert(mAgentDemos);
            long[] affecation_ids = db.getIAffectation().insert(mAffectationDemos);
            long[] typeMenage_ids = db.getITypeMenageDao().insert(mTypeMenageDemos);

            return null;
        }
    }

    public void getDivisionSantes(){
        scanCheckApiInterface.getDPS().enqueue(new Callback<List<DivisionProvincialeSante>>() {
            @Override
            public void onResponse(Call<List<DivisionProvincialeSante>> call, Response<List<DivisionProvincialeSante>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        mDPSs.addAll(response.body());
                        Log.d(TAG, "onResponse: List of DivisionProvincialSante : " + mDPSs.size());
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DivisionProvincialeSante>> call, Throwable t) {
                Log.e(TAG, "getdivisionSante onFailure: NETWORK FAILURE");
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void getAiresSante() {
        scanCheckApiInterface.getAirSantes().enqueue(new Callback<List<AirsSante>>() {
            @Override
            public void onResponse(Call<List<AirsSante>> call, Response<List<AirsSante>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        mASs.addAll(response.body());
                        Log.d(TAG, "onResponse: List of AireSante : " + mASs.size());
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AirsSante>> call, Throwable t) {
                Log.e(TAG, "getAirSante onFailure: NETWORK FAILURE");
            }
        });
    }

    public void getZoneSante() {
        scanCheckApiInterface.getZoneSante().enqueue(new Callback<List<ZoneSante>>() {
            @Override
            public void onResponse(Call<List<ZoneSante>> call, Response<List<ZoneSante>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        mZSs.addAll(response.body());
                        Log.d(TAG, "onResponse: List of ZoneSante : " + mZSs.size());
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ZoneSante>> call, Throwable t) {
                Log.e(TAG, "getZoneSante onFailure: NETWORK FAILURE");
            }
        });
    }

    public void getSiteDistributions(){
        scanCheckApiInterface.getSiteDistribution().enqueue(new Callback<List<SiteDistribution>>() {
            @Override
            public void onResponse(Call<List<SiteDistribution>> call, Response<List<SiteDistribution>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        mSDs.addAll(response.body());
                        Log.d(TAG, "onResponse: Liste des SiteDistribution : " + mSDs.size());
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SiteDistribution>> call, Throwable t) {
                Log.e(TAG, "getSiteDistribution onFailure: NETWORK FAILURE");
            }
        });
    }

    public void getTypeAgent() {
        mTypeAgents.add(new TypeAgent("1001", "AGENT DENOMBREMENT"));
        mTypeAgents.add(new TypeAgent("1002", "IT DENOMBREMENT"));
    }

    public void getAgentDenombrementDemos(){
        mAgentDenombrementDemos.add(new AgentDenombrement("1501", "David","ADR1001", "1001"));
        mAgentDenombrementDemos.add(new AgentDenombrement("1502", "Alain", "ADR1001", "1002"));
        mAgentDenombrementDemos.add(new AgentDenombrement("1503", "Willy", "ADR1001", "1003"));
        mAgentDenombrementDemos.add(new AgentDenombrement("1504", "Willy", "ADR1001", "1004"));
    }

    public void getAgentDemos(){
        mAgentDemos.add(new Agent("1", "David", "1111"));
        mAgentDemos.add(new Agent("2", "Willy", "0000"));
    }

    public void getAffectationsDemos() {
        mAffectationDemos.add(new Affectation("1", "1", "1001", "ADR1001", "20/11/2019"));
        mAffectationDemos.add(new Affectation("2", "2", "1002", "ADR1001", "20/11/2019"));
    }

    public void getAgenDistributionsDemo() {
        mAgentDistDemos.add(new AgentDistribution("100001", "Agent Dist 1", "role 1", "000001", "1001"));
        mAgentDistDemos.add(new AgentDistribution("100002", "Agent Dist 2", "role 2", "000001", "1002"));
        mAgentDistDemos.add(new AgentDistribution("100002", "Agent Dist 2", "role 2", "000001", "1003"));
    }

    public void getTypemenageDemo(){
        mTypeMenageDemos.add(new TypeMenage("1", "Ménage 1-2", "MENAGE TRADITIONNEL"));
        mTypeMenageDemos.add(new TypeMenage("2", "Ménage 3-4", "MENAGE TRADITIONNEL"));
        mTypeMenageDemos.add(new TypeMenage("3", "Ménage 4-6", "MENAGE TRADITIONNEL"));
        mTypeMenageDemos.add(new TypeMenage("4", "Ménage 7-8", "MENAGE TRADITIONNEL"));
        mTypeMenageDemos.add(new TypeMenage("5", "Ménage 9 ou plus", "MENAGE TRADITIONNEL"));
        mTypeMenageDemos.add(new TypeMenage("6", "ORPHELINAT", "MENAGE SPECIAL"));
        mTypeMenageDemos.add(new TypeMenage("7", "COUVENT", "MENAGE SPECIAL"));
        mTypeMenageDemos.add(new TypeMenage("8", "INTERNAT", "MENAGE SPECIALSPECIAL"));
        mTypeMenageDemos.add(new TypeMenage("9", "FOSA", "MENAGE SPECIAL"));
        mTypeMenageDemos.add(new TypeMenage("10", "HOTEL", "MENAGE SPECIAL"));
        mTypeMenageDemos.add(new TypeMenage("11", "MILITAIRE EN DEPLACEMENT", "MENAGE SPECIAL"));
        mTypeMenageDemos.add(new TypeMenage("12", "DEPLACES", "MENAGE SPECIAL"));
        mTypeMenageDemos.add(new TypeMenage("13", "REFUGIE", "MENAGE SPECIAL"));
        mTypeMenageDemos.add(new TypeMenage("14", "PRISON", "MENAGE SPECIAL"));
    }

    void showProgressDiag(ProgressDialog progressDiag){
        progressDiag.setMessage("Initialization des donnees. Rassurez vous que l'internet est active");
        progressDiag.setCancelable(false);
        progressDiag.show();
    }

    void hideProgressDiag(ProgressDialog progressDiag){
        progressDiag.dismiss();
    }

}
