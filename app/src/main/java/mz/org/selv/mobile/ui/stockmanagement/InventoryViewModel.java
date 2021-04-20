package mz.org.selv.mobile.ui.stockmanagement;

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

import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;

public class InventoryViewModel extends AndroidViewModel {
    public InventoryViewModel(@NonNull Application application) {
        super(application);
        referenceDataService = new ReferenceDataService(getApplication());
    }

    private MutableLiveData<List<JSONObject>> lineItems;
    ReferenceDataService referenceDataService;


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
        System.out.println("creating live data");
        if (lineItems == null) {
            System.out.println("null live data");
            lineItems = new MutableLiveData<>();
        }
        return lineItems;
    }

    public void updateInventoryLineItems(String orderableName, String lotCode, int quantity, int soh, List adjustments) {
        JSONObject lineItem = new JSONObject();
        Lot lot = referenceDataService.getLotByCode(lotCode);
        Orderable orderable = referenceDataService.getOrderableByName(orderableName);
        try {
            lineItem.put("lotCode", (lot.getLotCode()));
            lineItem.put("expirationDate", lot.getExpirationDate());
            lineItem.put("orderableId", orderable.getUuid());
            lineItem.put("physicalStock", quantity);
            lineItem.put("orderableName", orderable.getName());

            //if not available
            lineItem.put("stockOnHand", soh);


        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        List currentItems = getLastLineUpdatedItem().getValue();
        if (currentItems == null) {
            currentItems = new ArrayList<JSONObject>();
        }
        currentItems.add(lineItem);
        lineItems.setValue(currentItems);
    }
}
