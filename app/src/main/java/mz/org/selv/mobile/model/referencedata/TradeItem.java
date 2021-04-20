package mz.org.selv.mobile.model.referencedata;

import android.content.ContentValues;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;

public class TradeItem implements Table {
    String tradeItemId;
    String orderableId;

    public String getTradeItemId() {
        return tradeItemId;
    }

    public void setTradeItemId(String tradeItemId) {
        this.tradeItemId = tradeItemId;
    }

    public String getOrderableId() {
        return orderableId;
    }

    public void setOrderableId(String orderableId) {
        this.orderableId = orderableId;
    }

    @Override
    public String getTableName() {
        return Database.TradeItem.TABLE_NAME;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(Database.TradeItem.COLUMN_TRADE_ITEM_ID, tradeItemId);
        cv.put(Database.TradeItem.COLUMN_ORDERABLE_ID, orderableId);

        return cv;
    }

    @Override
    public String[] getColumnNames() {
        return Database.TradeItem.ALL_COLUMNS;
    }
}
