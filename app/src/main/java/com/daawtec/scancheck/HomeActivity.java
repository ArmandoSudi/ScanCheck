package com.daawtec.scancheck;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.DivisionProvincialeSante;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.entites.ZoneSante;
import com.daawtec.scancheck.service.ScanCheckApi;
import com.daawtec.scancheck.service.ScanCheckApiInterface;
import com.daawtec.scancheck.ui.MenageFragment;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.daawtec.scancheck.ui.RapportFragment;
import com.daawtec.scancheck.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements MenageFragment.OnFragmentInteractionListener {

    private static final String TAG = "HomeActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    ProgressDialog mProgressDialog;
    FloatingActionButton mFab;

    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;

    ScanCheckApiInterface scanCheckApiInterface;

    List<DivisionProvincialeSante> mDPSs = new ArrayList<>();
    List<ZoneSante> mZSs = new ArrayList<>();
    List<AirsSante> mASs = new ArrayList<>();
    List<SiteDistribution> mSDs = new ArrayList<>();
    List<RelaisCommunautaire> mRecos = new ArrayList<>();
    List<Macaron> mMacarons = new ArrayList<>();

    private static final int REQUEST_CODE_QR_SCAN = 101;

    @Override
    protected void onStart() {
        super.onStart();

        mSharedPref = getPreferences(Context.MODE_PRIVATE);
        mEditor = mSharedPref.edit();

        scanCheckApiInterface = ScanCheckApi.getService();

        mProgressDialog = new ProgressDialog(this);

        boolean isInitialized = mSharedPref.getBoolean(Constant.KEY_IS_INITIALIZED, false);
        if(!isInitialized){
            getInitialData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = findViewById(R.id.fab);
        mFab.hide();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mFab);
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mFab.hide();
                        break;
                    case 1:
                        mFab.show();
                        break;
                    case 2:
                        mFab.show();
                        break;

                    default:
                        mFab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = mViewPager.getCurrentItem();
                switch(index){
                    case 1:
                        Intent intent_one = new Intent(HomeActivity.this, CreateMenageActivity.class);
                        startActivity(intent_one);
                        break;
                    case 2:
                        Intent intentTwo = new Intent(HomeActivity.this, CreateRapportActivity.class);
                        startActivity(intentTwo);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_init_db){
            ScanCheckDB db = ScanCheckDB.getDatabase(this);
            new InitDB(db).execute();
        }

        if(id == R.id.action_sync){
            Intent intent = new Intent(this, SyncActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class ScanQRFragment extends Fragment {

        private static final String TAG = "ScanQRFragment";
        private static final String ARG_SECTION_NUMBER = "section_number";

        TextView numeroMacaronTV;

        Activity mActivity;
        ScanCheckDB db;

        public ScanQRFragment() {
        }

        public static ScanQRFragment newInstance() {
            ScanQRFragment fragment = new ScanQRFragment();
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
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            Button scanBT = rootView.findViewById(R.id.scanne_bt);
            scanBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Start the qr scan activity
                    Intent i = new Intent(getActivity(),QrCodeActivity.class);
                    startActivityForResult( i,REQUEST_CODE_QR_SCAN);
                }
            });
            numeroMacaronTV = rootView.findViewById(R.id.numero_macaron_tv);

            return rootView;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == REQUEST_CODE_QR_SCAN)
            {
                if(data==null)
                    return;
                //Getting the passed result
                String qrCode = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                numeroMacaronTV.setText("" + qrCode);
                Log.d(TAG,"Scan result : "+ qrCode);

                checkMacaron(qrCode);

            }
        }

        public void checkMacaron(final String qrCode){
            (new AsyncTask<Void, Void, Macaron>(){
                @Override
                protected void onPostExecute(Macaron macaron) {
                    super.onPostExecute(macaron);

                    if (macaron instanceof Macaron){
                        Log.d(TAG, "onPostExecute: LE MACARON EXIST");
                        showDialog("Le macaron existe", mActivity);
                    } else {
                        Log.d(TAG, "onPostExecute: LE MACARON N'EXISTE PAS");
                        showDialog("Le macaron n'existe pas", mActivity);
                    }
                }

                @Override
                protected Macaron doInBackground(Void... voids) {
                    return db.getIMacaronDao().check(qrCode);
                }
            }).execute();
        }

        void showDialog(String message, Context context){
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Resultat");
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(resultCode != Activity.RESULT_OK)
//        {
//            Log.d(TAG,"COULD NOT GET A GOOD RESULT.");
//            if(data==null)
//                return;
//            //Getting the passed result
//            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
//            if( result!=null)
//            {
//                AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
//                alertDialog.setTitle("Scan Error");
//                alertDialog.setMessage("QR Code could not be scanned");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                alertDialog.show();
//            }
//            return;
//
//        }
//
//        if(requestCode == REQUEST_CODE_QR_SCAN)
//        {
//            if(data==null)
//                return;
//            //Getting the passed result
//            String qrCode = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
//            Log.d(TAG,"Scan result :"+ qrCode);
//
//            checkMacaron(qrCode);
//
//        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        FloatingActionButton mFab;

        public SectionsPagerAdapter(FragmentManager fm, FloatingActionButton fab) {
            super(fm);
            this.mFab = fab;
        }

        @Override
        public Fragment getItem(int position) {

            switch(position){
                case 0:
                    mFab.hide();
                    return ScanQRFragment.newInstance();
                case 1:
                    mFab.show();
                    return MenageFragment.newInstance();
                case 2 : return RapportFragment.newInstance();
                default : return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public void canInsert(){
        if (mDPSs.size() > 0 &&
                mZSs.size() > 0 &&
                mASs.size() > 0 &&
                mMacarons.size() > 0 &&
                mSDs.size() > 0 &&
                mRecos.size() > 0){
            Log.d(TAG, "canInsert: INSERTING VALUES IN THE DATABASE");
            ScanCheckDB db = ScanCheckDB.getDatabase(this);
            new InitDB(db).execute();
        }
    }

    public void getInitialData(){
        showProgressDiag(mProgressDialog);

        getDivisionSantes();
        getAiresSante();
        getZoneSante();
        getMacarons();
        getSiteDistributions();
        getRecos();
    }

    public class InitDB extends AsyncTask<Void, Void, Void>{

        private static final String TAG = "InitDB";

        ScanCheckDB db;

        public InitDB(ScanCheckDB db) {
            this.db = db;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Log.e(TAG, "onPostExecute: DATABASE INITIALIZED ");
            // pour ne pas initialiser la base des donnees lors de redemarrage ulterieurs
            mEditor.putBoolean(Constant.KEY_IS_INITIALIZED, true);
            mEditor.commit();
            hideProgressDiag(mProgressDialog);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long[] dps_ids = db.getIDivisionProvincialSanteDao().insert(mDPSs);
            long[] zs_ids = db.getIZoneSanteDao().insert(mZSs);
            long[] as_ids = db.getIAirSanteDao().insert(mASs);
            long[] reco_dis = db.getIRelaisCommunautaireDao().insert(mRecos);
            long[] macaron_ids = db.getIMacaronDao().insert(mMacarons);
            long[] sd_ids = db.getISiteDistributionDao().insert(mSDs);
            return null;
        }
    }

    public void getDivisionSantes(){
        scanCheckApiInterface.getDPS().enqueue(new Callback<List<DivisionProvincialeSante>>() {
            @Override
            public void onResponse(Call<List<DivisionProvincialeSante>> call, Response<List<DivisionProvincialeSante>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        mDPSs.addAll(response.body());
                        Log.d(TAG, "onResponse: List of DivisionProvincialSante : " + mDPSs.size());
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DivisionProvincialeSante>> call, Throwable t) {
                Log.e(TAG, "getdivisionSante onFailure: NETWORK FAILURE");
            }
        });
    }

    public void getAiresSante() {
        scanCheckApiInterface.getAirSantes().enqueue(new Callback<List<AirsSante>>() {
            @Override
            public void onResponse(Call<List<AirsSante>> call, Response<List<AirsSante>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        mASs.addAll(response.body());
                        Log.d(TAG, "onResponse: List of AireSante : " + mASs.size());
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AirsSante>> call, Throwable t) {
                Log.e(TAG, "getAirSante onFailure: NETWORK FAILURE");
            }
        });
    }

    public void getZoneSante() {
        scanCheckApiInterface.getZoneSante().enqueue(new Callback<List<ZoneSante>>() {
            @Override
            public void onResponse(Call<List<ZoneSante>> call, Response<List<ZoneSante>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        mZSs.addAll(response.body());
                        Log.d(TAG, "onResponse: List of ZoneSante : " + mZSs.size());
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ZoneSante>> call, Throwable t) {
                Log.e(TAG, "getZoneSante onFailure: NETWORK FAILURE");
            }
        });
    }

    public void getMacarons(){
        scanCheckApiInterface.getMacarons().enqueue(new Callback<List<Macaron>>() {
            @Override
            public void onResponse(Call<List<Macaron>> call, Response<List<Macaron>> response) {
                if (response.isSuccessful()){
                    if(response.body() != null){
                        mMacarons.addAll(response.body());
                        Log.d(TAG, "onResponse: List of Macarons : " + mMacarons.size());
//                        for (Macaron macaron: mMacarons ){
//                            Log.e(TAG, "DISPLAY MACARON : " + macaron.codeMacaron );
//                        }
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Macaron>> call, Throwable t) {
                Log.e(TAG, "getMacarons onFailure: NETWORK FAILURE");
            }
        });
    }

    public void getSiteDistributions(){
        scanCheckApiInterface.getSiteDistribution().enqueue(new Callback<List<SiteDistribution>>() {
            @Override
            public void onResponse(Call<List<SiteDistribution>> call, Response<List<SiteDistribution>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        mSDs.addAll(response.body());
                        Log.d(TAG, "onResponse: Liste des SiteDistribution : " + mSDs.size());
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SiteDistribution>> call, Throwable t) {
                Log.e(TAG, "getSiteDistribution onFailure: NETWORK FAILURE");
            }
        });
    }

    public void getRecos() {
        scanCheckApiInterface.getReco().enqueue(new Callback<List<RelaisCommunautaire>>() {
            @Override
            public void onResponse(Call<List<RelaisCommunautaire>> call, Response<List<RelaisCommunautaire>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        mRecos.addAll(response.body());
                        Log.d(TAG, "onResponse: Liste des Recos : " + mRecos.size());
                        canInsert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RelaisCommunautaire>> call, Throwable t) {
                Log.e(TAG, "getReco onFailure: NETWORK FAILURE");
            }
        });
    }

    void showProgressDiag(ProgressDialog progressDiag){
        progressDiag.setMessage("Initialization des donnees. Rassurez vous que l'internet est active");
        progressDiag.setCancelable(false);
        progressDiag.show();
    }

    void hideProgressDiag(ProgressDialog progressDiag){
        progressDiag.dismiss();
    }

}
