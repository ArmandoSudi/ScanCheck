package com.daawtec.scancheck.entites;

public class DashboardStat {
    int menages;
    int menagesServis;
    int MILD;
    int MILDdistribues;
    int badVerification;
    double tauxMenagesServis;
    double tauxMILDdistribues;
    double tauxBadVerification;

    public DashboardStat() {
    }

    public int getMenages() {
        return menages;
    }

    public void setMenages(int menages) {
        this.menages = menages;
    }

    public int getMenagesServis() {
        return menagesServis;
    }

    public void setMenagesServis(int menagesServis) {
        this.menagesServis = menagesServis;
    }

    public int getMILD() {
        return MILD;
    }

    public void setMILD(int MILD) {
        this.MILD = MILD;
    }

    public int getMILDdistribues() {
        return MILDdistribues;
    }

    public void setMILDdistribues(int MILDdistribues) {
        this.MILDdistribues = MILDdistribues;
    }

    public int getBadVerification() {
        return badVerification;
    }

    public void setBadVerification(int badVerification) {
        this.badVerification = badVerification;
    }

    public double getTauxMenagesServis() {
        if (menages > 0 ) return menagesServis/menages*100; // pourcentage
        else return 0;
    }

    public double getTauxMILDdistribues() {
        if (MILD > 0) return MILDdistribues/MILD*100; // pourcentage
        else return 0;
    }

    public double getTauxBadVerification() {
        if (badVerification > 0 )return badVerification/menagesServis*100; // pourcentage
        return 0;
    }

    @Override
    public String toString() {
        return "DashboardStat{" +
                "menages=" + menages +
                ", menagesServis=" + menagesServis +
                ", MILD=" + MILD +
                ", MILDdistribues=" + MILDdistribues +
                ", badVerification=" + badVerification +
                ", tauxMenagesServis=" + tauxMenagesServis +
                ", tauxMILDdistribues=" + tauxMILDdistribues +
                ", tauxBadVerification=" + tauxBadVerification +
                '}';
    }
}
