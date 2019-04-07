package com.daawtec.scancheck.ui;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.database.ScanCheckDB;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    TextView mMenageServiTV, mMildDistribueTV, mTauxEchecTV, mMenageCountTV;
    ProgressBar mMenageServiPB, mMildDistribuePB, mTauxEchecPB;
    ScanCheckDB db;
    Activity mActivity;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        db = ScanCheckDB.getDatabase(mActivity);
        getMenageCount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mMenageCountTV = view.findViewById(R.id.menage_count_tv);
        mMenageServiTV = view.findViewById(R.id.menage_servi_tv);
        mMildDistribueTV = view.findViewById(R.id.mild_distribue_tv);
        mTauxEchecTV = view.findViewById(R.id.taux_echec_tv);

        mMenageServiPB = view.findViewById(R.id.menage_servi_pb);
        mMildDistribuePB = view.findViewById(R.id.mild_distribue_pb);
        mTauxEchecPB = view.findViewById(R.id.taux_echec_pb);

        return view;
    }

    public void getMenageCount(){
        (new AsyncTask<Void, Void, Integer>(){
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);

                int count = integer;
                mMenageCountTV.setText("" + count);
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                return db.getIMenageDao().size();
            }
        }).execute();
    }

}
