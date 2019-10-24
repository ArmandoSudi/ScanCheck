package com.daawtec.scancheck.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.adapters.MacaronAdapter;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.utils.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MacaronFragment extends Fragment {

    private static final String TAG = "MacaronFragment";

    Activity mActivity;
    MacaronAdapter mMacaronAdapter;
    Calendar mCalendar = Calendar.getInstance();

    SharedPreferences mSharedPref;
    ScanCheckDB db;

    public MacaronFragment() {
        // Required empty public constructor
    }

    public static MacaronFragment newInstance() {
        MacaronFragment fragment = new MacaronFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mMacaronAdapter = new MacaronAdapter(mActivity);
        db = ScanCheckDB.getDatabase(mActivity);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mMacaronAdapter != null) {
            mMacaronAdapter.clear();
            loadMacaron();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_macaron, container, false);

        RecyclerView macaronRV = view.findViewById(R.id.macaron_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        macaronRV.setLayoutManager(linearLayoutManager);
        macaronRV.setHasFixedSize(true);
        macaronRV.addItemDecoration(new DividerItemDecoration(mActivity,linearLayoutManager.getOrientation()));
        macaronRV.setAdapter(mMacaronAdapter);

        loadMacaron();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loadMacaron(){
        (new AsyncTask<Void, Void, List<Macaron>>(){
            @Override
            protected void onPostExecute(List<Macaron> macarons) {
                super.onPostExecute(macarons);

                if (macarons != null) {
                    if (macarons.size() > 0){
                        mMacaronAdapter.clear();
                        mMacaronAdapter.addAll(macarons);
                        mMacaronAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected List<Macaron> doInBackground(Void... voids) {
                return db.getIMacaronDao().all();
            }
        }).execute();


    }

}
