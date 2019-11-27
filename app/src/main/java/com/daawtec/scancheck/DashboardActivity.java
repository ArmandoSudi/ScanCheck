package com.daawtec.scancheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.SyncAsyncTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

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
    @BindView(R.id.ajouter_agent_card)
    CardView mAjouterAgentCD;

    Intent mIntent;
    String mCodeAs, mCodeAgentDenombrement, mCodeSd, mCodeItDenombrement, mCodeItDistribution, mCodeBureauCentralZone;

    SharedPreferences mSharedPref;
    String mCodeAgent, mCodeTypeAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mCodeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_AGENT, null);
        mCodeTypeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_TYPE_AGENT, null);

        Log.e(TAG, "onCreate: CODE AGENT: " + mCodeAgent);
        Log.e(TAG, "onCreate: CODE TYPE-AGENT: " + mCodeTypeAgent);

        ButterKnife.bind(this);

        if (mCodeTypeAgent != null && mCodeTypeAgent.equals(Constant.AGENT_DENOMBREMENT)){
            mMicroplanCD.setVisibility(View.GONE);
            mGestionMildCD.setVisibility(View.GONE);
            mDistributionCD.setVisibility(View.GONE);
            mDenombrementCD.setVisibility(View.GONE);
            mAjouterAgentCD.setVisibility(View.GONE);
        } else if (mCodeTypeAgent != null && mCodeTypeAgent.equals(Constant.IT_DENOMBREMENT)) {
            mDistributionCD.setVisibility(View.GONE);
            mGestionMildCD.setVisibility(View.GONE);
            mDenombrementCD.setVisibility(View.GONE);
        }

        ButterKnife.bind(this);
    }

//    public void initView(){
//        if (mCodeAgentDenombrement != null){
//            mMicroplanCD.setVisibility(View.GONE);
//            mGestionMildCD.setVisibility(View.GONE);
//            mDistributionCD.setVisibility(View.GONE);
//            mDenombrementCD.setVisibility(View.GONE);
//        } else if (mCodeSd != null) {
//            mMicroplanCD.setVisibility(View.GONE);
//            mMacaronCD.setVisibility(View.GONE);
//            mDistributionCD.setVisibility(View.GONE);
//            mDenombrementCD.setVisibility(View.GONE);
//        } else if (mCodeItDenombrement != null){
//            mDistributionCD.setVisibility(View.GONE);
//            mGestionMildCD.setVisibility(View.GONE);
//            mDenombrementCD.setVisibility(View.GONE);
//        } else if (mCodeItDistribution != null) {
//            mMacaronCD.setVisibility(View.GONE);
//            mMicroplanCD.setVisibility(View.GONE);
//            mMenageCD.setVisibility(View.GONE);
//            mDenombrementCD.setVisibility(View.GONE);
//        } else if (mCodeBureauCentralZone != null){
//            mMacaronCD.setVisibility(View.GONE);
//            mMicroplanCD.setVisibility(View.GONE);
//            mMenageCD.setVisibility(View.GONE);
//            mGestionMildCD.setVisibility(View.GONE);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base_activity_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_synchroniser){
            new SyncAsyncTask(this).execute();
        }

        return true;
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

    @OnClick(R.id.ajouter_agent_card)
    public void startCreateAgentActivity(){
        Intent intent = new Intent(DashboardActivity.this, ListeAgentActivity.class);
        startActivity(intent);
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
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_RAPPORT_ACTIVITY);
        startActivity(mIntent);
//        if (mCodeSd != null) {
//            mIntent.putExtra(Constant.KEY_CODE_AGENT_SD, mCodeSd);
//            mIntent.putExtra(Constant.ACTION, Constant.ACTION_RAPPORT_ACTIVITY);
//            startActivity(mIntent);
//        } else if (mCodeAs != null) {
//            mIntent.putExtra(Constant.KEY_CODE_AGENT_AS, mCodeAs);
//            mIntent.putExtra(Constant.ACTION, Constant.ACTION_RAPPORT_ACTIVITY);
//            startActivity(mIntent);
//        }

    }

    @OnClick(R.id.microplan_card)
    public void startMicroPlanActivity() {
        mIntent = new Intent(DashboardActivity.this, BaseActivity.class);
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_SITEDIST_ACTIVITY);
        startActivity(mIntent);
    }
}
