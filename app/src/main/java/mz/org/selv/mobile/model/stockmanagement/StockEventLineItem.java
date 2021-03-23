package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Table;

public class StockEventLineItem  implements Table {

    private String orderableId ;
    private String lotId ;
    private int quantity;
    private String occurredDate;
    private String extraData;
    private String stockcardId;


    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public ContentValues getContentValues() {
        return null;
    }

    @Override
    public String[] getColumnNames() {
        return new String[0];
    }
}
