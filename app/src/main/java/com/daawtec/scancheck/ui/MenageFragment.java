package com.daawtec.scancheck.ui;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.adapters.MenageAdapter;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Menage;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    MenageAdapter mMenageAdapter;
    private Activity mActivity;
    ScanCheckDB db;

    public MenageFragment() {
        // Required empty public constructor
    }

    public static MenageFragment newInstance() {
        MenageFragment fragment = new MenageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mMenageAdapter = new MenageAdapter(mActivity);
        db = ScanCheckDB.getDatabase(mActivity);
        new LoadMenageAsync(db).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_menage, container, false);

        RecyclerView menageRV = view.findViewById(R.id.menage_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        menageRV.setLayoutManager(linearLayoutManager);
        menageRV.setHasFixedSize(true);
        menageRV.addItemDecoration(new DividerItemDecoration(mActivity,linearLayoutManager.getOrientation()));
        menageRV.setAdapter(mMenageAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class LoadMenageAsync extends AsyncTask<Void, Void, List<Menage>>{

        ScanCheckDB db;

        public LoadMenageAsync(ScanCheckDB db) {
            this.db = db;
        }

        @Override
        protected void onPostExecute(List<Menage> menages) {
            super.onPostExecute(menages);
            if(menages != null && menages.size() > 0){
                mMenageAdapter.addAll(menages);
                mMenageAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected List<Menage> doInBackground(Void... voids) {
            return db.getIMenageDao().all();
        }
    }
}
