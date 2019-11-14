package com.daawtec.scancheck.ui;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.utils.Constant;

import java.util.List;

public class RapportFragment extends Fragment {

    private static final String TAG = "RapportFragment";
    TextView mMenageOneTV, mMenageTwoTV, mMenageThreeTV, mMenageFourTV, mMenageFiveTV;
    TextView mMildAttenduTv, mMildRecuTV, mMildServiTV, mSoldeTV;

    Activity mActivity;
    ScanCheckDB db;
    String mCodeAs, mCodeSd;
    boolean isDenombrement;

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
        db = ScanCheckDB.getDatabase(mActivity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (isDenombrement){
            View view = inflater.inflate(R.layout.fragment_rapport_denombrement, container, false);
            initViewDenombrement(view);
            loadDataDenombrement();
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_rapport_distribution, container, false);
            initViewDistribution(view);
            loadDataDistribution();
            return view;
        }
    }

    public void initViewDenombrement(View view){
        mMenageOneTV = view.findViewById(R.id.menage_one_tv);
        mMenageTwoTV = view.findViewById(R.id.menage_two_tv);
        mMenageThreeTV = view.findViewById(R.id.menage_three_tv);
        mMenageFourTV = view.findViewById(R.id.menage_four_tv);
        mMenageFiveTV = view.findViewById(R.id.menage_five_tv);
    }

    public void initViewDistribution(View view) {
        mMildAttenduTv = view.findViewById(R.id.mild_attendu_tv);
        mMildRecuTV = view.findViewById(R.id.mild_recu_tv);
        mMildServiTV = view.findViewById(R.id.mild_servi_tv);
        mSoldeTV = view.findViewById(R.id.solde_tv);
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
                nbrMenageOne = db.getIMenageDao().getCountByTailleMenage(1) + db.getIMenageDao().getCountByTailleMenage(2);
                nbrMenageTwo = db.getIMenageDao().getCountByTailleMenage(3) + db.getIMenageDao().getCountByTailleMenage(4);
                nbrMenageThree = db.getIMenageDao().getCountByTailleMenage(5) + db.getIMenageDao().getCountByTailleMenage(6);
                nbrMenageFour = db.getIMenageDao().getCountByTailleMenage(7) + db.getIMenageDao().getCountByTailleMenage(8);
                nbrMenageFive = db.getIMenageDao().getCountByTailleMenage(9) + db.getIMenageDao().getCountZBigMenage();
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
