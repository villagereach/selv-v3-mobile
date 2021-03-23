package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class Facility implements Table {
    private String id;
    private String code;
    private String name;
    private String zone;
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return id;
    }

    public void setUuid(String uuid) {
        this.id = uuid;
    }

    @Override
    public String getTableName() {
        return Database.Facility.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.Facility.COLUMN_NAME, name);
        cv.put(Database.Facility.COLUMN_CODE, code);
        cv.put(Database.Facility.COLUMN_TYPE, type);
        cv.put(Database.Facility.COLUMN_UUID, id);
        cv.put(Database.Facility.COLUMN_ZONE, zone);

        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.Facility.ALL_COLUMNS;
    }
}
