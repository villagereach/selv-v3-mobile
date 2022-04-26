package mz.org.selv.mobile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mz.org.selv.mobile.service.openlmis.OlmisServiceCallback;
import mz.org.selv.mobile.service.openlmis.OlmisAuthService;

public class MainActivityViewModel extends AndroidViewModel implements OlmisServiceCallback {

    SharedPreferences sharedPrefs;
    OlmisAuthService olmisAuthService;
    public static final String APP_SHARED_PREFS = "selv_mobile_prefs";
    public static final String KEY_TOKEN_EXPIRATION = "expires_in";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_TOKEN_RECEIVE_TIME = "token_datetime";
    private static final int ACTION_REFRESH_TOKEN = 1;
    private static final int ACTION_UPDATE_USER = 2;
    private int action;
    private int retryCounter = 0;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        sharedPrefs = getApplication().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void setupApplication() {
        if (validateToken()) {

        } else {

        }
    }

    @Override
    public void onTokenRefreshed(int loginResult) {
        if (loginResult == 1) { // token refreshed
            if (retryCounter < 1) {
                System.out.println("try again later");
                setupApplication();
            } else {
                System.out.println("try again later");
            }

        } else {
            System.out.println("method token not refreshed");
        }
    }

    @Override
    public void onUserLoggedIn(JSONObject response) {

    }

    @Override
    public void onStockCardSummariesResponse(JSONObject stockCardSummaries) {

    }

    public boolean validateToken() {
        int tokenExpiration = sharedPrefs.getInt(KEY_TOKEN_EXPIRATION, 0);
        String tokenTimeObtainedString = sharedPrefs.getString(KEY_TOKEN_RECEIVE_TIME, "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!tokenTimeObtainedString.equals("")) {
            try {
                Date timeObtainedToken = dateFormat.parse(tokenTimeObtainedString);
                Date currentTime = new Date();
                //get time difference in seconds
                long timeDifference = (currentTime.getTime() - timeObtainedToken.getTime()) / 6000;
                //check if token expired

                if (timeDifference > tokenExpiration) {
                    System.out.println("Token Expired Getting new Token");
                    //refresh token
                    olmisAuthService = new OlmisAuthService(getApplication(),this);
                    olmisAuthService.refreshToken();
                    //increase counter to avoid loop
                    retryCounter++;
                    return false;
                } else {
                    return true;
                }

            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        retryCounter++;
        return false;
    }
}
