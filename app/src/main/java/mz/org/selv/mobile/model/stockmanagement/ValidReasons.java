package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class ValidReasons implements Table {
    String programId;
    String reasonId;
    String facilitytypeId;
    String id;

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getFacilitytypeId() {
        return facilitytypeId;
    }

    public void setFacilitytypeId(String facilitytypeId) {
        this.facilitytypeId = facilitytypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return Database.ValidReasons.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.ValidReasons.COLUMN_NAME_FACILITY_TYPE_ID, facilitytypeId);
        cv.put(Database.ValidReasons.COLUMN_NAME_PROGRAM_ID, programId);
        cv.put(Database.ValidReasons.COLUMN_NAME_REASON_ID, reasonId);
        cv.put(Database.ValidReasons.COLUMN_NAME_UUID, id);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.ValidReasons.ALL_COLUMNS;
    }
}
