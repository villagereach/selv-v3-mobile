package mz.org.selv.mobile.ui.sync;

import static mz.org.selv.mobile.auth.LoginHelper.APP_SHARED_PREFS;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_ACCESS_TOKEN;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

import mz.org.selv.mobile.model.Entity;
import mz.org.selv.mobile.service.openlmis.SyncEntities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SyncViewModel extends AndroidViewModel {

    private RequestQueue requestQueue;

    public SyncViewModel(@NonNull Application application) {
        super(application);
    }

    private Entity entity;


    private Map<Integer, Integer> syncStatus = new HashMap<>();
    private Map<Integer, JSONArray> entitiesMap = new HashMap<>();

    public void sync(int type) {
        switch (type) {
            case 1: // sync metadata
                syncEntities(Entity.PROGRAM, "https://test.selv.org.mz/api/programs");
                syncEntities(Entity.ORDERABLES, "https://test.selv.org.mz/api/orderables");
                syncEntities(Entity.LOTS, "https://test.selv.org.mz/api/lots");
                syncEntities(Entity.FACILITY_TYPE_APPROVED_PRODUCTS, "https://test.selv.org.mz/api/facilityTypeApprovedProducts");
                syncEntities(Entity.FACILITY_TYPE, "https://test.selv.org.mz/api/facilityTypes");
                syncEntities(Entity.REASON, "https://test.selv.org.mz/api/stockCardLineItemReasons");
                syncEntities(Entity.VALID_REASONS, "https://test.selv.org.mz/api/validReasons");
                syncEntities(Entity.VALID_SOURCES, "https://test.selv.org.mz/api/validSources");
                syncEntities(Entity.VALID_DESTINATION, "https://test.selv.org.mz/api/validDestinations");
                break;

            case 2: // sync data
                break;

            case 3: // sync all
                break;
        }
    }


    public void syncEntities(int entity, String uri) {

        syncStatus.put(entity, 0);

        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        //since sometimes we receive an array and other a an object
                        if (entity == Entity.ORDERABLES || entity == Entity.FACILITY_TYPE_APPROVED_PRODUCTS || entity == Entity.FACILITY_TYPE || entity == Entity.LOTS || entity == Entity.VALID_DESTINATION
                                || entity == Entity.VALID_SOURCES) {
                            JSONObject jsonResponse = new JSONObject(response);
                            syncStatus.put(entity, 1);
                            entitiesMap.put(entity, jsonResponse.getJSONArray("content"));
                            saveEntities();

                        } else if (entity == Entity.PROGRAM) {
                            JSONArray jsonResponse = new JSONArray(response);
                            syncStatus.put(entity, 1);
                            entitiesMap.put(entity, jsonResponse);
                            saveEntities();
                        } else {
                            JSONArray jsonResponse = new JSONArray(response);
                            syncStatus.put(entity, 1);
                            entitiesMap.put(entity, jsonResponse);
                            saveEntities();
                        }
                    } catch (JSONException ex) {
                        syncStatus.put(entity, -1);
                        ex.printStackTrace();
                    }
                } else {
                    syncStatus.put(entity, -1);

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
                // SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                // String auth = "Bearer " + sharedPrefs.getString(KEY_ACCESS_TOKEN, "");
                String auth = "Bearer 90194d7a-ffde-4fcb-bc37-2fd476f3a666";
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (entity == Entity.VALID_SOURCES || entity == Entity.VALID_DESTINATION){
                        params.put("facilityId", "8e498daf-cdff-48b2-971f-0c53ef66e14d");
                        params.put("programId", "fabfd914-1bb1-470c-9e5d-f138b3ce70b8");
                } else {
                    params.put("facilityId", "8e498daf-cdff-48b2-971f-0c53ef66e14d");
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void syncValidSources(int entity, String uri) {
        syncStatus.put(entity, 0);

        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONArray jsonResponse = new JSONArray(response);
                        syncStatus.put(entity, 1);
                        entitiesMap.put(entity, jsonResponse);
                        saveEntities();

                    } catch (JSONException ex) {
                        syncStatus.put(entity, -1);
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("error");
                    syncStatus.put(entity, -1);

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
                // SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                // String auth = "Bearer " + sharedPrefs.getString(KEY_ACCESS_TOKEN, "");
                String auth = "Bearer 3c3eaa86-a004-4f11-8f64-3282664be2b3";
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("facilityId", "8e498daf-cdff-48b2-971f-0c53ef66e14d");
                params.put("programId", "e6efef7a-53be-465e-9d0f-970438520ddb");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public int saveEntities() {
        if (!(syncStatus.containsValue(-1) || syncStatus.containsValue(0))) {
            SyncEntities syncEntities = new SyncEntities(getApplication());
            for (Map.Entry<Integer, JSONArray> entry : entitiesMap.entrySet()) {
                syncEntities.saveEntities(entry.getKey(), entry.getValue());
            }
        } else {
            //System.out.println("not finished");
        }       return 1;
    }
}
