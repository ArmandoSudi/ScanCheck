package com.daawtec.scancheck;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.adapters.InventairePhysiqueAdapter;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.utils.Constant;
import com.daawtec.scancheck.utils.Utils;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class GestionMildSiteDistActivity extends AppCompatActivity {

    private static final String TAG = "GestionMildSiteDistActi";

    String mCodeSd;
    Calendar mCalendar = Calendar.getInstance();
    ScanCheckDB db;

    @BindView(R.id.dist_inventaire_rv)
    RecyclerView mInventaireRV;
    @BindView(R.id.dist_empty_msg_tv)
    TextView mEmptyMsgTV;

    InventairePhysiqueAdapter mInventaireAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_mild_site_dist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mCodeSd = intent.getStringExtra(Constant.KEY_CODE_SITE_DISTRIBUTION);
        mCodeSd = intent.getStringExtra(Constant.KEY_CODE_AGENT_SD);
        db = ScanCheckDB.getDatabase(this);

        mInventaireAdapter = new InventairePhysiqueAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mInventaireRV.setLayoutManager(linearLayoutManager);
        mInventaireRV.setLayoutManager(linearLayoutManager);
        mInventaireRV.setHasFixedSize(true);
        mInventaireRV.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
        mInventaireRV.setAdapter(mInventaireAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateMildStockDialog("Enregistrement du stock", GestionMildSiteDistActivity.this, "");
            }
        });

        loadInventaire(mCodeSd);
    }

    public void showCreateMildStockDialog(String message, Context context, final String codeMacaron) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_create_stock_mild, null);
        builder.setView(customLayout);
        final EditText nombreMildET = customLayout.findViewById(R.id.nombre_mild_et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int nombreMildServi = Utils.stringToInt(nombreMildET.getText().toString());
                saveInventaire(nombreMildServi);
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

    public void saveInventaire(final int nombreMild){
        (new AsyncTask<Void, Void, long[]>(){
            @Override
            protected void onPostExecute(long[] longs) {
                super.onPostExecute(longs);

                if (longs[0] > 0){
                    Toast.makeText(GestionMildSiteDistActivity.this, "Inventaire enregistre", Toast.LENGTH_SHORT).show();
                    loadInventaire(mCodeSd);
                } else {
                    Toast.makeText(GestionMildSiteDistActivity.this, "Impossible d'enregistrer l'inventaire", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected long[] doInBackground(Void... voids) {
                InventairePhysique inventaire = new InventairePhysique();
                inventaire.codeInventaire = Utils.getTimeStamp();
                inventaire.quantitePhysique = nombreMild;
                inventaire.codeSD = mCodeSd;
                inventaire.date = mCalendar.getTime();

                return db.getIIventairePhysiqueDao().insert(inventaire);
            }
        }).execute();
    }

    public void loadInventaire(final String codeSd){
        (new AsyncTask<Void, Void, List<InventairePhysique>>(){
            @Override
            protected void onPostExecute(List<InventairePhysique> inventairePhysiques) {
                super.onPostExecute(inventairePhysiques);

                if (inventairePhysiques != null) {
                    if (inventairePhysiques.size() > 0){
                        mInventaireAdapter.clear();
                        mInventaireAdapter.addAll(inventairePhysiques);
                        mEmptyMsgTV.setVisibility(View.GONE);
                        mInventaireAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected List<InventairePhysique> doInBackground(Void... voids) {
                return db.getIIventairePhysiqueDao().getByCodeSd(codeSd);
            }
        }).execute();
    }

    public void loadStock(final String codeSd) {
        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                //TODO display the stock related to this SD;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                //TODO load all the stock related to this SD
                return null;
            }
        }).execute();
    }

}
