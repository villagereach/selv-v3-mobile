package mz.org.selv.mobile.ui.stockmanagement.viewmodel;

import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventory;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventoryLineItem;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventoryLineItemAdjustments;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;
import mz.org.selv.mobile.service.stockmanagement.StockManagementService;
import mz.org.selv.mobile.ui.adapters.InventoryItemsAdapter;

public class InventoryViewModel extends AndroidViewModel {
    public InventoryViewModel(@NonNull Application application) {
        super(application);
        referenceDataService = new ReferenceDataService(getApplication());
        stockManagementService = new StockManagementService(getApplication());
    }

    private MutableLiveData<List<JSONObject>> lineItems;
    private ReferenceDataService referenceDataService;
    private StockManagementService stockManagementService;

    public List getOrderables(String programId, String facilityTypeId) {

        List<String> orderableNames = new ArrayList<String>();
        List<Orderable> orderables = referenceDataService.getValidOrderables(programId, facilityTypeId);
        for (int i = 0; i < orderables.size(); i++) {
            orderableNames.add(orderables.get(i).getName());
        }
        return orderableNames;
    }

    public List getLots(String orderableName) {
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> lotCodes = new ArrayList<String>();
        List<Lot> lots = referenceDataService.getLotsByOrderableName(orderableName);
        for (int i = 0; i < lots.size(); i++) {
            lotCodes.add(lots.get(i).getLotCode());
        }
        return lotCodes;
    }

    public Lot getLotByCode(String lotCode) {
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());

        Lot lot = referenceDataService.getLotByCode(lotCode);
        return lot;
    }

    public List getReasonNames(String facilityTypeId, String programId) {
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> reasonNames = referenceDataService.getReasonNameByValidReason(facilityTypeId, programId);
        return reasonNames;
    }

    public LiveData<List<JSONObject>> getLastLineUpdatedItem() {
        if (lineItems == null) {
            lineItems = new MutableLiveData<>();
        }
        return lineItems;
    }

    public void updateInventoryLineItems(String orderableName, String lotCode, int quantity, int soh, int position, List adjustments) {
        JSONObject lineItem = new JSONObject();
        Lot lot = referenceDataService.getLotByCode(lotCode);
        Orderable orderable = referenceDataService.getOrderableByName(orderableName);
        try {
            lineItem.put("lotCode", (lot.getLotCode()));
            lineItem.put("expirationDate", lot.getExpirationDate());
            lineItem.put("lotId", lot.getId());
            lineItem.put("orderableId", orderable.getUuid());
            lineItem.put("physicalStock", quantity);
            lineItem.put("orderableName", orderable.getName());
            lineItem.put("adjustments", adjustments);
            //if not available
            lineItem.put("stockOnHand", soh);


            List currentItems = getLastLineUpdatedItem().getValue();

            if (currentItems == null) {
                currentItems = new ArrayList<JSONObject>();
            }



            if (position > -1) {
                JSONObject currentItem = (JSONObject) currentItems.get(position);
                currentItem.put("physicalStock", quantity);
                System.out.println("Position A: "+position);
                currentItems.remove(position);
                currentItems.add(position, currentItem);
            } else {
                currentItems.add(lineItem);
            }
            lineItems.setValue(currentItems);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    public int saveInventory(String signature, String programId, String facilityId, String occurredDate, InventoryItemsAdapter lineItemsAdapter) {
        List<PhysicalInventoryLineItem> lineItems = new ArrayList<>();
        String inventoryId = UUID.randomUUID().toString();
        PhysicalInventory inventory = new PhysicalInventory();
        inventory.setSignature(signature);
        inventory.setProgramId(programId);
        inventory.setOccurredDate(occurredDate);
        inventory.setFacilityId(facilityId);
        inventory.setId(inventoryId);
        for (int i = 0; i < lineItemsAdapter.getCount(); i++) {
            PhysicalInventoryLineItem lineItem = new PhysicalInventoryLineItem();
            System.out.println(lineItemsAdapter.getItem(i).toString());
            try {
                lineItem.setOrderableId(lineItemsAdapter.getItem(i).getString("orderableId"));
                lineItem.setLotId(lineItemsAdapter.getItem(i).getString("lotId"));
                lineItem.setPhysicalStock(lineItemsAdapter.getItem(i).getInt("physicalStock"));

                //lineItem.setPreviousStockOnHand();
                lineItem.setPhysicalInventoryId(inventoryId);
                lineItems.add(lineItem);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        if (lineItems.size() > 0) {
            stockManagementService = new StockManagementService(getApplication());
            stockManagementService.saveInventory(inventory, lineItems, null);
        }
        return 1;
    }

    public void getInventoryLineItems(String programId, String facilityId) {
        getLastLineUpdatedItem().getValue();
        lineItems.setValue(stockManagementService.getNewInventoryLineItem(programId, facilityId));
    }

    public String getReasonType(String reasonName){
        return referenceDataService.getReasonByName(reasonName).getType();
    }
}
