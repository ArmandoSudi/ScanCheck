package com.daawtec.scancheck;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Agent;
import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.entites.AgentDistribution;
import com.daawtec.scancheck.entites.Campagne;
import com.daawtec.scancheck.service.ScanCheckApi;
import com.daawtec.scancheck.service.ScanCheckApiInterface;
import com.daawtec.scancheck.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ScanCheckDB db;
    private static final String TAG = "LoginActivity";

    @BindView(R.id.code_et)
    EditText mCodeET;

    String mTypeAgent;
    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;

    ScanCheckApiInterface scanCheckApiInterface;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        if (Build.VERSION.SDK_INT >= 23) {
            if (LoginActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || LoginActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || LoginActivity.this.checkSelfPermission(Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED
                    || LoginActivity.this.checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

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

        ButterKnife.bind(this);

        db = ScanCheckDB.getDatabase(this);
        scanCheckApiInterface = ScanCheckApi.getService();

        Button scanBT = findViewById(R.id.scan_bt);
        scanBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String code = mCodeET.getText().toString();
            login(code);

            }
        });
    }

    @OnItemSelected(R.id.type_agent_sp)
    public void spinnerItemSelected(Spinner spinner, int position) {
        mTypeAgent = (String) spinner.getItemAtPosition(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constant.REQUEST_CODE_LOGIN) {
            if(data==null)
                return;
            //Getting the passed result
            String qrCode = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.i(TAG, "onActivityResult: codeQR: " + qrCode );

//            login(qrCode);
        }
    }

    public void login(final String codeAuthentification){

        Log.e(TAG, "login: TYPE AGENT: " + mTypeAgent );
        new AuthentificationTask(codeAuthentification).execute();
    }

    final class AuthentificationTask extends AsyncTask<Void, Void, Boolean>{

        public String codeAuthentification;

        public AuthentificationTask(String codeAuthentification) {
            this.codeAuthentification = codeAuthentification;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress("Authentification en cours...");
        }

        Agent agent;
        Intent intent;
        @Override
        protected void onPostExecute(Boolean value) {
            super.onPostExecute(value);

            hideProgress();

            if (value) startActivity(intent);
            else
                Toast.makeText(LoginActivity.this, "Cet agent n'existe pas", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (mTypeAgent.equals(Constant.AGENT_DENOMBREMENT)) {

                agent = db.getIAgentDao().getAgentByAuth(codeAuthentification);

                if (agent != null) {
                    // C'est un agent de denombrement qui vas utiliser l'application
                    mEditor.putString(Constant.KEY_CURRENT_CODE_TYPE_AGENT, Constant.AGENT_DENOMBREMENT);
                    mEditor.putString(Constant.KEY_CURRENT_CODE_AGENT, agent.CodeAgent);
                    mEditor.commit();
                    intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    return true;
                }
            }

            else if (mTypeAgent.equals(Constant.IT_DENOMBREMENT)){

                agent = db.getIAgentDao().getAgentByAuth(codeAuthentification);

                if (agent != null) {
                    // C'est un IT denombrement qui va utiliser l'application
                    mEditor.putString(Constant.KEY_CURRENT_CODE_TYPE_AGENT, Constant.IT_DENOMBREMENT);
                    mEditor.putString(Constant.KEY_CURRENT_CODE_AGENT, agent.CodeAgent);
                    mEditor.commit();
                    intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    return true;
                }
            }

            if(agent == null){

                Agent agent = null;
                Affectation affectation = null;
                Campagne campagne = null;

                try {
                    Response<Agent> rAgent = scanCheckApiInterface.getAgent(codeAuthentification).execute();
                    if (rAgent != null && rAgent.isSuccessful()) {

                        agent = rAgent.body();

                        if(agent != null){

                            Response<Affectation> rAffectation = scanCheckApiInterface.getAffectation(agent.CodeAgent).execute();
                            if (rAffectation != null && rAffectation.isSuccessful()) {

                                affectation = rAffectation.body();

                                if(affectation != null){

                                    Response<Campagne> rCampagne = scanCheckApiInterface.getCampagne(affectation.CodeCampagne).execute();
                                    if (rCampagne != null && rCampagne.isSuccessful()) {

                                        campagne = rCampagne.body();
                                    }
                                }
                            }
                        }
                    }
                }catch (Exception ex){

                }

                if(agent != null && affectation != null && mTypeAgent.equals(affectation.codeTypeAgent)){

                    if (campagne != null) {
                        try {
                            db.getICampagneDao().insert(campagne);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    try {
                        db.getIAgentDao().insert(agent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }

                    if(affectation != null){
                        try {
                            db.getIAffectation().insert(affectation);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    mEditor.putString(Constant.KEY_CURRENT_CODE_TYPE_AGENT, affectation.codeTypeAgent);
                    mEditor.putString(Constant.KEY_CURRENT_CODE_AGENT, agent.CodeAgent);
                    mEditor.commit();
                    intent = new Intent(LoginActivity.this, DashboardActivity.class);

                    return true;
                }
            }

            return false;
        }
    }

    protected void showProgress(final String message) {
        hideProgress();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressDialog.show(LoginActivity.this, "", message);
            }
        });
    }

    protected void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }
}
