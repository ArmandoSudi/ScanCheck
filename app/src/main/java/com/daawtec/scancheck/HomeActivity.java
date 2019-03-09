package com.daawtec.scancheck;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
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

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.ui.MenageFragment;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.daawtec.scancheck.ui.RapportFragment;
import com.daawtec.scancheck.utils.Utils;

public class HomeActivity extends AppCompatActivity implements MenageFragment.OnFragmentInteractionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    FloatingActionButton mFab;

    private static final int REQUEST_CODE_QR_SCAN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = findViewById(R.id.fab);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mFab);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CreateMenageActivity.class);
                startActivity(intent);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class ScanQRFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        Activity mActivity;

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

//            FloatingActionButton fab = mActivity.findViewById(R.id.fab);
//            fab.hide();
            return rootView;
        }
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
                    return MenageFragment.newInstance("List of menage");
                case 2 : return RapportFragment.newInstance("List of menage");
                default : return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
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
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.getIDivisionProvincialSanteDao().insert(Utils.getDivisionSantes());
            db.getIZoneSanteDao().insert(Utils.getZoneSantes());
            db.getIAirSanteDao().insert(Utils.getAireSantes());
            db.getIRelaisCommunautaireDao().insert(Utils.getReco());
            return null;
        }
    }
}
