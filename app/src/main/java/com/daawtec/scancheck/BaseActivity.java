package com.daawtec.scancheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.ui.MacaronFragment;
import com.daawtec.scancheck.ui.MenageFragment;
import com.daawtec.scancheck.ui.RapportFragment;
import com.daawtec.scancheck.ui.ScanQRFragment;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.Utils;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Calendar;
import java.util.Date;

public class BaseActivity extends AppCompatActivity implements MenageFragment.OnFragmentInteractionListener {
    private static final String TAG = "BaseActivity";

    FragmentManager mFragmentManager;
    Fragment mCurrentFragment;

    FloatingActionButton fab;
    SharedPreferences mSharedPref;
    Calendar mCalendar = Calendar.getInstance();
    ScanCheckDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String value = intent.getStringExtra(Constant.ACTION);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
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
            String agentCodeAS = mSharedPref.getString(Constant.KEY_CODE_AGENT_AS, null);
            String codeAgentDenombrement = mSharedPref.getString(Constant.KEY_CODE_AGENT_DENOMBREMENT, null);
            Date dateEnregistrement = mCalendar.getTime();

            Log.e(TAG, "agentCodeAS " + agentCodeAS );
            Log.e(TAG, "codeAgentDenombrement " + codeAgentDenombrement );
            Log.e(TAG, "dateEnregistrement " + dateEnregistrement );

            if (agentCodeAS != null && codeAgentDenombrement != null) {
                //Getting the passed result
                String qrCode = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Log.e(TAG, "onActivityResult: code macaron: " + qrCode );
                Macaron macaron = new Macaron(qrCode, agentCodeAS, codeAgentDenombrement,
                        dateEnregistrement, false );
                saveMacaron(macaron);
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
                mCurrentFragment = MacaronFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;
            case Constant.ACTION_MENAGE_ACTIVITY:
                setTitle("Ménage");
                mCurrentFragment = MenageFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;
                case Constant.ACTION_DISTRIBUTION_ACTIVITY:
                    setTitle("Distribution");
                    mCurrentFragment = ScanQRFragment.newInstance();
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;
                case Constant.ACTION_RAPPORT_ACTIVITY:
                    setTitle("Rapport");
                    mCurrentFragment = RapportFragment.newInstance();
                    fab.hide();
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
                        startActivity(intent);
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
}
