package com.daawtec.scancheck.entites;

public class RapportDenombrementIT {
    public String date;
    public int macaronRecu;
    public int macaronUtilise;
    public int menage;
    public int orphelinat, couvent, internat, fosa, hotel;
    public int militaire, deplaces, refugie, prison;

    @Override
    public String toString() {
        return "RapportDenombrementIT{" +
                "date='" + date + '\'' +
                ", macaronRecu=" + macaronRecu +
                ", macaronUtilise=" + macaronUtilise +
                ", menage=" + menage +
                ", orphelinat=" + orphelinat +
                ", couvent=" + couvent +
                ", internat=" + internat +
                ", fosa=" + fosa +
                ", hotel=" + hotel +
                ", militaire=" + militaire +
                ", deplaces=" + deplaces +
                ", refugie=" + refugie +
                ", prison=" + prison +
                '}';
    }
}
