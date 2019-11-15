package com.daawtec.scancheck.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Macaron;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MacaronAdapter extends RecyclerView.Adapter<MacaronAdapter.VH> {

    private static final String TAG = "MacaronAdapter";

    Activity mActivity;
    public List<Macaron> macarons = new ArrayList<>();
    String mDateFormat = "dd/MM/yyyy";
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(mDateFormat, Locale.FRANCE);
    ScanCheckDB db;

    public static class VH extends RecyclerView.ViewHolder{
        TextView codeMacaronTV, dateEnregistrementTV;
        ImageView stateIV;
        public VH(View view){
            super(view);
            codeMacaronTV = view.findViewById(R.id.code_macaron_tv);
            dateEnregistrementTV = view.findViewById(R.id.date_enregistrement_tv);
            stateIV = view.findViewById(R.id.state_iv);
        }
    }

    public MacaronAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        db = ScanCheckDB.getDatabase(mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        final Macaron macaron = macarons.get(i);
        vh.codeMacaronTV.setText(macaron.codeMacaron + "");
        vh.dateEnregistrementTV.setText(macaron.dateEnregistrement);
        if (macaron.isAffected) vh.stateIV.setImageResource(R.drawable.green_circle);
        else vh.stateIV.setImageResource(R.drawable.red_circle);

        vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog("Etes-vous sur de vouloir supprimer ce MACARON ?", mActivity, macaron);
                return true;
            }
        });

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " + macaron );
            }
        });
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.macaron_item, viewGroup, false);
        return new VH(view);
    }

    @Override
    public int getItemCount() {
        return macarons.size();
    }

    public void addAll(List<Macaron> macaronList){
        macarons.addAll(macaronList);
    }

    public void clear(){
        macarons.clear();
    }

    void showDeleteDialog(String message, Context context, final Macaron macaron){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMacaron(macaron);
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

    public void deleteMacaron(final Macaron macaron){
        (new AsyncTask<Void, Void, Integer>(){
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                
                if (integer > 0) {
                    Toast.makeText(mActivity, "Macaron supprimé", Toast.LENGTH_LONG).show();
                    macarons.remove(macaron);
                    notifyDataSetChanged();
                }
                else Toast.makeText(mActivity, "Impossible de supprimer le macaron", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                return db.getIMacaronDao().delete(macaron);
            }
        }).execute();
    }
}
