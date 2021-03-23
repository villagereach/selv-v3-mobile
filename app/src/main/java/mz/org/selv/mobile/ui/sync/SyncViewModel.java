package mz.org.selv.mobile.ui.sync;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.service.openlmis.ReferenceDataService;
import mz.org.selv.mobile.service.openlmis.SyncEntities;

public class SyncViewModel extends AndroidViewModel {

    private RequestQueue requestQueue;
    public SyncViewModel(@NonNull Application application) {
        super(application);
    }

    private enum Entity {
        PROGRAM,
        ORDERABLES,
        FACILITY_TYPE_APPROVED_PRODUCTS,
        FACILITY_TYPE,
        REASON,
        VALID_REASONS
    }

    private Map<Enum, Boolean> syncStatus = new HashMap<>();
    private Map<Enum, JSONArray> entitiesMap = new HashMap<>();

    public void sync(int type){
        switch (type){
            case 1: // sync metadata
                syncEntities(Entity.PROGRAM, "https://test.selv.org.mz/api/programs");
                syncEntities(Entity.ORDERABLES, "https://test.selv.org.mz/api/orderables");
                syncEntities(Entity.FACILITY_TYPE_APPROVED_PRODUCTS, "https://test.selv.org.mz/api/facilityTypeApprovedProducts");
                syncEntities(Entity.FACILITY_TYPE, "https://test.selv.org.mz/api/facilityTypes");
                syncEntities(Entity.REASON, "https://test.selv.org.mz/api/programs");
                syncEntities(Entity.VALID_REASONS, "https://test.selv.org.mz/api/programs");

                break;

            case 2: // sync data
                break;

            case 3: // sync all
                break;
        }
    }


    public void syncEntities(Enum entity, String uri){
        System.out.println(syncStatus);
        syncStatus.put(entity, false);

        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try{
                        //since sometimes we receive an array and other a an object
                        if(entity == Entity.ORDERABLES || entity == Entity.FACILITY_TYPE_APPROVED_PRODUCTS || entity == Entity.FACILITY_TYPE){
                            JSONObject jsonResponse = new JSONObject(response);
                            syncStatus.put(entity, true);
                            entitiesMap.put(entity, jsonResponse.getJSONArray("content"));
                            saveEntities();

                        } else if(entity == Entity.PROGRAM){
                            JSONArray jsonResponse = new JSONArray(response);
                            syncStatus.put(entity, true);
                            entitiesMap.put(entity, jsonResponse);
                            saveEntities();
                        } else {
                            JSONArray jsonResponse = new JSONArray(response);
                            syncStatus.put(entity, true);
                            entitiesMap.put(entity, jsonResponse);
                            saveEntities();
                        }
                        //       progressDialog.dismiss();
                    } catch (JSONException ex){
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("Empty...");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("error", "" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                //String credentials = String.format("%s:%s", "admin", "password");
                String auth = "Bearer 53fb48d7-3ad4-4766-9acc-efa25cff5080";
                //String auth = "Bearer 9f81cba6-0462-42e7-8e32-9fbbd2941b57";
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "client_credentials");
                params.put("username", "admin");
                params.put("password", "password");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public int saveEntities(){

        System.out.println(syncStatus);
        if(!syncStatus.containsValue(false)){
            System.out.println("finished");
            for(Map.Entry<Enum, JSONArray> entry: entitiesMap.entrySet()){
                System.out.println("working....");
            }
        } else {
            System.out.println("not finished");
        }
       // SyncEntities syncEntities = new SyncEntities(getApplication());
      //  syncEntities.saveEntities(1, entitiesJson);
        return 1;
    }
}
