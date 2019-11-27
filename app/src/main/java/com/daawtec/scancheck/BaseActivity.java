package com.daawtec.scancheck;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.ui.DistributionFragment;
import com.daawtec.scancheck.ui.MacaronFragment;
import com.daawtec.scancheck.ui.MenageFragment;
import com.daawtec.scancheck.ui.RapportFragment;
import com.daawtec.scancheck.ui.SiteDistributionFragment;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.SyncAsyncTask;
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
    String mCodeAs, mCodeSd, mCodeAgent, mCodeTypeAgent;
    boolean isAgentIT = false;
    String mCodeAsSD;

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

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();
        mCodeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_AGENT, null);
        mCodeTypeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_AGENT, null);
        db = ScanCheckDB.getDatabase(this);

        if (mCodeTypeAgent.equals(Constant.IT_DENOMBREMENT)) {
            isAgentIT = true;
            setTitle("Enregistrer ménage spécial");
        } else {
            setTitle("Enregistrer ménage");
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentFragment instanceof MacaronFragment){
                    Intent intent = new Intent(BaseActivity.this, QrCodeActivity.class);
                    startActivityForResult( intent, Constant.REQUEST_CODE_QR_SCAN_MACARON);
                } else if (mCurrentFragment instanceof MenageFragment) {

//                    Intent intent = new Intent(BaseActivity.this, CreateMenageActivity.class);
//                    intent.putExtra(Constant.CODE_QR, "code_qr");
//                    startActivityForResult(intent, Constant.CODE_ACTION_ADD_MENAGE);

                    Intent intent = new Intent(BaseActivity.this, QrCodeActivity.class);
                    startActivityForResult( intent, Constant.REQUEST_CODE_QR_SCAN_MENAGE);

                } else if (mCurrentFragment instanceof RapportFragment) {
                    Snackbar.make(view, "Rapport Fragment", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (mCurrentFragment instanceof SiteDistributionFragment){
                    Snackbar.make(view, "Ajouter site de distribution", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    showCreateSdDialog("", BaseActivity.this, "QW");
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

            Log.e(TAG, "onActivityResult: SCAN: code as : " + mCodeAs );

            if(data==null)
                return;
            String dateEnregistrement = Utils.formatDate(mCalendar.getTime());

            if (mCodeAs != null) {
                //Getting the passed result
                String qrCode = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Log.e(TAG, "onActivityResult: code macaron: " + qrCode );
                Macaron macaron = new Macaron(qrCode, mCodeAs, mCodeAgent,
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.base_activity_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_synchroniser){
            new SyncAsyncTask(this).execute();
        }

        return true;
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
                mCurrentFragment = MacaronFragment.newInstance(mCodeAgent, mCodeAs);
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;
            case Constant.ACTION_MENAGE_ACTIVITY:
                if (isAgentIT) setTitle("Ménages speciaux");
                else setTitle("Ménages");
                mCurrentFragment = MenageFragment.newInstance(mCodeAgent);
                mFragmentManager.beginTransaction()
                        .replace(R.id.container, mCurrentFragment)
                        .commit();
                break;
                case Constant.ACTION_DISTRIBUTION_ACTIVITY:
                    setTitle("Distribution");
                    mCurrentFragment = DistributionFragment.newInstance("");
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
                }
            }

            @Override
            protected long[] doInBackground(Void... voids) {
                Log.e(TAG, "doInBackground: " + macaron.toString() );
                Affectation affectation = db.getIAffectation().getAffectationByAgent(mCodeAgent);
                macaron.codeAs = affectation.CodeAs;
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

    public void showCreateSdDialog(String message, Context context, final String codeMacaron) {

        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Creation de Site de Distribution");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_confirmation_mild, null);
        builder.setView(customLayout);
        final EditText nomET = customLayout.findViewById(R.id.nombre_mild_et);
        final EditText populationET = customLayout.findViewById(R.id.population_et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nom = nomET.getText().toString();
                int population = Integer.parseInt(populationET.getText().toString());
                saveSD(nom, population);
            }
        });

        builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveSD(final String nom, final int population){
        (new AsyncTask<Void, Void, long[]>(){
            @Override
            protected void onPostExecute(long[] longs) {
                super.onPostExecute(longs);

                if (longs[0] > 0) {
                    Toast.makeText(BaseActivity.this, "Site de distribution créée", Toast.LENGTH_SHORT).show();
                    if (mCurrentFragment instanceof SiteDistributionFragment) {
                        ((SiteDistributionFragment) mCurrentFragment).loadSD();
                    }
                }
            }

            @Override
            protected long[] doInBackground(Void... voids) {
                SiteDistribution site = new SiteDistribution();
                site.codeSD = Utils.generateId();
                Affectation affectation = db.getIAffectation().getAffectationByAgent(mCodeAgent);

                site.codeAS = affectation.CodeAs;
                site.nom = nom;

                affectation.populationMacroPlan = population;
                db.getIAffectation().update(affectation);

                return db.getISiteDistributionDao().insert(site);
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
