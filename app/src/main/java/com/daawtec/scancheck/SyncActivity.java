package com.daawtec.scancheck;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.BadVerification;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.service.ScanCheckApi;
import com.daawtec.scancheck.service.ScanCheckApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncActivity extends AppCompatActivity {

    private static final String TAG = "SyncActivity";

    TextView mMenageMsgTV, mMacaronMsgTV;
    Button mSyncBT;

    ScanCheckDB db;
    ScanCheckApiInterface scanCheckApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        db = ScanCheckDB.getDatabase(this);
        scanCheckApiInterface = ScanCheckApi.getService();

        initView();
    }

    public void initView() {
        mMenageMsgTV = findViewById(R.id.post_menage_msg_tv);
        mMacaronMsgTV = findViewById(R.id.post_macaron_msg_tv);
        mSyncBT = findViewById(R.id.sync_bt);
        mSyncBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncData();
            }
        });
    }

    public void syncData(){
        getMenages();
        getMacarons();
        //TODO UNCOMMENT WHEN THE API WILL BE AVAILABLE
//        getInventairePhysiques();
//        getBadVerifications();
    }

    void postMenages(List<Menage> menages){
        scanCheckApiInterface.postMenage(menages).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String serverResponse = response.body();
                mMenageMsgTV.setText(serverResponse);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    void postMacarons(final List<Macaron> macarons){
        Log.e(TAG, "postMacarons: SIZE OF MACARONS : " + macarons.size() );
        scanCheckApiInterface.postMacarons(macarons).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String serverResponse = (String) response.body();
                mMacaronMsgTV.setText(serverResponse);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    void postInventairePhysiques(List<InventairePhysique> inventairePhysiques){
        scanCheckApiInterface.postInventairesPhysiques(inventairePhysiques).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String serverResponse = response.body();
                //TODO UPDATE TEXTVIEW
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    void postBadVerifications(List<BadVerification> badVerifications){
        scanCheckApiInterface.postBadVerification(badVerifications).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String serverResponse = response.body();
                //TODO UPDATE THE TEXTVIEW
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    void getMenages(){
        (new AsyncTask<Void, Void, List<Menage>>(){
            @Override
            protected void onPostExecute(List<Menage> menages) {
                super.onPostExecute(menages);

                if (menages != null){
                    if (menages.size() > 0) {
                        Log.d(TAG, "onPostExecute: POSTING MENAGE");
                        postMenages(menages);
                    } else {
                        Log.d(TAG, "onPostExecute: NO MENAGE FOUND IN THE DATABASE");
                    }
                } else {
                    Log.d(TAG, "onPostExecute: ERROR LOADING MENAGE FROM DATABASE");
                }
            }

            @Override
            protected List<Menage> doInBackground(Void... voids) {
                return db.getIMenageDao().all();
            }
        }).execute();
    }

    void getMacarons() {
        (new AsyncTask<Void, Void, List<Macaron>>(){
            @Override
            protected void onPostExecute(List<Macaron> macarons) {
                super.onPostExecute(macarons);

                if(macarons != null){
                    if (macarons.size() > 0){
                        Log.d(TAG, "onPostExecute: POSTING MACARON");
                        postMacarons(macarons);
                    } else {
                        Log.d(TAG, "onPostExecute: NO MACARON FOUND IN THE DATABASE");
                    }
                } else {
                    Log.d(TAG, "onPostExecute: ERROR LOADING MACARON FROM DATABASE");
                }
            }

            @Override
            protected List<Macaron> doInBackground(Void... voids) {
                return db.getIMacaronDao().all();
            }
        }).execute();
    }

    void getInventairePhysiques(){
        (new AsyncTask<Void, Void, List<InventairePhysique>>(){
            @Override
            protected void onPostExecute(List<InventairePhysique> inventairePhysiques) {
                super.onPostExecute(inventairePhysiques);

                if(inventairePhysiques != null){
                    if(inventairePhysiques.size() > 0){
                        Log.d(TAG, "onPostExecute: POSTING INVENTAIRE PHYSIQUE");
                        postInventairePhysiques(inventairePhysiques);
                    } else {
                        Log.d(TAG, "onPostExecute: NO INVENTAIRE PHYSIQUE FOUND IN THE DB");
                    }
                } else {
                    Log.d(TAG, "onPostExecute: ERROR LOADING INVENTAIRE PHYSIQUES FROM DATABASE");
                }
            }

            @Override
            protected List<InventairePhysique> doInBackground(Void... voids) {
                return db.getIIventairePhysiqueDao().all();
            }
        }).execute();
    }

    void getBadVerifications() {
        (new AsyncTask<Void, Void, List<BadVerification>>(){
            @Override
            protected void onPostExecute(List<BadVerification> badVerifications) {
                super.onPostExecute(badVerifications);
                if (badVerifications != null){
                    if (badVerifications.size() > 0){
                        Log.d(TAG, "onPostExecute: POSTING BAD VERIFICATIONS");
                        postBadVerifications(badVerifications);
                    } else {
                        Log.d(TAG, "onPostExecute: NO BAD-VERIFICATION FOUND IN THE DATABASE");
                    }
                } else {
                    Log.d(TAG, "onPostExecute: ERROR LOADING BAD-VERIFICATION");
                }
            }

            @Override
            protected List<BadVerification> doInBackground(Void... voids) {
                return db.getIBadVerificationDao().all();
            }
        }).execute();
    }
}
