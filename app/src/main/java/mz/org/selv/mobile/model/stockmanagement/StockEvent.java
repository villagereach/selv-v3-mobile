package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class StockEvent implements Table {
    String facilityId;
    String programId;
    String orderableId;
    String lotId;
    String processedDate;
    String occurredDate;
    String type; // 1-receive, 2-issue, 3-adjustment, 4 inventory
    String id;
    int status;

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

    public String getOccurredDate() {
        return occurredDate;
    }

    public void setOccurredDate(String occurredDate) {
        this.occurredDate = occurredDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(String processedDate) {
        this.processedDate = processedDate;
    }

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
        cv.put(Database.StockEvent.COLUMN_PROCESSED_DATE, processedDate);
        cv.put(Database.StockEvent.COLUMN_OCCURRED_DATE, occurredDate);
        cv.put(Database.StockEvent.COLUMN_NAME_TYPE, type);
        cv.put(Database.StockEvent.COLUMN_NAME_STATUS, status);
        cv.put(Database.StockEvent.COLUMN_LOT_ID, lotId);
        cv.put(Database.StockEvent.COLUMN_ORDERABLE_ID, orderableId);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.StockEvent.ALL_COLUMNS;
    }
}
