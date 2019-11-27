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
import com.daawtec.scancheck.adapters.RapportDenombrementITAdapter;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.entites.RapportDenombrement;
import com.daawtec.scancheck.entites.RapportDenombrementIT;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RapportFragment extends Fragment {

    private static final String TAG = "RapportFragment";
    TextView mMildAttenduTv, mMildRecuTV, mMildServiTV, mSoldeTV;
    RecyclerView mDenombrementRV;

    Activity mActivity;
    ScanCheckDB db;
    String mCodeAs, mCodeSd, mCodeAgent, mCodeTypeAgent, mDateDebutCampagne;
    RapportDenombrementAdapter mRapportDenombrementAdapter;
    RapportDenombrementITAdapter mRapportDenombrementITAdapter;
    boolean isItDenombrement;
    SharedPreferences mSharedPref;

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

        mActivity = getActivity();
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mCodeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_AGENT, null);
        mCodeTypeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_TYPE_AGENT, null);
        mDateDebutCampagne = mSharedPref.getString(Constant.KEY_DATE_DEBUT_CAMPAGNE, null);

        if (mCodeTypeAgent.equals(Constant.IT_DENOMBREMENT)){
            isItDenombrement = true;
        } else {
            isItDenombrement = false;
        }

        db = ScanCheckDB.getDatabase(mActivity);
        mRapportDenombrementAdapter = new RapportDenombrementAdapter(mActivity);
        mRapportDenombrementITAdapter = new RapportDenombrementITAdapter(mActivity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rapport_denombrement, container, false);
        TextView dateDebutTV = view.findViewById(R.id.date_debut_campagne_tv);

        if (mDateDebutCampagne == null){
            dateDebutTV.setText("La Campagne n'est pas encore disponible");
        } else {
            dateDebutTV.setText("Date debut de la campagne : " + mDateDebutCampagne);
        }
        if (!isItDenombrement) {
            loadRapportDenombrement(mDateDebutCampagne);
            initViewDenombrement(view);
        }
        else {
            loadRapportDenombrementIT(mDateDebutCampagne);
            initViewITdenombrement(view);
        }
        return view;

    }

    public void initViewDenombrement(View view){
        mDenombrementRV = view.findViewById(R.id.rapport_denombrement_rv);
        mRapportDenombrementAdapter = new RapportDenombrementAdapter(mActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mDenombrementRV.setLayoutManager(linearLayoutManager);
        mDenombrementRV.setHasFixedSize(true);
        mDenombrementRV.setAdapter(mRapportDenombrementAdapter);
    }

    public void initViewITdenombrement(View view){
        mDenombrementRV = view.findViewById(R.id.rapport_denombrement_rv);
        mRapportDenombrementITAdapter = new RapportDenombrementITAdapter(mActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mDenombrementRV.setLayoutManager(linearLayoutManager);
        mDenombrementRV.setHasFixedSize(true);
        mDenombrementRV.setAdapter(mRapportDenombrementITAdapter);
    }

    public void initViewDistribution(View view) {
        mMildAttenduTv = view.findViewById(R.id.mild_attendu_tv);
        mMildRecuTV = view.findViewById(R.id.mild_recu_tv);
        mMildServiTV = view.findViewById(R.id.mild_servi_tv);
        mSoldeTV = view.findViewById(R.id.solde_tv);
    }

    public void loadRapportDenombrement(final String dateDebutCampagne){
        (new AsyncTask<Void, Void, Boolean>(){

            List<RapportDenombrement> rapportDenombrements = new ArrayList<>();
            int macaronUtilise, menage, menageOneTwo, menageThreeFour, menageFiveSix, menageSevenEight, macaronRecu;

            @Override
            protected void onPostExecute(Boolean value) {
                super.onPostExecute(value);

                Log.e(TAG, "onPostExecute: VALUE: " + value );
                Log.e(TAG, "onPostExecute: SIZE:  " + rapportDenombrements.size() );

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
                        rapportDenombrement.date = Utils.addDayToDate(dateDebutCampagne,i);
                        macaronRecu = db.getIMacaronDao().getNombreMacaronRecusFromDay(Utils.addDayToDate(dateDebutCampagne,i));
                        Log.i(TAG, "MACARONS RECUS : " + macaronRecu);
                         macaronUtilise = db.getIMacaronDao().getNombreMacaronUtilisesFromDay(Utils.addDayToDate(dateDebutCampagne,i), true);
                        Log.i(TAG, "MACARONS UTILISES : " + macaronUtilise);
                         menage = db.getIMenageDao().getNombreMenageByDay(Utils.addDayToDate(dateDebutCampagne,i));
                        Log.i(TAG, "MENAGES : " + menage);
                         menageOneTwo = db.getIMenageDao().getCountByTailleMenage(1, Utils.addDayToDate(dateDebutCampagne,i)) + db.getIMenageDao().getCountByTailleMenage(2, Utils.addDayToDate(dateDebutCampagne,i));
                         menageThreeFour = db.getIMenageDao().getCountByTailleMenage(3, Utils.addDayToDate(dateDebutCampagne,i)) + db.getIMenageDao().getCountByTailleMenage(4, Utils.addDayToDate(dateDebutCampagne,i));
                         menageFiveSix = db.getIMenageDao().getCountByTailleMenage(5, Utils.addDayToDate(dateDebutCampagne,i)) + db.getIMenageDao().getCountByTailleMenage(6, Utils.addDayToDate(dateDebutCampagne,i));
                         menageSevenEight = db.getIMenageDao().getCountByTailleMenage(7, Utils.addDayToDate(dateDebutCampagne,i)) + db.getIMenageDao().getCountByTailleMenage(8, Utils.addDayToDate(dateDebutCampagne,i));
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

    public void loadRapportDenombrementIT(final String dateDebutCampagne){
        (new AsyncTask<Void, Void, Boolean>(){

            List<RapportDenombrementIT> rapports = new ArrayList<>();

            int macaronUtilise, macaronRecu;
            public int orphelinat, couvent, internat, fosa, hotel;
            public int militaire, deplaces, refugie, prison;

            @Override
            protected void onPostExecute(Boolean value) {
                super.onPostExecute(value);

                if (value) {
                    if (rapports.size() > 0){
                        mRapportDenombrementITAdapter.addAll(rapports);
                        mRapportDenombrementITAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                for ( int i=0; i <= 7; i++){
                    RapportDenombrementIT rapport = new RapportDenombrementIT();

                    try {
                        rapport.date = Utils.addDayToDate(dateDebutCampagne,i);
                        macaronRecu = db.getIMacaronDao().getNombreMacaronRecusFromDay(Utils.addDayToDate(dateDebutCampagne,i));
                        macaronUtilise = db.getIMacaronDao().getNombreMacaronUtilisesFromDay(Utils.addDayToDate(dateDebutCampagne,i), true);
                        orphelinat = db.getIMenageDao().getCountByType("6");
                        couvent = db.getIMenageDao().getCountByType("7");
                        internat = db.getIMenageDao().getCountByType("8");
                        fosa = db.getIMenageDao().getCountByType("9");
                        hotel = db.getIMenageDao().getCountByType("10");
                        militaire = db.getIMenageDao().getCountByType("11");
                        deplaces = db.getIMenageDao().getCountByType("12");
                        refugie = db.getIMenageDao().getCountByType("13");
                        prison = db.getIMenageDao().getCountByType("14");
                    } catch (Exception e) {
                        Log.e(TAG, "doInBackground: " + e.getMessage() );
                        return false;
                    }

                    int soldeMacaron = macaronRecu - macaronUtilise;

                    rapport.macaronRecu = macaronRecu;
                    rapport.macaronUtilise = macaronUtilise;
                    rapport.solde = soldeMacaron;
                    rapport.orphelinat = orphelinat;
                    rapport.couvent = couvent;
                    rapport.internat = internat;
                    rapport.fosa = fosa;
                    rapport.hotel = hotel;
                    rapport.militaire = militaire;
                    rapport.deplaces = deplaces;
                    rapport.refugie = refugie;
                    rapport.prison = prison;

                    if (macaronRecu > 0) rapports.add(rapport);

                }
                return true;
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
