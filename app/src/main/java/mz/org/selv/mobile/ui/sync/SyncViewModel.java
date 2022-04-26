package mz.org.selv.mobile.ui.sync;

import static android.util.Base64.DEFAULT;
import static mz.org.selv.mobile.BuildConfig.CLIENT_ID;
import static mz.org.selv.mobile.BuildConfig.CLIENT_SECRET;
import static mz.org.selv.mobile.auth.LoginHelper.APP_SHARED_PREFS;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_ACCESS_TOKEN;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_HOME_FACILITY_ID;
import static mz.org.selv.mobile.BuildConfig.BASE_URL;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.auth.LoginHelper;
import mz.org.selv.mobile.model.Entity;
import mz.org.selv.mobile.model.referencedata.Program;
import mz.org.selv.mobile.model.stockmanagement.ValidSource;
import mz.org.selv.mobile.service.openlmis.SyncEntities;
import mz.org.selv.mobile.service.referencedata.ReferenceDataService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SyncViewModel extends AndroidViewModel {

    private MutableLiveData<String> status;

    private enum State {CONNECTING, DOWNLOADING, SAVING, AUTHENTICATING}

    private RequestQueue requestQueue;
    private static final String PROGRAMS_URI = BASE_URL + "/api/programs";
    private static final String FACILITY_TYPES_URI = BASE_URL + "/api/facilityTypes";
    private static final String FACILITIES_URI = BASE_URL + "/api/facilities";
    private static final String VALID_SOURCES_URI = BASE_URL + "/api/validSources";
    private static final String VALID_DESTINATIONS_URI = BASE_URL + "/api/validDestinations";
    private static final String ORDERABLES_URI = BASE_URL + "/api/orderables";
    private static final String LOTS_URI = BASE_URL + "/api/lots";
    private static final String REASON_URI = BASE_URL + "/api/stockCardLineItemReasons";
    private static final String VALID_REASONS_URI = BASE_URL + "/api/validReasons";
    private static final String FTAPS_URI = BASE_URL + "/api/facilityTypeApprovedProducts";

    private static final String STATUS_CONNECTING = "connecting";
    private static final String STATUS_AUTHENTICATING = "connecting";
    private static final String STATUS_DOWNLOADING = "downloading";
    private static final String STATUS_SAVING = "saving";
    private static final String STATUS_FINISHED = "finished";


    private static final String ACCESS_TOKEN_URI = BASE_URL + "/api/oauth/token";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_USER_ID = "referenceDataUserId";
    public static final String KEY_HOME_FACILITY_ID = "homeFacilityId";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN_EXPIRATION = "expires_in";
    public static final String APP_SHARED_PREFS = "selv_mobile_prefs";
    public static final String KEY_TOKEN_RECEIVE_TIME = "token_datetime";

    public SyncViewModel(@NonNull Application application) {
        super(application);
    }

    private Map<Integer, Integer> syncStatus = new HashMap<>();
    private Map<Integer, JSONArray> entitiesMap = new HashMap<>();
    private List<String> programIdList = new ArrayList<>();

    public MutableLiveData<String> getStatus() {
        if (status == null) {
            System.out.println("creating new status....");
            status = new MutableLiveData<>();
        }
        return status;
    }

    public void sync(int type, int entity) {
        switch (type) {
            case 1: // sync all
                syncEntity(Entity.PROGRAM);
                syncEntity(Entity.ORDERABLES);
                syncEntity(Entity.LOTS);
                syncEntity(Entity.FACILITY_TYPE_APPROVED_PRODUCTS);
                syncEntity(Entity.FACILITY);
                syncEntity(Entity.FACILITY_TYPE);
                syncEntity(Entity.REASON);
                syncEntity(Entity.VALID_REASONS);
                syncEntity(Entity.VALID_SOURCES);
                syncEntity(Entity.VALID_DESTINATION);
                break;

            case 2: // sync single entity
                //reset sync status and entities map
                syncStatus = new HashMap<>();
                entitiesMap = new HashMap<>();
                if (entity == Entity.VALID_SOURCES) {
                    ReferenceDataService referenceDataService = new ReferenceDataService(getApplication().getApplicationContext());
                    List<Program> programs = referenceDataService.getPrograms();
                    for (int i = 0; i < programs.size(); i++) {
                        syncValidSources(programs.get(i).getUuid());
                    }

                } else if (entity == Entity.VALID_DESTINATION) {
                    ReferenceDataService referenceDataService = new ReferenceDataService(getApplication().getApplicationContext());
                    List<Program> programs = referenceDataService.getPrograms();
                    for (int i = 0; i < programs.size(); i++) {
                        syncValidDestinations(programs.get(i).getUuid());
                    }
                } else {
                    syncEntity(entity);
                }

                break;

            case 3: // sync data
                syncEntity(entity);
                break;
        }
    }

    public void syncEntity(int entity) {

        if (validateToken()) {
            getEntity(entity);
        } else {
            getStatus().setValue(STATUS_AUTHENTICATING);
            requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());
            StringRequest loginRequest = new StringRequest(Request.Method.POST, ACCESS_TOKEN_URI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                try {
                                    SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    JSONObject responseObject = new JSONObject(response);
                                    String accessToken = responseObject.getString(KEY_ACCESS_TOKEN);
                                    String userId = responseObject.getString(KEY_USER_ID);
                                    int tokenExpiration = responseObject.getInt(KEY_TOKEN_EXPIRATION);
                                    SharedPreferences.Editor editor = sharedPrefs.edit();
                                    editor.putString(KEY_ACCESS_TOKEN, accessToken);
                                    editor.putString(KEY_USER_ID, userId);
                                    editor.putInt(KEY_TOKEN_EXPIRATION, tokenExpiration);
                                    editor.putString(KEY_TOKEN_RECEIVE_TIME, dateFormat.format(new Date()));
                                    editor.apply();
                                    getEntity(entity);
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    },
                    error -> {
                        Toast.makeText(getApplication().getApplicationContext(), R.string.string_login_failed, Toast.LENGTH_SHORT).show();
                        getStatus().setValue(STATUS_FINISHED);
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String plainCreds = CLIENT_ID + ":" + CLIENT_SECRET;
                    String base64Creds = Base64
                            .encodeToString(plainCreds.getBytes(StandardCharsets.UTF_8), DEFAULT);
                    headers.put("Authorization", "Basic " + base64Creds);
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("grant_type", "password");
                    SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                    params.put(KEY_USERNAME, sharedPrefs.getString(KEY_USERNAME, ""));
                    params.put(KEY_PASSWORD, sharedPrefs.getString(KEY_PASSWORD, ""));
                    return params;
                }
            };
            requestQueue.add(loginRequest);
        }
    }

    public void getEntity(int entity) {

        String uri = "";
        switch (entity) {
            case Entity.PROGRAM:
                uri = PROGRAMS_URI;
                break;
            case Entity.ORDERABLES:
                uri = ORDERABLES_URI;
                break;
            case Entity.FACILITY:
                uri = FACILITIES_URI;
                break;
            case Entity.FACILITY_TYPE:
                uri = FACILITY_TYPES_URI;
                break;
            case Entity.FACILITY_TYPE_APPROVED_PRODUCTS:
                uri = FTAPS_URI;
                break;
            case Entity.LOTS:
                uri = LOTS_URI;
                break;
            case Entity.REASON:
                uri = REASON_URI;
                break;
            case Entity.VALID_REASONS:
                uri = VALID_REASONS_URI;
                break;

        }

        getStatus().setValue(STATUS_CONNECTING);

        syncStatus.put(entity, 0);

        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        //since sometimes we receive an array and other a an object
                        if (entity == Entity.ORDERABLES || entity == Entity.FACILITY_TYPE_APPROVED_PRODUCTS || entity == Entity.FACILITY_TYPE || entity == Entity.LOTS ||
                        entity == Entity.FACILITY) {

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
                System.out.println("Error......");
                getStatus().setValue(STATUS_FINISHED);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                String auth = "Bearer " + sharedPrefs.getString(KEY_ACCESS_TOKEN, "");
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                params.put("facilityId", sharedPrefs.getString(KEY_HOME_FACILITY_ID, ""));
                params.put("programId", "fabfd914-1bb1-470c-9e5d-f138b3ce70b8");

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getValidSources() {

        getStatus().setValue(STATUS_CONNECTING);

        syncStatus.put(Entity.VALID_SOURCES, 0);

        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, VALID_SOURCES_URI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        //since sometimes we receive an array and other a an object
                        JSONObject jsonResponse = new JSONObject(response);
                        syncStatus.put(Entity.VALID_SOURCES, 1);
                        entitiesMap.put(Entity.VALID_SOURCES, jsonResponse.getJSONArray("content"));
                        saveEntities();

                    } catch (JSONException ex) {
                        syncStatus.put(Entity.VALID_SOURCES, -1);
                        ex.printStackTrace();
                    }

                } else {
                    syncStatus.put(Entity.VALID_SOURCES, -1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getStatus().setValue(STATUS_FINISHED);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                String auth = "Bearer " + sharedPrefs.getString(KEY_ACCESS_TOKEN, "");
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                params.put("facilityId", sharedPrefs.getString(KEY_HOME_FACILITY_ID, ""));
                params.put("programId", "fabfd914-1bb1-470c-9e5d-f138b3ce70b8");

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    // since valid source depends on the programs to sync
    public void syncValidSources(String programId) {

        syncStatus.put(Entity.VALID_SOURCES, 0);

        if(validateToken()){
            getValidSources();
        } else {
            getStatus().setValue(STATUS_AUTHENTICATING);
            requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());
            StringRequest loginRequest = new StringRequest(Request.Method.POST, ACCESS_TOKEN_URI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                try {
                                    SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    JSONObject responseObject = new JSONObject(response);
                                    String accessToken = responseObject.getString(KEY_ACCESS_TOKEN);
                                    String userId = responseObject.getString(KEY_USER_ID);
                                    int tokenExpiration = responseObject.getInt(KEY_TOKEN_EXPIRATION);
                                    SharedPreferences.Editor editor = sharedPrefs.edit();
                                    editor.putString(KEY_ACCESS_TOKEN, accessToken);
                                    editor.putString(KEY_USER_ID, userId);
                                    editor.putInt(KEY_TOKEN_EXPIRATION, tokenExpiration);
                                    editor.putString(KEY_TOKEN_RECEIVE_TIME, dateFormat.format(new Date()));
                                    editor.apply();
                                    getValidSources();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    },
                    error -> {
                        Toast.makeText(getApplication().getApplicationContext(), R.string.string_login_failed, Toast.LENGTH_SHORT).show();
                        getStatus().setValue(STATUS_FINISHED);
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    String plainCreds = CLIENT_ID + ":" + CLIENT_SECRET;
                    String base64Creds = Base64
                            .encodeToString(plainCreds.getBytes(StandardCharsets.UTF_8), DEFAULT);
                    headers.put("Authorization", "Basic " + base64Creds);
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("grant_type", "password");
                    SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                    params.put(KEY_USERNAME, sharedPrefs.getString(KEY_USERNAME, ""));
                    params.put(KEY_PASSWORD, sharedPrefs.getString(KEY_PASSWORD, ""));
                    return params;
                }
            };
            loginRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(loginRequest);
        }

    }

    public void syncValidDestinations(String programId) {

        LoginHelper loginHelper = new LoginHelper(getApplication().getApplicationContext());

        loginHelper.obtainAccessToken(getApplication().getApplicationContext());

        syncStatus.put(Entity.VALID_DESTINATION, 0);

        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, VALID_DESTINATIONS_URI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        //since sometimes we receive an array and other a an object
                        JSONObject jsonResponse = new JSONObject(response);
                        syncStatus.put(Entity.VALID_DESTINATION, 1);
                        entitiesMap.put(Entity.VALID_DESTINATION, jsonResponse.getJSONArray("content"));
                        saveEntities();

                    } catch (JSONException ex) {
                        syncStatus.put(Entity.VALID_DESTINATION, -1);
                        ex.printStackTrace();
                    }
                } else {
                    syncStatus.put(Entity.VALID_DESTINATION, -1);
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
                SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                String auth = "Bearer " + sharedPrefs.getString(KEY_ACCESS_TOKEN, "");
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                params.put("facilityId", sharedPrefs.getString(KEY_HOME_FACILITY_ID, ""));
                params.put("programId", programId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public int saveEntities() {
        if (!(syncStatus.containsValue(-1) || syncStatus.containsValue(0))) {
            getStatus().setValue(STATUS_SAVING);
            SyncEntities syncEntities = new SyncEntities(getApplication());
            for (Map.Entry<Integer, JSONArray> entry : entitiesMap.entrySet()) {
                syncEntities.saveEntities(entry.getKey(), entry.getValue());
            }
            getStatus().setValue(STATUS_FINISHED);
        } else {
            //System.out.println("not finished");
        }
        return 1;
    }

    // this methods checks if token is active, if yes it returns true, if not it triggers the token update and wait 5s and tries again for 3 times, if all fails it returns false
    public boolean validateToken() {
        SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        //int tokenTimeout = sharedPrefs.getInt(KEY_TOKEN_EXPIRATION, 0);
        String tokenReceiveTime = sharedPrefs.getString(KEY_TOKEN_RECEIVE_TIME, "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long tokenReceiveTimeMs = 0;
        try {
            if(!tokenReceiveTime.equals("")){
                Date tokenReceiveDate = dateFormat.parse(tokenReceiveTime);
                tokenReceiveTimeMs = tokenReceiveDate.getTime();
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        if ((new Date().getTime() - tokenReceiveTimeMs)/1000 < 60*2) {
            return true;
        } else {
            return false;
        }
    }
}
