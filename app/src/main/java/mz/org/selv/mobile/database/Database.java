package mz.org.selv.mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Collection;

public class Database {
    public final static String DATABASE_NAME = "SELV_MOBILE";
    public final static int  DATABASE_VERSION = 6;

    public static  class User implements BaseColumns {

    }

    public static  class Facility implements BaseColumns {
        public static String COLUMN_CODE = "code";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_UUID = "id";
        public static String COLUMN_ZONE = "zoneId";
        public static String COLUMN_TYPE = "type";
        public static String TABLE_NAME = "facility";
        public static String[] ALL_COLUMNS = {COLUMN_CODE, COLUMN_NAME, COLUMN_UUID, COLUMN_ZONE, COLUMN_TYPE};

    }

    public static  class FacilityType implements BaseColumns {
        public static String COLUMN_CODE = "code";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_UUID = "id";
        public static String TABLE_NAME = "facility_type";
        public static String[] ALL_COLUMNS = {COLUMN_CODE, COLUMN_NAME, COLUMN_UUID};
    }

    public static  class Orderable implements BaseColumns {
        public static String COLUMN_CODE = "code";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_UUID = "id";
        public static String TABLE_NAME = "orderable";
        public static String[] ALL_COLUMNS = {COLUMN_CODE, COLUMN_NAME, COLUMN_UUID};
    }

    public static  class ProgramOrderable implements BaseColumns {
        public static String COLUMN_NAME_PROGRAM_ID = "programid";
        public static String COLUMN_NAME_ORDERABLE_ID = "orderableid";
        public static String TABLE_NAME = "program_orderable";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_ORDERABLE_ID, COLUMN_NAME_PROGRAM_ID};
    }

    public static  class FacilityTypeApprovedProduct implements BaseColumns {
        public static String COLUMN_NAME_FACILITY_TYPE_ID = "facilityTypeId";
        public static String COLUMN_NAME_ORDERABLE_ID = "orderableid";
        public static String TABLE_NAME = "facility_type_approved_product";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_ORDERABLE_ID, COLUMN_NAME_FACILITY_TYPE_ID};
    }

    public static  class ProcessingPeriod implements BaseColumns {

    }

    public static  class ProcessingSchedulle implements BaseColumns {

    }

    public static  class Program implements BaseColumns {
        public static  String COLUMN_NAME = "name";
        public static  String COLUMN_CODE = "code";
        public static  String COLUMN_UUID =  "id";
        public static  String COLUMN_STATUS =  "status";
        public static  String COLUMN_LAST_SYNC =  "last_sync";
        public static  String COLUMN_ACTIVE = "active";
        public static String COLUMN_DESCRIPTION = "description";
        public static String TABLE_NAME = "program";
        public static String[] ALL_COLUMNS = {COLUMN_NAME, COLUMN_CODE, COLUMN_UUID, COLUMN_STATUS, COLUMN_LAST_SYNC, COLUMN_ACTIVE};
    }

    public static  class Requisition implements BaseColumns {

    }

    public static  class PhysicalInventory implements BaseColumns {
        public static  String COLUMN_NAME = "name";
        public static  String COLUMN_CODE = "code";
        public static  String COLUMN_UUID =  "id";
        public static  String COLUMN_STATUS =  "status";
        public static  String COLUMN_LAST_SYNC =  "last_sync";
        public static  String COLUMN_ACTIVE = "active";
        public static String COLUMN_DESCRIPTION = "description";
        public static String TABLE_NAME = "program";
        public static String[] ALL_COLUMNS = {COLUMN_NAME, COLUMN_CODE, COLUMN_UUID, COLUMN_STATUS, COLUMN_LAST_SYNC, COLUMN_ACTIVE};
    }

    public static  class ReasonCategory implements BaseColumns {

    }

    public static  class ReasonType implements BaseColumns {

    }

    public static  class Reason implements BaseColumns {
        public static String COLUMN_NAME_CATEGORY = "category";
        public static String COLUMN_NAME_TYPE = "type";
        public static String COLUMN_NAME_NAME = "name";
        public static String COLUMN_NAME_UUID = "id";
        public static String TABLE_NAME = "Reason";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_NAME, COLUMN_NAME_TYPE, COLUMN_NAME_CATEGORY, COLUMN_NAME_UUID};
    }

    public static  class ValidReasons implements BaseColumns {
        public static String COLUMN_NAME_FACILITY_TYPE_ID = "facilitytype";
        public static String COLUMN_NAME_REASON_ID = "reasonid";
        public static String COLUMN_NAME_PROGRAM_ID = "programid";
        public static String COLUMN_NAME_UUID = "id";
        public static String TABLE_NAME = "validreasons";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_PROGRAM_ID, COLUMN_NAME_PROGRAM_ID, COLUMN_NAME_FACILITY_TYPE_ID, COLUMN_NAME_UUID};
    }

    public static  class StockCard implements BaseColumns {
        public static String COLUMN_NAME_ORDERABLE_ID = "orderbaleId";
        public static String COLUMN_NAME_LOT_ID = "lotid";
        public static String COLUMN_NAME_STOCK_ON_HAND = "stockonhand";
        public static String COLUMN_NAME_PROGRAM_ID = "programid";
        public static String COLUMN_NAME_FACILITY_ID = "facilityid";
        public static String TABLE_NAME = "stock_card";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_ORDERABLE_ID, COLUMN_NAME_LOT_ID, COLUMN_NAME_FACILITY_ID,COLUMN_NAME_STOCK_ON_HAND, COLUMN_NAME_PROGRAM_ID};
    }

    public static  class StockEvent implements BaseColumns {
        public static String COLUMN_NAME_FACILITY_ID = "orderbaleId";
        public static String COLUMN_NAME_UUID = "id";
        public static String COLUMN_NAME_PROGRAM_ID = "programid";
        public static String TABLE_NAME = "stock_event";
        public static String[] ALL_COLUMNS = {COLUMN_NAME_UUID, COLUMN_NAME_FACILITY_ID, COLUMN_NAME_PROGRAM_ID};
    }

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public Database(Context context){
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

    public void deleteTable(String tableName){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +tableName);
    }

    public void deleteAll(String tableName){
        sqLiteDatabase.execSQL("DELETE FROM  " +tableName);
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
}


