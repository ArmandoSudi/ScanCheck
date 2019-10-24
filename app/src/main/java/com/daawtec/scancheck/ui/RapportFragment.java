package com.daawtec.scancheck.ui;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.adapters.InventairePhysiqueAdapter;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.InventairePhysique;

import org.w3c.dom.Text;

import java.util.List;

public class RapportFragment extends Fragment {

    private static final String TAG = "RapportFragment";
    TextView mNombreMacaronTV, mNombreMildTV, mNombreMenageTV, mStockMildTV;
    TextView mNombreBadVerificationTV, mNombreMenageServiTV;

    Activity mActivity;
    ScanCheckDB db;

    public RapportFragment() {
        // Required empty public constructor
    }

    public static RapportFragment newInstance() {
        RapportFragment fragment = new RapportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        db = ScanCheckDB.getDatabase(mActivity);

        new GetInventaireAsync(mActivity).execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rapport, container, false);

        initView(view);

        loadData();

        return view;
    }

    public void initView(View view){
        mNombreMacaronTV = view.findViewById(R.id.nombre_macaron_tv);
        mNombreMildTV = view.findViewById(R.id.nombre_mild_tv);
        mNombreMenageTV = view.findViewById(R.id.nombre_menage_tv);
        mStockMildTV = view.findViewById(R.id.stock_mild_tv);
        mNombreBadVerificationTV = view.findViewById(R.id.nombre_bad_verification_tv);
        mNombreMenageServiTV = view.findViewById(R.id.nombre_menage_servi_tv);
    }

    public void loadData() {
        (new AsyncTask<Void, Void, Void>(){

            int nombreMacaron, nombreMild, nombreMenage, nombreMenageServi, stockMild;
            int nombreBadVerification;

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                mNombreMacaronTV.setText(Integer.toString(nombreMacaron));
                mNombreMildTV.setText(Integer.toString(nombreMild));
                mNombreMenageTV.setText(Integer.toString(nombreMenage));
                mStockMildTV.setText(Integer.toString(stockMild));
                mNombreBadVerificationTV.setText(Integer.toString(nombreBadVerification));
                mNombreMenageServiTV.setText(Integer.toString(nombreMenageServi));

            }

            @Override
            protected Void doInBackground(Void... voids) {
                nombreMacaron = db.getIMacaronDao().getCount();
                nombreMild = 0;
                stockMild = 0;
                nombreMenage = db.getIMenageDao().getCount();
                nombreMenageServi = db.getIMenageDao().getCountMenageServi(true);
                nombreBadVerification = db.getIBadVerificationDao().size();
                return null;
            }
        }).execute();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class GetInventaireAsync extends AsyncTask<Void, Void, List<InventairePhysique>>{

        Activity mActivity;
        ScanCheckDB db;

        public GetInventaireAsync(Activity mActivity) {
            this.mActivity = mActivity;
            this.db = ScanCheckDB.getDatabase(mActivity);
        }

        @Override
        protected void onPostExecute(List<InventairePhysique> inventairePhysiques) {
            super.onPostExecute(inventairePhysiques);

//            if (inventairePhysiques != null && inventairePhysiques.size() > 0){
//                mInventaireAdapter.addAll(inventairePhysiques);
//                mInventaireAdapter.notifyDataSetChanged();
//            }
        }

        @Override
        protected List<InventairePhysique> doInBackground(Void... voids) {
            return db.getIIventairePhysiqueDao().all();
        }
    }
}
