package mz.org.selv.mobile.ui.stockmanagement.viewmodel;

import android.app.Application;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.model.stockmanagement.CalculatedStockOnHand;
import mz.org.selv.mobile.model.stockmanagement.Reason;
import mz.org.selv.mobile.model.stockmanagement.StockCard;
import mz.org.selv.mobile.model.stockmanagement.StockEvent;
import mz.org.selv.mobile.model.stockmanagement.StockEventLineItem;
import mz.org.selv.mobile.model.stockmanagement.ValidDestination;
import mz.org.selv.mobile.model.stockmanagement.ValidSource;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;
import mz.org.selv.mobile.service.stockmanagement.StockManagementService;

import org.json.JSONObject;

public class StockEventLineItemViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> lineItemMutableLiveData;
    private MutableLiveData<Orderable> selectedOrderable;
    private MutableLiveData<Lot> selectedLot;
    private MutableLiveData<ValidSource> validSourceLiveData;
    private MutableLiveData<ValidDestination> selectedDestination;
    private MutableLiveData<ValidSource> selectedSource;
    private MutableLiveData<Reason> selectedReason;

    public StockEventLineItemViewModel(@NonNull Application application) {
        super(application);
    }

    public List getOrderables(String programId, String facilityTypeId) {
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> orderableNames = new ArrayList<String>();
        List<Orderable> orderables = referenceDataService.getValidOrderables(programId, facilityTypeId);
        for (int i = 0; i < orderables.size(); i++) {
            orderableNames.add(orderables.get(i).getName());
        }
        return orderableNames;
    }

    public List getLotCodes(String orderableName) {
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> lotCodes = new ArrayList<String>();
        List<Lot> lots = referenceDataService.getLotsByOrderableName(orderableName);
        for (int i = 0; i < lots.size(); i++) {
            lotCodes.add(lots.get(i).getLotCode());
        }
        return lotCodes;
    }

    public List getReasonNames(String facilityTypeId, String programId, String category, String type) {
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> reasonNames = referenceDataService.getReasonNameByValidReason(facilityTypeId, programId, category, type);
        return reasonNames;
    }

    public void setSelectedSource(String facilityTypeId, String programId, String sourceName) {
        StockManagementService stockManagementService = new StockManagementService(getApplication());
        ValidSource validSource = stockManagementService.getValidSourceByName(facilityTypeId, programId, sourceName);
        getSelectedValidSource().setValue(validSource);
    }

    public MutableLiveData<ValidSource> getSelectedValidSource() {
        if (selectedSource == null) {
            selectedSource = new MutableLiveData<>();
        }
        return selectedSource;
    }


    public List getValidSources(String facilityTypeId, String programId) {
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> validSources = new ArrayList<String>();
        List<ValidSource> validSourceList = referenceDataService.getValidSources(facilityTypeId, programId);
        for (int i = 0; i < validSourceList.size(); i++) {
            validSources.add(validSourceList.get(i).getName());
        }
        return validSources;
    }

    public int saveEvent(String action, String facilityId, String facilityTypeId, String programId, String orderableName, String lotCode, String validSourceOrDestinationName, String sourceDestinationComments, String reasonName, String reasonComments, int quantity, String vvm, String occurredDate) {
        if (action.equals("receive")) {
            StockManagementService stockManagementService = new StockManagementService(getApplication());
            stockManagementService.saveEvent(action, facilityId, facilityTypeId,programId, orderableName, lotCode, validSourceOrDestinationName, sourceDestinationComments,
                    reasonName, reasonComments, quantity, null, occurredDate);
        }
        return -1;
    }
}
