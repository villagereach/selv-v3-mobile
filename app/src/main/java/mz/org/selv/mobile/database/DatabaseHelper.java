package mz.org.selv.mobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE_SYNC_STATUS = "" +
            "create table " + Database.SyncStatus.TABLE_NAME + "(" +
            Database.SyncStatus.COLUMN_SERVER_ID + " text, " +
            Database.SyncStatus.COLUMN_ENTITY + " text, " +
            Database.SyncStatus.COLUMN_DATE + " text, " +
            Database.SyncStatus.COLUMN_USER_ID + " text)";

    public static final String CREATE_TABLE_SERVER = "" +
            "create table " + Database.Server.TABLE_NAME + "(" +
            Database.Server.COLUMN_SERVER_ID + " text, " +
            Database.Server.COLUMN_URL + " text, " +
            Database.Server.COLUMN_LAST_TIME_USED + " text)";

    public static final String CREATE_TABLE_USER = "" +
            "create table " + Database.User.TABLE_NAME + " (" +
            Database.User.COLUMN_USER_ID + " text," +
            Database.User.COLUMN_SERVER_ID + " text," +
            Database.User.COLUMN_TOKEN_EXPIRATION + " text," +
            Database.User.COLUMN_TOKEN + " text," +
            Database.User.COLUMN_NAME + " text," +
            Database.User.COLUMN_SURNAME + " text," +
            Database.User.COLUMN_HOME_FACILITY_NAME + " text," +
            Database.User.COLUMN_HOME_FACLITY_ID + " text," +
            Database.User.COLUMN_IS_LOGGED_IN + " text," +
            Database.User.COLUMN_LAST_TIME_LOGGED_IN_SERVER + " text," +
            Database.User.COLUMN_USERNAME + " text," +
            Database.User.COLUMN_PASSWORD + " text)";

    public static final String CREATE_TABLE_FACILITY = "" +
            "create table " + Database.Facility.TABLE_NAME + " (" +
            Database.Facility.COLUMN_CODE + " text," +
            Database.Facility.COLUMN_NAME + " text," +
            Database.Facility.COLUMN_FACILITY_TYPE_CODE + " text," +
            Database.Facility.COLUMN_FACILITY_TYPE_ID + " text," +
            Database.Facility.COLUMN_ZONE + " text," +
            Database.Facility.COLUMN_UUID + " text)";

    public static final String CREATE_TABLE_ORDERABLE = "" +
            "create table " + Database.Orderable.TABLE_NAME + " (" +
            Database.Orderable.COLUMN_CODE + " text," +
            Database.Orderable.COLUMN_NAME + " text," +
            Database.Orderable.COLUMN_UUID + " text)";

    public static final String CREATE_TABLE_LOT = "" +
            "create table " + Database.Lot.TABLE_NAME + " (" +
            Database.Lot.COLUMN_CODE + " text," +
            Database.Lot.COLUMN_EXPIRATION_DATE + " text," +
            Database.Lot.COLUMN_TRADE_ITEM_ID + " text," +
            Database.Lot.COLUMN_UUID + " text)";

    public static final String CREATE_TABLE_TRADE_ITEM = "" +
            "create table " + Database.TradeItem.TABLE_NAME + " (" +
            Database.TradeItem.COLUMN_ORDERABLE_ID + " text," +
            Database.TradeItem.COLUMN_TRADE_ITEM_ID + " text)";

    public static final String CREATE_TABLE_PROGRAM = "" +
            "create table " + Database.Program.TABLE_NAME + " (" +
            Database.Program.COLUMN_CODE + " text," +
            Database.Program.COLUMN_NAME + " text," +
            Database.Program.COLUMN_DESCRIPTION + " text," +
            Database.Program.COLUMN_ACTIVE + " text," +
            Database.Program.COLUMN_LAST_SYNC + " text," +
            Database.Program.COLUMN_STATUS + " text," +
            Database.Program.COLUMN_UUID + " text)";

    public static final String CREATE_TABLE_REASON = "" +
            "create table " + Database.Reason.TABLE_NAME + " (" +
            Database.Reason.COLUMN_NAME_NAME + " text," +
            Database.Reason.COLUMN_NAME_TYPE + " text," +
            Database.Reason.COLUMN_NAME_CATEGORY + " text," +
            Database.Reason.COLUMN_NAME_UUID + " text)";

    public static final String CREATE_TABLE_PROGRAM_ORDERABLE = "" +
            "create table " + Database.ProgramOrderable.TABLE_NAME + " (" +
            Database.ProgramOrderable.COLUMN_NAME_PROGRAM_ID + " text," +
            Database.ProgramOrderable.COLUMN_NAME_ORDERABLE_ID + " text)";

    public static final String CREATE_TABLE_VALID_REASONS = "" +
            "create table " + Database.ValidReasons.TABLE_NAME + " (" +
            Database.ValidReasons.COLUMN_NAME_FACILITY_TYPE_ID + " text," +
            Database.ValidReasons.COLUMN_NAME_PROGRAM_ID + " text," +
            Database.ValidReasons.COLUMN_NAME_REASON_ID + " text," +
            Database.ValidReasons.COLUMN_NAME_UUID + " text)";

    public static final String CREATE_TABLE_FACILITY_TYPE = "" +
            "create table " + Database.FacilityType.TABLE_NAME + " (" +
            Database.FacilityType.COLUMN_CODE + " text," +
            Database.FacilityType.COLUMN_NAME + " text," +
            Database.FacilityType.COLUMN_UUID + " text)";

    public static final String CREATE_TABLE_FACILITY_TYPE_APPROVED_PRODUCT = "" +
            "create table " + Database.FacilityTypeApprovedProductAndProgram.TABLE_NAME + " (" +
            Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_FACILITY_TYPE_ID + " text," +
            Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_PROGRAM_ID + " text," +
            Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_UUID + " text," +
            Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_ORDERABLE_ID + " text)";

    //physical inventor
    //

    public static final String CREATE_TABLE_PHYSICAL_INVENTORY = "" +
            "create table " + Database.PhysicalInventory.TABLE_NAME + " (" +
            Database.PhysicalInventory.COLUMN_OCCURRED_DATE + " text," +
            Database.PhysicalInventory.COLUMN_SIGNATURE + " text," +
            Database.PhysicalInventory.COLUMN_FACILITY_ID + " text," +
            Database.PhysicalInventory.COLUMN_DESCRIPTION + " text," +
            Database.PhysicalInventory.COLUMN_PROGRAM_ID + " text," +
            Database.PhysicalInventory.COLUMN_STATUS + " text," +

            Database.PhysicalInventory.COLUMN_UUID + " Text)";

    public static final String CREATE_TABLE_PHYSICAL_INVENTORY_LINE_ITEM = "" +
            "create table " + Database.PhysicalInventoryLineItem.TABLE_NAME + " (" +
            Database.PhysicalInventoryLineItem.COLUMN_ORDERABLE_ID + " text," +
            Database.PhysicalInventoryLineItem.COLUMN_LOT_ID + " text," +
            Database.PhysicalInventoryLineItem.COLUMN_PHYSICAL_STOCK + " INTEGER," +
            Database.PhysicalInventoryLineItem.COLUMN_PREVIOUS_STOCK_ON_HAND + " INTEGER," +
            Database.PhysicalInventoryLineItem.COLUMN_PHYSICAL_INVENTORY_ID + " text)";

    public static final String CREATE_TABLE_PHYSICAL_INVENTORY_LINE_ITEM_ADJUSTMENT = "" +
            "create table " + Database.PhysicalInventoryLineItemAdjustment.TABLE_NAME + " (" +
            Database.PhysicalInventoryLineItemAdjustment.COLUMN_STOCK_CARD_LINE_ITEM_ID + " text," +
            Database.PhysicalInventoryLineItemAdjustment.COLUMN_ID + " text," +
            Database.PhysicalInventoryLineItemAdjustment.COLUMN_PHYSICAL_INVENTORY_LINE_ITEM_ID + " text," +
            Database.PhysicalInventoryLineItemAdjustment.COLUMN_REASON_ID + " text," +
            Database.PhysicalInventoryLineItemAdjustment.COLUMN_QUANTITY + " INTEGER)";

    public static final String CREATE_TABLE_STOCK_CARD = "" +
            "create table " + Database.StockCard.TABLE_NAME + " (" +
            Database.StockCard.COLUMN_NAME_PROGRAM_ID + " text," +
            Database.StockCard.COLUMN_NAME_LOT_ID + " text," +
            Database.StockCard.COLUMN_NAME_ORDERABLE_ID + " text," +
            Database.StockCard.COLUMN_NAME_STOCK_ON_HAND + " text," +
            Database.StockCard.COLUMN_NAME_ID + " text," +
            Database.StockCard.COLUMN_NAME_IS_ACTIVE + " text," +
            Database.StockCard.COLUMN_NAME_FACILITY_ID + " text)";

    public static final String CREATE_TABLE_STOCK_CARD_LINE_ITEM = "" +
            "create table " + Database.StockCardLineItem.TABLE_NAME + " (" +
            Database.StockCardLineItem.COLUMN_ORDERABLE_ID + " text," +
            Database.StockCardLineItem.COLUMN_LOT_ID + " text," +
            Database.StockCardLineItem.COLUMN_QUANTITY + "  integer, " +
            Database.StockCardLineItem.COLUMN_STOCK_ON_HAND + "  integer, " +
            Database.StockCardLineItem.COLUMN_DESTINATION_ID + "  text, " +
            Database.StockCardLineItem.COLUMN_DESTINATION_FREE_TEXT + "  text, " +
            Database.StockCardLineItem.COLUMN_EXTRA_DATA + "  text, " +
            Database.StockCardLineItem.COLUMN_SOURCE_ID + "  text, " +
            Database.StockCardLineItem.COLUMN_SOURCE_FREE_TEXT + "  text, " +
            Database.StockCardLineItem.COLUMN_REASON_FREE_TEXT + "  text, " +
            Database.StockCardLineItem.COLUMN_REASON_ID + "  text, " +
            Database.StockCardLineItem.COLUMN_ORIGIN_EVENT_ID + "  text, " +
            Database.StockCardLineItem.COLUMN_STOCK_CARD_ID + "  text, " +
            Database.StockCardLineItem.COLUMN_PROCCESSED_DATE + "  text, " +
            Database.StockCardLineItem.COLUMN_OCCURRED_DATE + "  text, " +
            Database.StockCardLineItem.COLUMN_ID + " text) ";

    public static final String CREATE_TABLE_STOCK_EVENT = "" +
            "create table " + Database.StockEvent.TABLE_NAME + " (" +
            Database.StockEvent.COLUMN_NAME_FACILITY_ID + " text," +
            Database.StockEvent.COLUMN_NAME_PROGRAM_ID + " text," +
            Database.StockEvent.COLUMN_NAME_TYPE + " text," +
            Database.StockEvent.COLUMN_NAME_STATUS + " text," +
            Database.StockEvent.COLUMN_NAME_UUID + " text," +
            Database.StockEvent.COLUMN_OCCURRED_DATE + " text," +
            Database.StockEvent.COLUMN_PROCESSED_DATE + " text)";

    public static final String CREATE_TABLE_STOCK_EVENT_LINE_ITEM = "" +
            "create table " + Database.StockEventLineItem.TABLE_NAME + " (" +
            Database.StockEventLineItem.COLUMN_ORDERABLE_ID + " text," +
            Database.StockEventLineItem.COLUMN_FACILITY_ID + " text," +
            Database.StockEventLineItem.COLUMN_LOT_ID + " text," +
            Database.StockEventLineItem.COLUMN_QUANTITY + "  integer, " +
            Database.StockEventLineItem.COLUMN_DESTINATION_ID + "  text, " +
            Database.StockEventLineItem.COLUMN_PROGRAM_ID + "  text, " +
            Database.StockEventLineItem.COLUMN_DESTINATION_FREE_TEXT + "  text, " +
            Database.StockEventLineItem.COLUMN_EXTRA_DATA + "  text, " +
            Database.StockEventLineItem.COLUMN_SOURCE_ID + "  text, " +
            Database.StockEventLineItem.COLUMN_SOURCE_FREE_TEXT + "  text, " +
            Database.StockEventLineItem.COLUMN_REASON_FREE_TEXT + "  text, " +
            Database.StockEventLineItem.COLUMN_REASON_ID + "  text, " +
            Database.StockEventLineItem.COLUMN_STOCK_EVENT_ID + "  text, " +
            Database.StockEventLineItem.COLUMN_OCCURRED_DATE + "  text, " +
            Database.StockEventLineItem.COLUMN_PROCCESSED_DATE + "  text, " +
            Database.StockEventLineItem.COLUMN_ID + " text) ";

    public static final String CREATE_TABLE_CALCULATED_STOCK_ON_HAND = "" +
            "create table " + Database.CalculatedStockOnHand.TABLE_NAME + " (" +
            Database.CalculatedStockOnHand.COLUMN_OCCURRED_DATE + " text," +
            Database.CalculatedStockOnHand.COLUMN_STOCK_CARD_ID + " text," +
            Database.CalculatedStockOnHand.COLUMN_ID + " text," +
            Database.CalculatedStockOnHand.COLUMN_NAME_FACILITY_ID + " text," +
            Database.CalculatedStockOnHand.COLUMN_NAME_PROGRAM_ID + " text," +
            Database.CalculatedStockOnHand.COLUMN_ORDERABLE_ID + " text," +
            Database.CalculatedStockOnHand.COLUMN_LOT_ID + " text," +
            Database.CalculatedStockOnHand.COLUMN_STOCK_ON_HAND + " integer)";


    public static final String CREATE_TABLE_VALID_SOURCES = "create table " +
            Database.ValidSources.TABLE_NAME + "(" +
            Database.ValidSources.COLUMN_NAME_FREE_TEXT_ALLOWED + " text," +
            Database.ValidSources.COLUMN_NAME_PROGRAM_ID + " text," +
            Database.ValidSources.COLUMN_NAME_FACILITY_TYPE_ID + " text," +
            Database.ValidSources.COLUMN_NAME_FACILITY_ID + " text," +
            Database.ValidSources.COLUMN_NAME_NODE_ID + " text," +
            Database.ValidSources.COLUMN_NAME_NODE_REFERENCE_ID + " text," +
            Database.ValidSources.COLUMN_NAME_NAME + " text," +
            Database.ValidSources.COLUMN_NAME_REFERENCE_DATA_FACILITY + " text," +
            Database.ValidSources.COLUMN_NAME_ID + " text)";

    public static final String CREATE_TABLE_VALID_DESTINATIONS = "create table " +
            Database.ValidDestinations.TABLE_NAME + "(" +
            Database.ValidDestinations.COLUMN_NAME_FREE_TEXT_ALLOWED + " text," +
            Database.ValidDestinations.COLUMN_NAME_PROGRAM_ID + " text," +
            Database.ValidDestinations.COLUMN_NAME_FACILITY_TYPE_ID + " text," +
            Database.ValidDestinations.COLUMN_NAME_FACILITY_ID + " text," +
            Database.ValidDestinations.COLUMN_NAME_NODE_ID + " text," +
            Database.ValidDestinations.COLUMN_NAME_NODE_REFERENCE_ID + " text," +
            Database.ValidDestinations.COLUMN_NAME_NAME + " text," +
            Database.ValidDestinations.COLUMN_NAME_REFERENCE_DATA_FACILITY + " text," +
            Database.ValidDestinations.COLUMN_NAME_ID + " text)";


    // Drop Database
    public static final String DROP_TABLE_FACILITY = "drop table if exists " + Database.Facility.TABLE_NAME;
    public static final String DROP_TABLE_FACILITY_TYPE = "drop table if exists " + Database.FacilityType.TABLE_NAME;
    public static final String DROP_TABLE_FACILITY_TYPE_APPROVED_PRODUCT = "drop table if exists " + Database.FacilityTypeApprovedProductAndProgram.TABLE_NAME;
    public static final String DROP_TABLE_ORDERABLE = "drop table if exists " + Database.Orderable.TABLE_NAME;
    public static final String DROP_TABLE_LOT = "drop table if exists " + Database.Lot.TABLE_NAME;
    public static final String DROP_TABLE_TRADE_ITEM = "drop table if exists " + Database.TradeItem.TABLE_NAME;
    public static final String DROP_TABLE_PROGRAM = "drop table if exists " + Database.Program.TABLE_NAME;
    public static final String DROP_TABLE_VALID_REASONS = "drop table if exists " + Database.ValidReasons.TABLE_NAME;
    public static final String DROP_TABLE_PROGRAM_ORDERABLE = "drop table if exists " + Database.ProgramOrderable.TABLE_NAME;
    public static final String DROP_TABLE_INVENTORY = "drop table if exists " + Database.PhysicalInventory.TABLE_NAME;
    public static final String DROP_TABLE_REASON = "drop table if exists " + Database.Reason.TABLE_NAME;
    public static final String DROP_TABLE_STOCK_CARD = "drop table if exists " + Database.StockCard.TABLE_NAME;


    public static final String DROP_TABLE_PHYSICAL_INVENTORY = "drop table if exists " + Database.PhysicalInventory.TABLE_NAME;
    public static final String DROP_TABLE_PHYSICAL_INVENTORY_LINE_ITEM = "drop table if exists " + Database.PhysicalInventoryLineItem.TABLE_NAME;
    public static final String DROP_TABLE_PHYSICAL_INVENTORY_LINE_ITEM_ADJUSTMENT = "drop table if exists " + Database.PhysicalInventoryLineItemAdjustment.TABLE_NAME;


    public static final String DROP_TABLE_STOCK_CARD_LINE_ITEM = "drop table if exists " + Database.StockCardLineItem.TABLE_NAME;
    public static final String DROP_TABLE_STOCK_EVENT = "drop table if exists " + Database.StockEvent.TABLE_NAME;
    public static final String DROP_TABLE_STOCK_EVENT_LINE_ITEM = "drop table if exists " + Database.StockEventLineItem.TABLE_NAME;
    public static final String DROP_TABLE_CALCULATED_STOCK_ON_HAND = "drop table if exists " + Database.CalculatedStockOnHand.TABLE_NAME;
    public static final String DROP_TABLE_VALID_SOURCES = "drop table if exists " + Database.ValidSources.TABLE_NAME;
    public static final String DROP_TABLE_VALID_DESTINATIONS = "drop table if exists " + Database.ValidDestinations.TABLE_NAME;


    public static final String DROP_TABLE_USER = "drop table if exists " + Database.User.TABLE_NAME;
    public static final String DROP_TABLE_SERVER = "drop table if exists " + Database.Server.TABLE_NAME;
    public static final String DROP_TABLE_SYNC_STATUS = "drop table if exists " + Database.SyncStatus.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Reference Data
        // CREATE TABLES
        db.execSQL(CREATE_TABLE_SERVER);
        db.execSQL(CREATE_TABLE_FACILITY);
        db.execSQL(CREATE_TABLE_FACILITY_TYPE);
        db.execSQL(CREATE_TABLE_PROGRAM);
        db.execSQL(CREATE_TABLE_ORDERABLE);
        db.execSQL(CREATE_TABLE_TRADE_ITEM);
        db.execSQL(CREATE_TABLE_LOT);
        db.execSQL(CREATE_TABLE_PROGRAM_ORDERABLE);
        db.execSQL(CREATE_TABLE_REASON);
        db.execSQL(CREATE_TABLE_FACILITY_TYPE_APPROVED_PRODUCT);
        db.execSQL(CREATE_TABLE_VALID_REASONS);
        db.execSQL(CREATE_TABLE_PHYSICAL_INVENTORY);
        db.execSQL(CREATE_TABLE_PHYSICAL_INVENTORY_LINE_ITEM);
        db.execSQL(CREATE_TABLE_STOCK_CARD_LINE_ITEM);
        db.execSQL(CREATE_TABLE_STOCK_EVENT);
        db.execSQL(CREATE_TABLE_STOCK_EVENT_LINE_ITEM);
        db.execSQL(CREATE_TABLE_STOCK_CARD);
        db.execSQL(CREATE_TABLE_CALCULATED_STOCK_ON_HAND);
        db.execSQL(CREATE_TABLE_PHYSICAL_INVENTORY_LINE_ITEM_ADJUSTMENT);
        db.execSQL(CREATE_TABLE_VALID_SOURCES);
        db.execSQL(CREATE_TABLE_VALID_DESTINATIONS);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_SYNC_STATUS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DROPB Tables
        db.execSQL(DROP_TABLE_SERVER);
        db.execSQL(DROP_TABLE_FACILITY);
        db.execSQL(DROP_TABLE_FACILITY_TYPE);
        db.execSQL(DROP_TABLE_PROGRAM);
        db.execSQL(DROP_TABLE_ORDERABLE);
        db.execSQL(DROP_TABLE_TRADE_ITEM);
        db.execSQL(DROP_TABLE_LOT);
        db.execSQL(DROP_TABLE_PROGRAM_ORDERABLE);
        db.execSQL(DROP_TABLE_REASON);
        db.execSQL(DROP_TABLE_FACILITY_TYPE_APPROVED_PRODUCT);
        db.execSQL(DROP_TABLE_VALID_REASONS);
        db.execSQL(DROP_TABLE_PHYSICAL_INVENTORY);
        db.execSQL(DROP_TABLE_PHYSICAL_INVENTORY_LINE_ITEM);
        db.execSQL(DROP_TABLE_STOCK_CARD_LINE_ITEM);
        db.execSQL(DROP_TABLE_STOCK_EVENT);
        db.execSQL(DROP_TABLE_STOCK_EVENT_LINE_ITEM);
        db.execSQL(DROP_TABLE_STOCK_CARD);
        db.execSQL(DROP_TABLE_CALCULATED_STOCK_ON_HAND);
        db.execSQL(DROP_TABLE_PHYSICAL_INVENTORY_LINE_ITEM_ADJUSTMENT);
        db.execSQL(DROP_TABLE_VALID_SOURCES);
        db.execSQL(DROP_TABLE_VALID_DESTINATIONS);
        db.execSQL(DROP_TABLE_USER);
        db.execSQL(DROP_TABLE_SYNC_STATUS);


        //create table
        db.execSQL(CREATE_TABLE_SERVER);
        db.execSQL(CREATE_TABLE_FACILITY);
        db.execSQL(CREATE_TABLE_FACILITY_TYPE);
        db.execSQL(CREATE_TABLE_PROGRAM);
        db.execSQL(CREATE_TABLE_ORDERABLE);
        db.execSQL(CREATE_TABLE_TRADE_ITEM);
        db.execSQL(CREATE_TABLE_LOT);
        db.execSQL(CREATE_TABLE_PROGRAM_ORDERABLE);
        db.execSQL(CREATE_TABLE_REASON);
        db.execSQL(CREATE_TABLE_FACILITY_TYPE_APPROVED_PRODUCT);
        db.execSQL(CREATE_TABLE_VALID_REASONS);
        db.execSQL(CREATE_TABLE_PHYSICAL_INVENTORY);
        db.execSQL(CREATE_TABLE_PHYSICAL_INVENTORY_LINE_ITEM);
        db.execSQL(CREATE_TABLE_STOCK_EVENT_LINE_ITEM);
        db.execSQL(CREATE_TABLE_STOCK_CARD_LINE_ITEM);
        db.execSQL(CREATE_TABLE_STOCK_EVENT);
        db.execSQL(CREATE_TABLE_STOCK_CARD);
        db.execSQL(CREATE_TABLE_CALCULATED_STOCK_ON_HAND);
        db.execSQL(CREATE_TABLE_PHYSICAL_INVENTORY_LINE_ITEM_ADJUSTMENT);
        db.execSQL(CREATE_TABLE_VALID_SOURCES);
        db.execSQL(CREATE_TABLE_VALID_DESTINATIONS);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_SYNC_STATUS);
    }
}
