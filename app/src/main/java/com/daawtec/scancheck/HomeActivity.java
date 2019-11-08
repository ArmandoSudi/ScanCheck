package com.daawtec.scancheck;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.drm.DrmStore;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.AffectationMacaronAS;
import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.DivisionProvincialeSante;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.entites.TypeAgent;
import com.daawtec.scancheck.entites.ZoneSante;
import com.daawtec.scancheck.service.ScanCheckApi;
import com.daawtec.scancheck.service.ScanCheckApiInterface;
import com.daawtec.scancheck.ui.DashboardFragment;
import com.daawtec.scancheck.ui.MenageFragment;

import com.daawtec.scancheck.ui.RapportFragment;
import com.daawtec.scancheck.ui.ScanQRFragment;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.CreateAgentActivity;
import com.daawtec.scancheck.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    List<AgentDenombrement> mAgentDemos = new ArrayList<>();
    List<Macaron> mMacarons = new ArrayList<>();
    List<TypeAgent> mTypeAgents = new ArrayList<>();

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
                mAgentDemos.size() > 0)
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
        getTypeAgent();
        getAgentDemos();

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
            long[] agents = db.getIAgentDenombrementDao().insert(mAgentDemos);

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

    public void getAgents(){
        scanCheckApiInterface.getAgents().enqueue(new Callback<List<AgentDenombrement>>() {
            @Override
            public void onResponse(Call<List<AgentDenombrement>> call, Response<List<AgentDenombrement>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        mAgents.addAll(response.body());
                        Log.d(TAG, "onResponse: Liste des agents : " + mAgents);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AgentDenombrement>> call, Throwable t) {
                Log.e(TAG, "getAgent onFailure: " + t.getMessage());
            }
        });
    }

    public void getMacarons() {
        scanCheckApiInterface.getMacarons().enqueue(new Callback<List<Macaron>>() {
            @Override
            public void onResponse(Call<List<Macaron>> call, Response<List<Macaron>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mMacarons.addAll(response.body());
                        Log.d(TAG, "onResponse: Liste des macarons : " + mMacarons);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Macaron>> call, Throwable t) {
                Log.e(TAG, "getMacarons onFailure : " + t.getMessage());
            }
        });
    }

    public void getTypeAgent() {
        mTypeAgents.add(new TypeAgent("1001", "Enqueteur"));
        mTypeAgents.add(new TypeAgent("1002", "Distributeur"));
        mTypeAgents.add(new TypeAgent("1003", "Superviseur"));
    }

    public void getAgentDemos(){
        mAgentDemos.add(new AgentDenombrement("150000000000001", "David","ADR1001", "1001"));
        mAgentDemos.add(new AgentDenombrement("150000000000002", "Alain", "ADR1001", "1002"));
        mAgentDemos.add(new AgentDenombrement("150000000000003", "Willy", "ADR1001", "1003"));
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
