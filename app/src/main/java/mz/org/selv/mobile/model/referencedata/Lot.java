package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class Lot implements Table {

    String lotCode;
    String expirationDate;
    String id;
    String orderableId;

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

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

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    @Override
    public String getTableName() {
        return Database.Lot.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.Lot.COLUMN_CODE, lotCode);
        cv.put(Database.Lot.COLUMN_EXPIRATION_DATE, expirationDate);
        cv.put(Database.Lot.COLUMN_UUID, id);
        cv.put(Database.Lot.COLUMN_TRADE_ITEM_ID, orderableId);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.Lot.ALL_COLUMNS;
    }
}
