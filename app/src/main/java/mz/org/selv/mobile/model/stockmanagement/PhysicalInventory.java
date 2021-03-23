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
        return null;
    }

    @Override
    public String[] getColumnNames() {
        return Database.PhysicalInventory.ALL_COLUMNS;
    }
}
