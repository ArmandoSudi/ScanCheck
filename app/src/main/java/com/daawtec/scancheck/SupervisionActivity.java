package com.daawtec.scancheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupervisionActivity extends AppCompatActivity {

    private static final String TAG = "SupervisionActivity";

    ScanCheckDB db = ScanCheckDB.getDatabase(this);
    String mCodeAgentSuperviSeur, mCodeAgentEnqueteur;

    @BindView(R.id.radioEstSurTerrain)
    RadioGroup radioGroup1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCodeAgentSuperviSeur = intent.getStringExtra(Constant.KEY_CODE_AGENT_SUPERVISEUR);
        mCodeAgentEnqueteur = intent.getStringExtra(Constant.KEY_CODE_AGENT_ENQUETEUR);

        Toast.makeText(this, mCodeAgentSuperviSeur + " : " + mCodeAgentEnqueteur, Toast.LENGTH_SHORT).show();

    }

    public void saveSupervision(){

    }

    public void collectData() {
         int selected = radioGroup1.getCheckedRadioButtonId();
    }
}
