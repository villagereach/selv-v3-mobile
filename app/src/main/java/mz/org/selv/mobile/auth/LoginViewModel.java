package mz.org.selv.mobile.auth;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.util.Map;

import mz.org.selv.mobile.model.auth.User;
import mz.org.selv.mobile.service.auth.AuthService;
import mz.org.selv.mobile.service.config.ConfigurationService;
import mz.org.selv.mobile.service.openlmis.OlmisAuthService;
import mz.org.selv.mobile.service.openlmis.OlmisServiceCallback;

public class LoginViewModel extends AndroidViewModel implements OlmisServiceCallback {

    private AuthService authService;
    private MutableLiveData<Boolean> isLoggedIn;
    private String serverUrl;
    private String username;
    private String password;

    private static final String FACILITIES_BASE_URI =  "/api/facilities/";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_USER_ID = "referenceDataUserId";
    public static final String KEY_HOME_FACILITY_ID = "homeFacilityId";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN_EXPIRATION = "expires_in";
    public static final String KEY_HOME_FACILITY_CODE = "homeFacilityCode";
    public static final String KEY_HOME_FACILITY_NAME = "homeFacilityName";
    public static final String APP_SHARED_PREFS = "selv_mobile_prefs";


    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getSession() {
        if(isLoggedIn == null){
            isLoggedIn = new MutableLiveData<Boolean>();
        }
        return isLoggedIn;
    }


    public void login(String serverUrl, String username, String password){
        if(!localLogin(serverUrl, username, password)){
            remoteLogin(serverUrl, username, password);
        }
    }

    public boolean localLogin(String serverUrl, String username, String password){
        this.username = username;
        this.password = password;
        this.serverUrl = serverUrl;
        authService = new AuthService(getApplication());
        User user = authService.localLogin(serverUrl, username, password);
        if(user != null){
            saveSession(serverUrl, username, password,null);
            return true;
        }  else {
            return false;
        }
    }

    public void remoteLogin(String serverUrl, String username, String password){
        OlmisAuthService olmisAuthService = new OlmisAuthService(getApplication(), this);
        olmisAuthService.login(serverUrl,username, password);
    }

    public void saveSession(String serverUrl, String username, String password, JSONObject userDetails){
        AuthService authService = new AuthService(getApplication());
        Map session = authService.saveSession(serverUrl, username, password, userDetails);
        SharedPreferences sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, session.get("token").toString());
        editor.putString(KEY_USER_ID, "userId");
        editor.apply();
        getSession().setValue(true);
    }

    public String getLastUsedServer(){
        ConfigurationService configurationService = new ConfigurationService(getApplication());
        return configurationService.lastUsedService();
    }

    @Override
    public void onTokenRefreshed(int loginResult) {

    }

    @Override
    public void onUserLoggedIn(JSONObject response) {
        saveSession(serverUrl, username, password, response);
    }

    @Override
    public void onStockCardSummariesResponse(JSONObject stockCardSummaries) {

    }
}
