package mz.org.selv.mobile.ui.auth;

import static android.util.Base64.DEFAULT;
import static mz.org.selv.mobile.BuildConfig.BASE_URL;
import static mz.org.selv.mobile.BuildConfig.CLIENT_ID;
import static mz.org.selv.mobile.BuildConfig.CLIENT_SECRET;

import android.app.Application;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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
import mz.org.selv.mobile.SelvApplication;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthViewModel extends AndroidViewModel {

  private static final String ACCESS_TOKEN_URI = BASE_URL + "/api/oauth/token";

  private RequestQueue requestQueue;
  public AuthViewModel(@NonNull Application application) {
    super(application);
  }

  void obtainAccessToken(String username, String password) {
    requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

    StringRequest loginRequest = new StringRequest(Method.POST, ACCESS_TOKEN_URI,
        new Listener<String>() {
          @Override
          public void onResponse(String response) {
            if (response != null) {
              try {
                JSONObject responseObject = new JSONObject(response);
                String accessToken = responseObject.getString("access_token");
                ((SelvApplication) getApplication()).setAccessToken(accessToken);
                System.out.println("CSA Logged in successfully, token = " + accessToken);
              } catch (JSONException ex) {
                ex.printStackTrace();
              }
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
        params.put("username", username);
        params.put("password", password);
        return params;
      }
    };
    requestQueue.add(loginRequest);
  }
}
