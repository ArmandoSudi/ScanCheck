package com.daawtec.scancheck.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.entites.Macaron;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MacaronAdapter extends RecyclerView.Adapter<MacaronAdapter.VH> {

    Activity mActivity;
    public List<Macaron> macarons = new ArrayList<>();
    String mDateFormat = "dd/MM/yyyy";
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(mDateFormat, Locale.FRANCE);

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
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        final Macaron macaron = macarons.get(i);
        vh.codeMacaronTV.setText(macaron.codeMacaron + "");
        vh.dateEnregistrementTV.setText(mSimpleDateFormat.format(macaron.dateEnregistrement));
        if (macaron.isAffected) vh.stateIV.setImageResource(R.drawable.green_circle);
        else vh.stateIV.setImageResource(R.drawable.red_circle);
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
}
