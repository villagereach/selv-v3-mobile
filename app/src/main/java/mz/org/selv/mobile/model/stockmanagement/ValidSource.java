package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class ValidSource implements Table {
    String isFreeTextAllowed;
    String programId;
    String nodeId;
    String referenceId;
    String name;
    String facilityTypeId;
    String refDataFacility;
    String id;

    public String getIsFreeTextAllowed() {
        return isFreeTextAllowed;
    }

    public void setIsFreeTextAllowed(String isFreeTextAllowed) {
        this.isFreeTextAllowed = isFreeTextAllowed;
    }

    public String getFacilityTypeId() {
        return facilityTypeId;
    }

    public void setFacilityTypeId(String facilityTypeId) {
        this.facilityTypeId = facilityTypeId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefDataFacility() {
        return refDataFacility;
    }

    public void setRefDataFacility(String refDataFacility) {
        this.refDataFacility = refDataFacility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return Database.ValidSources.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.ValidSources.COLUMN_NAME_FREE_TEXT_ALLOWED, isFreeTextAllowed);
        cv.put(Database.ValidSources.COLUMN_NAME_ID, id);
        cv.put(Database.ValidSources.COLUMN_NAME_NAME, name);
        cv.put(Database.ValidSources.COLUMN_NAME_NODE_ID, nodeId);
        cv.put(Database.ValidSources.COLUMN_NAME_NODE_REFERENCE_ID, referenceId);
        cv.put(Database.ValidSources.COLUMN_NAME_PROGRAM_ID, programId);
        cv.put(Database.ValidSources.COLUMN_NAME_REFERENCE_DATA_FACILITY, refDataFacility);
        cv.put(Database.ValidSources.COLUMN_NAME_FACILITY_TYPE_ID, facilityTypeId);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.ValidSources.ALL_COLUMNS;
    }
}
