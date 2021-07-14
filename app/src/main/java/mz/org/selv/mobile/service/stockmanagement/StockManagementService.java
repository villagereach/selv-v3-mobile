package mz.org.selv.mobile.service.stockmanagement;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mz.org.selv.mobile.database.Converter;
import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.stockmanagement.CalculatedStockOnHand;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventory;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventoryLineItem;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventoryLineItemAdjustment;
import mz.org.selv.mobile.model.stockmanagement.Reason;
import mz.org.selv.mobile.model.stockmanagement.StockCard;
import mz.org.selv.mobile.model.stockmanagement.StockCardLineItem;
import mz.org.selv.mobile.model.stockmanagement.StockEvent;
import mz.org.selv.mobile.model.stockmanagement.StockEventLineItem;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;

public class StockManagementService {

    private Context mContext;
    private ReferenceDataService referenceDataService;

    public StockManagementService(Context context) {
        this.mContext = context;
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

                if(stockOnHand >= 0){
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

        CalculatedStockOnHand lastStockOnHand =  new CalculatedStockOnHand();
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

        if(lastStockOnHand.getStockCardId()!= null){
            System.out.println("CurrentStockOnHand"+lastStockOnHand.getStockOnHand());
            return lastStockOnHand.getStockOnHand();
        } else {
            return -1;
        }
    }
}
