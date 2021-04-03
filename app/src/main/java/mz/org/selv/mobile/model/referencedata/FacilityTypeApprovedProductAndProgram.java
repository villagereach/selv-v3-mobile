package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class FacilityTypeApprovedProductAndProgram implements Table {

    String orderableId;
    String facilityTypeId;
    String programId;
    String id;

    public String getProgramId() {
        return programId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getOrderableId() {
        return orderableId;
    }

    public void setOrderableId(String productId) {
        this.orderableId = productId;
    }

    public String getFacilityTypeId() {
        return facilityTypeId;
    }

    public void setFacilityTypeId(String facilityTypeId) {
        this.facilityTypeId = facilityTypeId;
    }

    @Override
    public String getTableName() {
        return Database.FacilityTypeApprovedProductAndProgram.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_FACILITY_TYPE_ID, facilityTypeId);
        cv.put(Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_ORDERABLE_ID, orderableId);
        cv.put(Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_PROGRAM_ID, programId);
        cv.put(Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_UUID, id);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.FacilityTypeApprovedProductAndProgram.ALL_COLUMNS;
    }
}
