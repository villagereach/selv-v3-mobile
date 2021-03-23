package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class FacilityTypeApprovedProduct implements Table {

    String orderableId;
    String facilityTypeId;

    public String getOrderableId() {
        return orderableId;
    }

    public void setOrderableId(String productId) {
        this.orderableId = productId;
    }

    public String getFacilityTypeId() {
        return facilityTypeId;
    }

    public void setFacilityTypeId(String facilityId) {
        this.facilityTypeId = facilityTypeId;
    }

    @Override
    public String getTableName() {
        return Database.FacilityTypeApprovedProduct.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.FacilityTypeApprovedProduct.COLUMN_NAME_FACILITY_TYPE_ID, facilityTypeId);
        cv.put(Database.FacilityTypeApprovedProduct.COLUMN_NAME_ORDERABLE_ID, orderableId);

        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.FacilityTypeApprovedProduct.ALL_COLUMNS;
    }
}
