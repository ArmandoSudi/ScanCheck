package com.daawtec.scancheck;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Agent;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.Utils;

import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAgentDenombrementActivity extends AppCompatActivity {

    private static final String TAG = "CreateAgentDenombrement";

    @BindView(R.id.nom_agent_et)
    EditText mNomAgentET;
    @BindView(R.id.code_authentification_tv)
    TextView mCodeAuthentificationTV;
    @BindView(R.id.numero_telephone_et)
    EditText mNumeroTelephoneET;
    @BindView(R.id.save_bt)
    Button mSaveBT;

    String mCodeAgent;
    SharedPreferences mSharedPref;
    ScanCheckDB db;
    final String mCodeAffectation = Utils.generateId();
    String mCodeAuthentification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_agent_denombrement);

        ButterKnife.bind(this);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        db = ScanCheckDB.getDatabase(this);

        mCodeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_AGENT, null);

        if (mCodeAgent == null) {
            Toast.makeText(this, "IT Denombremement introuvable", Toast.LENGTH_SHORT).show();
            finish();
        }


        mSaveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAgent(collectData());
            }
        });

    }

    public Agent collectData() {

        boolean isValid = true;

        String nom = mNomAgentET.getText().toString();
        String numero = mNumeroTelephoneET.getText().toString();

        if (nom.equals("")) isValid = false;
        if (numero.equals("")) isValid = false;

        Random random = new Random();

        Agent agent = new Agent();
        agent.NomAgent = nom;

        mCodeAuthentification = Integer.toString(1000 + random.nextInt(9000) );
        agent.CodeAuthentification = mCodeAuthentification;

        agent.telephone = numero;
        agent.CodeAgent = Utils.generateId();

        if (isValid) return agent;
        else return null;
    }

    public void saveAgent(final Agent agent){
        (new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);

                    if (result) {
                        Toast.makeText(CreateAgentDenombrementActivity.this, "Agent Enregistre", Toast.LENGTH_SHORT).show();
                        mCodeAuthentificationTV.setText(mCodeAuthentification + "");
//                        Intent intent = new Intent(CreateAgentDenombrementActivity.this, ListeAgentActivity.class);
//                        startActivity(intent);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",result);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    } else {
                        Toast.makeText(CreateAgentDenombrementActivity.this, "Echec d'enregistrement", Toast.LENGTH_SHORT).show();
                    }

            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                Affectation affectationIT = null;

                try {
                    affectationIT = db.getIAffectation().getAffectationByAgent(mCodeAgent);
                } catch (Exception ex){
                    Log.e(TAG, "doInBackground: " + ex.getMessage() );
                    return null;
                }

                if (affectationIT != null) {
                    Affectation affectation = new Affectation();
                    affectation.codeAffectation = mCodeAffectation;
                    affectation.CodeAs = affectationIT.CodeAs;
                    affectation.CodeCampagne = affectationIT.CodeCampagne;
                    affectation.codeAgent = agent.CodeAgent;
                    affectation.codeTypeAgent = Constant.AGENT_DENOMBREMENT;
                    affectation.dateAffectation = Utils.formatDate(new Date());

                    try {
                        db.getIAgentDao().insert(agent);
                        db.getIAffectation().insert(affectation);

                        return true;

                    } catch (Exception ex){
                        Log.e(TAG, "doInBackground: " + ex.getMessage());
                        return false;
                    }
                }

                return null;
            }
        }).execute();
    }

    public void loadAgent(){
        (new AsyncTask<Void, Void, Agent>(){
            @Override
            protected void onPostExecute(Agent agent) {
                super.onPostExecute(agent);
            }

            @Override
            protected Agent doInBackground(Void... voids) {
                return null;
            }
        }).execute();
    }
}
