package com.daawtec.scancheck.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.daawtec.scancheck.CreateMenageActivity;
import com.daawtec.scancheck.R;
import com.daawtec.scancheck.adapters.SiteDistributionAdapter;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.SiteDistribution;

import java.util.List;

public class SiteDistributionFragment extends Fragment {

    private static final String TAG = "SiteDistributionFragmen";
    Activity mActivity;
    SiteDistributionAdapter mSiteDistAdapter;
    ScanCheckDB db;

    public void SiteDistributionFragment(){

    }

    public static SiteDistributionFragment newInstance(){
        SiteDistributionFragment fragment = new SiteDistributionFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        db = ScanCheckDB.getDatabase(mActivity);
        mSiteDistAdapter = new SiteDistributionAdapter(mActivity);

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e(TAG, "onStart: MIROPLAN" );
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "onResume: MIROPLAN" );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_site_distribution, container, false);

        RecyclerView siteDistributionRV = view.findViewById(R.id.site_distribution_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        siteDistributionRV.setLayoutManager(linearLayoutManager);
        siteDistributionRV.setHasFixedSize(true);
        siteDistributionRV.addItemDecoration(new DividerItemDecoration(mActivity,linearLayoutManager.getOrientation()));
        siteDistributionRV.setAdapter(mSiteDistAdapter);

        loadSD();

        return view;
    }

    public void loadSD(){
        (new AsyncTask<Void, Void, List<SiteDistribution>>(){
            @Override
            protected void onPostExecute(List<SiteDistribution> siteDistributions) {
                super.onPostExecute(siteDistributions);

                Log.e(TAG, "onPostExecute: " + siteDistributions.size() );

                if (siteDistributions != null) {
                    if (siteDistributions.size() > 0) {
                        mSiteDistAdapter.clear();
                        mSiteDistAdapter.addAll(siteDistributions);
                        mSiteDistAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected List<SiteDistribution> doInBackground(Void... voids) {
                return db.getISiteDistributionDao().all();
            }
        }).execute();

    }
}
