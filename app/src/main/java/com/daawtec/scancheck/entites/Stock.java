package com.daawtec.scancheck.entites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "STOCK", foreignKeys = {
        @ForeignKey(entity = Stock.class, childColumns = "CODE_STOCK_PARENT", parentColumns = "CODE_STOCK")
})
public class Stock {
    @PrimaryKey
    @ColumnInfo(name="CODE_STOCK")
    public String codeStock;

    @ColumnInfo(name="CODE_AGENT")
    public String codeAgent;

    @ColumnInfo(name="CODE_STOCK_PARENT")
    public String codeStockParent;

    @ColumnInfo(name="TYPE_STOCK")
    public String typeStock;

    @ColumnInfo(name="QUANTITY")
    int quantity;

    @ColumnInfo(name="DATE")
    public String date;

}
