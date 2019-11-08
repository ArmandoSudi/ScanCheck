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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.utils.Constant;

public class LoginActivity extends AppCompatActivity {

    ScanCheckDB db;
    private static final String TAG = "LoginActivity";

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

        db = ScanCheckDB.getDatabase(this);

        Button scanBT = findViewById(R.id.scan_bt);
        scanBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, QrCodeActivity.class);
                startActivityForResult( intent, Constant.REQUEST_CODE_LOGIN);
            }
        });
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

            login(qrCode);
        }
    }

    public void login(final String codeQR){
        (new AsyncTask<Void, Void, String>(){
            private final String AGENT_ENQUETEUR = "agent_enqueteur";
            private final String AGENT_SUPERVISEUR = "agent_superviseur";
            private final String AGENT_DISTRIBUTEUR = "agent_distributeur";
            AgentDenombrement agent;
            Intent intent;
            @Override
            protected void onPostExecute(String value) {
                super.onPostExecute(value);

                if (value != null) {
                    switch (value){
                        case AGENT_ENQUETEUR:
                            intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtra(Constant.KEY_CODE_AGENT_AS, agent.codeAs);
                            intent.putExtra(Constant.KEY_CODE_AGENT_DENOMBREMENT, agent.codeAgentDenombrement);
                            startActivity(intent);
                            break;
                        case AGENT_SUPERVISEUR:
                            intent = new Intent(LoginActivity.this, CheckActivity.class);
                            intent.putExtra(Constant.KEY_CODE_AGENT_AS, agent.codeAs);
                            intent.putExtra(Constant.KEY_CODE_AGENT_SUPERVISEUR, agent.codeAgentDenombrement);
                            startActivity(intent);
                            break;
                        case AGENT_DISTRIBUTEUR:
                            intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtra(Constant.KEY_CODE_AGENT_AS, agent.codeAs);
                            intent.putExtra(Constant.KEY_CODE_AGENT_DENOMBREMENT, agent.codeAgentDenombrement);
                            startActivity(intent);
                            break;
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Code de l'agent n'est pas retrouvé", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                agent = db.getIAgentDenombrementDao().get(codeQR);
                if (agent instanceof AgentDenombrement){
                    if (agent.codeTypeAgent.equals("1001")){
                       // L'agent est un enqueteur
                        Log.d(TAG, "doInBackground: AGENT EST ENQUETEUR: " + agent.codeTypeAgent);
                       return AGENT_ENQUETEUR;
                    } else if (agent.codeTypeAgent.equals("1002")){
                        // L;agent est un distributeur
                        Log.d(TAG, "doInBackground: AGENT EST DISTRIBUTEUR: " + agent.codeTypeAgent);
                        return AGENT_DISTRIBUTEUR;
                    } else if (agent.codeTypeAgent.equals("1003")){
                        // L'agent est un superviseur
                        Log.d(TAG, "doInBackground: AGENT EST SUPERVISEUR: " + agent.codeTypeAgent);
                        return AGENT_SUPERVISEUR;
                    }
                } else {
                    return null;
                }
                return null;
            }
        }).execute();
    }
}
