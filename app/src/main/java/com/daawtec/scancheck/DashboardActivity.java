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
    @BindView(R.id.denombrement_card)
    CardView mDenombrementCD;

    Intent mIntent;
    String mCodeAs, mCodeAgentDenombrement, mCodeSd, mCodeItDenombrement, mCodeItDistribution, mCodeBureauCentralZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        boolean isAgentDenombrement = intent.getBooleanExtra(Constant.KEY_IS_AGENT_DENOMBREMENT, false);
        mCodeAs = intent.getStringExtra(Constant.KEY_CODE_AGENT_AS);

        mCodeAgentDenombrement = intent.getStringExtra(Constant.KEY_CODE_AGENT_DENOMBREMENT);
        mCodeSd = intent.getStringExtra(Constant.KEY_CODE_AGENT_SD);
        mCodeItDenombrement = intent.getStringExtra(Constant.KEY_CODE_IT_DENOMBREMENT);
        mCodeItDistribution = intent.getStringExtra(Constant.KEY_CODE_IT_DISTRIBUTION);
        mCodeBureauCentralZone = intent.getStringExtra(Constant.KEY_CODE_BUREAU_CENTRAL_ZONE);

        if (mCodeAgentDenombrement != null){
            mMicroplanCD.setVisibility(View.GONE);
            mGestionMildCD.setVisibility(View.GONE);
            mDistributionCD.setVisibility(View.GONE);
            mDenombrementCD.setVisibility(View.GONE);
        } else if (mCodeSd != null) {
            mMicroplanCD.setVisibility(View.GONE);
            mMacaronCD.setVisibility(View.GONE);
            mDistributionCD.setVisibility(View.GONE);
            mDenombrementCD.setVisibility(View.GONE);
        } else if (mCodeItDenombrement != null){
            mDistributionCD.setVisibility(View.GONE);
            mGestionMildCD.setVisibility(View.GONE);
            mDenombrementCD.setVisibility(View.GONE);
        } else if (mCodeItDistribution != null) {
            mMacaronCD.setVisibility(View.GONE);
            mMicroplanCD.setVisibility(View.GONE);
            mMenageCD.setVisibility(View.GONE);
            mDenombrementCD.setVisibility(View.GONE);
        } else if (mCodeBureauCentralZone != null){
            mMacaronCD.setVisibility(View.GONE);
            mMicroplanCD.setVisibility(View.GONE);
            mMenageCD.setVisibility(View.GONE);
            mGestionMildCD.setVisibility(View.GONE);
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
        mIntent.putExtra(Constant.KEY_CODE_AGENT_IT, mCodeItDenombrement);
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
        if (mCodeSd != null){
            mIntent = new Intent(DashboardActivity.this, GestionMildSiteDistActivity.class);
            mIntent.putExtra(Constant.KEY_CODE_AGENT_SD, mCodeSd);
        } else if (mCodeItDistribution != null){
            mIntent = new Intent(DashboardActivity.this, GestionMildActivity.class);
            mIntent.putExtra(Constant.KEY_CODE_IT_DISTRIBUTION, mCodeItDistribution);
        }

        mIntent.putExtra(Constant.ACTION, Constant.ACTION_DISTRIBUTION_ACTIVITY);
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
