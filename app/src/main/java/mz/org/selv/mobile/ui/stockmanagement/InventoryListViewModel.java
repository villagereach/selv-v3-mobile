package mz.org.selv.mobile.ui.stockmanagement;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.model.referencedata.Program;
import mz.org.selv.mobile.model.stockmanagement.PhysicalInventory;
import mz.org.selv.mobile.service.referencedata.ProgramService;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;
import mz.org.selv.mobile.service.stockmanagement.StockManagementService;

public class InventoryListViewModel extends AndroidViewModel {
    StockManagementService stockManagementService;
    ReferenceDataService referenceDataService;
    public InventoryListViewModel(@NonNull Application application) {
        super(application);
    }

    public List<JSONObject> getInventories(String programId, String facilityId){
        stockManagementService = new StockManagementService(getApplication());
        referenceDataService = new ReferenceDataService(getApplication());
        List<PhysicalInventory> inventories = stockManagementService.getInventories(programId, facilityId);
        List<JSONObject> inventoriesJson = new ArrayList<>();
        for(int i = 0; i < inventories.size(); i++){

            JSONObject inventory = new JSONObject();
            try{
                inventory.put("programId", inventories.get(i).getProgramId());
                inventory.put("programName", referenceDataService.getProgramById(programId).getName());
                inventory.put("signature", inventories.get(i).getSignature());
                inventory.put("occurredaDate", inventories.get(i).getOccurredDate());
                inventory.put("status", inventories.get(i).getStatus());
                inventory.put("facilityId", inventories.get(i).getFacilityId());
                inventoriesJson.add(inventory);
            } catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return inventoriesJson;
    }
}
