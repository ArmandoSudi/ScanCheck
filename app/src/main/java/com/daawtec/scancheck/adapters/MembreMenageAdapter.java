package com.daawtec.scancheck.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.MembreMenage;

import java.util.ArrayList;
import java.util.List;

public class MembreMenageAdapter extends RecyclerView.Adapter<MembreMenageAdapter.VH> {

    Activity mActivity;
    List<MembreMenage> membreMenages = new ArrayList<>();
    ScanCheckDB db;

    public static class VH extends RecyclerView.ViewHolder{
        TextView nameTV;
        public VH(View view){
            super(view);
            nameTV = view.findViewById(R.id.name_tv);
        }
    }

    public MembreMenageAdapter(Activity mActivity) {
        this.mActivity = mActivity;
//        this.db = db;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        final MembreMenage membreMenage = membreMenages.get(i);
        vh.nameTV.setText(membreMenage.Nom + " " + membreMenage.Prenom);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.membre_menage_item, viewGroup, false);
        return new VH(view);
    }

    @Override
    public int getItemCount() {
        return membreMenages.size();
    }

    public void addAll(List<MembreMenage> membreMenages){
        this.membreMenages.addAll(membreMenages);
    }

    public List<MembreMenage> all(){
        return this.membreMenages;
    }

    public void add(MembreMenage membreMenage){
        this.membreMenages.add(membreMenage);
    }

    public void clear(){
        this.membreMenages.clear();
    }
}
