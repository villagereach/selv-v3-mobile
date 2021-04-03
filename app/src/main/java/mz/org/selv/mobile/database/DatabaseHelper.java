package mz.org.selv.mobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE_FACILITY = "" +
            "create table "+Database.Facility.TABLE_NAME+" (" +
            Database.Facility.COLUMN_CODE+" text," +
            Database.Facility.COLUMN_NAME+" text," +
            Database.Facility.COLUMN_TYPE+" text," +
            Database.Facility.COLUMN_ZONE+" text," +
            Database.Facility.COLUMN_UUID+" text)";

    public static final String CREATE_TABLE_ORDERABLE = "" +
            "create table "+Database.Orderable.TABLE_NAME+" (" +
            Database.Orderable.COLUMN_CODE+" text," +
            Database.Orderable.COLUMN_NAME+" text," +
            Database.Orderable.COLUMN_UUID+" text)";

    public static final String CREATE_TABLE_LOT = "" +
            "create table "+Database.Lot.TABLE_NAME+" (" +
            Database.Lot.COLUMN_CODE+" text," +
            Database.Lot.COLUMN_EXPIRATION_DATE+" text," +
            Database.Lot.COLUMN_ORDERABLE_ID+" text," +
            Database.Lot.COLUMN_UUID+" text)";

    public static final String CREATE_TABLE_PROGRAM = "" +
            "create table "+Database.Program.TABLE_NAME+" (" +
            Database.Program.COLUMN_CODE+" text," +
            Database.Program.COLUMN_NAME+" text," +
            Database.Program.COLUMN_DESCRIPTION+" text,"+
            Database.Program.COLUMN_ACTIVE+" text,"+
            Database.Program.COLUMN_LAST_SYNC+" text,"+
            Database.Program.COLUMN_STATUS+" text,"+
            Database.Program.COLUMN_UUID+" text)";

    public static final String CREATE_TABLE_REASON = "" +
            "create table "+Database.Reason.TABLE_NAME+" (" +
            Database.Reason.COLUMN_NAME_NAME+" text," +
            Database.Reason.COLUMN_NAME_TYPE+" text," +
            Database.Reason.COLUMN_NAME_CATEGORY+" text," +
            Database.Reason.COLUMN_NAME_UUID+" text)";

    public static final String CREATE_TABLE_PROGRAM_ORDERABLE = "" +
            "create table "+Database.ProgramOrderable.TABLE_NAME+" (" +
            Database.ProgramOrderable.COLUMN_NAME_PROGRAM_ID+" text," +
            Database.ProgramOrderable.COLUMN_NAME_ORDERABLE_ID+" text)" ;

    public static final String CREATE_TABLE_VALID_REASONS = "" +
            "create table "+Database.ValidReasons.TABLE_NAME+" (" +
            Database.ValidReasons.COLUMN_NAME_FACILITY_TYPE_ID+" text," +
            Database.ValidReasons.COLUMN_NAME_PROGRAM_ID+" text," +
            Database.ValidReasons.COLUMN_NAME_REASON_ID+" text," +
            Database.ValidReasons.COLUMN_NAME_UUID+" text)" ;

    public static final String CREATE_TABLE_FACILITY_TYPE = "" +
            "create table "+Database.FacilityType.TABLE_NAME+" (" +
            Database.FacilityType.COLUMN_CODE+" text," +
            Database.FacilityType.COLUMN_NAME+" text," +
            Database.FacilityType.COLUMN_UUID+" text)";

    public static final String CREATE_TABLE_FACILITY_TYPE_APPROVED_PRODUCT = "" +
            "create table "+ Database.FacilityTypeApprovedProductAndProgram.TABLE_NAME+" (" +
            Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_FACILITY_TYPE_ID+" text," +
            Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_PROGRAM_ID+" text," +
            Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_UUID+" text," +
            Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_ORDERABLE_ID+" text)";

    public static final String CREATE_TABLE_STOCK_CARD_LINE_ITEM = "" +
            "create table "+Database.Facility.TABLE_NAME+" (" +
            Database.Facility.COLUMN_CODE+" text," +
            Database.Facility.COLUMN_NAME+" text," +
            Database.Facility.COLUMN_TYPE+" text," +
            Database.Facility.COLUMN_ZONE+" text," +
            Database.Facility.COLUMN_UUID+" text)";

    public static final String CREATE_TABLE_STOCK_EVENT = "" +
            "create table "+Database.Facility.TABLE_NAME+" (" +
            Database.Facility.COLUMN_CODE+" text," +
            Database.Facility.COLUMN_NAME+" text," +
            Database.Facility.COLUMN_TYPE+" text," +
            Database.Facility.COLUMN_ZONE+" text," +
            Database.Facility.COLUMN_UUID+" text)";

    public static final String CREATE_TABLE_STOCK_EVENT_LINE_ITEM = "" +
            "create table "+Database.Facility.TABLE_NAME+" (" +
            Database.Facility.COLUMN_CODE+" text," +
            Database.Facility.COLUMN_NAME+" text," +
            Database.Facility.COLUMN_TYPE+" text," +
            Database.Facility.COLUMN_ZONE+" text," +
            Database.Facility.COLUMN_UUID+" text)";



    // Drop Database
    public static final String DROP_TABLE_FACILITY = "drop table if exists "+Database.Facility.TABLE_NAME;
    public static final String DROP_TABLE_FACILITY_TYPE = "drop table if exists "+Database.FacilityType.TABLE_NAME;
    public static final String DROP_TABLE_FACILITY_TYPE_APPROVED_PRODUCT = "drop table if exists "+ Database.FacilityTypeApprovedProductAndProgram.TABLE_NAME;
    public static final String DROP_TABLE_ORDERABLE = "drop table if exists "+Database.Orderable.TABLE_NAME;
    public static final String DROP_TABLE_LOT = "drop table if exists "+Database.Lot.TABLE_NAME;
    public static final String DROP_TABLE_PROGRAM = "drop table if exists "+Database.Program.TABLE_NAME;
    public static final String DROP_TABLE_VALID_REASONS = "drop table if exists "+Database.ValidReasons.TABLE_NAME;
    public static final String DROP_TABLE_PROGRAM_ORDERABLE= "drop table if exists "+Database.ProgramOrderable.TABLE_NAME;
    public static final String DROP_TABLE_INVENTORY = "drop table if exists "+Database.PhysicalInventory.TABLE_NAME;
    public static final String DROP_TABLE_REASON = "drop table if exists "+Database.Reason.TABLE_NAME;
    public static final String DROP_TABLE_STOCK_CARD = "drop table if exists "+Database.StockCard.TABLE_NAME;
   // public static final String DROP_TABLE_STOCK_CARD_LINE_ITEM = "drop table if exists "+Database.StockCardLineItem.TABLE_NAME;
    public static final String DROP_TABLE_STOCK_EVENT = "drop table if exists "+Database.StockEvent.TABLE_NAME;
  //  public static final String DROP_TABLE_STOCK_EVENT_LINE_ITEM = "drop table if exists "+Database.StockEventLineItem.TABLE_NAME;

    public DatabaseHelper(Context context){
        super(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     //Reference Data
        // CREATE TABLES
        db.execSQL(CREATE_TABLE_FACILITY);
        db.execSQL(CREATE_TABLE_FACILITY_TYPE);
        db.execSQL(CREATE_TABLE_PROGRAM);
        db.execSQL(CREATE_TABLE_ORDERABLE);
        db.execSQL(CREATE_TABLE_LOT);
        db.execSQL(CREATE_TABLE_PROGRAM_ORDERABLE);
        db.execSQL(CREATE_TABLE_REASON);
        db.execSQL(CREATE_TABLE_FACILITY_TYPE_APPROVED_PRODUCT);
        db.execSQL(CREATE_TABLE_VALID_REASONS);

      //  db.execSQL(CREATE_TABLE_STOCK_CARD_LINE_ITEM);
     //   db.execSQL(CREATE_TABLE_STOCK_EVENT);
    //    db.execSQL(CREATE_TABLE_STOCK_EVENT_LINE_ITEM);
    //    db.execSQL(CREATE_TABLE_STOCKCARD);
    //    db.execSQL(CREATE_TABLE_PROCESSING_PERIOD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DROPB Tables
        db.execSQL(DROP_TABLE_FACILITY);
        db.execSQL(DROP_TABLE_FACILITY_TYPE);
        db.execSQL(DROP_TABLE_PROGRAM);
        db.execSQL(DROP_TABLE_ORDERABLE);
        db.execSQL(DROP_TABLE_LOT);
        db.execSQL(DROP_TABLE_PROGRAM_ORDERABLE);
        db.execSQL(DROP_TABLE_REASON);
        db.execSQL(DROP_TABLE_FACILITY_TYPE_APPROVED_PRODUCT);
        db.execSQL(DROP_TABLE_VALID_REASONS);
        //create table
        db.execSQL(CREATE_TABLE_FACILITY);
        db.execSQL(CREATE_TABLE_FACILITY_TYPE);
        db.execSQL(CREATE_TABLE_PROGRAM);
        db.execSQL(CREATE_TABLE_ORDERABLE);
        db.execSQL(CREATE_TABLE_LOT);
        db.execSQL(CREATE_TABLE_PROGRAM_ORDERABLE);
        db.execSQL(CREATE_TABLE_REASON);
        db.execSQL(CREATE_TABLE_FACILITY_TYPE_APPROVED_PRODUCT);
        db.execSQL(CREATE_TABLE_VALID_REASONS);
    }
}
