package mz.org.selv.mobile.service.openlmis;

import static android.util.Base64.DEFAULT;
import static mz.org.selv.mobile.BuildConfig.CLIENT_ID;
import static mz.org.selv.mobile.BuildConfig.CLIENT_SECRET;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import mz.org.selv.mobile.R;

public class OlmisAuthService {

    Context appContext;
    OlmisServiceCallback OlmisServiceCallbackListener;
    private static final String ACCESS_TOKEN_URI_PATH = "/api/oauth/token";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_USER_ID = "referenceDataUserId";
    public static final String KEY_HOME_FACILITY_ID = "homeFacilityId";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN_EXPIRATION = "expires_in";
    public static final String KEY_HOME_FACILITY_CODE = "homeFacilityCode";
    public static final String KEY_HOME_FACILITY_NAME = "homeFacilityName";
    public static final String APP_SHARED_PREFS = "selv_mobile_prefs";
    public static final String KEY_TOKEN_RECEIVE_TIME = "token_datetime";
    SharedPreferences sharedPrefs;
    RequestQueue requestQueue;


    public OlmisAuthService(Context appContext, OlmisServiceCallback OlmisServiceCallbackListener) {
        this.appContext = appContext;
        this.OlmisServiceCallbackListener = OlmisServiceCallbackListener;
        sharedPrefs = this.appContext.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(appContext);
    }

    public void login(String serverUrl, String username, String password) {
        StringRequest loginRequest = new StringRequest(Request.Method.POST, serverUrl + ACCESS_TOKEN_URI_PATH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject responseObject = new JSONObject(response);
                                OlmisServiceCallbackListener.onUserLoggedIn(responseObject);
                            } catch (JSONException ex) {

                                ex.printStackTrace();
                            }
                        }
                    }
                },
                error -> {
                    Toast.makeText(appContext, R.string.string_login_failed, Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                    Log.e("error", "" + error);
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String plainCreds = CLIENT_ID + ":" + CLIENT_SECRET; // client:secret
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
                params.put(KEY_USERNAME, username);
                params.put(KEY_PASSWORD, password);
                return params;
            }
        };

        requestQueue.add(loginRequest);
    }

    public void refreshToken() {
        RequestQueue requestQueue = Volley.newRequestQueue(appContext);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://test.selv.org.mz" + ACCESS_TOKEN_URI_PATH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject responseObject = new JSONObject(response);
                                if (OlmisServiceCallbackListener != null) {
                                    System.out.println(responseObject.toString());
                                    OlmisServiceCallbackListener.onTokenRefreshed(1);
                                }

                            } catch (JSONException ex) {
                                System.out.println("User NOT Logged In......");
                                ex.printStackTrace();
                            }
                        }
                    }
                },
                error -> {
                    Toast.makeText(appContext, R.string.string_login_failed, Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                    Log.e("error", "" + error);
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String plainCreds = CLIENT_ID + ":" + CLIENT_SECRET; // client:secret
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
                params.put(KEY_USERNAME, "admin");
                params.put(KEY_PASSWORD, "password");
                return params;
            }
        };

        requestQueue.add(loginRequest);

    }
}
