package com.daawtec.scancheck.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Agent;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.MembreMenage;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.service.ScanCheckApi;
import com.daawtec.scancheck.service.ScanCheckApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "SyncAsyncTask";

    Activity mActivity;
    ScanCheckApiInterface scanCheckApiInterface;
    ScanCheckDB db;

    List<Menage> menages = new ArrayList<>();
    List<Macaron> macarons = new ArrayList<>();
    List<Affectation> affectations = new ArrayList<>();
    List<SiteDistribution> siteDistributions = new ArrayList<>();
    List<MembreMenage> membreMenages = new ArrayList<>();
    List<Agent> agents = new ArrayList<>();

    ProgressDialog mProgressDialog;

    public SyncAsyncTask(Activity mActivity) {
        this.mActivity = mActivity;
        db = ScanCheckDB.getDatabase(mActivity);
        scanCheckApiInterface = ScanCheckApi.getService();
        mProgressDialog = new ProgressDialog(mActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        showProgressDiag(mProgressDialog);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        hideProgressDiag(mProgressDialog);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            menages = db.getIMenageDao().all();
            macarons = db.getIMacaronDao().all();
            affectations = db.getIAffectation().all();
            agents = db.getIAgentDao().all();
            siteDistributions = db.getISiteDistributionDao().all();
            membreMenages = db.getIMembreMenageDao().all();
        } catch (Exception ex){
            Log.e(TAG, "doInBackground: " + ex.getMessage() );
        }

        if (macarons.size() > 0) postMacaron(macarons);
        if (agents.size() > 0) postAgents(agents);
        if (affectations.size() > 0) {
            for (Affectation affectation : affectations){
                updateAffecation(affectation.codeAffectation, affectation.populationMacroPlan);
                Log.e(TAG, "doInBackground: POPULATION : " + affectation.populationMacroPlan );
            }

            //TODO DECOMMENTER L'UPLOAD DES AFFECTATIONS
            postAffectation(affectations);
        }
        if (siteDistributions.size() > 0) postSDs(siteDistributions);

        if (menages.size() > 0) postMenage(menages);
        if (membreMenages.size() > 0) postMembreMenage(membreMenages);


        getSDs(db);

        return null;
    }

    private void postMenage(List<Menage> menages){
        try {
            Response<String> rMenage = scanCheckApiInterface.postMenage(menages).execute();
            if (rMenage != null && rMenage.isSuccessful()){
                Log.i(TAG, "postMenage: " + rMenage.body() );
            }
        } catch (Exception ex){
            Log.e(TAG, "postMenage: " + ex.getMessage() );
        }

    }

    private void postMacaron(List<Macaron> macarons){
        try {
            Response<String> rMacarons = scanCheckApiInterface.postMacarons(macarons).execute();
            if (rMacarons != null && rMacarons.isSuccessful()){
                Log.i(TAG, "postMacaron: " + rMacarons.body());
            }
        } catch (Exception ex){
            Log.e(TAG, "postMenage: " + ex.getMessage() );
        }
    }

    private void updateAffecation(String codeAffectaiton, int nombrePopulation){
        try{
            Response<String> rAffectation = scanCheckApiInterface.updatePopulationMacroPlan(codeAffectaiton, nombrePopulation).execute();
            if (rAffectation != null && rAffectation.isSuccessful()){
                Log.i(TAG, "updateAffecation: " + rAffectation.body());
            }
        } catch (Exception ex){
            Log.e(TAG, "updateAffecation: " + ex.getMessage() );
        }
    }

    private void postSDs(List<SiteDistribution> sd){
        try {
            Response<String> rSd = scanCheckApiInterface.postSds(sd).execute();
            if (rSd != null && rSd.isSuccessful()){
                Log.i(TAG, "postSDs: " + rSd.body());
            }
        } catch (Exception ex){
            Log.e(TAG, "postSDs: " + ex.getMessage() );
        }
    }

    private void postMembreMenage(List<MembreMenage> membreMenages){
        try {
            Response<String> rSd = scanCheckApiInterface.postMembreMenage(membreMenages).execute();
            if (rSd != null && rSd.isSuccessful()){
                Log.i(TAG, "postMembreMenage: " + rSd.body());
            }
        } catch (Exception ex){
            Log.e(TAG, "postMembreMenage: " + ex.getMessage() );
        }
    }

    public void postAgents(List<Agent> agents){
        try {
            Response<String> rAgent = scanCheckApiInterface.postAgents(agents).execute();
            if (rAgent != null && rAgent.isSuccessful()){
                Log.i(TAG, "postAgents: " + rAgent.body());
            }
        } catch (Exception ex){
            Log.e(TAG, "postAgents: " + ex.getMessage() );
        }
    }

    public void postAffectation(List<Affectation> affectations){
        try {
            Response<String> rAffectations = scanCheckApiInterface.postAffectations(affectations).execute();
            if (rAffectations != null && rAffectations.isSuccessful()){
                Log.i(TAG, "postAffectations: " + rAffectations.body());
            }
        } catch (Exception ex){
            Log.e(TAG, "postAffectations: " + ex.getMessage() );
        }
    }

    // Recuperer les SDs crees par le IT pour les agents denombrement
    private void getSDs(ScanCheckDB db){
        try {
            Response<List<SiteDistribution>> rSds = scanCheckApiInterface.getSiteDistribution().execute();
            if (rSds != null && rSds.isSuccessful()) {
                List<SiteDistribution> sds = rSds.body();
                Log.i(TAG, "getSDs: Nombre des SDs: " + sds.size());
                if (sds.size() > 0) db.getISiteDistributionDao().insert(sds);
            }
        } catch (Exception ex){
            Log.e(TAG, "getSDs: " + ex.getMessage());
        }
    }

    void showProgressDiag(ProgressDialog progressDiag){
        progressDiag.setMessage("Synchronisation des données. Rassurez vous que l'internet est active");
        progressDiag.setCancelable(false);
        progressDiag.show();
    }

    void hideProgressDiag(ProgressDialog progressDiag){
        progressDiag.dismiss();
    }
}
