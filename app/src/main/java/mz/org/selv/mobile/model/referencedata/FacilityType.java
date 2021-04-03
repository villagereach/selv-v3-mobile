package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class FacilityType implements Table {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String name;
    String id;
    String code;



    @Override
    public String getTableName() {
        return Database.FacilityType.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.FacilityType.COLUMN_CODE, code);
        cv.put(Database.FacilityType.COLUMN_NAME, name);
        cv.put(Database.FacilityType.COLUMN_UUID, id);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.FacilityType.ALL_COLUMNS;
    }
}
