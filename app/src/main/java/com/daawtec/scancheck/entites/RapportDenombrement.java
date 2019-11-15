package com.daawtec.scancheck.entites;

public class RapportDenombrement {
    public String date;
    public int macaronRecu;
    public int macaronUtilise;
    public int menage;
    public int menageOneTwo;
    public int menageThreeFour;
    public int menageFiveSix;
    public int menageSevenEight;
    public int soldeMacaron;

    @Override
    public String toString() {
        return "RapportDenombrement{" +
                "macaronRecu=" + macaronRecu +
                ", macaronUtilise=" + macaronUtilise +
                ", menage=" + menage +
                ", menageOneTwo=" + menageOneTwo +
                ", menageThreeFour=" + menageThreeFour +
                ", menageFiveSix=" + menageFiveSix +
                ", menageSevenEight=" + menageSevenEight +
                ", soldeMacaron=" + soldeMacaron +
                '}';
    }
}
