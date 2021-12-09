package mz.org.selv.mobile.ui.stockmanagement.viewmodel;

import android.app.Application;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private MutableLiveData<Map<String, String>> selectedLot;
    private MutableLiveData<ValidSource> validSourceLiveData;
    private MutableLiveData<ValidDestination> selectedDestination;
    private MutableLiveData<ValidSource> selectedSource;
    private MutableLiveData<StockCard> selectedStockCard;
    private MutableLiveData<Reason> selectedReason;
    private StockManagementService stockManagementService;
    private List<Map<String, String>> availableLots;


    public StockEventLineItemViewModel(@NonNull Application application) {
        super(application);
        stockManagementService = new StockManagementService(getApplication());
    }

    public List getOrderables(String programId, String facilityTypeId, String facilityId, String action) {
        List<String> orderableNames = new ArrayList<String>();
        List<Orderable> orderables = new ArrayList<>();
        if (action.equals("issue") || action.equals("adjustment")) {

            orderables = stockManagementService.getAvailableOrderables(programId, facilityId);

        } else {
            ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
            orderables = referenceDataService.getValidOrderables(programId, facilityTypeId);
        }
        for (int i = 0; i < orderables.size(); i++) {
            orderableNames.add(orderables.get(i).getName());
        }
        return orderableNames;
    }

    public List getLotCodes(String orderableName, String facilityId, String programId, String action) {

        List<String> lotCodes = new ArrayList<String>();
        List<Map<String, String>> lots;
        if (action.equals("adjustment")) {
            availableLots = stockManagementService.getAvailableLotsAndStockOnHandByOrderableName(orderableName, facilityId, programId, action);
        } else if (action.equals("issue")) {
            availableLots = stockManagementService.getAvailableLotsAndStockOnHandByOrderableName(orderableName, facilityId, programId, action);
        } else {
            availableLots = stockManagementService.getAvailableLotsAndStockOnHandByOrderableName(orderableName, facilityId, programId, action);
            ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
            List<Lot> lotList = referenceDataService.getLotsByOrderableName(orderableName);

            if (availableLots == null || availableLots.size() == 0) {

                availableLots = new ArrayList<>();
                for(int i = 0; i < lotList.size(); i++){
                    Map<String, String > lotDetails = new HashMap<>();
                    lotDetails.put("code", lotList.get(i).getLotCode());
                    lotDetails.put("expirationDate", lotList.get(i).getExpirationDate());
                    lotDetails.put("stockOnHand", 0+"");
                    availableLots.add(lotDetails);
                }
            } else {
                for(int i = 0; i < lotList.size(); i++){
                    boolean newItem = true;
                    for(int j = 0; j < availableLots.size(); j++){
                        if(lotList.get(i).getLotCode().equals(availableLots.get(j).get("code"))){
                            newItem = false;
                        }
                    }
                    if(newItem){
                        Map<String, String > lotDetails = new HashMap<>();
                        lotDetails.put("code", lotList.get(i).getLotCode());
                        lotDetails.put("expirationDate", lotList.get(i).getExpirationDate());
                        lotDetails.put("stockOnHand", 0+"");
                        availableLots.add(lotDetails);
                    }
                }
            }
        }

        for (int i = 0; i < availableLots.size(); i++) {
            System.out.println("code: "+availableLots.get(i).get("code"));
            lotCodes.add(availableLots.get(i).get("code"));
        }
        return lotCodes;
    }


    public void getLotsAndDetails(String orderableName, String facilityId, String programId, String action) {

        List<String> lotCodes = new ArrayList<String>();
        List<Map<String, String>> lots;

        if (action.equals("adjustment")) {
            availableLots = stockManagementService.getAvailableLotsAndStockOnHandByOrderableName(orderableName, facilityId, programId, action);
        } else if (action.equals("issue")) {
            availableLots = stockManagementService.getAvailableLotsAndStockOnHandByOrderableName(orderableName, facilityId, programId, action);
        } else {
            ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
            availableLots = referenceDataService.getLotsByOrderableName(orderableName);
        }
    }

    public List getAvailableLotCodesAndQuantity(String orderableName, String facilityId, String programId, String action) {

        List<String> lotCodes = new ArrayList<String>();
        List<Lot> lots = new ArrayList<>();

        if (action.equals("issue") || action.equals("adjustment")) {
            lots = stockManagementService.getAvailableLotsByName(orderableName, facilityId, programId);
        } else {
            ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
            lots = referenceDataService.getLotsByOrderableName(orderableName);
        }

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

    public Map<String, String> getLot(String lotCode) {
        for (int i = 0; i < availableLots.size(); i++) {
            if (availableLots.get(i).get("code").equals(lotCode)) {
                return availableLots.get(i);
            }
        }
        return null;
    }

    public void setSelectedLot(String lotCode) {
        getSelectedLot().setValue(getLot(lotCode));
    }

    public void setSelectedSource(String facilityTypeId, String programId, String sourceName) {
        StockManagementService stockManagementService = new StockManagementService(getApplication());
        ValidSource validSource = stockManagementService.getValidSourceByName(facilityTypeId, programId, sourceName);
        getSelectedValidSource().setValue(validSource);
    }

    public void setSelectedDestination(String facilityTypeId, String programId, String destinationName) {
        StockManagementService stockManagementService = new StockManagementService(getApplication());
        facilityTypeId = "be01380b-4939-47a1-a5ce-72c691d63a8e";
        ValidDestination validDestination = stockManagementService.getValidDestinationByName(facilityTypeId, programId, destinationName);
        getSelectedValidDestination().setValue(validDestination);
    }

    public MutableLiveData<ValidSource> getSelectedValidSource() {
        if (selectedSource == null) {
            selectedSource = new MutableLiveData<>();
        }
        return selectedSource;
    }

    public MutableLiveData<ValidDestination> getSelectedValidDestination() {
        if (selectedDestination == null) {
            selectedDestination = new MutableLiveData<>();
        }
        return selectedDestination;
    }

    public MutableLiveData<Map<String, String>> getSelectedLot() {
        if (selectedLot == null) {
            selectedLot = new MutableLiveData<Map<String, String>>();
        }
        return selectedLot;
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

    public List getValidDestinations(String facilityTypeId, String programId) {
        ReferenceDataService referenceDataService = new ReferenceDataService(getApplication());
        List<String> validDestinations = new ArrayList<String>();
        List<ValidDestination> validDestinationList = referenceDataService.getValidDestinations(facilityTypeId, programId);
        for (int i = 0; i < validDestinationList.size(); i++) {
            validDestinations.add(validDestinationList.get(i).getName());
        }
        return validDestinations;
    }

    public int saveEvent(String action, String facilityId, String facilityTypeId, String programId, String orderableName, String lotCode, String validSourceOrDestinationName, String sourceDestinationComments, String reasonName, String reasonComments, int quantity, String vvm, String occurredDate) {
        if (action.equals("receive") || action.equals("issue")) {
            facilityTypeId =  "be01380b-4939-47a1-a5ce-72c691d63a8e";
            StockManagementService stockManagementService = new StockManagementService(getApplication());
            stockManagementService.saveEvent(action, facilityId, facilityTypeId, programId, orderableName, lotCode, validSourceOrDestinationName, sourceDestinationComments,
                    reasonName, reasonComments, quantity, null, occurredDate);
        } else if(action.equals("adjustment")){
            StockManagementService stockManagementService = new StockManagementService(getApplication());
            stockManagementService.saveEvent(action, facilityId, facilityTypeId, programId, orderableName, lotCode, validSourceOrDestinationName, sourceDestinationComments,
                    reasonName, reasonComments, quantity, null, occurredDate);
        }
        return -1;
    }

}
