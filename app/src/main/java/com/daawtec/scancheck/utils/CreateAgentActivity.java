package com.daawtec.scancheck.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.daawtec.scancheck.DashboardActivity;
import com.daawtec.scancheck.R;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.AgentDenombrement;
import com.daawtec.scancheck.entites.AirsSante;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class CreateAgentActivity extends AppCompatActivity {
    private static final String TAG = "CreateAgentActivity";

    @BindView(R.id.nom_et)
    EditText mNomET;
    @BindView(R.id.as_sp)
    Spinner mAsSP;

    SharedPreferences mSharedPref;
    ScanCheckDB db;

    String mCodeAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_agent);

        ButterKnife.bind(this);

        db = ScanCheckDB.getDatabase(this);

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAgentRegistered = mSharedPref.getBoolean(Constant.KEY_IS_AGENT_REGISTERED, false);
        if (isAgentRegistered){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }

        loadAirSante();

        mAsSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AirsSante airSante = (AirsSante) parent.getItemAtPosition(position);
                mCodeAs = airSante.codeAS;
                Log.e(TAG, "onItemSelected: " + mCodeAs );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.save_bt)
    public void collectAgent(){

        String nom = mNomET.getText().toString();

        if (nom.equals("")){
            mNomET.setError("Le nom ne peut pas etre vide");
            return;
        }

        if (nom != null || mCodeAs != null) {
            AgentDenombrement agent = new AgentDenombrement();
            agent.nom = nom;
            agent.codeAs = mCodeAs;
            agent.codeAgentDenombrement = Utils.getTimeStamp();
            saveAgent(agent);
        }
    }

    public void saveAgent(final AgentDenombrement agent){
        (new AsyncTask<Void, Void, long[]>(){
            @Override
            protected long[] doInBackground(Void... voids) {
                return db.getIAgentDenombrementDao().insert(agent);
            }

            @Override
            protected void onPostExecute(long[] longs) {
                super.onPostExecute(longs);

                if (longs[0] > 0){

                    SharedPreferences.Editor editor = mSharedPref.edit();
                    editor.putBoolean(Constant.KEY_IS_AGENT_REGISTERED, true);
                    editor.putString(Constant.KEY_CODE_AGENT_DENOMBREMENT, agent.codeAgentDenombrement);
                    editor.putString(Constant.KEY_CODE_AGENT_AS, agent.codeAs);
                    editor.commit();

                    Toast.makeText(CreateAgentActivity.this, "Agent crée", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreateAgentActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            }
        }).execute();
    }

    public void loadAirSante(){
        (new AsyncTask<Void, Void, List<AirsSante>>(){
            @Override
            protected void onPostExecute(List<AirsSante> airsSantes) {
                super.onPostExecute(airsSantes);


                if (airsSantes != null ){
                    if (airsSantes.size() > 0){
                        airsSantes.add(0, new AirsSante());
                        mAsSP.setAdapter(new ArrayAdapter<>(
                                CreateAgentActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                airsSantes
                        ));
                    }
                }
            }

            @Override
            protected List<AirsSante> doInBackground(Void... voids) {
                return db.getIAirSanteDao().all();
            }
        }).execute();
    }
}
