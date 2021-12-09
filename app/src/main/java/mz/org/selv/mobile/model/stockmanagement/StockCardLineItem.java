package mz.org.selv.mobile.model.stockmanagement;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class StockCardLineItem implements Table {

    private String orderableId;
    private String lotId;
    private int quantity;
    private String occurredDate;
    private String proccessedDate;
    private String extraData;
    private String stockcardId;
    private String destinationId;
    private String destinationFreeText;
    private String sourceId;
    private String sourceFreeText;
    private String originEventId;
    private String reasonFreeText;
    private String reasonId;
    private String id;
    private int stockOnHand;

    public String getProccessedDate() {
        return proccessedDate;
    }

    public void setProccessedDate(String proccessedDate) {
        this.proccessedDate = proccessedDate;
    }

    public int getStockOnHand() {
        return stockOnHand;
    }

    public void setStockOnHand(int stockOnHand) {
        this.stockOnHand = stockOnHand;
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

    public String getOriginEventId() {
        return originEventId;
    }

    public void setOriginEventId(String originEventId) {
        this.originEventId = originEventId;
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
        return Database.StockCardLineItem.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.StockCardLineItem.COLUMN_ORDERABLE_ID, orderableId);
        cv.put(Database.StockCardLineItem.COLUMN_LOT_ID, lotId);
        cv.put(Database.StockCardLineItem.COLUMN_QUANTITY, quantity);
        cv.put(Database.StockCardLineItem.COLUMN_DESTINATION_ID, destinationId);
        cv.put(Database.StockCardLineItem.COLUMN_DESTINATION_FREE_TEXT, destinationFreeText);
        cv.put(Database.StockCardLineItem.COLUMN_EXTRA_DATA, extraData);
        cv.put(Database.StockCardLineItem.COLUMN_SOURCE_ID, sourceId);
        cv.put(Database.StockCardLineItem.COLUMN_SOURCE_FREE_TEXT, sourceFreeText);
        cv.put(Database.StockCardLineItem.COLUMN_REASON_FREE_TEXT, reasonFreeText);
        cv.put(Database.StockCardLineItem.COLUMN_REASON_ID, reasonId);
        cv.put(Database.StockCardLineItem.COLUMN_ORIGIN_EVENT_ID, originEventId);
        cv.put(Database.StockCardLineItem.COLUMN_OCCURRED_DATE, occurredDate);
        cv.put(Database.StockCardLineItem.COLUMN_ID, id);
        cv.put(Database.StockCardLineItem.COLUMN_STOCK_ON_HAND, stockOnHand);
        cv.put(Database.StockCardLineItem.COLUMN_PROCCESSED_DATE, proccessedDate);
        cv.put(Database.StockCardLineItem.COLUMN_STOCK_CARD_ID, stockcardId);
        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.StockCardLineItem.ALL_COLUMNS;
    }

}
