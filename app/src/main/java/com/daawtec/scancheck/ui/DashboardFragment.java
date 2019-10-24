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
import com.daawtec.scancheck.entites.DashboardStat;

import java.util.Date;

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

    @Override
    public void onStart() {
        super.onStart();
        getStats();
    }

    public void getStats(){

        (new AsyncTask<Void, Void, DashboardStat>(){
            @Override
            protected void onPostExecute(DashboardStat stat) {
                super.onPostExecute(stat);

                if (stat != null){
                    Log.e(TAG, "onPostExecute: " + stat );
                    mMenageCountTV.setText("" + stat.getMenages());
                    mMenageServiTV.setText("" + (int)stat.getTauxMenagesServis() + " %");
                    mMildDistribueTV.setText("" + (int)stat.getTauxMILDdistribues()+ " %");
                    mTauxEchecTV.setText("" + (int)stat.getTauxBadVerification() + " %");

                    mMenageServiPB.setProgress((int)stat.getTauxMenagesServis());
                    mMildDistribuePB.setProgress((int)stat.getTauxMILDdistribues());
                    mTauxEchecPB.setProgress((int)stat.getTauxBadVerification());
                }
            }

            @Override
            protected DashboardStat doInBackground(Void... voids) {
                DashboardStat stat = new DashboardStat();
                stat.setMenages(db.getIMenageDao().size());
                stat.setMenagesServis(db.getIIAffectationMacaronASDao().getNombreMenagesServis());
                stat.setMILD(db.getIIAffectationMacaronASDao().getNombreTotalMILD());
                stat.setMILDdistribues(db.getIIAffectationMacaronASDao().getNombreMILDDistribues());
                stat.setBadVerification(db.getIBadVerificationDao().size());
                return stat;
            }
        }).execute();
    }

}
