package mz.org.selv.mobile;

import static android.util.Base64.DEFAULT;
import static mz.org.selv.mobile.BuildConfig.BASE_URL;
import static mz.org.selv.mobile.BuildConfig.CLIENT_ID;
import static mz.org.selv.mobile.BuildConfig.CLIENT_SECRET;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String ACCESS_TOKEN_URI = BASE_URL + "/api/oauth/token";

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_auth);

        Button btLogIn = findViewById(R.id.bt_log_in);
        btLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), R.string.string_logging_in, Toast.LENGTH_SHORT).show();
                EditText username = findViewById(R.id.tx_username);
                EditText password = findViewById(R.id.tx_password);
                obtainAccessToken(LoginActivity.this, username.getText().toString(), password.getText().toString());
            }
        });

        TextView linkTextView = findViewById(R.id.tv_forgot_password);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    void obtainAccessToken(Context context, String username, String password) {
        requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

        StringRequest loginRequest = new StringRequest(Method.POST, ACCESS_TOKEN_URI,
            new Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        try {
                            Toast.makeText(getApplicationContext(), R.string.string_login_success, Toast.LENGTH_SHORT).show();
                            JSONObject responseObject = new JSONObject(response);
                            String accessToken = responseObject.getString("access_token");
                            ((SelvApplication) getApplication()).setAccessToken(accessToken);
                            ((SelvApplication) getApplication()).setUsername(username);
                            ((SelvApplication) getApplication()).setPassword(password);
                            Log.d(this.getClass().toString(), "Login successful, token = " + accessToken);
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            },
            new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), R.string.string_login_success, Toast.LENGTH_SHORT).show();
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
