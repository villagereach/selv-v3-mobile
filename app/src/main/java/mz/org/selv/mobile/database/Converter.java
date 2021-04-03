package mz.org.selv.mobile.database;

import android.database.Cursor;

import mz.org.selv.mobile.model.referencedata.Facility;
import mz.org.selv.mobile.model.referencedata.FacilityTypeApprovedProductAndProgram;
import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.model.referencedata.Program;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventory;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventoryLineItem;
import mz.org.selv.mobile.model.stockmanagement.Reason;
import mz.org.selv.mobile.model.stockmanagement.StockEvent;

public class Converter {

    public static Program cursorToProgram(Cursor cursor){
        Program program = new Program();
        program.setDescription(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_DESCRIPTION)));
        program.setName(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_NAME)));
        program.setCode(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_CODE)));
        program.setActive(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_ACTIVE)));
        program.setUuid(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_UUID)));
        return program;
    }

    public static Facility cursorToFacility(Cursor cursor){
        Facility facility = new Facility();
        facility.setCode(cursor.getString(cursor.getColumnIndex(Database.Facility.COLUMN_CODE)));
        facility.setName(cursor.getString(cursor.getColumnIndex(Database.Facility.COLUMN_NAME)));
        facility.setUuid(cursor.getString(cursor.getColumnIndex(Database.Facility.COLUMN_UUID)));
        facility.setType(cursor.getString(cursor.getColumnIndex(Database.Facility.COLUMN_TYPE)));
        return facility;
    }

    public static Orderable cursorToOrderable(Cursor cursor){
        Orderable orderable = new Orderable();
        orderable.setCode(cursor.getString(cursor.getColumnIndex(Database.Orderable.COLUMN_UUID)));
        orderable.setName(cursor.getString(cursor.getColumnIndex(Database.Orderable.COLUMN_NAME)));
        orderable.setUuid(cursor.getString(cursor.getColumnIndex(Database.Orderable.COLUMN_UUID)));
        return orderable;
    }

    public static Lot cursorToLot(Cursor cursor){
        Lot lot = new Lot();
        lot.setLotCode(cursor.getString(cursor.getColumnIndex(Database.Lot.COLUMN_CODE)));
        lot.setOrderableId(cursor.getString(cursor.getColumnIndex(Database.Lot.COLUMN_ORDERABLE_ID)));
        lot.setExpirationDate(cursor.getString(cursor.getColumnIndex(Database.Lot.COLUMN_EXPIRATION_DATE)));
        lot.setId(cursor.getString(cursor.getColumnIndex(Database.Lot.COLUMN_UUID)));
        return lot;
    }

    public static mz.org.selv.mobile.model.referencedata.FacilityTypeApprovedProductAndProgram cursorToFacilityTypeApprovedProduct(Cursor cursor){
        FacilityTypeApprovedProductAndProgram fType = new mz.org.selv.mobile.model.referencedata.FacilityTypeApprovedProductAndProgram();
        fType.setFacilityTypeId(cursor.getString(cursor.getColumnIndex(Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_FACILITY_TYPE_ID)));
        fType.setOrderableId(cursor.getString(cursor.getColumnIndex(Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_ORDERABLE_ID)));
        return fType;
    }

    public static Reason cursorToReason(Cursor cursor){
        Reason reason = new Reason();
        reason.setCategory(cursor.getString(cursor.getColumnIndex(Database.Reason.COLUMN_NAME_CATEGORY)));
        reason.setType(cursor.getString(cursor.getColumnIndex(Database.Reason.COLUMN_NAME_TYPE)));
        reason.setId(cursor.getString(cursor.getColumnIndex(Database.Reason.COLUMN_NAME_UUID)));
        reason.setName(cursor.getString(cursor.getColumnIndex(Database.Reason.COLUMN_NAME_NAME)));
        return reason;
    }

    public static PhysicalInventory cursorToInventory(Cursor cursor){
        PhysicalInventory inventory = new PhysicalInventory();
        inventory.setFacilityId(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_FACILITY_ID)));
        inventory.setOccurredDate(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_OCCURRED_DATE)));
        inventory.setProgramId(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_PROGRAM_ID)));
        inventory.setStatus(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_STATUS)));
        inventory.setSignature(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_SIGNATURE)));
        return inventory;
    }

    public static PhysicalInventoryLineItem cursorToInventoryLineItem(Cursor cursor){
        PhysicalInventoryLineItem inventoryLineItem = new PhysicalInventoryLineItem();
        return inventoryLineItem;
    }

    public static StockEvent cursorToStockEvent(Cursor cursor){
        StockEvent event = new StockEvent();
        event.setFacilityId(cursor.getString(cursor.getColumnIndex(Database.StockEvent.COLUMN_NAME_FACILITY_ID)));
        event.setProgramId(cursor.getString(cursor.getColumnIndex(Database.StockEvent.COLUMN_NAME_PROGRAM_ID)));
        event.setId(cursor.getString(cursor.getColumnIndex(Database.StockEvent.COLUMN_NAME_UUID)));
        return event;
    }
}