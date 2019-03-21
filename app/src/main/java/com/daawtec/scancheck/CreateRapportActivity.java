package com.daawtec.scancheck;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.InventairePhysique;
import com.daawtec.scancheck.entites.SiteDistribution;
import com.daawtec.scancheck.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateRapportActivity extends AppCompatActivity {

    private static final String TAG = "CreateRapportActivity";

    EditText mQuantitePhysiqueET, mNombreMacaronET;
    ImageView mDateIV;
    TextView mDateIdentificationTV, mQuantiteTheoriqueTV, mEcartTV;
    Button mSaveBT;
    Spinner mSiteDistributionSP;
    Date mDateIdentification;

    private Calendar mCalendar = Calendar.getInstance();

    String mCodeSD;
    int mQuantiteDispo;

    ScanCheckDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rapport);

        db = ScanCheckDB.getDatabase(this);

        initView();
    }

    public void initView() {
        mQuantiteTheoriqueTV = findViewById(R.id.quantite_theorique_tv);
        mQuantitePhysiqueET = findViewById(R.id.quantite_physique_et);
        mEcartTV = findViewById(R.id.ecart_tv);
        mNombreMacaronET = findViewById(R.id.nombre_macaron_et);
        mDateIdentificationTV = findViewById(R.id.date_identification_tv);
        mSiteDistributionSP = findViewById(R.id.site_distribution_sp);

        loadSiteDistribution(mSiteDistributionSP);

        mSiteDistributionSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SiteDistribution siteDistribution = (SiteDistribution) parent.getItemAtPosition(position);
                mCodeSD = siteDistribution.codeSD;
                mQuantiteDispo = siteDistribution.quantiteLivree;
                mQuantiteTheoriqueTV.setText("" + mQuantiteDispo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mQuantitePhysiqueET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null ) {
                    if (!s.toString().equals("")) {
                        int quantitePhysique = Integer.parseInt(s.toString());
                        updateEcart(quantitePhysique);
                        Log.d(TAG, "beforeTextChanged: " + quantitePhysique);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final DatePickerDialog.OnDateSetListener dateIdentificationListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(mDateIdentificationTV);
                mDateIdentification  = mCalendar.getTime();
            }
        };

        mDateIV = findViewById(R.id.date_identification_iv);
        mDateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateRapportActivity.this, dateIdentificationListener, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mSaveBT = findViewById(R.id.save_bt);
        mSaveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectRapport();
            }
        });

    }

    public void updateLabel(TextView view) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        view.setText(sdf.format(mCalendar.getTime()));
    }

    public void updateEcart(int quantitePhysique){
        int ecart = quantitePhysique - mQuantiteDispo;
        mEcartTV.setText("" + ecart);
    }

    public void collectRapport() {
        int quantiteTheorique = Utils.stringToInt(mQuantiteTheoriqueTV.getText().toString());
        int quantitePyhsique = Utils.stringToInt(mQuantitePhysiqueET.getText().toString());
        int ecart = Utils.stringToInt(mEcartTV.getText().toString());
        int nombreMacaron = Utils.stringToInt(mNombreMacaronET.getText().toString());

        boolean isValid = true;

        if (quantitePyhsique == 0){ isValid = false; }
        if (mDateIdentification == null){ isValid = false; }
        if (nombreMacaron == 0) { isValid = false; }

        String codeInventairePhysique = Utils.getTimeStamp();

        if (isValid) {
            InventairePhysique inventairePhysique = new InventairePhysique(codeInventairePhysique, mCodeSD, mDateIdentification,
                    quantiteTheorique, quantitePyhsique, ecart, nombreMacaron);
            saveRapport(inventairePhysique);
        } else {
            showErrorMessage();
        }

    }

    public void saveRapport(final InventairePhysique inventairePhysique) {
        (new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast.makeText(CreateRapportActivity.this, "Rapport enregistre", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateRapportActivity.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                db.getIIventairePhysiqueDao().insert(inventairePhysique);
                return null;
            }
        }).execute();
    }

    public void loadSiteDistribution(final Spinner spinner){
        (new AsyncTask<Void, Void, List<SiteDistribution>>(){
            @Override
            protected void onPostExecute(List<SiteDistribution> siteDistributions) {
                super.onPostExecute(siteDistributions);

                if (siteDistributions != null ){
                    if(siteDistributions.size() > 0) {
                        spinner.setAdapter(new ArrayAdapter<SiteDistribution>(CreateRapportActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, siteDistributions));
                    }
                }
            }

            @Override
            protected List<SiteDistribution> doInBackground(Void... voids) {
                List<SiteDistribution> sites = db.getISiteDistributionDao().all();
                return sites;
            }
        }).execute();
    }

    public void showErrorMessage(){
        Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
    }


}
