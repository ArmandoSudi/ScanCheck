package com.daawtec.scancheck;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.ui.DistributionFragment;
import com.daawtec.scancheck.ui.MacaronFragment;
import com.daawtec.scancheck.ui.MenageFragment;
import com.daawtec.scancheck.ui.RapportFragment;
import com.daawtec.scancheck.ui.SiteDistributionFragment;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity implements MenageFragment.OnFragmentInteractionListener {
    private static final String TAG = "BaseActivity";

    FragmentManager mFragmentManager;
    Fragment mCurrentFragment;

    FloatingActionButton fab;
    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;
    Calendar mCalendar = Calendar.getInstance();
    String mDateFormat = "dd/MM/yyyy";
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(mDateFormat, Locale.FRANCE);
    ScanCheckDB db;
    String mCodeAs, mCodeSd, mCodeAgentDenombrement, mCodeAgentDist, mCodeAgentIT;
    boolean isAgentIT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        checkCameraPermission();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String value = intent.getStringExtra(Constant.ACTION);

        mCodeAs = intent.getStringExtra(Constant.KEY_CODE_AGENT_AS);
        mCodeSd = intent.getStringExtra(Constant.KEY_CODE_AGENT_SD);
        mCodeAgentDenombrement = intent.getStringExtra(Constant.KEY_CODE_AGENT_DENOMBREMENT);
        mCodeAgentDist = intent.getStringExtra(Constant.KEY_CODE_AGENT_DIST);
        mCodeAgentIT = intent.getStringExtra(Constant.KEY_CODE_AGENT_IT);

        if (mCodeAgentIT != null) {
            isAgentIT = true;
        }

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();
        db = ScanCheckDB.getDatabase(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentFragment instanceof MacaronFragment){
                    Intent intent = new Intent(BaseActivity.this, QrCodeActivity.class);
                    startActivityForResult( intent, Constant.REQUEST_CODE_QR_SCAN_MACARON);
                } else if (mCurrentFragment instanceof MenageFragment) {
                    Intent intent = new Intent(BaseActivity.this, QrCodeActivity.class);
                    startActivityForResult( intent, Constant.REQUEST_CODE_QR_SCAN_MENAGE);
                } else if (mCurrentFragment instanceof RapportFragment) {
                    Snackbar.make(view, "Rapport Fragment", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (mCurrentFragment instanceof SiteDistributionFragment){
                    Snackbar.make(view, "Ajouter site de distribution", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }
        });

        mFragmentManager = getSupportFragmentManager();

        if (value != null) inflateFragement(value);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constant.REQUEST_CODE_QR_SCAN_MENAGE) {
            if(data==null)
                return;
            //Getting the passed result
            String qrCode = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.e(TAG, "onActivityResult: Ajouter Menage pour le macaron : " + qrCode );

            checkMacaron(qrCode);

        }

        if(requestCode == Constant.REQUEST_CODE_QR_SCAN_MACARON) {

            if(data==null)
                return;
            String dateEnregistrement = Utils.formatDate(mCalendar.getTime());

            if (mCodeAs != null && mCodeAgentDenombrement != null) {
                //Getting the passed result
                String qrCode = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Log.e(TAG, "onActivityResult: code macaron: " + qrCode );
                Macaron macaron = new Macaron(qrCode, mCodeAs, mCodeAgentDenombrement,
                        dateEnregistrement, false );
                saveMacaron(macaron);
            }

        }

        if (requestCode == Constant.CODE_ACTION_ADD_MENAGE){
            Log.e(TAG, "onActivityResult: MENAGE ENREGISTRE 1");
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "onActivityResult: MENAGE ENREGISTRE 2");
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        mFragmentManager.beginTransaction()
                .replace(R.id.container, mCurrentFragment)
                .commit();
    }

    public void inflateFragement(String action){
        switch(action){
            case Constant.ACTION_MACARON_ACTIVITY :
                setTitle("Macarons");
                mCurrentFragment = MacaronFragment.newInstance(mCodeAgentDenombrement, mCodeAs);
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;
            case Constant.ACTION_MENAGE_ACTIVITY:
                if (isAgentIT) setTitle("Ménages speciaux");
                else setTitle("Ménages");
                mCurrentFragment = MenageFragment.newInstance(mCodeAgentDenombrement);
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;
                case Constant.ACTION_DISTRIBUTION_ACTIVITY:
                    setTitle("Distribution");
                    mCurrentFragment = DistributionFragment.newInstance(mCodeAgentDist);
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;
                case Constant.ACTION_RAPPORT_ACTIVITY:
                    setTitle("Rapport");
                    mCurrentFragment = RapportFragment.newInstance(mCodeAs, mCodeSd);
                    fab.hide();
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;
            case Constant.ACTION_SITEDIST_ACTIVITY:
                setTitle("Sites de distribution");
                mCurrentFragment = SiteDistributionFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;

        }
    }

    public void saveMacaron(final Macaron macaron){
        (new AsyncTask<Void, Void, long[]>(){
            @Override
            protected void onPostExecute(long[] longs) {
                super.onPostExecute(longs);

                if(longs[0] > 0){
                    Toast.makeText(BaseActivity.this, "Macaron enregistré", Toast.LENGTH_SHORT).show();
                    String jourOne = mSharedPref.getString(Constant.KEY_JOUR_ONE, null);
                    if (jourOne == null){
                        // C'est le premier jour de denombrement
                        Date date = mCalendar.getTime();
                        String jour = mSimpleDateFormat.format(date);
                        mEditor.putString(Constant.KEY_JOUR_ONE, jour);
                    } else {

                    }
                }
            }

            @Override
            protected long[] doInBackground(Void... voids) {
                return db.getIMacaronDao().insert(macaron);
            }
        }).execute();
    }

    public void checkMacaron(final String codeMacaron){
        (new AsyncTask<Void, Void, Macaron>(){
            @Override
            protected void onPostExecute(Macaron macaron) {
                super.onPostExecute(macaron);
                if (macaron instanceof Macaron){
                    if (macaron.isAffected){
                        Toast.makeText(BaseActivity.this, "Ce Macaron a déjà été affecté", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(BaseActivity.this, CreateMenageActivity.class);
                        intent.putExtra(Constant.CODE_QR, macaron.codeMacaron);
                        if (mCodeAgentIT != null) intent.putExtra(Constant.KEY_CODE_AGENT_IT, mCodeAgentIT);
                        startActivityForResult(intent, Constant.CODE_ACTION_ADD_MENAGE);
                    }
                } else {
                    Toast.makeText(BaseActivity.this, "Ce Macaron n'existe pas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Macaron doInBackground(Void... voids) {
                return db.getIMacaronDao().get(codeMacaron);
            }
        }).execute();
    }

    public void checkCameraPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (BaseActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{
                                Manifest.permission.CAMERA},
                        Constant.REQUEST_CODE_ASK_PERMISSIONS
                );
            }
        }
    }
}
