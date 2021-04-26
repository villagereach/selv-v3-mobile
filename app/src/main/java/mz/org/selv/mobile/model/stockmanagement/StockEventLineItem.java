package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class StockEventLineItem implements Table {

    private String orderableId;
    private String lotId;
    private int quantity;
    private String occurredDate;
    private String extraData;
    private String stockcardId;
    private String destinationId;
    private String destinationFreeText;
    private String sourceId;
    private String sourceFreeText;
    private String stockEventId;
    private String reasonFreeText;
    private String reasonId;
    private String id;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOccurredDate() {
        return occurredDate;
    }

    public void setOccurredDate(String occurredDate) {
        this.occurredDate = occurredDate;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getStockcardId() {
        return stockcardId;
    }

    public void setStockcardId(String stockcardId) {
        this.stockcardId = stockcardId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationFreeText() {
        return destinationFreeText;
    }

    public void setDestinationFreeText(String destinationFreeText) {
        this.destinationFreeText = destinationFreeText;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceFreeText() {
        return sourceFreeText;
    }

    public void setSourceFreeText(String sourceFreeText) {
        this.sourceFreeText = sourceFreeText;
    }

    public String getStockEventId() {
        return stockEventId;
    }

    public void setStockEventId(String stockEventId) {
        this.stockEventId = stockEventId;
    }

    public String getReasonFreeText() {
        return reasonFreeText;
    }

    public void setReasonFreeText(String reasonFreeText) {
        this.reasonFreeText = reasonFreeText;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTableName() {
        return Database.StockEventLineItem.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.StockEventLineItem.COLUMN_ORDERABLE_ID, orderableId);
        cv.put(Database.StockEventLineItem.COLUMN_LOT_ID, lotId);
        cv.put(Database.StockEventLineItem.COLUMN_QUANTITY, quantity);
        cv.put(Database.StockEventLineItem.COLUMN_DESTINATION_ID, destinationId);
        cv.put(Database.StockEventLineItem.COLUMN_DESTINATION_FREE_TEXT, destinationFreeText);
        cv.put(Database.StockEventLineItem.COLUMN_EXTRA_DATA, extraData);
        cv.put(Database.StockEventLineItem.COLUMN_SOURCE_ID, sourceId);
        cv.put(Database.StockEventLineItem.COLUMN_SOURCE_FREE_TEXT, sourceFreeText);
        cv.put(Database.StockEventLineItem.COLUMN_REASON_FREE_TEXT, reasonFreeText);
        cv.put(Database.StockEventLineItem.COLUMN_REASON_ID, reasonId);
        cv.put(Database.StockEventLineItem.COLUMN_STOCK_EVENT_ID, stockEventId);
        cv.put(Database.StockEventLineItem.COLUMN_OCCURRED_DATE, occurredDate);
        cv.put(Database.StockEventLineItem.COLUMN_ID, id);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.StockEventLineItem.ALL_COLUMNS;
    }
}
