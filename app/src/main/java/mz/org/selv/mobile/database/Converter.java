package mz.org.selv.mobile.database;

import android.database.Cursor;

import mz.org.selv.mobile.model.referencedata.Facility;
import mz.org.selv.mobile.model.referencedata.FacilityTypeApprovedProductAndProgram;
import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.model.referencedata.Program;
import mz.org.selv.mobile.model.referencedata.TradeItem;
import mz.org.selv.mobile.model.stockmanagement.CalculatedStockOnHand;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventory;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventoryLineItem;
import mz.org.selv.mobile.model.stockmanagement.Reason;
import mz.org.selv.mobile.model.stockmanagement.StockCard;
import mz.org.selv.mobile.model.stockmanagement.StockCardLineItem;
import mz.org.selv.mobile.model.stockmanagement.StockEvent;
import mz.org.selv.mobile.model.stockmanagement.StockEventLineItem;
import mz.org.selv.mobile.model.stockmanagement.ValidDestination;
import mz.org.selv.mobile.model.stockmanagement.ValidReasons;
import mz.org.selv.mobile.model.stockmanagement.ValidSource;

public class Converter {

    public static Program cursorToProgram(Cursor cursor) {
        Program program = new Program();
        program.setDescription(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_DESCRIPTION)));
        program.setName(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_NAME)));
        program.setCode(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_CODE)));
        program.setActive(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_ACTIVE)));
        program.setUuid(cursor.getString(cursor.getColumnIndex(Database.Program.COLUMN_UUID)));
        return program;
    }

    public static Facility cursorToFacility(Cursor cursor) {
        Facility facility = new Facility();
        facility.setCode(cursor.getString(cursor.getColumnIndex(Database.Facility.COLUMN_CODE)));
        facility.setName(cursor.getString(cursor.getColumnIndex(Database.Facility.COLUMN_NAME)));
        facility.setUuid(cursor.getString(cursor.getColumnIndex(Database.Facility.COLUMN_UUID)));
        facility.setType(cursor.getString(cursor.getColumnIndex(Database.Facility.COLUMN_TYPE)));
        return facility;
    }

    public static Orderable cursorToOrderable(Cursor cursor) {
        Orderable orderable = new Orderable();
        orderable.setCode(cursor.getString(cursor.getColumnIndex(Database.Orderable.COLUMN_UUID)));
        orderable.setName(cursor.getString(cursor.getColumnIndex(Database.Orderable.COLUMN_NAME)));
        orderable.setUuid(cursor.getString(cursor.getColumnIndex(Database.Orderable.COLUMN_UUID)));
        return orderable;
    }

    public static Lot cursorToLot(Cursor cursor) {
        Lot lot = new Lot();
        lot.setLotCode(cursor.getString(cursor.getColumnIndex(Database.Lot.COLUMN_CODE)));
        lot.setOrderableId(cursor.getString(cursor.getColumnIndex(Database.Lot.COLUMN_TRADE_ITEM_ID)));
        lot.setExpirationDate(cursor.getString(cursor.getColumnIndex(Database.Lot.COLUMN_EXPIRATION_DATE)));
        lot.setId(cursor.getString(cursor.getColumnIndex(Database.Lot.COLUMN_UUID)));
        return lot;
    }

    public static TradeItem cursorToTradeItem(Cursor cursor) {
        TradeItem tradeItem = new TradeItem();
        tradeItem.setTradeItemId(cursor.getString(cursor.getColumnIndex(Database.TradeItem.COLUMN_TRADE_ITEM_ID)));
        tradeItem.setOrderableId(cursor.getString(cursor.getColumnIndex(Database.TradeItem.COLUMN_ORDERABLE_ID)));
        return tradeItem;
    }

    public static FacilityTypeApprovedProductAndProgram cursorToFacilityTypeApprovedProduct(Cursor cursor) {
        FacilityTypeApprovedProductAndProgram fType = new mz.org.selv.mobile.model.referencedata.FacilityTypeApprovedProductAndProgram();
        fType.setFacilityTypeId(cursor.getString(cursor.getColumnIndex(Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_FACILITY_TYPE_ID)));
        fType.setOrderableId(cursor.getString(cursor.getColumnIndex(Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_ORDERABLE_ID)));
        return fType;
    }

    public static Reason cursorToReason(Cursor cursor) {
        Reason reason = new Reason();
        reason.setCategory(cursor.getString(cursor.getColumnIndex(Database.Reason.COLUMN_NAME_CATEGORY)));
        reason.setType(cursor.getString(cursor.getColumnIndex(Database.Reason.COLUMN_NAME_TYPE)));
        reason.setId(cursor.getString(cursor.getColumnIndex(Database.Reason.COLUMN_NAME_UUID)));
        reason.setName(cursor.getString(cursor.getColumnIndex(Database.Reason.COLUMN_NAME_NAME)));
        return reason;
    }

    public static PhysicalInventory cursorToInventory(Cursor cursor) {
        PhysicalInventory inventory = new PhysicalInventory();
        inventory.setFacilityId(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_FACILITY_ID)));
        inventory.setOccurredDate(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_OCCURRED_DATE)));
        inventory.setProgramId(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_PROGRAM_ID)));
        inventory.setStatus(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_STATUS)));
        inventory.setSignature(cursor.getString(cursor.getColumnIndex(Database.PhysicalInventory.COLUMN_SIGNATURE)));
        return inventory;
    }


    public static StockCard cursorToStockCard(Cursor cursor) {
        StockCard stockCard = new StockCard();
        stockCard.setProgramId(cursor.getString(cursor.getColumnIndex(Database.StockCard.COLUMN_NAME_PROGRAM_ID)));
        stockCard.setOrderableId(cursor.getString(cursor.getColumnIndex(Database.StockCard.COLUMN_NAME_ORDERABLE_ID)));
        stockCard.setLotId(cursor.getString(cursor.getColumnIndex(Database.StockCard.COLUMN_NAME_LOT_ID)));
        stockCard.setFacilityId(cursor.getString(cursor.getColumnIndex(Database.StockCard.COLUMN_NAME_FACILITY_ID)));
        stockCard.setId(cursor.getString(cursor.getColumnIndex(Database.StockCard.COLUMN_NAME_ID)));
        return stockCard;
    }

    public static CalculatedStockOnHand cursorToCalculatedStockOnHand(Cursor cursor) {
        CalculatedStockOnHand calculatedStockOnHand = new CalculatedStockOnHand();
        calculatedStockOnHand.setOccuredDate(cursor.getString(cursor.getColumnIndex(Database.CalculatedStockOnHand.COLUMN_OCCURRED_DATE)));
        calculatedStockOnHand.setStockCardId(cursor.getString(cursor.getColumnIndex(Database.CalculatedStockOnHand.COLUMN_STOCK_CARD_ID)));
        calculatedStockOnHand.setStockOnHand(cursor.getInt(cursor.getColumnIndex(Database.CalculatedStockOnHand.COLUMN_STOCK_ON_HAND)));
        return calculatedStockOnHand;
    }

    public static PhysicalInventoryLineItem cursorToInventoryLineItem(Cursor cursor) {
        PhysicalInventoryLineItem inventoryLineItem = new PhysicalInventoryLineItem();
        return inventoryLineItem;
    }

    public static StockEvent cursorToStockEvent(Cursor cursor) {
        StockEvent event = new StockEvent();
        event.setFacilityId(cursor.getString(cursor.getColumnIndex(Database.StockEvent.COLUMN_NAME_FACILITY_ID)));
        event.setProgramId(cursor.getString(cursor.getColumnIndex(Database.StockEvent.COLUMN_NAME_PROGRAM_ID)));
        event.setId(cursor.getString(cursor.getColumnIndex(Database.StockEvent.COLUMN_NAME_UUID)));
        event.setProcessedDate(cursor.getString(cursor.getColumnIndex(Database.StockEvent.COLUMN_PROCESSED_DATE)));
        event.setStatus(cursor.getInt(cursor.getColumnIndex(Database.StockEvent.COLUMN_NAME_STATUS)));
        event.setType(cursor.getString(cursor.getColumnIndex(Database.StockEvent.COLUMN_NAME_TYPE)));
        return event;
    }

    public static ValidReasons cursorToValidReasons(Cursor cursor) {
        ValidReasons validReasons = new ValidReasons();
        validReasons.setFacilitytypeId(cursor.getString(cursor.getColumnIndex(Database.ValidReasons.COLUMN_NAME_FACILITY_TYPE_ID)));
        validReasons.setProgramId(cursor.getString(cursor.getColumnIndex(Database.ValidReasons.COLUMN_NAME_PROGRAM_ID)));
        validReasons.setReasonId(cursor.getString(cursor.getColumnIndex(Database.ValidReasons.COLUMN_NAME_REASON_ID)));
        return validReasons;
    }

    public static ValidSource cursorToValidSource(Cursor cursor) {
        ValidSource validSource = new ValidSource();
        validSource.setRefDataFacility(cursor.getString(cursor.getColumnIndex(Database.ValidSources.COLUMN_NAME_REFERENCE_DATA_FACILITY)));
        validSource.setNodeId(cursor.getString(cursor.getColumnIndex(Database.ValidSources.COLUMN_NAME_NODE_ID)));
        validSource.setReferenceId(cursor.getString(cursor.getColumnIndex(Database.ValidSources.COLUMN_NAME_NODE_REFERENCE_ID)));
        validSource.setName(cursor.getString(cursor.getColumnIndex(Database.ValidSources.COLUMN_NAME_NAME)));
        validSource.setIsFreeTextAllowed(cursor.getString(cursor.getColumnIndex(Database.ValidSources.COLUMN_NAME_FREE_TEXT_ALLOWED)));
        validSource.setFacilityTypeId(cursor.getString(cursor.getColumnIndex(Database.ValidSources.COLUMN_NAME_FACILITY_TYPE_ID)));
        validSource.setId(cursor.getString(cursor.getColumnIndex(Database.ValidSources.COLUMN_NAME_ID)));
        validSource.setProgramId(cursor.getString(cursor.getColumnIndex(Database.ValidSources.COLUMN_NAME_PROGRAM_ID)));
        return validSource;
    }

    public static ValidDestination cursorToValidDestinations(Cursor cursor) {
        ValidDestination validDestination = new ValidDestination();
        validDestination.setRefDataFacility(cursor.getString(cursor.getColumnIndex(Database.ValidDestinations.COLUMN_NAME_REFERENCE_DATA_FACILITY)));
        validDestination.setNodeId(cursor.getString(cursor.getColumnIndex(Database.ValidDestinations.COLUMN_NAME_NODE_ID)));
        validDestination.setReferenceId(cursor.getString(cursor.getColumnIndex(Database.ValidDestinations.COLUMN_NAME_NODE_REFERENCE_ID)));
        validDestination.setName(cursor.getString(cursor.getColumnIndex(Database.ValidDestinations.COLUMN_NAME_NAME)));
        validDestination.setIsFreeTextAllowed(cursor.getString(cursor.getColumnIndex(Database.ValidDestinations.COLUMN_NAME_FREE_TEXT_ALLOWED)));
        validDestination.setFacilityTypeId(cursor.getString(cursor.getColumnIndex(Database.ValidDestinations.COLUMN_NAME_FACILITY_TYPE_ID)));
        validDestination.setId(cursor.getString(cursor.getColumnIndex(Database.ValidDestinations.COLUMN_NAME_ID)));
        validDestination.setProgramId(cursor.getString(cursor.getColumnIndex(Database.ValidDestinations.COLUMN_NAME_PROGRAM_ID)));
        return validDestination;
    }

    public static StockEventLineItem cursorToStockEventLineItem(Cursor cursor) {
        StockEventLineItem stockEventLineItem = new StockEventLineItem();
        stockEventLineItem.setSourceId(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_SOURCE_ID)));
        stockEventLineItem.setDestinationFreeText(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_DESTINATION_FREE_TEXT)));
        stockEventLineItem.setReasonId(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_REASON_ID)));
        stockEventLineItem.setReasonFreeText(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_REASON_FREE_TEXT)));
        stockEventLineItem.setOccurredDate(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_OCCURRED_DATE)));
        stockEventLineItem.setProccessedDate(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_PROCCESSED_DATE)));
        stockEventLineItem.setLotId(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_LOT_ID)));
        stockEventLineItem.setOrderableId(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_ORDERABLE_ID)));
        stockEventLineItem.setProgramId(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_PROGRAM_ID)));
        stockEventLineItem.setFacilityId(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_FACILITY_ID)));
        stockEventLineItem.setDestinationId(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_DESTINATION_ID)));
        stockEventLineItem.setId(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_ID)));
        stockEventLineItem.setQuantity(cursor.getInt(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_QUANTITY)));
        stockEventLineItem.setStockEventId(cursor.getString(cursor.getColumnIndex(Database.StockEventLineItem.COLUMN_STOCK_EVENT_ID)));
        return stockEventLineItem;
    }

    public static StockCardLineItem cursorToStockCardLineItem(Cursor cursor) {
        StockCardLineItem stockCardLineItem = new StockCardLineItem();
        stockCardLineItem.setSourceId(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_SOURCE_ID)));
        stockCardLineItem.setDestinationFreeText(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_DESTINATION_FREE_TEXT)));
        stockCardLineItem.setReasonId(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_REASON_ID)));
        stockCardLineItem.setReasonFreeText(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_REASON_FREE_TEXT)));
        stockCardLineItem.setOccurredDate(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_OCCURRED_DATE)));
        stockCardLineItem.setProccessedDate(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_PROCCESSED_DATE)));
        stockCardLineItem.setStockcardId(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_STOCK_CARD_ID)));
        stockCardLineItem.setLotId(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_LOT_ID)));
        stockCardLineItem.setOrderableId(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_ORDERABLE_ID)));
        stockCardLineItem.setDestinationId(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_DESTINATION_ID)));
        stockCardLineItem.setId(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_ID)));
        stockCardLineItem.setQuantity(cursor.getInt(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_QUANTITY)));
        stockCardLineItem.setOriginEventId(cursor.getString(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_ORIGIN_EVENT_ID)));
        stockCardLineItem.setStockOnHand(cursor.getInt(cursor.getColumnIndex(Database.StockCardLineItem.COLUMN_STOCK_ON_HAND)));
        return stockCardLineItem;
    }
}
