package com.daawtec.scancheck;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Agent;
import com.daawtec.scancheck.entites.Campagne;
import com.daawtec.scancheck.service.ScanCheckApi;
import com.daawtec.scancheck.service.ScanCheckApiInterface;
import com.daawtec.scancheck.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ScanCheckDB db;
    private static final String TAG = "LoginActivity";

    @BindView(R.id.code_et)
    EditText mCodeET;

    @BindView(R.id.actualiser_tv)
    TextView mActualiserTV;

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
                    || LoginActivity.this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || LoginActivity.this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || LoginActivity.this.checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.VIBRATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
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

        mActualiserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra(Constant.KEY_ACTION_ACTUALISER, Constant.ACTION_ACTUALISER);
                startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_activity_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_recuperer_parametres){

        }

        return true;
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
        Affectation affectation;
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

            agent = db.getIAgentDao().getAgentByAuth(codeAuthentification);

            // L'agent existe deja dans la base des donnees
            if(agent != null ){

                affectation = db.getIAffectation().getAffectationByAgent(agent.CodeAgent);
                if(affectation != null) {

                    // retouner FAUX si le role choisi pour l'authentification n'est pas affecte a l'agent.
                    if(!affectation.codeTypeAgent.equals(mTypeAgent)){
                        return false;
                    }

                    // Enregistrer de facon global la date de debut de la campagne
                    //mEditor.putString(Constant.KEY_DATE_DEBUT_CAMPAGNE, affectation.dateAffectation);

                    // Enregistrer de facon globable le role de l'agent
                    if (mTypeAgent.equals(Constant.AGENT_DENOMBREMENT)) {
                        mEditor.putString(Constant.KEY_CURRENT_CODE_TYPE_AGENT, Constant.AGENT_DENOMBREMENT);
                    } else if (mTypeAgent.equals(Constant.IT_DENOMBREMENT)) {
                        mEditor.putString(Constant.KEY_CURRENT_CODE_TYPE_AGENT, Constant.IT_DENOMBREMENT);
                    }else{
                        return false;
                    }

                    // Enregistrer de facon globale le code de l'agent
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
