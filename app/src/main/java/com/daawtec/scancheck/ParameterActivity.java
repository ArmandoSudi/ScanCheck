package com.daawtec.scancheck;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.AirsSante;
import com.daawtec.scancheck.entites.RelaisCommunautaire;
import com.daawtec.scancheck.utils.Constant;

import java.util.List;

public class ParameterActivity extends AppCompatActivity {

    private static final String TAG = "ParameterActivity";

    LinearLayout mUsernameLL, mPasswordLL;
    EditText mUsernameET, mPasswordET;
    TextView mUsernameTV, mPasswordTV;
    Spinner mRecoSP, mAsSP;

    SharedPreferences mSharedPref;
    SharedPreferences.Editor mEditor;

    ScanCheckDB db;

    String mRecoCode, mAsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        db = ScanCheckDB.getDatabase(this);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPref.edit();

        mUsernameTV = findViewById(R.id.username_tv);
        mPasswordTV = findViewById(R.id.password_tv);

        refresh();

        initView();
    }

    public void initView(){

        LayoutInflater inflater = this.getLayoutInflater();

        final View userNameView = inflater.inflate(R.layout.username_dialog, null);
        final View passwordView = inflater.inflate(R.layout.password_dialog, null);

        mUsernameLL = findViewById(R.id.username_ll);
        mPasswordLL = findViewById(R.id.password_ll);

        mUsernameET = userNameView.findViewById(R.id.username_et);
        mPasswordET = passwordView.findViewById(R.id.password_et);

        final AlertDialog usernameDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Changer le nom d'utilisateur")
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton("Valider",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveUsername(mUsernameET.getText().toString());
                                refresh();
                            }
                        })
                .create();
        final AlertDialog passwordDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Changer le mot de passe")
                .setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton("Valider",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                savePassword(mPasswordET.getText().toString());
                                refresh();
                            }
                        })
                .create();


        mUsernameLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameDialogBuilder.setView(userNameView);
                usernameDialogBuilder.show();
            }
        });


        mPasswordLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordDialogBuilder.setView(passwordView);
                passwordDialogBuilder.show();
            }
        });

        mAsSP = findViewById(R.id.as_sp);
        mRecoSP = findViewById(R.id.reco_sp);
        loadAs();
        mAsSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AirsSante AS = (AirsSante) parent.getItemAtPosition(position);
                mEditor.putString(Constant.KEY_USER_AS, AS.getCodeAS());
                mEditor.commit();
                loadReco(AS.getCodeAS());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRecoSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RelaisCommunautaire reco = (RelaisCommunautaire) parent.getItemAtPosition(position);
                mEditor.putString(Constant.KEY_USER_RECO, reco.getCodeReco());
                mEditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void savePassword(String password){
        mEditor.putString(Constant.KEY_PASSWORD, password);
        mEditor.apply();
        Toast.makeText(this, "Vous aviez change votre mot de passe", Toast.LENGTH_SHORT).show();
    }

    public void saveUsername(String username){
        mEditor.putString(Constant.KEY_USERNAME, username);
        mEditor.apply();
        Toast.makeText(this, "Vous aviez change votre nom d'utilisateur", Toast.LENGTH_SHORT).show();
    }

    public void refresh(){
        String username = mSharedPref.getString(Constant.KEY_USERNAME, "Pas Configure");
        String password = mSharedPref.getString(Constant.KEY_PASSWORD, "");
        mUsernameTV.setText(username);
        mPasswordTV.setText(password);
    }

    public void loadAs(){
        (new AsyncTask<Void, Void, List<AirsSante>>(){
            @Override
            protected void onPostExecute(List<AirsSante> airsSantes) {
                super.onPostExecute(airsSantes);
                if (airsSantes != null && airsSantes.size() > 0 ) {
                    mAsSP.setAdapter(new ArrayAdapter<AirsSante>(
                            ParameterActivity.this,
                            android.R.layout.simple_spinner_item,
                            airsSantes
                    ));
                }
            }

            @Override
            protected List<AirsSante> doInBackground(Void... voids) {
                return db.getIAirSanteDao().all();
            }
        }).execute();
    }

    public void loadReco(final String asCode){
        (new AsyncTask<Void, Void, List<RelaisCommunautaire>>(){
            @Override
            protected void onPostExecute(List<RelaisCommunautaire> relaisCommunautaires) {
                super.onPostExecute(relaisCommunautaires);


                if (relaisCommunautaires != null && relaisCommunautaires.size() > 0) {
                    Log.e(TAG, "onPostExecute: " + relaisCommunautaires.get(0));
                    mRecoSP.setAdapter(new ArrayAdapter<RelaisCommunautaire>(
                            ParameterActivity.this,
                            android.R.layout.simple_spinner_item,
                            relaisCommunautaires));
                } else {
                    mRecoSP.setAdapter(null);
                }
            }

            @Override
            protected List<RelaisCommunautaire> doInBackground(Void... voids) {
                return db.getIRelaisCommunautaireDao().get(asCode);
            }
        }).execute();
    }
}
