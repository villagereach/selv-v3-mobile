package mz.org.selv.mobile.service.openlmis.stockmanagement;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mz.org.selv.mobile.service.openlmis.OlmisServiceCallback;

public class OlmisStockManagementService {
    Context appContext;
    OlmisServiceCallback olmisServiceCallback;
    private static final String STOCK_CARD_SUMMARIES_URI_PATH = "/api/stockmanagement/stockCards";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String APP_SHARED_PREFS = "selv_mobile_prefs";
    public static final String KEY_TOKEN_RECEIVE_TIME = "token_datetime";
    public static final String KEY_FACILITY_ID = "token_datetime";
    public static final String KEY_PROGRAM_ID = "token_datetime";

    SharedPreferences sharedPrefs;
    RequestQueue requestQueue;


    public OlmisStockManagementService(Context appContext, OlmisServiceCallback olmisServiceCallbackListener) {
        this.appContext = appContext;
        this.olmisServiceCallback = olmisServiceCallbackListener;
        sharedPrefs = this.appContext.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(appContext);
    }

    public void getStockCardSummaries(String serverUrl, String facilityId, String programId) {
        StringRequest stockCardSummariesRequest = new StringRequest(Request.Method.GET, serverUrl+ STOCK_CARD_SUMMARIES_URI_PATH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject responseObject = new JSONObject(response);
                                olmisServiceCallback.onStockCardSummariesResponse(responseObject);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                String auth = "Bearer " + sharedPrefs.getString(KEY_ACCESS_TOKEN, "");
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(KEY_FACILITY_ID, facilityId);
                params.put(KEY_PROGRAM_ID, programId);
                return params;
            }
        };
        requestQueue.add(stockCardSummariesRequest);
    }
}
