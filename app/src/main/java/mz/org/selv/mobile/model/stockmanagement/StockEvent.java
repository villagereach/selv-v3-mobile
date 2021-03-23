package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class StockEvent implements Table {
    String facilityId;
    String programId;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    @Override
    public String getTableName() {
        return Database.StockEvent.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.StockEvent.COLUMN_NAME_FACILITY_ID, facilityId);
        cv.put(Database.StockEvent.COLUMN_NAME_PROGRAM_ID, programId);
        cv.put(Database.StockEvent.COLUMN_NAME_UUID, id);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.StockEvent.ALL_COLUMNS;
    }
}
