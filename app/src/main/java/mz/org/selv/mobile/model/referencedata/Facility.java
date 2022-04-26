package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class Facility implements Table {
    private String id;
    private String code;
    private String name;
    private String zone;
    private String facilityTypeCode;
    private String facilityTypeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacilityTypeId() {
        return facilityTypeId;
    }

    public void setFacilityTypeId(String facilityTypeId) {
        this.facilityTypeId = facilityTypeId;
    }

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


    public String getFacilityTypeCode() {
        return facilityTypeCode;
    }

    public void setFacilityTypeCode(String type) {
        this.facilityTypeCode = type;
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
        cv.put(Database.Facility.COLUMN_FACILITY_TYPE_CODE, facilityTypeCode);
        cv.put(Database.Facility.COLUMN_UUID, id);
        cv.put(Database.Facility.COLUMN_ZONE, zone);
        cv.put(Database.Facility.COLUMN_FACILITY_TYPE_ID, facilityTypeId);


        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.Facility.ALL_COLUMNS;
    }
}
