package com.daawtec.scancheck;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.utils.Constant;

import java.util.List;

public class ParameterActivity extends AppCompatActivity {

    private static final String TAG = "ParameterActivity";

    Spinner mRecoSP, mAsSP, mSDSP;

    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;

    ScanCheckDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        db = ScanCheckDB.getDatabase(this);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        initView();
    }

    public void initView(){

        mAsSP = findViewById(R.id.as_sp);
        mRecoSP = findViewById(R.id.reco_sp);
        mSDSP = findViewById(R.id.sd_sp);
        loadAs();

        mAsSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AirsSante AS = (AirsSante) parent.getItemAtPosition(position);
                mEditor.putString(Constant.KEY_USER_AS, AS.getCodeAS());
                mEditor.commit();
                Log.i(TAG, "onItemSelected: AS parameter updated to -> AS: code_as: " + AS.CodeAS);
                loadReco(AS.getCodeAS());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRecoSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RelaisCommunautaire reco = (RelaisCommunautaire) parent.getItemAtPosition(position);
                mEditor.putString(Constant.KEY_USER_RECO, reco.getCodeReco());
                mEditor.commit();
                Log.i(TAG, "onItemSelected: Reco parameter updated to -> code_reco: " + reco.getCodeReco());
                loadSD(reco.codeReco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSDSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SiteDistribution SD = (SiteDistribution) parent.getItemAtPosition(position);
                mEditor.putString(Constant.KEY_USER_SD, SD.getCodeSD());
                mEditor.commit();
                Log.i(TAG, "onItemSelected: SD parameter updated to -> code_sc: " + SD.codeSD);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void loadAs(){
        (new AsyncTask<Void, Void, List<AirsSante>>(){
            @Override
            protected void onPostExecute(List<AirsSante> airsSantes) {
                super.onPostExecute(airsSantes);
                if (airsSantes != null && airsSantes.size() > 0 ) {
                    mAsSP.setAdapter(new ArrayAdapter<AirsSante>(
                            ParameterActivity.this,
                            android.R.layout.simple_spinner_item,
                            airsSantes
                    ));
                }
            }

            @Override
            protected List<AirsSante> doInBackground(Void... voids) {
                return db.getIAirSanteDao().all();
            }
        }).execute();
    }

    public void loadReco(final String asCode){
        (new AsyncTask<Void, Void, List<RelaisCommunautaire>>(){
            @Override
            protected void onPostExecute(List<RelaisCommunautaire> relaisCommunautaires) {
                super.onPostExecute(relaisCommunautaires);

                if (relaisCommunautaires != null && relaisCommunautaires.size() > 0) {
                    Log.e(TAG, "onPostExecute: " + relaisCommunautaires.get(0));
                    mRecoSP.setAdapter(new ArrayAdapter<RelaisCommunautaire>(
                            ParameterActivity.this,
                            android.R.layout.simple_spinner_item,
                            relaisCommunautaires));
                } else {
                    mRecoSP.setAdapter(null);
                }
            }

            @Override
            protected List<RelaisCommunautaire> doInBackground(Void... voids) {
                return db.getIRelaisCommunautaireDao().get(asCode);
            }
        }).execute();
    }

    public void loadSD(final String asCode){
        (new AsyncTask<Void, Void, List<SiteDistribution>>(){
            @Override
            protected void onPostExecute(List<SiteDistribution> siteDistributions) {
                super.onPostExecute(siteDistributions);

                if (siteDistributions != null && siteDistributions.size() > 0){
                    mSDSP.setAdapter(new ArrayAdapter<SiteDistribution>(
                            ParameterActivity.this,
                            android.R.layout.simple_spinner_item,
                            siteDistributions
                    ));
                } else {
                    mSDSP.setAdapter(null);
                }
            }

            @Override
            protected List<SiteDistribution> doInBackground(Void... voids) {
                return db.getISiteDistributionDao().get(asCode);
            }
        }).execute();
    }
}
