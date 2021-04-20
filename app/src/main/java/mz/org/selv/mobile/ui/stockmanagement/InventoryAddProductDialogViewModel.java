package mz.org.selv.mobile.ui.stockmanagement;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventoryLineItem;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;

public class InventoryAddProductDialogViewModel extends AndroidViewModel {
    private MutableLiveData<JSONObject> lineItemMutableLiveData;
    ReferenceDataService referenceDataService;
    public InventoryAddProductDialogViewModel(@NonNull Application application) {
        super(application);
        referenceDataService = new ReferenceDataService(getApplication());
    }

    public List getOrderables(String programId, String facilityTypeId){

        List<String> orderableNames = new ArrayList<String>();
        List<Orderable> orderables = referenceDataService.getValidOrderables(programId, facilityTypeId);
        for(int i = 0; i <orderables.size(); i++){
            orderableNames.add(orderables.get(i).getName());
        }
        return orderableNames;
    }

    public List getLots(String orderableName){
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> lotCodes = new ArrayList<String>();
        List<Lot> lots = referenceDataService.getLotsByOrderableName(orderableName);
        for(int i = 0; i< lots.size(); i++){
            lotCodes.add(lots.get(i).getLotCode());
        }
        return lotCodes;
    }

    public List getReasonNames(String facilityTypeId, String programId){
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> reasonNames = referenceDataService.getReasonNameByValidReason(facilityTypeId, programId);
        return reasonNames;
    }

    public void  addProduct(String orderableName, String lotCode, int quantity){
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
            lineItemMutableLiveData.setValue(lineItem);
        } catch (JSONException ex){
            ex.printStackTrace();
        }
    }
}
