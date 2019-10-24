package com.daawtec.scancheck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.drm.DrmStore;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.AffectationMacaronAS;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.DivisionProvincialeSante;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.entites.ZoneSante;
import com.daawtec.scancheck.service.ScanCheckApi;
import com.daawtec.scancheck.service.ScanCheckApiInterface;
import com.daawtec.scancheck.ui.DashboardFragment;
import com.daawtec.scancheck.ui.MenageFragment;

import com.daawtec.scancheck.ui.RapportFragment;
import com.daawtec.scancheck.ui.ScanQRFragment;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.CreateAgentActivity;
import com.daawtec.scancheck.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements MenageFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    ProgressDialog mProgressDialog;
    FloatingActionButton mFab;

    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;
    NavigationView mNavigationView;

    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;

    ScanCheckApiInterface scanCheckApiInterface;
    ScanCheckDB db;

    List<DivisionProvincialeSante> mDPSs = new ArrayList<>();
    List<ZoneSante> mZSs = new ArrayList<>();
    List<AirsSante> mASs = new ArrayList<>();
    List<SiteDistribution> mSDs = new ArrayList<>();

    public static final int REQUEST_CODE_QR_SCAN = 101;

    @Override
    protected void onStart() {
        super.onStart();

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        scanCheckApiInterface = ScanCheckApi.getService();

        mProgressDialog = new ProgressDialog(this);

        boolean isInitialized = mSharedPref.getBoolean(Constant.KEY_IS_INITIALIZED, false);
        if(!isInitialized){
            getInitialData();
            Log.e(TAG, "onStart: is not initialized");
        } else {
            Intent intent = new Intent(this, CreateAgentActivity.class);
            startActivity(intent);
            Log.e(TAG, "onStart: is initialized");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = ScanCheckDB.getDatabase(this);

        mDrawer = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
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

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ParameterActivity.class);
            startActivity(intent);
        }

        if(id == R.id.action_sync){
            Intent intent = new Intent(this, SyncActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_hash) {
            String a = "Hello";
            String b = "b";
            String c = "asdf34vdsf3";

            String aHash = Utils.hash(a);
            String bHash = Utils.hash(b);
            String cHash = Utils.hash(c);

            Log.d(TAG, "hash a : " + aHash );
            Log.d(TAG, "hash b : " + bHash);
            Log.d(TAG, "hash c : " + cHash);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.menu_scanner_macaron:
                mViewPager.setCurrentItem(0);
                mDrawer.closeDrawers();
                break;
            case R.id.menu_menages:
                mViewPager.setCurrentItem(1);
                mDrawer.closeDrawers();
                break;
            case R.id.menu_rapport:
                mViewPager.setCurrentItem(2);
                mDrawer.closeDrawers();
                break;
            case R.id.menu_dashboard:
                mViewPager.setCurrentItem(3);
                mDrawer.closeDrawers();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                case 2 :
                    return RapportFragment.newInstance();
                case 3 :
                    return DashboardFragment.newInstance();
                default : return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }

    public void canInsert(){
        if (mDPSs.size() > 0 &&
                mZSs.size() > 0 &&
                mASs.size() > 0
                && mSDs.size() > 0)
        {
            Log.d(TAG, "canInsert: INSERTING VALUES IN THE DATABASE");
            new InitDB(db).execute();
        }
    }

    public void getInitialData() {

        showProgressDiag(mProgressDialog);

        getDivisionSantes();
        getAiresSante();
        getZoneSante();
        getSiteDistributions();
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

            // Go to set the parameter of the user for the first initialization of the data of the app
            Intent intent = new Intent(HomeActivity.this, CreateAgentActivity.class);
            startActivity(intent);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            long[] dps_ids = db.getIDivisionProvincialSanteDao().insert(mDPSs);
            long[] zs_ids = db.getIZoneSanteDao().insert(mZSs);
            long[] as_ids = db.getIAirSanteDao().insert(mASs);
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
                Log.e(TAG, t.getMessage());
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

    void showProgressDiag(ProgressDialog progressDiag){
        progressDiag.setMessage("Initialization des donnees. Rassurez vous que l'internet est active");
        progressDiag.setCancelable(false);
        progressDiag.show();
    }

    void hideProgressDiag(ProgressDialog progressDiag){
        progressDiag.dismiss();
    }


}
