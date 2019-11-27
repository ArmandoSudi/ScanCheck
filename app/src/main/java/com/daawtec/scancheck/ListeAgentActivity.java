package com.daawtec.scancheck;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Affectation;
import com.daawtec.scancheck.entites.Agent;
import com.daawtec.scancheck.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class ListeAgentActivity extends AppCompatActivity {

    ScanCheckDB db;
    ListView agentLV;
    SharedPreferences mSharedPref;
    String mCodeAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_agent);

        db = ScanCheckDB.getDatabase(this);
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mCodeAgent = mSharedPref.getString(Constant.KEY_CURRENT_CODE_AGENT, null);

        if (mCodeAgent == null){
            finish();
        }

        agentLV = findViewById(R.id.agent_lv);

        loadAgent(mCodeAgent);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListeAgentActivity.this, CreateAgentDenombrementActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE_CREATE_AGENT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE_CREATE_AGENT){
            if (resultCode == Activity.RESULT_OK){
                loadAgent(mCodeAgent);
            }
        }
    }

    public void loadAgent(final String codeAgent){
        (new AsyncTask<Void, Void, List<Agent>>(){
            @Override
            protected void onPostExecute(List<Agent> agents) {
                super.onPostExecute(agents);

                agentLV.setAdapter(new ArrayAdapter<>(
                        ListeAgentActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        agents
                ));

            }

            @Override
            protected List<Agent> doInBackground(Void... voids) {
                Affectation affectation = db.getIAffectation().getAffectationByAgent(codeAgent);
                List<Affectation> affectations = db.getIAffectation().getByCodeAs(affectation.CodeAs);
                List<Agent> agents = new ArrayList<>();
                if (affectations != null && affectations.size()>0){
                    for(int i=0; i<affectations.size(); i++){
                        agents.add(db.getIAgentDao().get(affectations.get(i).codeAgent));
                    }
                }
                return db.getIAgentDao().all();
            }
        }).execute();
    }
}
