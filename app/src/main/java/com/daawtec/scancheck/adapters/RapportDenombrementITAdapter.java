package com.daawtec.scancheck.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.entites.RapportDenombrementIT;

import java.util.ArrayList;
import java.util.List;

public class RapportDenombrementITAdapter extends RecyclerView.Adapter<RapportDenombrementITAdapter.VH> {

    Activity mActivity;
    List<RapportDenombrementIT> rapportDenombrementITS = new ArrayList<>();

    public static class VH extends RecyclerView.ViewHolder {
        TextView macaronRecuTV, macaronUtiliseTV, menageTV, orphelinatTV, couventTV;
        TextView internatTV, fosaTV, hotelTV, militaireTV, deplaceTV, refugieTV, prisonTV;

        public VH(View view){
            super(view);
            macaronRecuTV = view.findViewById(R.id.macaron_recu_tv);
            macaronUtiliseTV = view.findViewById(R.id.macaron_utilise_tv);
            menageTV = view.findViewById(R.id.menage_tv);
            orphelinatTV = view.findViewById(R.id.orphelinat_tv);
            couventTV = view.findViewById(R.id.couvent_tv);
            internatTV = view.findViewById(R.id.internat_tv);
            fosaTV = view.findViewById(R.id.fosa_tv);
            hotelTV = view.findViewById(R.id.hotel_tv);
            militaireTV = view.findViewById(R.id.militaire_tv);
            deplaceTV = view.findViewById(R.id.deplace_tv);
            refugieTV = view.findViewById(R.id.refugie_tv);
            prisonTV = view.findViewById(R.id.prison_tv);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rapport_denombrement_it_item, viewGroup, false);
        return new RapportDenombrementITAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return rapportDenombrementITS.size();
    }
}
