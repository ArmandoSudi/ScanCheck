package com.daawtec.scancheck.adapters;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.entites.InventairePhysique;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InventairePhysiqueAdapter extends RecyclerView.Adapter<InventairePhysiqueAdapter.VH> {

    Activity mActivity;
    List<InventairePhysique> mInvetaires = new ArrayList<>();
    String mDateFormat = "dd/MM/yyyy";
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(mDateFormat, Locale.FRANCE);

    public static class VH extends RecyclerView.ViewHolder{
        TextView dateTV, quantitePhysiqueTV, quantiteTheoriqueTV, ecartTV, nbreMacaronTV;

        public VH(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.date_tv);
            quantitePhysiqueTV = itemView.findViewById(R.id.quantite_physique_tv);
            quantiteTheoriqueTV = itemView.findViewById(R.id.quantite_theorique_tv);
            ecartTV = itemView.findViewById(R.id.ecart_tv);
            nbreMacaronTV = itemView.findViewById(R.id.nbre_macaron_tv);
        }
    }

    public InventairePhysiqueAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int position) {
        final InventairePhysique inventaire = mInvetaires.get(position);
        vh.dateTV.setText(mSimpleDateFormat.format(inventaire.date));
        vh.quantitePhysiqueTV.setText("" + inventaire.getQuantitePhysique());
        vh.quantiteTheoriqueTV.setText("" + inventaire.getQuantiteTheorique());
        vh.ecartTV.setText("" + inventaire.getEcart());
        vh.nbreMacaronTV.setText("" + inventaire.getNombreMacaron());
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.inventaire_item, viewGroup, false);
        return new VH(view);
    }

    @Override
    public int getItemCount() {
        return mInvetaires.size();
    }

    public void clear(){
        mInvetaires.clear();
    }

    public void addAll(List<InventairePhysique> inventaires){
        mInvetaires.addAll(inventaires);
    }
}
