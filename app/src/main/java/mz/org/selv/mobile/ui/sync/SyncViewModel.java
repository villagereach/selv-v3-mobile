package mz.org.selv.mobile.ui.sync;

import static android.util.Base64.DEFAULT;

import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mz.org.selv.mobile.model.Entity;
import mz.org.selv.mobile.service.openlmis.SyncEntities;

public class SyncViewModel extends AndroidViewModel {

    private static final String ACCESS_TOKEN_URI = "https://test.selv.org.mz/api/oauth/token";
    private static final String CLIENT_ID = "client";
    private static final String CLIENT_SECRET = "secret";
    private String accessToken;
    private boolean loggedIn = false;

    private RequestQueue requestQueue;
    public SyncViewModel(@NonNull Application application) {
        super(application);
    }
    private Entity entity;


    private Map<Integer, Integer> syncStatus = new HashMap<>();
    private Map<Integer, JSONArray> entitiesMap = new HashMap<>();

    public void sync(int type){
        switch (type){
            case 1: // sync metadata
                if (!loggedIn) {
                    System.out.println("CSA Not logged in");
                    break;
                }
                syncEntities(Entity.PROGRAM, "https://test.selv.org.mz/api/programs");
                syncEntities(Entity.ORDERABLES, "https://test.selv.org.mz/api/orderables");
                syncEntities(Entity.LOTS, "https://test.selv.org.mz/api/lots");
                syncEntities(Entity.FACILITY_TYPE_APPROVED_PRODUCTS, "https://test.selv.org.mz/api/facilityTypeApprovedProducts");
                syncEntities(Entity.FACILITY_TYPE, "https://test.selv.org.mz/api/facilityTypes");
                syncEntities(Entity.REASON, "https://test.selv.org.mz/api/stockCardLineItemReasons");
                syncEntities(Entity.VALID_REASONS, "https://test.selv.org.mz/api/validReasons");
                //syncEntities(Entity.VALID_SOURCE, "https://test.selv.org.mz/api/validSources");
                //syncEntities(Entity.VALID_DESTINATION, "https://test.selv.org.mz/api/validDestinations");
                break;

            case 2: // sync data
                break;

            case 3: // sync all
                break;
        }
    }


    public void syncEntities(int entity, String uri){

        syncStatus.put(entity, 0);

        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try{
                        //since sometimes we receive an array and other a an object
                        if(entity == Entity.ORDERABLES || entity == Entity.FACILITY_TYPE_APPROVED_PRODUCTS || entity == Entity.FACILITY_TYPE || entity == Entity.LOTS){
                            JSONObject jsonResponse = new JSONObject(response);
                            syncStatus.put(entity, 1);
                            entitiesMap.put(entity, jsonResponse.getJSONArray("content"));
                            saveEntities();

                        } else if(entity == Entity.PROGRAM){
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
                    } catch (JSONException ex){
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
                String auth = "Bearer " + accessToken;
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("facilityId","8e498daf-cdff-48b2-971f-0c53ef66e14d");

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public int saveEntities(){
        if(!(syncStatus.containsValue(-1) || syncStatus.containsValue(0))){
            SyncEntities syncEntities = new SyncEntities(getApplication());
            for(Map.Entry<Integer, JSONArray> entry: entitiesMap.entrySet()){
                syncEntities.saveEntities(entry.getKey(), entry.getValue());
            }
        } else {
            //System.out.println("not finished");
        }
        return 1;
    }

    void obtainAccessToken() {
        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest loginRequest = new StringRequest(Method.POST, ACCESS_TOKEN_URI,
            new Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            accessToken = responseObject.getString("access_token");
                            loggedIn = true;
                            System.out.println("CSA Logged in successfully");
                        } catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    } else {
                        loggedIn = false;
                    }

                }
            },
            new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.e("error", "" + error);
                }
            }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String plainCreds = CLIENT_ID + ":" + CLIENT_SECRET;
                String base64Creds = Base64
                    .encodeToString(plainCreds.getBytes(StandardCharsets.UTF_8), DEFAULT);
                headers.put("Authorization", "Basic " + base64Creds);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "password");
                params.put("username", "admin");
                params.put("password", "");
                return params;
            }
        };
        requestQueue.add(loginRequest);
    }
}
