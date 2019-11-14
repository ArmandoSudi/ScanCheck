package com.daawtec.scancheck.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.daawtec.scancheck.HomeActivity;
import com.daawtec.scancheck.R;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.BadVerification;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.Menage;
import com.daawtec.scancheck.utils.Utils;

import java.util.Calendar;

public class DistributionFragment extends Fragment {

    private static final String TAG = "DistributionFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";

    TextView numeroMacaronTV;

    Activity mActivity;
    ScanCheckDB db;
    Calendar calendar;
    String mCodeAgentDistribution;

    public DistributionFragment() {}

    public static DistributionFragment newInstance(String codeAgentDistribution) {
        DistributionFragment fragment = new DistributionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        db = ScanCheckDB.getDatabase(mActivity);
        calendar = Calendar.getInstance();
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
                startActivityForResult( i, HomeActivity.REQUEST_CODE_QR_SCAN);
            }
        });

        numeroMacaronTV = rootView.findViewById(R.id.numero_macaron_tv);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == HomeActivity.REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String qrCode = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            numeroMacaronTV.setText("" + qrCode);
            Log.d(TAG,"Scan result : "+ qrCode);

            checkMacaron(qrCode);

        }
    }

    /**
     * Verifie si un macaron avec le code secret passe en parametre existe dans la base des donnees.
     * @param codeSecret
     */
    public void checkMacaron(final String codeSecret){
        (new AsyncTask<Void, Void, Integer>(){

            final int MACARON_EXIST = 100;
            final int MACARON_NON_EXIST = 200;
            final int BAD_VERIFICATION = 300;
            final int MENAGE_SERVI = 400;
            int nombreMild;
            String codeMacaron;

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                    switch(result){
                        case MACARON_EXIST:
                            showAffectationDialog("Ce menage est assigné " + nombreMild + " MILDs, Voulez-vous le servir ?", mActivity, codeMacaron);
//                            showAlertDialogButtonClicked("Ce menage est assigné " + nombreMild + " MILDs", mActivity, codeMacaron);
                            break;
                        case MACARON_NON_EXIST:
                            Toast.makeText(mActivity, "Ce macaron n'a pas été affecté", Toast.LENGTH_SHORT).show();
                            break;
                        case BAD_VERIFICATION:
                            showBadverificationDialog("Ce macaron est frauduleux. Voulez vous l'enregistrer comme tentative de fraude ?", mActivity, codeSecret);

                            break;
                        case MENAGE_SERVI:
                            Toast.makeText(mActivity, "Le menage a déjà servi", Toast.LENGTH_SHORT).show();
                            break;
                    }
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                Macaron macaron =  db.getIMacaronDao().get(codeSecret);
                if (macaron instanceof Macaron){
                    if (macaron.isAffected){
                        codeMacaron = macaron.codeMacaron;
                        Menage menage = db.getIMenageDao().getByCodeMacaron(codeMacaron);
                        if (!menage.etatServi){
                            nombreMild = menage.nombreMild;
                            return MACARON_EXIST;
                        } else {
                            return MENAGE_SERVI;
                        }
                    } else {
                        return MACARON_NON_EXIST;
                    }
                } else {
                    return BAD_VERIFICATION;
                }
            }
        }).execute();
    }

    /**
     * Mettre a jour la date_verification dans la table MACARON_AS dont le codeMacaron correspond
     * @param codeMacaron
     */
    public void updateMenage(final String codeMacaron, final int nombreMildServi){
        (new AsyncTask<Void, Void, Integer>(){
            @Override
            protected void onPostExecute(Integer row) {
                super.onPostExecute(row);

                if (row > 0){
                    Toast.makeText(mActivity, "Menage servi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                Menage menage = db.getIMenageDao().getByCodeMacaron(codeMacaron);

                if (menage instanceof Menage){
                    menage.nombreMildServi = nombreMildServi;
                    menage.etatServi = true;
                    return db.getIMenageDao().update(menage);
                } else {
                   return 0;
                }
            }
        }).execute();
    }

    /**
     * Creer une entree dans la table BAD_VERIFICATION pour le codeSecret encode dans le macaron
     * frauduleux
     * @param codeSecret
     */
    public void saveBadVerification(String codeSecret){
        final BadVerification badVerification = new BadVerification(Utils.getTimeStamp(),
                "raison echec",
                codeSecret,
                calendar.getTime());

        (new AsyncTask<Void, Void, long[]>(){
            @Override
            protected void onPostExecute(long[] rows) {
                super.onPostExecute(rows);

                Log.e(TAG, "onPostExecute: " + rows[0]);

            }

            @Override
            protected long[] doInBackground(Void... voids) {
                return db.getIBadVerificationDao().insert(badVerification);
            }
        }).execute();

    }

    void showAffectationDialog(String message, Context context, final String codeMacaron){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateMenage(codeMacaron, 0);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ANNULER",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    void showBadverificationDialog(String message, Context context, final String codeSecret){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Resultat");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveBadVerification(codeSecret);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "ANNULER",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void showAlertDialogButtonClicked(String message, Context context, final String codeMacaron) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_confirmation_mild, null);
        builder.setView(customLayout);
        final EditText nombreMildET = customLayout.findViewById(R.id.nombre_mild_et);
//        final TextView messageTV = customLayout.findViewById(R.id.message_tv);
//        messageTV.setText(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int nombreMildServi = Utils.stringToInt(nombreMildET.getText().toString());
                updateMenage(codeMacaron, nombreMildServi);
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
    // do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(mActivity, data, Toast.LENGTH_SHORT).show();
    }

}
