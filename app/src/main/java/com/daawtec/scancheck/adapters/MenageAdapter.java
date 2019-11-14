package com.daawtec.scancheck.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.Macaron;
import com.daawtec.scancheck.entites.Menage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenageAdapter extends RecyclerView.Adapter<MenageAdapter.VH> {

    Activity mActivity;
    List<Menage> mMenages = new ArrayList<>();
    String mDateFormat = "dd/MM/yyyy";
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(mDateFormat, Locale.FRANCE);
    ScanCheckDB db;

    public static class VH extends RecyclerView.ViewHolder{
        TextView nomResponsableTV, tailleMenageTV, dateIdentificationTV;

        public VH(@NonNull View view){
            super(view);
             nomResponsableTV = view.findViewById(R.id.nom_responsable_tv);
             tailleMenageTV = view.findViewById(R.id.taille_menage_tv);
             dateIdentificationTV = view.findViewById(R.id.date_identification_tv);
        }
    }

    public MenageAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        db = ScanCheckDB.getDatabase(mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        final Menage menage = mMenages.get(i);
        vh.nomResponsableTV.setText(menage.nomResponsable);
        vh.tailleMenageTV.setText("" + menage.tailleMenage);
        vh.dateIdentificationTV.setText(mSimpleDateFormat.format(menage.dateIdentification));

        vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog("Etes-vous sur de vouloir supprimer ce MENAGE ?", mActivity, menage);
                return true;
            }
        });
    }

    @NonNull
    @Override
    public MenageAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menage_item, viewGroup, false);
        return new MenageAdapter.VH(view);
    }

    @Override
    public int getItemCount() {
        return mMenages.size();
    }

    public void addAll(List<Menage> menages){
        this.mMenages.addAll(menages);
    }

    public void clear(){
        mMenages.clear();
    }

    public void deleteMenage(final Menage menage) {
        (new AsyncTask<Void, Void, Integer>(){
            @Override
            protected void onPostExecute(Integer value) {
                super.onPostExecute(value);

                if (value > 0){
                    Toast.makeText(mActivity, "Menage supprimé et le macaron associé est desaffecté", Toast.LENGTH_LONG).show();
                    mMenages.remove(menage);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mActivity, "Impossible du supprimer le ménage", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                int result = db.getIMenageDao().delete(menage);
                //The menage has been deleted successfully, de-assign the corresponding macaron
                if (result > 0) {
                    int resultTwo = db.getIMacaronDao().updateMacaronState(false, menage.codeMacaron);
                    if (resultTwo > 0) {
                        return 1;
                    } else return 0;
                } else return 0;
            }
        }).execute();
    }

    void showDeleteDialog(final String message, Context context, final Menage menage){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMenage(menage);
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
}
