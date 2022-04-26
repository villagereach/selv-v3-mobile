package mz.org.selv.mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import java.util.Collection;

public class Database {
    public final static String DATABASE_NAME = "selv_mobile_v3";
    public final static int DATABASE_VERSION = 8;

    public static class SyncStatus implements BaseColumns{
        public static String COLUMN_ENTITY = "entity";
        public static String COLUMN_DATE = "date";
        public static String COLUMN_USER_ID = "userId";
        public static String COLUMN_SERVER_ID = "serverId";
        public static String TABLE_NAME = "syncStatus";
        public static String[] ALL_COLUMNS = {COLUMN_ENTITY, COLUMN_DATE, COLUMN_SERVER_ID, COLUMN_USER_ID};
    }

    public static class User implements BaseColumns {
        public static String COLUMN_USERNAME = "username";
        public static String COLUMN_PASSWORD = "password";
        public static String COLUMN_HOME_FACLITY_ID = "homeFacilityId";
        public static String COLUMN_HOME_FACILITY_NAME = "homeFacilityName";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_SURNAME = "surname";
        public static String COLUMN_SERVER_ID = "serverId";
        public static String COLUMN_TOKEN = "token";
        public static String COLUMN_USER_ID = "userId";
        public static String COLUMN_IS_LOGGED_IN = "isLoggedIn";
        public static String COLUMN_LAST_TIME_LOGGED_IN_SERVER = "lastTimeLoggedInServer";
        public static String COLUMN_TOKEN_EXPIRATION = "tokenExpiration";
        public static String TABLE_NAME = "user";
        public static String[] ALL_COLUMNS = {COLUMN_USERNAME, COLUMN_TOKEN_EXPIRATION,COLUMN_SERVER_ID, COLUMN_USER_ID,COLUMN_LAST_TIME_LOGGED_IN_SERVER, COLUMN_PASSWORD, COLUMN_HOME_FACLITY_ID, COLUMN_HOME_FACILITY_NAME, COLUMN_SURNAME,COLUMN_IS_LOGGED_IN , COLUMN_TOKEN, COLUMN_NAME};

    }

    public static class Server implements BaseColumns{
        public static String COLUMN_URL = "url";
        public static String COLUMN_LAST_TIME_USED = "lastTimeUsed";
        public static String COLUMN_SERVER_ID = "serverId";
        public static String TABLE_NAME = "server";
        public static String[] ALL_COLUMNS = {COLUMN_URL, COLUMN_LAST_TIME_USED, COLUMN_SERVER_ID};
    }

    public static class Facility implements BaseColumns {
        public static String COLUMN_CODE = "code";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_UUID = "id";
        public static String COLUMN_ZONE = "zoneId";
        public static String COLUMN_FACILITY_TYPE_CODE = "type";
        public static String COLUMN_FACILITY_TYPE_ID = "facilityTypeId";
        public static String TABLE_NAME = "facility";
        public static String[] ALL_COLUMNS = {COLUMN_CODE, COLUMN_NAME, COLUMN_UUID, COLUMN_ZONE, COLUMN_FACILITY_TYPE_CODE, COLUMN_FACILITY_TYPE_ID};

    }

    public static class FacilityType implements BaseColumns {
        public static String COLUMN_CODE = "code";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_UUID = "id";
        public static String TABLE_NAME = "facility_type";
        public static String[] ALL_COLUMNS = {COLUMN_CODE, COLUMN_NAME, COLUMN_UUID};
    }

    public static class Orderable implements BaseColumns {
        public static String COLUMN_CODE = "productCode";
        public static String COLUMN_NAME = "fullProductName";
        public static String COLUMN_UUID = "id";
        public static String TABLE_NAME = "orderable";
        public static String[] ALL_COLUMNS = {COLUMN_CODE, COLUMN_NAME, COLUMN_UUID};
    }

    public static class Lot implements BaseColumns {
        public static String COLUMN_CODE = "lotCode";
        public static String COLUMN_EXPIRATION_DATE = "expirationDate";
        public static String COLUMN_UUID = "id";
        public static String COLUMN_TRADE_ITEM_ID = "tradeItemId";
        public static String TABLE_NAME = "lot";
        public static String[] ALL_COLUMNS = {COLUMN_CODE, COLUMN_EXPIRATION_DATE, COLUMN_UUID, COLUMN_TRADE_ITEM_ID};
    }

    public static class ProgramOrderable implements BaseColumns {
        public static String COLUMN_NAME_PROGRAM_ID = "programid";
        public static String COLUMN_NAME_ORDERABLE_ID = "orderableid";
        public static String TABLE_NAME = "program_orderable";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_ORDERABLE_ID, COLUMN_NAME_PROGRAM_ID};
    }

    public static class FacilityTypeApprovedProductAndProgram implements BaseColumns {
        public static String COLUMN_NAME_FACILITY_TYPE_ID = "facilityTypeId";
        public static String COLUMN_NAME_ORDERABLE_ID = "orderableid";
        public static String COLUMN_NAME_PROGRAM_ID = "programId";
        public static String COLUMN_NAME_UUID = "id";
        public static String TABLE_NAME = "facility_type_approved_product_program";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_ORDERABLE_ID, COLUMN_NAME_PROGRAM_ID, COLUMN_NAME_UUID, COLUMN_NAME_FACILITY_TYPE_ID};
    }

    public static class TradeItem implements BaseColumns {
        public static String COLUMN_ORDERABLE_ID = "orderbleId";
        public static String COLUMN_TRADE_ITEM_ID = "id";
        public static String TABLE_NAME = "tradeItem";
        public static String[] ALL_COLUMNS = {COLUMN_TRADE_ITEM_ID, COLUMN_ORDERABLE_ID};
    }

    public static class ProcessingPeriod implements BaseColumns {

    }

    public static class ProcessingSchedulle implements BaseColumns {

    }

    public static class Program implements BaseColumns {
        public static String COLUMN_NAME = "name";
        public static String COLUMN_CODE = "code";
        public static String COLUMN_UUID = "id";
        public static String COLUMN_STATUS = "status";
        public static String COLUMN_LAST_SYNC = "last_sync";
        public static String COLUMN_ACTIVE = "active";
        public static String COLUMN_DESCRIPTION = "description";
        public static String TABLE_NAME = "program";
        public static String[] ALL_COLUMNS = {COLUMN_NAME, COLUMN_CODE, COLUMN_DESCRIPTION, COLUMN_UUID, COLUMN_STATUS, COLUMN_LAST_SYNC, COLUMN_ACTIVE};
    }

    public static class Requisition implements BaseColumns {

    }

    public static class PhysicalInventory implements BaseColumns {
        public static String COLUMN_UUID = "id";
        public static String COLUMN_STATUS = "status";
        public static String COLUMN_LAST_SYNC = "last_sync";
        public static String COLUMN_ACTIVE = "active";
        public static String COLUMN_FACILITY_ID = "facilityid";
        public static String COLUMN_DESCRIPTION = "description";
        public static String COLUMN_OCCURRED_DATE = "occurredDate";
        public static String COLUMN_PROGRAM_ID = "programid";
        public static String COLUMN_SIGNATURE = "signature";
        public static String TABLE_NAME = "physicalInventory";
        public static String[] ALL_COLUMNS = {COLUMN_SIGNATURE, COLUMN_PROGRAM_ID, COLUMN_OCCURRED_DATE, COLUMN_FACILITY_ID, COLUMN_UUID, COLUMN_STATUS};
    }

    public static class PhysicalInventoryLineItem implements BaseColumns {
        public static String COLUMN_ORDERABLE_ID = "orderableId";
        public static String COLUMN_LOT_ID = "lotid";
        public static String COLUMN_PHYSICAL_STOCK = "physicalStock";
        public static String COLUMN_PREVIOUS_STOCK_ON_HAND = "previousStockOnHand";
        public static String COLUMN_PHYSICAL_INVENTORY_ID = "physicalInventoryId";
        public static String COLUMN_PHYSICAL_FACILITY_ID = "facilityId";
        public static String TABLE_NAME = "physicalInventoryLineItem";
        public static String[] ALL_COLUMNS = {COLUMN_ORDERABLE_ID, COLUMN_LOT_ID, COLUMN_PHYSICAL_FACILITY_ID, COLUMN_PHYSICAL_STOCK, COLUMN_PREVIOUS_STOCK_ON_HAND, COLUMN_PHYSICAL_INVENTORY_ID};
    }

    public static class PhysicalInventoryLineItemAdjustment implements BaseColumns {
        public static String COLUMN_QUANTITY = "quantity";
        public static String COLUMN_REASON_ID = "reasonId";
        public static String COLUMN_STOCK_CARD_LINE_ITEM_ID = "stockCardLineItem_id";
        public static String COLUMN_STOCK_EVENT_LINE_ITEM_ID = "stockEventLineItemId";
        public static String COLUMN_PHYSICAL_INVENTORY_LINE_ITEM_ID = "physicalInventoryLineItemId";
        public static String COLUMN_ID = "id";
        public static String TABLE_NAME = "physicalInventoryLineItemAdjustment";
        public static String[] ALL_COLUMNS = {COLUMN_QUANTITY, COLUMN_REASON_ID, COLUMN_STOCK_CARD_LINE_ITEM_ID, COLUMN_STOCK_EVENT_LINE_ITEM_ID, COLUMN_PHYSICAL_INVENTORY_LINE_ITEM_ID, COLUMN_ID};
    }

    public static class ValidSources implements BaseColumns {
        public static String COLUMN_NAME_FREE_TEXT_ALLOWED = "isFreeTextAllowed";
        public static String COLUMN_NAME_PROGRAM_ID = "programId";
        public static String COLUMN_NAME_NODE_ID = "nodeId";
        public static String COLUMN_NAME_NODE_REFERENCE_ID = "referenceId";
        public static String COLUMN_NAME_NAME = "name";
        public static String COLUMN_NAME_REFERENCE_DATA_FACILITY = "refDataFacility";
        public static String COLUMN_NAME_ID = "id";
        public static String COLUMN_NAME_FACILITY_TYPE_ID = "facilityTypeId";
        public static String COLUMN_NAME_FACILITY_ID = "facilityId";
        public static String TABLE_NAME = "ValidSources";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_FREE_TEXT_ALLOWED, COLUMN_NAME_FACILITY_ID, COLUMN_NAME_FACILITY_TYPE_ID,COLUMN_NAME_PROGRAM_ID, COLUMN_NAME_NODE_ID, COLUMN_NAME_NODE_REFERENCE_ID, COLUMN_NAME_NAME, COLUMN_NAME_REFERENCE_DATA_FACILITY,
                COLUMN_NAME_ID};
    }

    public static class ValidDestinations implements BaseColumns {
        public static String COLUMN_NAME_FREE_TEXT_ALLOWED = "isFreeTextAllowed";
        public static String COLUMN_NAME_PROGRAM_ID = "programId";
        public static String COLUMN_NAME_NODE_ID = "nodeId";
        public static String COLUMN_NAME_NODE_REFERENCE_ID = "referenceId";
        public static String COLUMN_NAME_NAME = "name";
        public static String COLUMN_NAME_REFERENCE_DATA_FACILITY = "refDataFacility";
        public static String COLUMN_NAME_ID = "id";
        public static String COLUMN_NAME_FACILITY_TYPE_ID = "facilityTypeId";
        public static String COLUMN_NAME_FACILITY_ID = "facilityId";
        public static String TABLE_NAME = "ValidDestinations";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_FREE_TEXT_ALLOWED,  COLUMN_NAME_FACILITY_ID, COLUMN_NAME_FACILITY_TYPE_ID, COLUMN_NAME_PROGRAM_ID, COLUMN_NAME_NODE_ID, COLUMN_NAME_NODE_REFERENCE_ID, COLUMN_NAME_NAME, COLUMN_NAME_REFERENCE_DATA_FACILITY,
                COLUMN_NAME_ID};
    }


    public static class ReasonType implements BaseColumns {

    }


    public static class ReasonCategory implements BaseColumns {

    }


    public static class Reason implements BaseColumns {
        public static String COLUMN_NAME_CATEGORY = "reasonCategory";
        public static String COLUMN_NAME_TYPE = "reasonType";
        public static String COLUMN_NAME_NAME = "name";
        public static String COLUMN_NAME_UUID = "id";
        public static String TABLE_NAME = "Reason";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_NAME, COLUMN_NAME_TYPE, COLUMN_NAME_CATEGORY, COLUMN_NAME_UUID};
    }

    public static class ValidReasons implements BaseColumns {
        public static String COLUMN_NAME_FACILITY_TYPE_ID = "facilitytype";
        public static String COLUMN_NAME_REASON_ID = "reasonid";
        public static String COLUMN_NAME_PROGRAM_ID = "programid";
        public static String COLUMN_NAME_UUID = "id";
        public static String TABLE_NAME = "validreasons";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_PROGRAM_ID, COLUMN_NAME_REASON_ID, COLUMN_NAME_FACILITY_TYPE_ID, COLUMN_NAME_UUID};
    }

    public static class StockCard implements BaseColumns {
        public static String COLUMN_NAME_ORDERABLE_ID = "orderableId";
        public static String COLUMN_NAME_LOT_ID = "lotid";
        public static String COLUMN_NAME_STOCK_ON_HAND = "stockonhand";
        public static String COLUMN_NAME_PROGRAM_ID = "programid";
        public static String COLUMN_NAME_ID = "id";
        public static String COLUMN_NAME_IS_ACTIVE = "isactive";
        public static String COLUMN_NAME_FACILITY_ID = "facilityid";
        public static String TABLE_NAME = "stock_card";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_ORDERABLE_ID, COLUMN_NAME_IS_ACTIVE,COLUMN_NAME_ID, COLUMN_NAME_LOT_ID, COLUMN_NAME_FACILITY_ID, COLUMN_NAME_STOCK_ON_HAND, COLUMN_NAME_PROGRAM_ID};
    }

    public static class StockEvent implements BaseColumns {
        public static String COLUMN_NAME_FACILITY_ID = "facilityId";
        public static String COLUMN_NAME_UUID = "id";
        public static String COLUMN_NAME_PROGRAM_ID = "programid";
        public static String TABLE_NAME = "stock_event";
        public static String COLUMN_NAME_STATUS = "status";
        public static String COLUMN_NAME_TYPE = "type";
        public static String COLUMN_PROCESSED_DATE = "processedDate";
        public static String COLUMN_OCCURRED_DATE = "occurredDate";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_UUID, COLUMN_PROCESSED_DATE, COLUMN_OCCURRED_DATE,COLUMN_NAME_STATUS,COLUMN_NAME_TYPE,COLUMN_NAME_FACILITY_ID, COLUMN_NAME_PROGRAM_ID};
    }

    public static class StockEventLineItem implements BaseColumns {
        public static String COLUMN_ORDERABLE_ID = "orderableId";
        public static String COLUMN_LOT_ID = "lotid";
        public static String COLUMN_QUANTITY = "quantity";
        public static String COLUMN_FACILITY_ID = "facilityId";
        public static String COLUMN_PROGRAM_ID = "programid";
        public static String COLUMN_DESTINATION_ID = "destinationId";
        public static String COLUMN_DESTINATION_FREE_TEXT = "destinationFreeText";
        public static String COLUMN_EXTRA_DATA = "extraData";
        public static String COLUMN_SOURCE_ID = "sourceId";
        public static String COLUMN_SOURCE_FREE_TEXT = "sourceFreeText";
        public static String COLUMN_REASON_FREE_TEXT = "reasonFreeText";
        public static String COLUMN_REASON_ID = "reasonId";
        public static String COLUMN_STOCK_EVENT_ID = "stockEventId";
        public static String COLUMN_OCCURRED_DATE = "occurredDate";
        public static String COLUMN_PROCCESSED_DATE = "proccessedDate";
        public static String COLUMN_ID = "id";
        public static String TABLE_NAME = "stockEventLineItem";
        public static String[] ALL_COLUMNS = {COLUMN_ORDERABLE_ID, COLUMN_PROCCESSED_DATE,COLUMN_FACILITY_ID, COLUMN_PROGRAM_ID,COLUMN_LOT_ID, COLUMN_QUANTITY, COLUMN_DESTINATION_ID, COLUMN_DESTINATION_FREE_TEXT, COLUMN_EXTRA_DATA, COLUMN_SOURCE_ID,
                COLUMN_SOURCE_FREE_TEXT, COLUMN_REASON_FREE_TEXT, COLUMN_REASON_ID, COLUMN_OCCURRED_DATE, COLUMN_ID, COLUMN_STOCK_EVENT_ID};
    }

    public static class StockCardLineItem implements BaseColumns {
        public static String COLUMN_ORDERABLE_ID = "orderableId";
        public static String COLUMN_LOT_ID = "lotid";
        public static String COLUMN_QUANTITY = "quantity";
        public static String COLUMN_DESTINATION_ID = "destinationId";
        public static String COLUMN_DESTINATION_FREE_TEXT = "destinationFreeText";
        public static String COLUMN_EXTRA_DATA = "extraData";
        public static String COLUMN_SOURCE_ID = "sourceId";
        public static String COLUMN_SOURCE_FREE_TEXT = "sourceFreeText";
        public static String COLUMN_REASON_FREE_TEXT = "reasonFreeText";
        public static String COLUMN_REASON_ID = "reasonId";
        public static String COLUMN_ORIGIN_EVENT_ID = "originEventId";
        public static String COLUMN_STOCK_ON_HAND = "stockOnHand";
        public static String COLUMN_OCCURRED_DATE = "occurredDate";
        public static String COLUMN_ID = "id";
        public static String COLUMN_STOCK_CARD_ID = "stockCardId";
        public static String COLUMN_PROCCESSED_DATE = "proccessedDate";
        public static String TABLE_NAME = "stockCardLineItem";
        public static String[] ALL_COLUMNS = {COLUMN_ORDERABLE_ID, COLUMN_LOT_ID, COLUMN_QUANTITY, COLUMN_DESTINATION_ID, COLUMN_DESTINATION_FREE_TEXT, COLUMN_EXTRA_DATA, COLUMN_SOURCE_ID,
                COLUMN_SOURCE_FREE_TEXT, COLUMN_REASON_FREE_TEXT, COLUMN_PROCCESSED_DATE,COLUMN_REASON_ID, COLUMN_STOCK_CARD_ID, COLUMN_OCCURRED_DATE, COLUMN_ID, COLUMN_STOCK_ON_HAND,COLUMN_ORIGIN_EVENT_ID};
    }

    public static class CalculatedStockOnHand implements BaseColumns {
        public static String COLUMN_ID = "id";
        public static String COLUMN_STOCK_CARD_ID = "stockCardId";
        public static String COLUMN_NAME_FACILITY_ID = "facilityId";
        public static String COLUMN_NAME_PROGRAM_ID = "programId";
        public static String COLUMN_STOCK_ON_HAND = "stockOnHand";
        public static String COLUMN_OCCURRED_DATE = "occuredDate";
        public static String TABLE_NAME = "calculatedStockOnHand";
        public static String COLUMN_ORDERABLE_ID = "orderableId";
        public static String COLUMN_LOT_ID = "lotid";
        public static String[] ALL_COLUMNS = {COLUMN_ID, COLUMN_ORDERABLE_ID, COLUMN_LOT_ID, COLUMN_NAME_PROGRAM_ID,COLUMN_NAME_FACILITY_ID,  COLUMN_STOCK_CARD_ID, COLUMN_OCCURRED_DATE, COLUMN_STOCK_ON_HAND};
    }

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public Database(Context context) {
        try {
            databaseHelper = new DatabaseHelper(context);
        } catch (Exception ex) {
            // Toast.makeText(context, context.getString(R.string.message_error_opening_db), Toast.LENGTH_LONG).show();
        }
    }

    public void open() {
        try {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        } catch (Exception ex) {
            Log.d("Error:", ex.getMessage());
        }
    }

    public void beginTransaction() {
        sqLiteDatabase.beginTransaction();
    }

    public void endTransaction() {
        sqLiteDatabase.endTransaction();
    }

    public void setTransactionSuccessful() {
        sqLiteDatabase.setTransactionSuccessful();
    }

    public void close() {
        databaseHelper.close();
    }

    public long insert(Collection<? extends Table> entities) {
        long insertedId = -1;
        for (Table entity : entities) {
            insertedId = sqLiteDatabase.insert(entity.getTableName(), null, entity.getContentValues());
        }
        return insertedId;
    }

    public Cursor rawQuery(String query, String[] selectionArgs) {
        Cursor cursor = sqLiteDatabase.rawQuery(query, selectionArgs);
        return cursor;
    }

    public long insert(Table table) {
        long insertedId = -1;
        insertedId = sqLiteDatabase.insert(table.getTableName(), null, table.getContentValues());
        return insertedId;
    }

    public int update(Class<? extends Table> table, ContentValues contentValues, String whereClause, String[] whereArgs) {
        Table entity = newInstance(table);
        int updatedRows = 0;
        updatedRows = sqLiteDatabase.update(entity.getTableName(), contentValues, whereClause, whereArgs);
        return updatedRows;
    }

    // Custom delete method
    public void deleteAll(Class<? extends Table> table) {
        Table entity = newInstance(table);
        sqLiteDatabase.execSQL("DELETE FROM " + entity.getTableName());

    }

    // another custom delete method

    public void deleteTable(String tableName) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public void deleteAll(String tableName) {
        sqLiteDatabase.execSQL("DELETE FROM  " + tableName);
    }

    public int delete(Class<? extends Table> table, ContentValues contentValues, String whereClause, String[] whereArgs) {
        Table entity = newInstance(table);
        int deletedRows = 0;
        deletedRows = sqLiteDatabase.update(entity.getTableName(), contentValues, whereClause, whereArgs);
        return deletedRows;
    }

    public Cursor select(Class<? extends Table> table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Table entity = newInstance(table);
        Cursor cursor = sqLiteDatabase.query(entity.getTableName(), entity.getColumnNames(), selection, selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    public Cursor select(Class<? extends Table> table, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Table entity = newInstance(table);
        Cursor cursor = sqLiteDatabase.query(entity.getTableName(), entity.getColumnNames(), selection, selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    private Table newInstance(Class<? extends Table> entity) {
        try {
            Table obj = entity.newInstance();
            return obj;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    //transaction methods
}


