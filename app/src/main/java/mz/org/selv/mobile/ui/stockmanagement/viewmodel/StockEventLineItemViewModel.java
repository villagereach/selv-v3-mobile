package mz.org.selv.mobile.ui.stockmanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;

import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;
import org.json.JSONObject;

public class StockEventLineItemViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> lineItemMutableLiveData;

    public StockEventLineItemViewModel(@NonNull Application application) {
        super(application);
    }

    public List getOrderables(String programId, String facilityTypeId){
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> orderableNames = new ArrayList<String>();
        List<Orderable> orderables = referenceDataService.getValidOrderables(programId, facilityTypeId);
        for(int i = 0; i <orderables.size(); i++){
            orderableNames.add(orderables.get(i).getName());
        }
        return orderableNames;
    }

    public List getLotCodes(String orderableName){
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
}
