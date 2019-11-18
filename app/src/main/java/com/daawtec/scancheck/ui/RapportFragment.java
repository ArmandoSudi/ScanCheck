package com.daawtec.scancheck.ui;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.adapters.RapportDenombrementAdapter;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.entites.RapportDenombrement;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RapportFragment extends Fragment {

    private static final String TAG = "RapportFragment";
    TextView mMenageOneTV, mMenageTwoTV, mMenageThreeTV, mMenageFourTV, mMenageFiveTV;
    TextView mMildAttenduTv, mMildRecuTV, mMildServiTV, mSoldeTV;
    RecyclerView mDenombrementRV;

    Activity mActivity;
    ScanCheckDB db;
    String mCodeAs, mCodeSd;
    RapportDenombrementAdapter mRapportDenombrementAdapter;
    boolean isDenombrement;
    SharedPreferences mSharedPref;

    int nbrMenageOne, nbrMenageTwo, nbrMenageThree, nbrMenageFour, nbrMenageFive;

    public RapportFragment() {
        // Required empty public constructor
    }

    public static RapportFragment newInstance(String codeAgentAs, String codeAgentSd) {
        RapportFragment fragment = new RapportFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_CODE_AGENT_AS, codeAgentAs);
        bundle.putString(Constant.KEY_CODE_AGENT_SD, codeAgentSd);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            mCodeAs = getArguments().getString(Constant.KEY_CODE_AGENT_AS);
            mCodeSd = getArguments().getString(Constant.KEY_CODE_AGENT_SD);
        }

        if (mCodeAs != null) {
            isDenombrement = true;
        } else {
            isDenombrement = false;
        }

        mActivity = getActivity();
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        db = ScanCheckDB.getDatabase(mActivity);
        mRapportDenombrementAdapter = new RapportDenombrementAdapter(mActivity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        loadRapportDenombrement();

        if (isDenombrement){
            View view = inflater.inflate(R.layout.fragment_rapport_denombrement, container, false);
            initViewDenombrement(view);
//            loadDataDenombrement();
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_rapport_distribution, container, false);
            initViewDistribution(view);
            loadDataDistribution();
            return view;
        }
    }

    public void initViewDenombrement(View view){
        mDenombrementRV = view.findViewById(R.id.rapport_denombrement_tv);
        mRapportDenombrementAdapter = new RapportDenombrementAdapter(mActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mDenombrementRV.setLayoutManager(linearLayoutManager);
        mDenombrementRV.setHasFixedSize(true);
        mDenombrementRV.setAdapter(mRapportDenombrementAdapter);
    }

    public void initViewDistribution(View view) {
        mMildAttenduTv = view.findViewById(R.id.mild_attendu_tv);
        mMildRecuTV = view.findViewById(R.id.mild_recu_tv);
        mMildServiTV = view.findViewById(R.id.mild_servi_tv);
        mSoldeTV = view.findViewById(R.id.solde_tv);
    }

    public void loadRapportDenombrement(){
        (new AsyncTask<Void, Void, Boolean>(){

            List<RapportDenombrement> rapportDenombrements = new ArrayList<>();
            // TODO set this up for the first macaron used
//            String dayOne = mSharedPref.getString(Constant.KEY_JOUR_ONE, null);

            String dayOne = "12/11/2019";
            int macaronUtilise, menage, menageOneTwo, menageThreeFour, menageFiveSix, menageSevenEight, macaronRecu;
            @Override
            protected void onPostExecute(Boolean value) {
                super.onPostExecute(value);

                if (value){
                    if (rapportDenombrements.size() > 0){
                        mRapportDenombrementAdapter.addAll(rapportDenombrements);
                        mRapportDenombrementAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                for (int i=0; i <= 7; i++) {

                    RapportDenombrement rapportDenombrement = new RapportDenombrement();

                    try {
                        rapportDenombrement.date = Utils.addDayToDate(dayOne,i);
                        macaronRecu = db.getIMacaronDao().getNombreMacaronRecusFromDay(Utils.addDayToDate(dayOne,i));
                        Log.i(TAG, "MACARONS RECUS : " + macaronRecu);
                         macaronUtilise = db.getIMacaronDao().getNombreMacaronUtilisesFromDay(Utils.addDayToDate(dayOne,i), true);
                        Log.i(TAG, "MACARONS UTILISES : " + macaronUtilise);
                         menage = db.getIMenageDao().getNombreMenageByDay(Utils.addDayToDate(dayOne,i));
                        Log.i(TAG, "MENAGES : " + menage);
                         menageOneTwo = db.getIMenageDao().getCountByTailleMenage(1, Utils.addDayToDate(dayOne,i)) + db.getIMenageDao().getCountByTailleMenage(2, Utils.addDayToDate(dayOne,i));
                         menageThreeFour = db.getIMenageDao().getCountByTailleMenage(3, Utils.addDayToDate(dayOne,i)) + db.getIMenageDao().getCountByTailleMenage(4, Utils.addDayToDate(dayOne,i));
                         menageFiveSix = db.getIMenageDao().getCountByTailleMenage(5, Utils.addDayToDate(dayOne,i)) + db.getIMenageDao().getCountByTailleMenage(6, Utils.addDayToDate(dayOne,i));
                         menageSevenEight = db.getIMenageDao().getCountByTailleMenage(7, Utils.addDayToDate(dayOne,i)) + db.getIMenageDao().getCountByTailleMenage(8, Utils.addDayToDate(dayOne,i));
                    } catch (Exception ex){
                        Log.e(TAG, "doInBackground: " + ex.getMessage());
                        return false;
                    }

                    int soldeMacaron = macaronRecu - macaronUtilise;

                    // Macaron recu cumule aussi le solde des macarons precedent
                    if ( i > 0 && rapportDenombrements.size() > 1) {
//                        macaronRecu += rapportDenombrements.get(i).soldeMacaron;
                    }
                    rapportDenombrement.macaronRecu = macaronRecu;

                    rapportDenombrement.macaronUtilise = macaronUtilise;
                    rapportDenombrement.menage = menage;
                    rapportDenombrement.menageOneTwo = menageOneTwo;
                    rapportDenombrement.menageThreeFour = menageThreeFour;
                    rapportDenombrement.menageFiveSix = menageFiveSix;
                    rapportDenombrement.menageSevenEight = menageSevenEight;
                    rapportDenombrement.soldeMacaron = soldeMacaron;

                    // Considerer seulement si les macarons ont ete recu ce jour la
                    if (macaronRecu > 0) rapportDenombrements.add(rapportDenombrement);
                }

                return true;
            }
        }).execute();
    }

    public void loadDataDenombrement() {
        (new AsyncTask<Void, Void, Void>(){

            int nbrMenageOne, nbrMenageTwo, nbrMenageThree, nbrMenageFour, nbrMenageFive;

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                mMenageOneTV.setText(Integer.toString(nbrMenageOne));
                mMenageTwoTV.setText(Integer.toString(nbrMenageTwo));
                mMenageThreeTV.setText(Integer.toString(nbrMenageThree));
                mMenageFourTV.setText(Integer.toString(nbrMenageFour));
                mMenageFiveTV.setText(Integer.toString(nbrMenageFive));

            }

            @Override
            protected Void doInBackground(Void... voids) {
                nbrMenageOne = db.getIMenageDao().getCountByTailleMenage(1, "") + db.getIMenageDao().getCountByTailleMenage(2, "");
                nbrMenageTwo = db.getIMenageDao().getCountByTailleMenage(3, "") + db.getIMenageDao().getCountByTailleMenage(4, "");
                nbrMenageThree = db.getIMenageDao().getCountByTailleMenage(5,"") + db.getIMenageDao().getCountByTailleMenage(6, "");
                nbrMenageFour = db.getIMenageDao().getCountByTailleMenage(7, "") + db.getIMenageDao().getCountByTailleMenage(8, "");
                nbrMenageFive = db.getIMenageDao().getCountByTailleMenage(9, "") + db.getIMenageDao().getCountZBigMenage();
                return null;
            }
        }).execute();
    }

    public void loadDataDistribution(){
        (new AsyncTask<Void, Void, Void>(){
            int nbrMildRecu, nbrMildAttendu, nbrMildServi, solde;
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                mMildAttenduTv.setText("Nombre des MILDs attendus : " + nbrMildAttendu);
                mMildRecuTV.setText("Nombre des MILDs recus : " + nbrMildRecu);
                mMildServiTV.setText("Nombre des MILDs servis : " + nbrMildServi);
                mSoldeTV.setText("Solde (MILDs restant dans le stock) : " + solde);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                // Nbre des milds recu est le total des milds recu en stock
                nbrMildRecu = db.getIIventairePhysiqueDao().getNbrMildByCodeSd(mCodeSd);

                // Nbre des milds attendus est le total des milds a remettre tel que enregistre dans les menages
                nbrMildAttendu = db.getIMenageDao().getNbreMildAttenduByCodeSd(mCodeSd);

                // Nbre des milds servis est le total des milds servis tel que enregistre dans les menages
                nbrMildServi = db.getIMenageDao().getNbreMildServiByCodeSd(mCodeSd);

                // Solde est la difference entre les milds en stock et les milds servis
                solde = nbrMildRecu - nbrMildServi;

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

}
