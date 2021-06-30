package mz.org.selv.mobile.ui.stockmanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;

public class InventoryItemDialogViewModel extends AndroidViewModel {
    private MutableLiveData<JSONObject> lineItemMutableLiveData;
    ReferenceDataService referenceDataService;

    public InventoryItemDialogViewModel(@NonNull Application application) {
        super(application);
        referenceDataService = new ReferenceDataService(getApplication());
    }

    public void  saveProduct(String orderableName, String lotCode, int quantity, JSONArray adjustments){
        JSONObject lineItem = new JSONObject();
        Lot lot = referenceDataService.getLotByCode(lotCode);
        lineItemMutableLiveData = new MutableLiveData<>();
        Orderable orderable = referenceDataService.getOrderableByName(orderableName);
        try{
            lineItem.put("lotCode", (lot.getId()));
            lineItem.put("expirationDate", lot.getExpirationDate());
            lineItem.put("orderableId", orderable.getUuid());
            lineItem.put("physicalStock", quantity);
            lineItem.put("orderableName", orderable.getName());
            lineItem.put("adjustments", adjustments);
            lineItemMutableLiveData.setValue(lineItem);
        } catch (JSONException ex){
            ex.printStackTrace();
        }
    }
}
