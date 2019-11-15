package com.daawtec.scancheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.macaron_card)
    CardView mMacaronCD;
    @BindView(R.id.menage_card)
    CardView mMenageCD;
    @BindView(R.id.distribution_card)
    CardView mDistributionCD;
    @BindView(R.id.rapport_card)
    CardView mRapportCD;
    @BindView(R.id.gestion_mild_card)
    CardView mGestionMildCD;
    @BindView(R.id.microplan_card)
    CardView mMicroplanCD;

    Intent mIntent;
    String mCodeAs, mCodeSd, mCodeAgentDenombrement, mCodeAgentDist, mCodeAgentIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        boolean isAgentDenombrement = intent.getBooleanExtra(Constant.KEY_IS_AGENT_DENOMBREMENT, false);
        mCodeAs = intent.getStringExtra(Constant.KEY_CODE_AGENT_AS);
        mCodeSd = intent.getStringExtra(Constant.KEY_CODE_AGENT_SD);
        mCodeAgentDenombrement = intent.getStringExtra(Constant.KEY_CODE_AGENT_DENOMBREMENT);
        mCodeAgentDist = intent.getStringExtra(Constant.KEY_CODE_AGENT_DIST);
        mCodeAgentIT = intent.getStringExtra(Constant.KEY_CODE_AGENT_IT);

        if (isAgentDenombrement){
            mDistributionCD.setVisibility(View.GONE);
            mGestionMildCD.setVisibility(View.GONE);
        } else {
            mMacaronCD.setVisibility(View.GONE);
            mMenageCD.setVisibility(View.GONE);
        }

        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @OnClick(R.id.macaron_card)
    public void startMacaronActivity(){
        mIntent = new Intent(DashboardActivity.this, BaseActivity.class);
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_MACARON_ACTIVITY);
        mIntent.putExtra(Constant.KEY_CODE_AGENT_AS, mCodeAs);
        mIntent.putExtra(Constant.KEY_CODE_AGENT_DENOMBREMENT, mCodeAgentDenombrement);
        mIntent.putExtra(Constant.KEY_CODE_AGENT_AS, Constant.ACTION_MACARON_ACTIVITY);
        startActivity(mIntent);
    }

    @OnClick(R.id.menage_card)
    public void startMenageActivity(){
        mIntent = new Intent(DashboardActivity.this, BaseActivity.class);
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_MENAGE_ACTIVITY);
        mIntent.putExtra(Constant.KEY_CODE_AGENT_DENOMBREMENT, mCodeAgentDenombrement);
        mIntent.putExtra(Constant.KEY_CODE_AGENT_IT, mCodeAgentIT);
        startActivity(mIntent);
    }

    @OnClick(R.id.distribution_card)
    public void startDistributionActivity(){
        mIntent = new Intent(DashboardActivity.this, BaseActivity.class);
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_DISTRIBUTION_ACTIVITY);
        startActivity(mIntent);
    }

    @OnClick(R.id.gestion_mild_card)
    public void startGestionMildActivity(){
        mIntent = new Intent(DashboardActivity.this, GestionMildActivity.class);
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_DISTRIBUTION_ACTIVITY);
        mIntent.putExtra(Constant.KEY_CODE_AGENT_SD, mCodeSd);
        startActivity(mIntent);
    }

    @OnClick(R.id.rapport_card)
    public void startRapportActivity(){
        mIntent = new Intent(DashboardActivity.this, BaseActivity.class);
        if (mCodeSd != null) {
            mIntent.putExtra(Constant.KEY_CODE_AGENT_SD, mCodeSd);
            mIntent.putExtra(Constant.ACTION, Constant.ACTION_RAPPORT_ACTIVITY);
            startActivity(mIntent);
        } else if (mCodeAs != null) {
            mIntent.putExtra(Constant.KEY_CODE_AGENT_AS, mCodeAs);
            mIntent.putExtra(Constant.ACTION, Constant.ACTION_RAPPORT_ACTIVITY);
            startActivity(mIntent);
        }

    }

    @OnClick(R.id.microplan_card)
    public void startMicroPlanActivity() {
        mIntent = new Intent(DashboardActivity.this, BaseActivity.class);
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_SITEDIST_ACTIVITY);
        startActivity(mIntent);
    }
}
