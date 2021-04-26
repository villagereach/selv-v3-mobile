package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;
import android.provider.BaseColumns;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class PhysicalInventoryLineItemAdjustments implements Table {
    String reasonId;
    String id;
    String physicalInventoryLineItemId;
    int quantity;
    String stockCardLineItemId;
    String stockEventLineItemId;


    public String getStockCardLineItemId() {
        return stockCardLineItemId;
    }

    public void setStockCardLineItemId(String stockCardLineItemId) {
        this.stockCardLineItemId = stockCardLineItemId;
    }

    public String getStockEventLineItemId() {
        return stockEventLineItemId;
    }

    public void setStockEventLineItemId(String stockEventLineItemId) {
        this.stockEventLineItemId = stockEventLineItemId;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getPhysicalInventoryLineItemId() {
        return physicalInventoryLineItemId;
    }

    public void setPhysicalInventoryLineItemId(String physicalInventoryLineItemId) {
        this.physicalInventoryLineItemId = physicalInventoryLineItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getTableName() {
        return Database.PhysicalInventoryLineItemAdjustment.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.PhysicalInventoryLineItemAdjustment.COLUMN_ID, id);
        cv.put(Database.PhysicalInventoryLineItemAdjustment.COLUMN_PHYSICAL_INVENTORY_LINE_ITEM_ID, physicalInventoryLineItemId);
        cv.put(Database.PhysicalInventoryLineItemAdjustment.COLUMN_QUANTITY, quantity);
        cv.put(Database.PhysicalInventoryLineItemAdjustment.COLUMN_REASON_ID, reasonId);
        cv.put(Database.PhysicalInventoryLineItemAdjustment.COLUMN_STOCK_CARD_LINE_ITEM_ID, stockCardLineItemId);
        cv.put(Database.PhysicalInventoryLineItemAdjustment.COLUMN_STOCK_EVENT_LINE_ITEM_ID, stockEventLineItemId);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.PhysicalInventoryLineItemAdjustment.ALL_COLUMNS;
    }
}
