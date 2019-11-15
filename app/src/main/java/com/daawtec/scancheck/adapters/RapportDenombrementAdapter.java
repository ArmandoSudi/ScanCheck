package com.daawtec.scancheck.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.entites.RapportDenombrement;

import java.util.ArrayList;
import java.util.List;

public class RapportDenombrementAdapter extends RecyclerView.Adapter<RapportDenombrementAdapter.VH> {

    Activity mActivity;
    List<RapportDenombrement> mRapportDenombrements = new ArrayList<>();

    public RapportDenombrementAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public static class VH extends RecyclerView.ViewHolder{
        TextView macaronRecuTV, macaronUtiliseTV, menageTV, menageOneTwoTV, menageThreeFourTV, menageFiveSixTV, menageSevenEightTV, soldeMacaronTV, titleTV;
        public VH(View view){
            super(view);
            macaronRecuTV = view.findViewById(R.id.macaron_recu_tv);
            macaronUtiliseTV = view.findViewById(R.id.macaron_utilise_tv);
            menageTV = view.findViewById(R.id.menage_tv);
            menageOneTwoTV = view.findViewById(R.id.menage_one_two_tv);
            menageThreeFourTV = view.findViewById(R.id.menage_three_four_tv);
            menageFiveSixTV = view.findViewById(R.id.menage_five_six_tv);
            menageSevenEightTV = view.findViewById(R.id.menage_seven_eight_tv);
            soldeMacaronTV = view.findViewById(R.id.solde_macaron_tv);
            titleTV = view.findViewById(R.id.title_tv);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rapport_denombrement_item, viewGroup, false);
        return new RapportDenombrementAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        RapportDenombrement rapport = mRapportDenombrements.get(i);

        vh.macaronRecuTV.setText("" + rapport.macaronRecu);
        vh.macaronUtiliseTV.setText("" + rapport.macaronUtilise);
        vh.menageTV.setText("" + rapport.menage);
        vh.menageOneTwoTV.setText("" + rapport.menageOneTwo);
        vh.menageThreeFourTV.setText("" + rapport.menageThreeFour);
        vh.menageFiveSixTV.setText("" + rapport.menageFiveSix);
        vh.menageSevenEightTV.setText("" + rapport.menageSevenEight);
        vh.soldeMacaronTV.setText("" + rapport.soldeMacaron);
        vh.titleTV.setText("Rapport du : " + rapport.date);
    }

    @Override
    public int getItemCount() {
        return mRapportDenombrements.size();
    }

    public void addAll(List<RapportDenombrement> rapportDenombrements){
        mRapportDenombrements.addAll(rapportDenombrements);
    }

    public void clear(){
        mRapportDenombrements.clear();
    }
}
