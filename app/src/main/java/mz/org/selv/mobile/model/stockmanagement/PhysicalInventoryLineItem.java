package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;
import android.provider.BaseColumns;

import mz.org.selv.mobile.database.Table;

public class PhysicalInventoryLineItem implements Table {
    private String id;
    private String physicalInventoryId;
    private int quanity;
    private String orderableId;
    private String lotId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhysicalInventoryId() {
        return physicalInventoryId;
    }

    public void setPhysicalInventoryId(String physicalInventoryId) {
        this.physicalInventoryId = physicalInventoryId;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public String getOrderableId() {
        return orderableId;
    }

    public void setOrderableId(String orderableId) {
        this.orderableId = orderableId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public ContentValues getContentValues() {
        return null;
    }

    @Override
    public String[] getColumnNames() {
        return new String[0];
    }
}
