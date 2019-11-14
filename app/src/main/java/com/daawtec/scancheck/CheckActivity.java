package com.daawtec.scancheck;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.utils.Constant;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckActivity extends AppCompatActivity {

    private static final String TAG = "CheckActivity";

    ScanCheckDB db;
    String mCodeAgentSuperviseur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCodeAgentSuperviseur = intent.getStringExtra(Constant.KEY_CODE_AGENT_SUPERVISEUR);

        if (mCodeAgentSuperviseur == null){
            Toast.makeText(this, "Impossible de recevoir le code de l'agent superviseur", Toast.LENGTH_SHORT).show();
            finish();
        }

        db = ScanCheckDB.getDatabase(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constant.REQUEST_CODE_CHECK_AGENT) {
            if(data==null)
                return;
            //Getting the passed result
            String qrCode = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.i(TAG, "onActivityResult: codeQR: " + qrCode );

            checkAgent(qrCode);
        }
    }

    @OnClick(R.id.scan_bt)
    public void scan(){
        Intent intent = new Intent(CheckActivity.this, QrCodeActivity.class);
        startActivityForResult( intent, Constant.REQUEST_CODE_CHECK_AGENT);
    }

    public void checkAgent(final String codeQR){
        (new AsyncTask<Void, Void, Boolean>(){
            AgentDenombrement agent;
            @Override
            protected void onPostExecute(Boolean value) {
                super.onPostExecute(value);

                if (value){
                    Intent intent = new Intent(CheckActivity.this, SupervisionActivity.class);
                    intent.putExtra(Constant.KEY_CODE_AGENT_SUPERVISEUR, mCodeAgentSuperviseur);
                    intent.putExtra(Constant.KEY_CODE_AGENT_ENQUETEUR, agent.codeAgentDenombrement);
                    startActivity(intent);
                }
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                agent = db.getIAgentDenombrementDao().get(codeQR);
                if (agent instanceof AgentDenombrement){
                        return true;
                } else {
                    return false;
                }
            }
        }).execute();
    }
}
