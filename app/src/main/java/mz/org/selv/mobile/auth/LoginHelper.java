package mz.org.selv.mobile.auth;

import static android.util.Base64.DEFAULT;
import static mz.org.selv.mobile.BuildConfig.BASE_URL;
import static mz.org.selv.mobile.BuildConfig.CLIENT_ID;
import static mz.org.selv.mobile.BuildConfig.CLIENT_SECRET;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import mz.org.selv.mobile.MainActivity;
import mz.org.selv.mobile.R;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginHelper {

    private static final String ACCESS_TOKEN_URI = BASE_URL + "/api/oauth/token";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String APP_SHARED_PREFS = "selv_mobile_prefs";

    Context appContext;

    public LoginHelper(Context appContext) {
        this.appContext = appContext;
    }

    /**
     * Use to make an async login request to the auth server. If request is successful,
     * KEY_ACCESS_TOKEN should be set to access token. Only set loginContext if logging in, in order
     * to move to MainActivity after successful login.
     *
     * @param loginContext login context
     */
    public void obtainAccessToken(Context loginContext) {
        RequestQueue requestQueue = Volley.newRequestQueue(appContext);

        StringRequest loginRequest = new StringRequest(Method.POST, ACCESS_TOKEN_URI,
            new Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        try {
                            Toast.makeText(appContext, R.string.string_login_success, Toast.LENGTH_SHORT).show();
                            JSONObject responseObject = new JSONObject(response);
                            String accessToken = responseObject.getString(KEY_ACCESS_TOKEN);
                            SharedPreferences sharedPrefs = appContext.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                            Editor editor = sharedPrefs.edit();
                            editor.putString(KEY_ACCESS_TOKEN, accessToken);
                            editor.apply();

                            Log.d(this.getClass().toString(), "Login successful, token = " + accessToken);

                            if (loginContext != null) {
                                Intent loginIntent = new Intent(loginContext, MainActivity.class);
                                loginContext.startActivity(loginIntent);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            },
            new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(appContext, R.string.string_login_failed, Toast.LENGTH_SHORT).show();
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
                SharedPreferences sharedPrefs = appContext.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "password");
                params.put(KEY_USERNAME, sharedPrefs.getString(KEY_USERNAME, ""));
                params.put(KEY_PASSWORD, sharedPrefs.getString(KEY_PASSWORD, ""));
                return params;
            }
        };
        requestQueue.add(loginRequest);
    }
}
