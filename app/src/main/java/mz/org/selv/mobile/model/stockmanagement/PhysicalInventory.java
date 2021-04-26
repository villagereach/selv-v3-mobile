package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class PhysicalInventory implements Table {
    String occurredDate;
    String signature;
    String programId;
    String facilityId;
    String status;
    String id;
    String lastSyncDate;

    public String getLastSyncDate() {
        return lastSyncDate;
    }

    public void setLastSyncDate(String lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOccurredDate() {
        return occurredDate;
    }

    public void setOccurredDate(String occurredDate) {
        this.occurredDate = occurredDate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getTableName() {
        return Database.PhysicalInventory.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.PhysicalInventory.COLUMN_UUID, id);
        cv.put(Database.PhysicalInventory.COLUMN_PROGRAM_ID, programId);
        cv.put(Database.PhysicalInventory.COLUMN_FACILITY_ID, facilityId);
        cv.put(Database.PhysicalInventory.COLUMN_OCCURRED_DATE, occurredDate);
        cv.put(Database.PhysicalInventory.COLUMN_SIGNATURE, signature);
        cv.put(Database.PhysicalInventory.COLUMN_STATUS, status);
        //cv.put(Database.PhysicalInventory.COLUMN_LAST_SYNC, lastSyncDate);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.PhysicalInventory.ALL_COLUMNS;
    }
}
