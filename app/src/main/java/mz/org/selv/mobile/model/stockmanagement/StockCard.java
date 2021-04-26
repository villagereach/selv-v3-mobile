package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class StockCard implements Table {

    String orderableId;
    String lotId;
    int stockOnHand;
    String programId;
    String facilityId;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderableId() {
        return orderableId;
    }

    public void setOrderableId(String orderableId) {
        this.orderableId = orderableId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public int getStockOnHand() {
        return stockOnHand;
    }

    public void setStockOnHand(int stockOnHand) {
        this.stockOnHand = stockOnHand;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    @Override
    public String getTableName() {
        return Database.StockCard.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.StockCard.COLUMN_NAME_FACILITY_ID, facilityId);
        cv.put(Database.StockCard.COLUMN_NAME_LOT_ID, lotId);
        cv.put(Database.StockCard.COLUMN_NAME_ORDERABLE_ID, orderableId);
        cv.put(Database.StockCard.COLUMN_NAME_STOCK_ON_HAND, stockOnHand);
        cv.put(Database.StockCard.COLUMN_NAME_PROGRAM_ID, programId);
        cv.put(Database.StockCard.COLUMN_NAME_ID, id);

        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.StockCard.ALL_COLUMNS;
    }
}
