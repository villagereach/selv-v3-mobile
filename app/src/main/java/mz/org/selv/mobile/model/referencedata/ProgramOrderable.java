package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class ProgramOrderable implements Table {

    String programId;
    String orderableId;

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getOrderableId() {
        return orderableId;
    }

    public void setOrderableId(String orderableId) {
        this.orderableId = orderableId;
    }

    @Override
    public String getTableName() {
        return Database.ProgramOrderable.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.ProgramOrderable.COLUMN_NAME_PROGRAM_ID, getProgramId());
        cv.put(Database.ProgramOrderable.COLUMN_NAME_ORDERABLE_ID, getOrderableId());
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.ProgramOrderable.ALL_COLUMNS;
    }
}
