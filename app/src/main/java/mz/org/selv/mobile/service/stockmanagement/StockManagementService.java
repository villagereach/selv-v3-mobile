package mz.org.selv.mobile.service.stockmanagement;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.DoubleAccumulator;

import mz.org.selv.mobile.database.Converter;
import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.model.auth.User;
import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.model.stockmanagement.CalculatedStockOnHand;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventory;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventoryLineItem;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventoryLineItemAdjustment;
import mz.org.selv.mobile.model.stockmanagement.Reason;
import mz.org.selv.mobile.model.stockmanagement.StockCard;
import mz.org.selv.mobile.model.stockmanagement.StockCardLineItem;
import mz.org.selv.mobile.model.stockmanagement.StockEvent;
import mz.org.selv.mobile.model.stockmanagement.StockEventLineItem;
import mz.org.selv.mobile.model.stockmanagement.ValidDestination;
import mz.org.selv.mobile.model.stockmanagement.ValidSource;
import mz.org.selv.mobile.service.auth.AuthService;
import mz.org.selv.mobile.service.openlmis.OlmisServiceCallback;
import mz.org.selv.mobile.service.openlmis.stockmanagement.OlmisStockManagementService;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;

public class StockManagementService  implements OlmisServiceCallback {

    private Context mContext;
    private ReferenceDataService referenceDataService;
    private AuthService authService;

    public StockManagementService(Context context) {
        this.mContext = context;
        authService = new AuthService(mContext);
    }


    public int saveInventory(PhysicalInventory physicalInventory, List<PhysicalInventoryLineItem> lineItems, List<PhysicalInventoryLineItemAdjustment> lineItemAdjustments) {

        int status = 0;
        if (validateInventoryDate(physicalInventory.getOccurredDate())) {

            Database database = new Database(mContext);
            database.open();
            database.beginTransaction();
            if (database.insert(physicalInventory) > 0) {

                if (database.insert(lineItems) > 0) {

                    if (lineItemAdjustments != null && lineItemAdjustments.size() > 0) {
                        if (database.insert(lineItemAdjustments) > 0) {
                            //save events
                            if (saveInventoryEvent(database, physicalInventory, lineItems)) {
                                database.setTransactionSuccessful();
                            }

                        } else {
                            database.endTransaction();
                            status = -1;
                        }
                    } else {
                        if (saveInventoryEvent(database, physicalInventory, lineItems)) {
                            database.setTransactionSuccessful();
                            status = 1;
                        } else {
                            // error saving events
                            status = -1;
                        }
                    }
                }
            }
            database.endTransaction();
            database.close();
        }
        return status;
    }

    public boolean saveInventoryEvent(Database database, PhysicalInventory physicalInventory, List<PhysicalInventoryLineItem> lineItems) {
        StockEvent stockEvent = new StockEvent();
        stockEvent.setId(physicalInventory.getId());
        stockEvent.setProgramId(physicalInventory.getProgramId());
        stockEvent.setFacilityId(physicalInventory.getFacilityId());
        stockEvent.setProcessedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (database.insert(stockEvent) > 0) {
            List<StockEventLineItem> eventLineItems = new ArrayList<>();
            for (int i = 0; i < lineItems.size(); i++) {
                StockEventLineItem stockEventLineItem = new StockEventLineItem();
                List<PhysicalInventoryLineItemAdjustment> adjustments = lineItems.get(i).getAdjustmentLineItems();
                //check if physical inventory have adjustments
                if (lineItems.get(i).getAdjustmentLineItems() != null && lineItems.get(i).getAdjustmentLineItems().size() > 0) {

                    for (int j = 0; j < adjustments.size(); j++) {
                        if (lineItems.get(i).getId().equals(adjustments.get(j).getPhysicalInventoryLineItemId())) {
                            stockEventLineItem = new StockEventLineItem();
                            stockEventLineItem.setLotId(lineItems.get(i).getLotId());
                            stockEventLineItem.setId(lineItems.get(i).getId());
                            stockEventLineItem.setOccurredDate(physicalInventory.getOccurredDate());
                            stockEventLineItem.setOrderableId(lineItems.get(i).getOrderableId());
                            stockEventLineItem.setQuantity(adjustments.get(j).getQuantity());
                            stockEventLineItem.setReasonId(adjustments.get(j).getReasonId());
                            stockEventLineItem.setStockEventId(stockEvent.getId());
                            eventLineItems.add(stockEventLineItem);
                        }
                    }
                } else {
                    stockEventLineItem.setLotId(lineItems.get(i).getLotId());
                    stockEventLineItem.setId(lineItems.get(i).getId());
                    stockEventLineItem.setOccurredDate(physicalInventory.getOccurredDate());
                    stockEventLineItem.setOrderableId(lineItems.get(i).getOrderableId());
                    stockEventLineItem.setQuantity(lineItems.get(i).getPhysicalStock());
                    stockEventLineItem.setStockEventId(stockEvent.getId());
                    eventLineItems.add(stockEventLineItem);
                }
            }
            if (database.insert(eventLineItems) > 0) {
                updateStockCards(database, stockEvent, eventLineItems);
                return true;
            }
        }
        return true;
    }

    public List<StockCardLineItem> updateStockCardLineItemsStockOnHand(Database database, StockCard stockCard, String date, int quantity, Reason reason) {
        List<StockCardLineItem> stockCardLineItems = getStockCardLineItemsByStockCard(database, stockCard, date, null);
        stockCardLineItems = orderStockCardLineItems(stockCardLineItems);
        for (int i = 0; i < stockCardLineItems.size(); i++) {
            if (reason.getType().equals("DEBIT")) {
                if ((stockCardLineItems.get(i).getStockOnHand() - quantity) > 0) {
                    stockCardLineItems.get(i).setStockOnHand(stockCardLineItems.get(i).getStockOnHand() - quantity);
                } else {
                    return null;
                }
            } else {
                stockCardLineItems.get(i).setStockOnHand(stockCardLineItems.get(i).getStockOnHand() + quantity);
            }
        }
        return stockCardLineItems;
    }

    public int saveStockCardLineItems(Database database, List<StockCardLineItem> stockCardLineItems) {
        for (int i = 0; 0 < stockCardLineItems.size(); i++) {
            int updated = database.update(StockCardLineItem.class, stockCardLineItems.get(i).getContentValues(), Database.StockCardLineItem.COLUMN_ID + "=?", new String[]{stockCardLineItems.get(i).getId()});
            if (updated < 0) {
                System.out.println("Stock Card Line Item not updated");
                return -1;
            }
        }
        return 1;
    }

    public boolean updateStockCards(Database database, StockEvent event, List<StockEventLineItem> lineItems) {

        Map<StockCard, List<StockCardLineItem>> mapStockCardLineItems = new HashMap<>();
        List<StockCard> stockCardList = new ArrayList<>();
        List<StockCardLineItem> stockCardLineItems = new ArrayList<>();
        Map<String, List<StockEventLineItem>> lineItemsMap = formatStockEventLineItems(lineItems);

        for (Map.Entry<String, List<StockEventLineItem>> entry : lineItemsMap.entrySet()) {
            StockCard stockCard = getStockCard(database, event.getFacilityId(), event.getProgramId(), entry.getKey());
            if (stockCard.getFacilityId() == null) {
                stockCard.setFacilityId(event.getFacilityId());
                stockCard.setLotId(entry.getKey());
                //set orderable id by getting the first event item
                stockCard.setOrderableId(entry.getValue().get(0).getOrderableId());
                stockCard.setProgramId(event.getProgramId());
                stockCard.setId(UUID.randomUUID().toString());
            }

            //get calculated stock on hand to update values

            //loop through line items
            for (int i = 0; i < entry.getValue().size(); i++) {
                StockCardLineItem stockCardLineItem = new StockCardLineItem();
                stockCardLineItem.setId(UUID.randomUUID().toString());
                stockCardLineItem.setOrderableId(entry.getValue().get(i).getOrderableId());
                stockCardLineItem.setQuantity(entry.getValue().get(i).getQuantity());
                stockCardLineItem.setOccurredDate(entry.getValue().get(i).getOccurredDate());
                stockCardLineItem.setLotId(entry.getValue().get(i).getLotId());
                stockCardLineItem.setStockcardId(stockCard.getId());
                stockCardLineItem.setExtraData("");
                stockCardLineItem.setOriginEventId(event.getId());
                stockCardLineItems.add(stockCardLineItem);
            }
            stockCardList.add(stockCard);
            mapStockCardLineItems.put(stockCard, stockCardLineItems);
        }


        if (database.insert(stockCardList) > 0) {
            if (database.insert(stockCardLineItems) > 0) {
                if (updateStockOnHand(database, mapStockCardLineItems)) {
                    //System.out.println("");
                } else {

                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean updateStockCard(Database database, StockCard stockCard, StockEvent event, StockEventLineItem stockEventLineItem, Reason reason) {

        //get calculated stock on hand to update values

        //loop through line items
        StockCardLineItem stockCardLineItem = new StockCardLineItem();
        stockCardLineItem.setId(UUID.randomUUID().toString());
        stockCardLineItem.setOrderableId(stockEventLineItem.getOrderableId());
        stockCardLineItem.setQuantity(stockEventLineItem.getQuantity());
        stockCardLineItem.setOccurredDate(stockEventLineItem.getOccurredDate());
        stockCardLineItem.setDestinationId(stockEventLineItem.getDestinationId());
        stockCardLineItem.setDestinationFreeText(stockEventLineItem.getDestinationFreeText());
        stockCardLineItem.setSourceId(stockEventLineItem.getSourceId());
        stockCardLineItem.setSourceFreeText(stockEventLineItem.getSourceFreeText());
        stockCardLineItem.setLotId(stockEventLineItem.getLotId());
        stockCardLineItem.setStockcardId(stockCard.getId());
        stockCardLineItem.setExtraData("");
        stockCardLineItem.setOriginEventId(event.getId());
        stockCardLineItem.setReasonId(stockEventLineItem.getReasonId());
        stockCardLineItem.setReasonFreeText(stockEventLineItem.getReasonFreeText());
        stockCardLineItem.setStockcardId(stockCard.getId());
        stockCardLineItem.setProccessedDate(event.getProcessedDate());

        //If the stock card have no quantity then its a new stock card
        int stockOnHand = getStockOnHand(database, stockCard, event.getOccurredDate());
        if (stockOnHand == 0) {
            stockCardLineItem.setStockOnHand(stockEventLineItem.getQuantity());
        } else {
            // update stock on hand
            if (reason.getType().equals("CREDIT")) {
                stockCardLineItem.setStockOnHand(stockEventLineItem.getQuantity() + stockOnHand);
            } else {
                stockCardLineItem.setStockOnHand(stockOnHand - stockEventLineItem.getQuantity());
            }
        }

        if (database.insert(stockCardLineItem) > 0) {
            System.out.println("stock card line item saved");
            //update stock card
            ContentValues cv = stockCard.getContentValues();
            cv.put(Database.StockCard.COLUMN_NAME_STOCK_ON_HAND, stockEventLineItem.getQuantity());
            database.update(StockCard.class, cv, Database.StockCard.COLUMN_NAME_ID + "=?", new String[]{stockCard.getId()});
            return true;
        } else {
            System.out.println("stock card line item not saved");
            return false;
        }
    }

    @SuppressLint("Range")
    public boolean updateStockOnHand(Database database, Map<StockCard, List<StockCardLineItem>> mapStockCardLineItems) {
        List<CalculatedStockOnHand> calculatedStockOnHandList = new ArrayList<>();
        for (Map.Entry<StockCard, List<StockCardLineItem>> entry : mapStockCardLineItems.entrySet()) {
            CalculatedStockOnHand calculatedStockOnHand = new CalculatedStockOnHand();
            int lastStockOnHand = 0;

            String lastStockOnHandId;
            for (int i = 0; i < entry.getValue().size(); i++) {
                Cursor cursorLastStockOnHandId = database.rawQuery("SELECT " + Database.CalculatedStockOnHand.COLUMN_ID + ", MAX(DATE( " + Database.CalculatedStockOnHand.COLUMN_OCCURRED_DATE + ")) " +
                        "FROM " + Database.CalculatedStockOnHand.TABLE_NAME + " WHERE DATE(" + Database.CalculatedStockOnHand.COLUMN_OCCURRED_DATE + ") <= DATE(?) AND " + Database.CalculatedStockOnHand.COLUMN_STOCK_CARD_ID +
                        "= ? GROUP BY " + Database.CalculatedStockOnHand.COLUMN_STOCK_CARD_ID, new String[]{entry.getValue().get(i).getOccurredDate(), entry.getKey().getId()});
                if (cursorLastStockOnHandId.getCount() > 0) {
                    cursorLastStockOnHandId.moveToFirst();
                    lastStockOnHandId = cursorLastStockOnHandId.getString(cursorLastStockOnHandId.getColumnIndex(Database.CalculatedStockOnHand.COLUMN_ID));
                    Cursor cursor = database.select(CalculatedStockOnHand.class, Database.CalculatedStockOnHand.COLUMN_ID + "=?", new String[]{lastStockOnHandId}, null, null, null);
                    cursor.moveToFirst();
                    lastStockOnHand = cursor.getInt(cursor.getColumnIndex(Database.CalculatedStockOnHand.COLUMN_STOCK_ON_HAND));
                } else {
                    //
                }

                if (entry.getValue().get(i).getReasonId() != null) { // if null is inventory

                    Cursor reasonCursor = database.select(Reason.class, Database.Reason.COLUMN_NAME_UUID + "=?", new String[]{entry.getValue().get(i).getReasonId()}, null, null, null);
                    if (reasonCursor.moveToFirst()) {
                        Reason reason = Converter.cursorToReason(reasonCursor);
                        if (reason.getType().equals("DEBIT")) {
                            lastStockOnHand = lastStockOnHand - entry.getValue().get(i).getQuantity();
                        } else {
                            lastStockOnHand = lastStockOnHand + entry.getValue().get(i).getQuantity();
                        }
                    }
                    reasonCursor.close();
                } else { // is inventory
                    lastStockOnHand = lastStockOnHand + entry.getValue().get(i).getQuantity();
                }

                cursorLastStockOnHandId.close();

            }
            calculatedStockOnHand.setStockOnHand(lastStockOnHand);
            calculatedStockOnHand.setStockCardId(entry.getKey().getId());
            calculatedStockOnHand.setOccuredDate(entry.getValue().get(0).getOccurredDate());
            calculatedStockOnHandList.add(calculatedStockOnHand);
        }

        if (database.insert(calculatedStockOnHandList) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Map formatStockEventLineItems(List<StockEventLineItem> lineItems) {
        List<String> lotIds = new ArrayList<>();
        for (int i = 0; i < lineItems.size(); i++) {
            if (!lotIds.contains(lineItems.get(i).getLotId())) {
                lotIds.add(lineItems.get(i).getLotId());
            }
        }

        //loopt through lots and not line items to get just 1 stock card

        Map<String, List<StockEventLineItem>> lineItemsByLotMap = new HashMap<>();
        for (int i = 0; i < lotIds.size(); i++) {
            List<StockEventLineItem> lineItemsByLot = new ArrayList<>();
            for (int j = 0; j < lineItems.size(); j++) {
                if (lineItems.get(j).getLotId().equals(lotIds.get(i))) {
                    lineItemsByLot.add(lineItems.get(j));
                }
            }
            lineItemsByLotMap.put(lotIds.get(i), lineItemsByLot);
        }
        return lineItemsByLotMap;
    }

    public Map formatStockCardLineItems(List<StockCardLineItem> lineItems) {
        List<String> stockCardIds = new ArrayList<>();
        for (int i = 0; i < lineItems.size(); i++) {
            if (!stockCardIds.contains(lineItems.get(i).getStockcardId())) {
                stockCardIds.add(lineItems.get(i).getStockcardId());
            }
        }

        //loopt through lots and not line items to get just 1 stock card

        Map<String, List<StockCardLineItem>> lineItemsByStockCardMap = new HashMap<>();
        for (int i = 0; i < stockCardIds.size(); i++) {
            List<StockCardLineItem> lineItemsByLot = new ArrayList<>();
            for (int j = 0; j < lineItems.size(); j++) {
                if (lineItems.get(j).getLotId().equals(stockCardIds.get(i))) {
                    lineItemsByLot.add(lineItems.get(j));
                }
            }
            lineItemsByStockCardMap.put(stockCardIds.get(i), lineItemsByLot);
        }
        return lineItemsByStockCardMap;
    }


    public StockCard getStockCard(Database database, String facilityId, String programId, String lotId) {
        StockCard stockCard = new StockCard();
        Cursor cursor = database.select(StockCard.class, Database.StockCard.COLUMN_NAME_FACILITY_ID + "=? AND " + Database.StockCard.COLUMN_NAME_PROGRAM_ID + "=? AND " + Database.StockCard.COLUMN_NAME_LOT_ID + "=?",
                new String[]{facilityId, programId, lotId}, null, null, null);
        if (cursor.moveToFirst()) {
            stockCard = Converter.cursorToStockCard(cursor);
        }
        cursor.close();
        return stockCard;
    }

    public StockCard getOrCreateStockCard(String facilityId, String programId, String lotId, String orderableId) {
        StockCard stockCard;
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(StockCard.class, Database.StockCard.COLUMN_NAME_FACILITY_ID + "=? AND " + Database.StockCard.COLUMN_NAME_PROGRAM_ID + "=? AND " + Database.StockCard.COLUMN_NAME_LOT_ID + "=?",
                new String[]{facilityId, programId, lotId}, null, null, null);
        if (cursor.moveToFirst()) {
            stockCard = Converter.cursorToStockCard(cursor);
        } else {
            stockCard = new StockCard();
            stockCard.setId(UUID.randomUUID().toString());
            stockCard.setFacilityId(facilityId);
            stockCard.setLotId(lotId);
            stockCard.setProgramId(programId);
            stockCard.setOrderableId(orderableId);
            stockCard.setStockOnHand(-1);
            database.insert(stockCard);
        }
        database.close();
        cursor.close();
        return stockCard;
    }

    public List<JSONArray> formatStockCard(List<StockEventLineItem> eventLineItems) {
        JSONArray stockCardsJSON = new JSONArray();
        //  List
        for (int i = 0; i < eventLineItems.size(); i++) {

        }
        return null;
    }

    public boolean validateInventoryDate(String date) {
        return true;
    }

    public List<PhysicalInventory> getInventories(String programId, String facilityId) {
        Database database = new Database(mContext);
        List<PhysicalInventory> inventories = new ArrayList<>();
        database.open();
        Cursor cursor = database.select(PhysicalInventory.class, Database.PhysicalInventory.COLUMN_PROGRAM_ID + "=? AND " + Database.PhysicalInventory.COLUMN_FACILITY_ID + "=?",
                new String[]{programId, facilityId}, null, null, null);
        while (cursor.moveToNext()) {
            inventories.add(Converter.cursorToInventory(cursor));
        }
        cursor.close();
        database.close();
        return inventories;
    }

    public List<StockCard> getStockCards(String programId, String facilityId) {
        Database database = new Database(mContext);
        List<StockCard> stockCards = new ArrayList<>();
        database.open();
        Cursor cursor = database.select(StockCard.class, Database.StockCard.COLUMN_NAME_PROGRAM_ID + "=? AND " + Database.StockCard.COLUMN_NAME_FACILITY_ID + "=?", new String[]{programId, facilityId},
                null, null, null);
        while (cursor.moveToNext()) {
            stockCards.add(Converter.cursorToStockCard(cursor));
        }
        cursor.close();
        database.close();
        return stockCards;
    }

    public StockCard getStockCard(String programId, String facilityId, String lotId) {

        return null;
    }

    public List<JSONObject> getNewInventoryLineItem(String programId, String facilityId) {
        List<StockCard> stockCards = getStockCards(programId, facilityId);
        List<JSONObject> inventoryLineItems = new ArrayList<>();
        referenceDataService = new ReferenceDataService(mContext);
        for (int i = 0; i < stockCards.size(); i++) {
            JSONObject inventoryLineItem = new JSONObject();
            Lot lot = referenceDataService.getLotById(stockCards.get(i).getLotId());
            int stockOnHand = getStockOnHand(stockCards.get(i).getId(), null);
            try {
                inventoryLineItem.put("orderableName", referenceDataService.getOrderableById(stockCards.get(i).getOrderableId()).getName());
                inventoryLineItem.put("lotCode", lot.getLotCode());
                inventoryLineItem.put("expirationDate", lot.getExpirationDate());
                inventoryLineItem.put("physicalStock", "");

                if (stockOnHand >= 0) {
                    inventoryLineItem.put("stockOnHand", stockOnHand);
                } else {
                    inventoryLineItem.put("stockOnHand", "");
                }

                inventoryLineItems.add(inventoryLineItem);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        return inventoryLineItems;
    }

    public int getStockOnHand(String stockCardId, String date) {

        SimpleDateFormat dateFormar = new SimpleDateFormat("dd-MM-yyyy");

        CalculatedStockOnHand lastStockOnHand = new CalculatedStockOnHand();
        if (date == null) {
            Database database = new Database(mContext);
            database.open();
            Cursor cursor = database.select(CalculatedStockOnHand.class, Database.CalculatedStockOnHand.COLUMN_STOCK_CARD_ID + "=?", new String[]{stockCardId}, null, null, null);

            if (cursor.getCount() > 0) {
                try {

                    Date maxDate = dateFormar.parse("2000-01-01");

                    while (cursor.moveToNext()) {
                        CalculatedStockOnHand calculatedStockOnHand = Converter.cursorToCalculatedStockOnHand(cursor);
                        Date occurredDate = dateFormar.parse(calculatedStockOnHand.getOccuredDate());

                        if (occurredDate.after(maxDate) || occurredDate.equals(maxDate)) {
                            maxDate = occurredDate;
                            lastStockOnHand = calculatedStockOnHand;
                        }
                    }

                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                cursor.close();
                database.close();
            } else {
                System.out.println("No stock on hand information");
            }

        } else {

        }

        if (lastStockOnHand.getStockCardId() != null) {
            System.out.println("CurrentStockOnHand" + lastStockOnHand.getStockOnHand());
            return lastStockOnHand.getStockOnHand();
        } else {
            return -1;
        }
    }

    public ValidSource getValidSourceByName(String facilityId, String programId, String sourceName) {
        Database database = new Database(mContext);
        database.open();
        ValidSource validSource;
        Cursor cursor = database.select(ValidSource.class, Database.ValidSources.COLUMN_NAME_FACILITY_ID + "=? AND " + Database.ValidSources.COLUMN_NAME_PROGRAM_ID + "= ? AND " +
                Database.ValidSources.COLUMN_NAME_NAME + "=? ", new String[]{facilityId, programId, sourceName}, null, null, null);
        if (cursor.moveToFirst()) {
            validSource = Converter.cursorToValidSource(cursor);
        } else {

            return null;
        }

        cursor.close();
        database.close();
        return validSource;
    }

    public ValidSource getValidSourceById(String validSourceId) {
        Database database = new Database(mContext);
        database.open();
        ValidSource validSource = new ValidSource();
        Cursor cursor = database.select(ValidSource.class, Database.ValidSources.COLUMN_NAME_ID + "=?", new String[]{validSourceId}, null, null, null);
        if (cursor.moveToFirst()) {
            validSource = Converter.cursorToValidSource(cursor);
        }
        cursor.close();
        database.close();
        return validSource;
    }

    public ValidDestination getValidDestinationByName(String facilityId, String programId, String sourceName) {
        Database database = new Database(mContext);
        database.open();
        ValidDestination validDestination;
        Cursor cursor = database.select(ValidDestination.class, Database.ValidSources.COLUMN_NAME_FACILITY_ID + "=? AND " + Database.ValidDestinations.COLUMN_NAME_PROGRAM_ID + "= ? AND " +
                Database.ValidDestinations.COLUMN_NAME_NAME + "=? ", new String[]{facilityId, programId, sourceName}, null, null, null);
        if (cursor.moveToFirst()) {
            validDestination = Converter.cursorToValidDestinations(cursor);
        } else {
            return null;
        }

        cursor.close();
        database.close();
        return validDestination;
    }

    public ValidDestination getValidDestinationById(String validDestinationId) {
        Database database = new Database(mContext);
        database.open();
        ValidDestination validDestination;
        Cursor cursor = database.select(ValidDestination.class, Database.ValidSources.COLUMN_NAME_ID + " = ? ", new String[]{validDestinationId}, null, null, null);
        if (cursor.moveToFirst()) {
            validDestination = Converter.cursorToValidDestinations(cursor);
        } else {
            return null;
        }

        cursor.close();
        database.close();
        return validDestination;
    }


    public int saveEvent(String action, String facilityId, String programId, String orderableName, String lotCode, String validSourceOrDestinationName, String sourceDestinationComments, String reasonName, String reasonComments,
                         int quantity, String vvm, String occurredDate) {

        if (action.equals("receive")) {
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Reason reason = new ReferenceDataService(mContext).getReasonByName(reasonName);
            ValidSource validSource = getValidSourceByName(facilityId, programId, validSourceOrDestinationName);
            referenceDataService = new ReferenceDataService(mContext);
            Orderable orderable = referenceDataService.getOrderableByName(orderableName);
            Lot lot = referenceDataService.getLotByCode(lotCode);
            Map validateEventStatus = validateEvent(action, orderable.getUuid(), lot.getId(), facilityId, programId, occurredDate, reason, quantity);
            if (validateEventStatus.get("status").equals("1")) {
                StockCard stockCard = getOrCreateStockCard(facilityId, programId, lot.getId(), orderable.getUuid());
                StockEvent event = new StockEvent();
                event.setType(action);
                event.setFacilityId(facilityId);
                event.setProgramId(programId);
                event.setId(UUID.randomUUID().toString());
                event.setOccurredDate(occurredDate);
                event.setProcessedDate(datetimeFormat.format(new Date()));
                StockEventLineItem eventLineItem = new StockEventLineItem();
                eventLineItem.setId(UUID.randomUUID().toString());
                eventLineItem.setReasonId(reason.getId());
                eventLineItem.setOrderableId(orderable.getUuid());
                eventLineItem.setQuantity(quantity);
                eventLineItem.setLotId(lot.getId());
                eventLineItem.setOccurredDate(occurredDate);
                eventLineItem.setSourceFreeText(sourceDestinationComments);
                eventLineItem.setReasonFreeText(reasonComments);
                eventLineItem.setSourceId(validSource.getId());
                eventLineItem.setProgramId(programId);
                eventLineItem.setStockEventId(event.getId());
                eventLineItem.setStockCardId(stockCard.getId());
                eventLineItem.setProccessedDate(event.getProcessedDate());
                Database database = new Database(mContext);
                database.open();
                if (database.insert(event) > 0) {
                    if (database.insert(eventLineItem) > 0) {
                        System.out.println("event line item saved");
                        updateStockCard(database, stockCard, event, eventLineItem, reason);
                        database.close();
                        return 1;
                    } else {
                        //delete rows
                    }
                } else {
                    System.out.println("event not saved");
                    return -2;
                }

            } else {
                return -1;
            }

        } else if (action.equals("issue")) {
            SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Reason reason = new ReferenceDataService(mContext).getReasonByName(reasonName);
            ValidDestination validDestination = getValidDestinationByName(facilityId, programId, validSourceOrDestinationName);
            referenceDataService = new ReferenceDataService(mContext);
            Orderable orderable = referenceDataService.getOrderableByName(orderableName);
            Lot lot = referenceDataService.getLotByCode(lotCode);
            Map validateEventStatus = validateEvent(action, orderable.getUuid(), lot.getId(), facilityId, programId, occurredDate, reason, quantity);
            if (validateEventStatus.get("status").equals("1")) {
                StockCard stockCard = getStockCard(facilityId, programId, orderable.getUuid(), lot.getId());
                StockEvent event = new StockEvent();
                event.setType(action);
                event.setFacilityId(facilityId);
                event.setProgramId(programId);
                event.setId(UUID.randomUUID().toString());
                event.setOccurredDate(occurredDate);
                event.setProcessedDate(datetimeFormat.format(new Date()));
                StockEventLineItem eventLineItem = new StockEventLineItem();
                eventLineItem.setId(UUID.randomUUID().toString());
                eventLineItem.setReasonId(reason.getId());
                eventLineItem.setOrderableId(orderable.getUuid());
                eventLineItem.setQuantity(quantity);
                eventLineItem.setLotId(lot.getId());
                eventLineItem.setOccurredDate(occurredDate);
                eventLineItem.setSourceFreeText(sourceDestinationComments);
                eventLineItem.setReasonFreeText(reasonComments);
                eventLineItem.setSourceId(validDestination.getId());
                eventLineItem.setProgramId(programId);
                eventLineItem.setStockEventId(event.getId());
                eventLineItem.setStockCardId(stockCard.getId());
                eventLineItem.setProccessedDate(event.getProcessedDate());
                eventLineItem.setFacilityId(facilityId);
                Database database = new Database(mContext);
                database.open();
                if (database.insert(event) > 0) {
                    if (database.insert(eventLineItem) > 0) {
                        System.out.println("Event issue line item saved");
                        updateStockCard(database, stockCard, event, eventLineItem, reason);
                        database.close();
                        return 1;
                    } else {
                        //delete rows
                    }
                } else {
                    System.out.println("event issue not saved");
                    return -2;
                }

            } else {
                return -1;
            }
        }
        return 0;
    }

    public Map validateEvent(String action, String orderableId, String lotId, String facilityId, String programId, String date, Reason reason, int quantity) {
        Map<String, String> result = new HashMap<>();

        //prevent event if inventory exists before event date
        Database database = new Database(mContext);
        database.open();
        Cursor cursor = database.select(StockEvent.class, Database.StockEvent.COLUMN_NAME_PROGRAM_ID + "= ? ANd  " + Database.StockEvent.COLUMN_NAME_TYPE + "= ? AND " + Database.StockEvent.COLUMN_NAME_FACILITY_ID
                + " = ?", new String[]{programId, "inventory", facilityId}, null, null, null);
        if (cursor.getCount() > 0) { //inventory exists
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date lastInventoryDate = new Date();
            try {
                while (cursor.moveToNext()) {
                    StockEvent stockEvent = Converter.cursorToStockEvent(cursor);
                    Date inventoryDate = simpleDateFormat.parse(stockEvent.getOccurredDate());
                    // check if the receive date is greater than the inventory date
                    if (cursor.getPosition() == 0) {
                        lastInventoryDate = inventoryDate;
                    }

                    if (lastInventoryDate.before(inventoryDate)) {
                        lastInventoryDate = inventoryDate;
                    }

                }
                Date eventDate = simpleDateFormat.parse(date);
                if (eventDate.before(lastInventoryDate)) {
                    result.put("status", "1");
                    result.put("message", "success");
                } else {
                    result.put("status", "-1");
                    result.put("message", "O evento nao pode ser submetido, existe um indetario datado de: " + simpleDateFormat.format(lastInventoryDate));

                }
            } catch (ParseException ex) {
                result.put("status", "-1");
                result.put("message", "ocorreu um erro");
                ex.printStackTrace();
            }
        } else {
            if (action.equals("issue")) {
                StockCard stockCard = getStockCard(facilityId, programId, orderableId, lotId);
                if (stockCard != null) {
                    List<StockCardLineItem> stockCardLineItems = getStockCardLineItemsByStockCard(database, stockCard, date, null);
                    if (stockCardLineItems.size() > 0) {
                        stockCardLineItems = orderStockCardLineItems(stockCardLineItems);

                        int stockOnHand = stockCardLineItems.get(0).getStockOnHand();
                        for(int i = 0; i < stockCardLineItems.size(); i++){
                            stockOnHand = stockOnHand - quantity;
                            if(stockOnHand < 0){
                                result.put("status", "-1");
                                result.put("message", "Este meovimento torna o stock disponivel negativo no dia "+stockCardLineItems.get(i).getOccurredDate());
                                System.out.println(result);
                                return result;
                            }
                        }

                    } else {
                        result.put("status", "1");
                        result.put("message", "");
                        System.out.println(result);
                    }
                }
                //
            } else if (action.equals("adjustment")) {
                int stockOnHand = getStockOnHand(orderableId, lotId, facilityId, programId, date);
            } else if (action.equals("receive")) {
                result.put("status", "1");
                result.put("message", "");
            }
        }
        return result;
    }


    public int getStockOnHand(String orderableId, String lotId, String facilityId, String programId, String date) {
        Database database = new Database(mContext);
        StockCard stockCard;
        database.open();
        Cursor stockCardCursor = database.select(StockCard.class, Database.StockCard.COLUMN_NAME_ORDERABLE_ID + " = ? " + Database.StockCard.COLUMN_NAME_FACILITY_ID + "=? AND " + Database.StockCard.COLUMN_NAME_LOT_ID + "" +
                "=? AND " + Database.StockCard.COLUMN_NAME_PROGRAM_ID + " = ?", new String[]{orderableId, facilityId, lotId, programId}, null, null, null);

        Cursor stockCardLineItems = database.select(CalculatedStockOnHand.class, Database.CalculatedStockOnHand.COLUMN_ORDERABLE_ID + "=? AND " +
                        Database.CalculatedStockOnHand.COLUMN_NAME_FACILITY_ID + "=? AND " + Database.CalculatedStockOnHand.COLUMN_NAME_PROGRAM_ID + " =? AND " +
                        Database.CalculatedStockOnHand.COLUMN_LOT_ID + "=? AND " + Database.CalculatedStockOnHand.COLUMN_OCCURRED_DATE + " <= ? ", new String[]{orderableId, facilityId, programId, lotId, date},
                null, null, Database.CalculatedStockOnHand.COLUMN_OCCURRED_DATE + " DESC");
        return 0;
    }

    public int getStockOnHand(Database database, StockCard stockCard, String date) {
        int stockOnHand = 0;
        List<StockCardLineItem> stockCardLineItems = getStockCardLineItemsByStockCard(database, stockCard, null, date);
        if (stockCardLineItems.size() > 0) {
            stockCardLineItems = orderStockCardLineItems(stockCardLineItems);
            stockOnHand = stockCardLineItems.get(0).getStockOnHand();
        }
        return stockOnHand;
    }

    public List getEventLineItemsByInterval(String orderableId, String lotId, String programId, String facilityId, String startDate, String endDate) {
        Database database = new Database(mContext);
        database.open();
        List<StockEventLineItem> stockEventLineItems = new ArrayList<>();
        Cursor cursor;
        if (startDate != null && endDate == null) {
            cursor = database.select(StockEventLineItem.class, Database.StockEventLineItem.COLUMN_FACILITY_ID + "=? AND " + Database.StockEventLineItem.COLUMN_PROGRAM_ID + "=? AND " +
                            Database.StockEventLineItem.COLUMN_ORDERABLE_ID + " = ? AND " + Database.StockEventLineItem.COLUMN_LOT_ID + " = ? " + Database.StockEventLineItem.COLUMN_OCCURRED_DATE + " > ?",
                    new String[]{facilityId, programId, orderableId, lotId, startDate}, null, null, Database.StockEventLineItem.COLUMN_OCCURRED_DATE + ",  " +
                            Database.StockEventLineItem.COLUMN_PROCCESSED_DATE + " ASC");

            return stockEventLineItems;
        } else if (startDate == null) {
            return stockEventLineItems;
        } else {
            cursor = database.select(StockEventLineItem.class, Database.StockEventLineItem.COLUMN_FACILITY_ID + "=? AND " + Database.StockEventLineItem.COLUMN_PROGRAM_ID + "=? AND " +
                            Database.StockEventLineItem.COLUMN_ORDERABLE_ID + " = ? AND " + Database.StockEventLineItem.COLUMN_LOT_ID + " = ? AND " + Database.StockEventLineItem.COLUMN_OCCURRED_DATE + " > ?",
                    new String[]{facilityId, programId, orderableId, lotId, startDate}, null, null, null);
        }

        if (cursor.moveToNext()) {
            stockEventLineItems.add(Converter.cursorToStockEventLineItem(cursor));
        }
        cursor.close();
        database.close();
        return stockEventLineItems;
    }

    public List<StockEvent> getStockEventByType(String facilityId, String programId, String action) {
        Database database = new Database(mContext);
        List<StockEvent> stockEvents = new ArrayList<StockEvent>();
        database.open();

        Cursor cursor = database.select(StockEvent.class, Database.StockEvent.COLUMN_NAME_FACILITY_ID + "= ? AND " + Database.StockEvent.COLUMN_NAME_PROGRAM_ID + "= ? AND "+Database.StockEvent.COLUMN_NAME_TYPE+
                        " = ? ",
                new String[]{facilityId, programId, action}, null, null, Database.StockEvent.COLUMN_OCCURRED_DATE + ", " + Database.StockEvent.COLUMN_PROCESSED_DATE + " DESC");
        while (cursor.moveToNext()) {
            stockEvents.add(Converter.cursorToStockEvent(cursor));
        }
        cursor.close();
        database.close();
        return stockEvents;
    }

    public List<StockEventLineItem> getStockEventLineItems(StockEvent stockEvent) {
        StockEventLineItem stockEventLineItem;
        Database database = new Database(mContext);
        List<StockEventLineItem> stockEventLineItemList = new ArrayList<>();
        database.open();
        Cursor cursor = database.select(StockEventLineItem.class, Database.StockEventLineItem.COLUMN_STOCK_EVENT_ID + "=?", new String[]{stockEvent.getId()}, null, null, null);
        while (cursor.moveToNext()) {
            stockEventLineItem = Converter.cursorToStockEventLineItem(cursor);
            stockEventLineItemList.add(stockEventLineItem);
        }
        cursor.close();
        database.close();
        return stockEventLineItemList;
    }

    public List<JSONObject> getStockEventLineItems(String facilityId, String programId, String action) {
        List<StockEvent> stockEvents = getStockEventByType(facilityId, programId, action);
        List<JSONObject> eventLineItemsJson = new ArrayList<>();
        ReferenceDataService referenceDataService = new ReferenceDataService(mContext);
        for (int i = 0; i < stockEvents.size(); i++) {
            List<StockEventLineItem> eventLineItemList = getStockEventLineItems(stockEvents.get(i));
            for (int j = 0; j < eventLineItemList.size(); j++) {
                JSONObject eventItem = new JSONObject();
                try {
                    eventItem.put("orderableName", referenceDataService.getOrderableById(eventLineItemList.get(j).getOrderableId()).getName());
                    eventItem.put("lotCode", referenceDataService.getLotById(eventLineItemList.get(j).getLotId()).getLotCode());
                    eventItem.put("expirationDate", referenceDataService.getLotById(eventLineItemList.get(j).getLotId()).getExpirationDate());
                    eventItem.put("reasonName", referenceDataService.getReasonById(eventLineItemList.get(j).getReasonId()).getName());
                    eventItem.put("reasonComments", referenceDataService.getLotById(eventLineItemList.get(j).getLotId()).getLotCode());
                    if (action.equals("receive")) {
                        eventItem.put("sourceOrDestinationName", getValidSourceById(eventLineItemList.get(j).getSourceId()).getName());
                        eventItem.put("sourceOrDestinationComments", eventLineItemList.get(j).getSourceFreeText());
                    } else if (action.equals("issue")) {
                        eventItem.put("sourceOrDestinationName", getValidDestinationById(eventLineItemList.get(j).getDestinationId()).getName());
                        eventItem.put("sourceOrDestinationComments", eventLineItemList.get(j).getDestinationFreeText());
                    } else if (action.equals("adjustment")) {

                    }

                    eventItem.put("quantity", eventLineItemList.get(j).getQuantity());
                    //eventItem.put("stockOnHand", "");
                    eventItem.put("status", stockEvents.get(i).getStatus());
                    eventItem.put("occurredDate", eventLineItemList.get(j).getOccurredDate());
                    eventLineItemsJson.add(eventItem);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }
        }
        System.out.println(eventLineItemsJson.toString());
        return eventLineItemsJson;
    }

    public List<Orderable> getAvailableOrderables(String programId, String facilityId) {
        Database database = new Database(mContext);
        List<Orderable> orderables = new ArrayList<>();
        database.open();
        System.out.println(programId);
        System.out.println(facilityId);

        Cursor cursorStockCards = database.select(StockCard.class, Database.StockCard.COLUMN_NAME_FACILITY_ID + "= ? AND " + Database.StockCard.COLUMN_NAME_PROGRAM_ID + "= ? AND " +
                        Database.StockCard.COLUMN_NAME_STOCK_ON_HAND + " > 0 ",
                new String[]{facilityId, programId}, null, null, null);
        while (cursorStockCards.moveToNext()) {
            String orderableId = Converter.cursorToStockCard(cursorStockCards).getOrderableId();
            Cursor cursorOrderable = database.select(Orderable.class, Database.Orderable.COLUMN_UUID + "=?", new String[]{orderableId}, null, null, null);
            cursorOrderable.moveToFirst();
            orderables.add(Converter.cursorToOrderable(cursorOrderable));
            cursorOrderable.close();
        }
        cursorStockCards.close();
        database.close();
        return orderables;
    }

    public List<Lot> getAvailableLotsByName(String orderableName, String facilityId, String programId) {
        Database database = new Database(mContext);
        List<Lot> lots = new ArrayList<>();
        database.open();
        Cursor cursorOrdrable = database.select(Orderable.class, Database.Orderable.COLUMN_NAME + " = ?", new String[]{orderableName}, null, null, null);
        cursorOrdrable.moveToFirst();
        Orderable orderable = Converter.cursorToOrderable(cursorOrdrable);
        Cursor cursorStockCards = database.select(StockCard.class, Database.StockCard.COLUMN_NAME_ORDERABLE_ID + "=? AND " + Database.StockCard.COLUMN_NAME_FACILITY_ID + "= ? AND " +
                        Database.StockCard.COLUMN_NAME_PROGRAM_ID + "= ? AND " + Database.StockCard.COLUMN_NAME_STOCK_ON_HAND + " > 0 ",
                new String[]{orderable.getUuid(), facilityId, programId}, null, null, null);
        while (cursorStockCards.moveToNext()) {
            String lotId = Converter.cursorToStockCard(cursorStockCards).getLotId();
            Cursor cursorLot = database.select(Lot.class, Database.Lot.COLUMN_UUID + "=?", new String[]{lotId}, null, null, null);
            cursorLot.moveToFirst();
            lots.add(Converter.cursorToLot(cursorLot));
            cursorLot.close();
        }
        cursorStockCards.close();
        database.close();
        return lots;
    }

    public List<Map<String, String>> getAvailableLotsAndStockOnHandByOrderableName(String orderableName, String facilityId, String programId, String action) {
        Database database = new Database(mContext);
        List<Lot> lots = new ArrayList<>();
        database.open();
        List<Map<String, String>> result = new ArrayList<>();
        Cursor cursorOrderable = database.select(Orderable.class, Database.Orderable.COLUMN_NAME + " = ?", new String[]{orderableName}, null, null, null);
        cursorOrderable.moveToFirst();
        Orderable orderable = Converter.cursorToOrderable(cursorOrderable);
        cursorOrderable.close();

        Cursor cursorStockCards = database.select(StockCard.class, Database.StockCard.COLUMN_NAME_ORDERABLE_ID + "=? AND " + Database.StockCard.COLUMN_NAME_FACILITY_ID + "= ? AND " +
                        Database.StockCard.COLUMN_NAME_PROGRAM_ID + "= ? ",
                new String[]{orderable.getUuid(), facilityId, programId}, null, null, null);

        while (cursorStockCards.moveToNext()) {
            StockCard stockCard = Converter.cursorToStockCard(cursorStockCards);
            Cursor cursorLot = database.select(Lot.class, Database.Lot.COLUMN_UUID + "= ? ", new String[]{stockCard.getLotId()}, null, null, null);
            cursorLot.moveToFirst();
            Lot lot = Converter.cursorToLot(cursorLot);
            int stockOnHand = getStockOnHand(database, stockCard, null);
            Map<String, String> lotAndStockOnHand = new HashMap<>();
            lotAndStockOnHand.put("code", lot.getLotCode());
            lotAndStockOnHand.put("stockOnHand", stockOnHand + "");
            lotAndStockOnHand.put("expirationDate", lot.getExpirationDate());
            lots.add(Converter.cursorToLot(cursorLot));
            cursorLot.close();

            if ((action.equals("issue") && stockOnHand > 0) || !action.equals("issue")) {
                result.add(lotAndStockOnHand);
            }
        }
        cursorStockCards.close();
        database.close();
        return result;
    }

    public StockCard getStockCard(String facilityId, String programId, String orderableId, String lotId) {
        System.out.println("facilityId" + facilityId + " " + programId + " " + orderableId + " " + lotId);
        Database database = new Database(mContext);
        StockCard stockCard;
        database.open();
        Cursor cursor = database.select(StockCard.class, Database.StockCard.COLUMN_NAME_FACILITY_ID + " = ? AND " + Database.StockCard.COLUMN_NAME_PROGRAM_ID + "=? AND " +
                Database.StockCard.COLUMN_NAME_ORDERABLE_ID + "= ? AND " + Database.StockCard.COLUMN_NAME_LOT_ID + "= ? ", new String[]{facilityId, programId, orderableId, lotId}, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println("stock card found");
            stockCard = Converter.cursorToStockCard(cursor);
            cursor.close();
            database.close();
            return stockCard;
        } else { // no stock card found
            System.out.println("no stock card");
            cursor.close();
            database.close();
            return null;
        }
    }

    public List getStockCardLineItemsByStockCard(Database database, StockCard stockCard, String startDate, String endDate) {
        List<StockCardLineItem> stockCardLineItems = new ArrayList<>();

        Cursor cursor;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cursor = database.select(StockCardLineItem.class, Database.StockCardLineItem.COLUMN_STOCK_CARD_ID + "=?", new String[]{stockCard.getId()}, null, null, null);

        while (cursor.moveToNext()) {
            StockCardLineItem stockCardLineItem = Converter.cursorToStockCardLineItem(cursor);

            try {
                if (startDate != null && endDate == null) { // bring all after start date
                    if (dateFormat.parse(stockCardLineItem.getOccurredDate()).after(dateFormat.parse(startDate)) ||
                            dateFormat.parse(startDate).equals(dateFormat.parse(stockCardLineItem.getOccurredDate()))) {
                        stockCardLineItems.add(stockCardLineItem);
                    }
                } else if (startDate == null && endDate != null) {
                    if (dateFormat.parse(stockCardLineItem.getOccurredDate()).before(dateFormat.parse(endDate)) ||
                            dateFormat.parse(endDate).equals(dateFormat.parse(stockCardLineItem.getOccurredDate()))) {
                        stockCardLineItems.add(stockCardLineItem);
                    }
                } else if (startDate != null && endDate != null) {
                    if (
                            (dateFormat.parse(stockCardLineItem.getOccurredDate()).after(dateFormat.parse(startDate)) ||
                                    dateFormat.parse(stockCardLineItem.getOccurredDate()).equals(dateFormat.parse(startDate))) &&
                                    (dateFormat.parse(stockCardLineItem.getOccurredDate()).before(dateFormat.parse(endDate)) ||
                                            dateFormat.parse(stockCardLineItem.getOccurredDate()).equals(dateFormat.parse(endDate)))) {
                        stockCardLineItems.add(stockCardLineItem);
                    }
                } else {
                    stockCardLineItems.add(stockCardLineItem);
                }

            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        cursor.close();
        return stockCardLineItems;
    }

    public List orderStockCardLineItems(List stockCardLineItems) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Collections.sort(stockCardLineItems, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                StockCardLineItem sc1 = (StockCardLineItem) o1;
                StockCardLineItem sc2 = (StockCardLineItem) o2;

                try {
                    Date date1 = dateFormat.parse(sc1.getOccurredDate());
                    Date date2 = dateFormat.parse(sc2.getOccurredDate());
                    if (date1.before(date2)) {
                        return 1;
                    } else if (date1.after(date2)) {
                        return -1;
                    } else {
                        Date proccessedDate1 = dateTimeFormat.parse(sc1.getProccessedDate());
                        Date proccessedDate2 = dateTimeFormat.parse(sc2.getProccessedDate());
                        if (proccessedDate1.before(proccessedDate2)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                } catch (ParseException exception) {
                    exception.printStackTrace();
                }
                return 0;
            }

        });
        return stockCardLineItems;
    }

    public List<StockCard> getStockCardsFromServer(String server, String facilityId, String programId){

        OlmisStockManagementService olmisStockManagementService = new OlmisStockManagementService(mContext, this);
        olmisStockManagementService.getStockCardSummaries(server, facilityId, programId);
        return null;
    }




    @Override
    public void onTokenRefreshed(int response) {

    }

    @Override
    public void onUserLoggedIn(JSONObject response) {

    }

    @Override
    public void onStockCardSummariesResponse(JSONObject stockCardSummaries) {

    }
}
