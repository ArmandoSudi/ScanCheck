package com.daawtec.scancheck.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daawtec.scancheck.R;
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
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        final Menage menage = mMenages.get(i);
        vh.nomResponsableTV.setText(menage.ageResponsable);
        vh.tailleMenageTV.setText("" + menage.tailleMenage);
        vh.dateIdentificationTV.setText(mSimpleDateFormat.format(menage.dateIdentification));
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
}
