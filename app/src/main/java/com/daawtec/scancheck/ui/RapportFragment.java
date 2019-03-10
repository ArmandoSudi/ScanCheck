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

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.adapters.InventairePhysiqueAdapter;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.InventairePhysique;

import java.util.List;

public class RapportFragment extends Fragment {

    private static final String TAG = "RapportFragment";

    InventairePhysiqueAdapter mInventaireAdapter;
    Activity mActivity;

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
        mInventaireAdapter = new InventairePhysiqueAdapter(mActivity);

        new GetInventaireAsync(mActivity).execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rapport, container, false);

        RecyclerView inventaireRV = view.findViewById(R.id.inventaire_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        inventaireRV.setLayoutManager(linearLayoutManager);
        inventaireRV.setHasFixedSize(true);
        inventaireRV.addItemDecoration(new DividerItemDecoration(mActivity,linearLayoutManager.getOrientation()));
        inventaireRV.setAdapter(mInventaireAdapter);
        return view;
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

            if (inventairePhysiques != null && inventairePhysiques.size() > 0){
                mInventaireAdapter.addAll(inventairePhysiques);
                mInventaireAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected List<InventairePhysique> doInBackground(Void... voids) {
            return db.getIIventairePhysiqueDao().all();
        }
    }
}
