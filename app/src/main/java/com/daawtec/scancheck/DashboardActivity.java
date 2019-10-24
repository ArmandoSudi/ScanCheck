package com.daawtec.scancheck;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

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

    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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
        startActivity(mIntent);
    }

    @OnClick(R.id.menage_card)
    public void startMenageActivity(){
        mIntent = new Intent(DashboardActivity.this, BaseActivity.class);
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_MENAGE_ACTIVITY);
        startActivity(mIntent);
    }

    @OnClick(R.id.distribution_card)
    public void startDistributionActivity(){
        mIntent = new Intent(DashboardActivity.this, BaseActivity.class);
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_DISTRIBUTION_ACTIVITY);
        startActivity(mIntent);
    }

    @OnClick(R.id.rapport_card)
    public void startRapportActivity(){
        mIntent = new Intent(DashboardActivity.this, BaseActivity.class);
        mIntent.putExtra(Constant.ACTION, Constant.ACTION_RAPPORT_ACTIVITY);
        startActivity(mIntent);
    }
}
