package com.daawtec.scancheck;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
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
import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.entites.AgentDistribution;
import com.daawtec.scancheck.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class LoginActivity extends AppCompatActivity {

    ScanCheckDB db;
    private static final String TAG = "LoginActivity";

    @BindView(R.id.code_et)
    EditText mCodeET;

    String mTypeAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        Button scanBT = findViewById(R.id.scan_bt);
        scanBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, QrCodeActivity.class);
//                startActivityForResult( intent, Constant.REQUEST_CODE_LOGIN);
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

    public void loginWithCode(final String code){

    }

    public void login(final String codeAuthentification){

        Log.e(TAG, "login: TYPE AGENT: " + mTypeAgent );
        (new AsyncTask<Void, Void, Boolean>(){
            AgentDenombrement agent;
            AgentDistribution agentDist;
            Intent intent;
            @Override
            protected void onPostExecute(Boolean value) {
                super.onPostExecute(value);

                if (value) startActivity(intent);
                else
                    Toast.makeText(LoginActivity.this, "Cet agent n'existe pas", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                if (mTypeAgent.equals(Constant.AGENT_DENOMBREMENT)) {
                    agent = db.getIAgentDenombrementDao().getAgentByCodeAuth(codeAuthentification);
                    if (agent != null) {
                        intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra(Constant.KEY_CODE_AGENT_AS, agent.codeAs);
                        intent.putExtra(Constant.KEY_CODE_AGENT_DENOMBREMENT, agent.codeAgentDenombrement);
                        intent.putExtra(Constant.KEY_IS_AGENT_DENOMBREMENT, true);
                        return true;
                    } else {
                        return false;
                    }

                } else if (mTypeAgent.equals("SITE DISTRIBUTION")) {
                    agentDist = db.getIAgentDistributionDao().getAgentByCodeAuth(codeAuthentification);
                    if (agentDist != null) {
                        agentDist = db.getIAgentDistributionDao().getAgentByCodeAuth(codeAuthentification);
                        intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra(Constant.KEY_CODE_AGENT_SD, agentDist.codeSd);
                        intent.putExtra(Constant.KEY_CODE_SITE_DISTRIBUTION, agentDist.codeSd);
                        return true;
                    } else {
                        return false;
                    }

                } else if (mTypeAgent.equals("IT DENOMBREMENT")){
                    agent = db.getIAgentDenombrementDao().getAgentByCodeAuth(codeAuthentification);
                    if (agent != null) {
                        intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra(Constant.KEY_CODE_AGENT_AS, agent.codeAs);
                        intent.putExtra(Constant.KEY_CODE_IT_DENOMBREMENT, agent.codeAgentDenombrement);
                        return true;
                    } else {
                        return false;
                    }
                } else if (mTypeAgent.equals("IT DISTRIBUTION")){
                    agent = db.getIAgentDenombrementDao().getAgentByCodeAuth(codeAuthentification);
                    if (agent != null) {
                        intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra(Constant.KEY_CODE_AGENT_AS, agent.codeAs);
                        intent.putExtra(Constant.KEY_CODE_IT_DISTRIBUTION, agent.codeAgentDenombrement);
                        return true;
                    } else {
                        return false;
                    }
                } else if (mTypeAgent.equals("BUREAU CENTRAL ZONE")){
                    agent = db.getIAgentDenombrementDao().getAgentByCodeAuth(codeAuthentification);
                    if (agent != null) {
                        intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra(Constant.KEY_CODE_AGENT_AS, agent.codeAs);
                        intent.putExtra(Constant.KEY_CODE_BUREAU_CENTRAL_ZONE, agent.codeAgentDenombrement);
                        return true;
                    } else {
                        return false;
                    }
                }


                return false;
            }
        }).execute();
    }
}
