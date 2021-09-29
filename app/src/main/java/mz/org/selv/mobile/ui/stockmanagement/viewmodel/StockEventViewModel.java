package mz.org.selv.mobile.ui.stockmanagement.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.json.JSONObject;

import java.util.List;

import mz.org.selv.mobile.model.stockmanagement.StockEvent;
import mz.org.selv.mobile.model.stockmanagement.StockEventLineItem;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;
import mz.org.selv.mobile.service.stockmanagement.StockManagementService;


public class StockEventViewModel extends AndroidViewModel {
    StockManagementService stockManagementService;
    public StockEventViewModel(@NonNull Application application) {
        super(application);
    }
    // TODO: Implement the ViewModel

    public List<JSONObject> getEventLineItems(String facilityId, String programId, String action){

        stockManagementService = new StockManagementService(getApplication());
        List<JSONObject> eventLineItems = stockManagementService.getStockEventLineItems(facilityId, programId, action);
        return eventLineItems;
    }
}