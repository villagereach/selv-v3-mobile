package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class PhysicalInventoryLineItem implements Table {
    private String id;
    private String physicalInventoryId;
    private int physicalStock;
    private String orderableId;
    private String lotId;
    private int previousStockOnHand;

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

    public int getPreviousStockOnHand() {
        return previousStockOnHand;
    }

    public void setPreviousStockOnHand(int previousStockOnHand) {
        this.previousStockOnHand = previousStockOnHand;
    }

    public int getPhysicalStock() {
        return physicalStock;
    }

    public void setPhysicalStock(int physicalStock) {
        this.physicalStock = physicalStock;
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
        return Database.PhysicalInventoryLineItem.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.PhysicalInventoryLineItem.COLUMN_LOT_ID, lotId);
        cv.put(Database.PhysicalInventoryLineItem.COLUMN_ORDERABLE_ID, orderableId);
        cv.put(Database.PhysicalInventoryLineItem.COLUMN_PHYSICAL_INVENTORY_ID, physicalInventoryId);
        cv.put(Database.PhysicalInventoryLineItem.COLUMN_PHYSICAL_STOCK, physicalStock);
        cv.put(Database.PhysicalInventoryLineItem.COLUMN_PREVIOUS_STOCK_ON_HAND, previousStockOnHand);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.PhysicalInventoryLineItem.ALL_COLUMNS;
    }
}
