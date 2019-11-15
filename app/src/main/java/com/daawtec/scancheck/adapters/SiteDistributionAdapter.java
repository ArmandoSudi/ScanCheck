package com.daawtec.scancheck.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daawtec.scancheck.R;
import com.daawtec.scancheck.database.ScanCheckDB;
import com.daawtec.scancheck.entites.SiteDistribution;

import java.util.ArrayList;
import java.util.List;

public class SiteDistributionAdapter extends RecyclerView.Adapter<SiteDistributionAdapter.VH> {

    Activity mActivity;
    List<SiteDistribution> mSiteDist = new ArrayList<>();
    ScanCheckDB db;

    public static class VH extends RecyclerView.ViewHolder{
        TextView nomTV;
        public VH(@NonNull View view) {
            super(view);
            nomTV = view.findViewById(R.id.nom_tv);
        }
    }

    public SiteDistributionAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        final SiteDistribution siteDist = mSiteDist.get(i);

        vh.nomTV.setText("" + siteDist.nom );
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.site_distribution_item, viewGroup, false);
        return new SiteDistributionAdapter.VH(view);
    }

    @Override
    public int getItemCount() {
        return mSiteDist.size();
    }

    public void addAll(List<SiteDistribution> siteDistributions){
        mSiteDist.addAll(siteDistributions);
    }

    public void clear(){
        mSiteDist.clear();
    }
}

