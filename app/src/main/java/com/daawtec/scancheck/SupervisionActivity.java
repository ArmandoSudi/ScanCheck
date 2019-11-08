package com.daawtec.scancheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.utils.Constant;

public class SupervisionActivity extends AppCompatActivity {

    private static final String TAG = "SupervisionActivity";

    ScanCheckDB db = ScanCheckDB.getDatabase(this);
    String mCodeAgentSuperviSeur, mCodeAgentEnqueteur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision);

        Intent intent = getIntent();
        mCodeAgentSuperviSeur = intent.getStringExtra(Constant.KEY_CODE_AGENT_SUPERVISEUR);
        mCodeAgentEnqueteur = intent.getStringExtra(Constant.KEY_CODE_AGENT_ENQUETEUR);

        Toast.makeText(this, mCodeAgentSuperviSeur + " : " + mCodeAgentEnqueteur, Toast.LENGTH_SHORT).show();

    }

    public void saveSupervision(){

    }
}
