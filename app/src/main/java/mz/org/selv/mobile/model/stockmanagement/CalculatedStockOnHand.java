package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class CalculatedStockOnHand implements Table {
    String stockCardId;
    int stockOnHand;
    String occuredDate;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStockCardId() {
        return stockCardId;
    }

    public void setStockCardId(String stockCardId) {
        this.stockCardId = stockCardId;
    }

    public int getStockOnHand() {
        return stockOnHand;
    }

    public void setStockOnHand(int stockOnHand) {
        this.stockOnHand = stockOnHand;
    }

    public String getOccuredDate() {
        return occuredDate;
    }

    public void setOccuredDate(String occuredDate) {
        this.occuredDate = occuredDate;
    }

    @Override
    public String getTableName() {
        return Database.CalculatedStockOnHand.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.CalculatedStockOnHand.COLUMN_OCCURRED_DATE, occuredDate);
        cv.put(Database.CalculatedStockOnHand.COLUMN_STOCK_CARD_ID, stockCardId);
        cv.put(Database.CalculatedStockOnHand.COLUMN_STOCK_ON_HAND, stockOnHand);
        cv.put(Database.CalculatedStockOnHand.COLUMN_ID, id);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.CalculatedStockOnHand.ALL_COLUMNS;
    }
}
