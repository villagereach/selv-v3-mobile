package mz.org.selv.mobile.ui.home;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;

import org.json.JSONObject;

import mz.org.selv.mobile.service.openlmis.OlmisServiceCallback;
import mz.org.selv.mobile.service.openlmis.OlmisAuthService;

public class HomeViewModel extends AndroidViewModel implements OlmisServiceCallback {

    SharedPreferences sharedPrefs;
    OlmisAuthService olmisAuthService;
    public HomeViewModel(Application application) {
        super(application);

    }



    public void refreshToken(){
        OlmisAuthService olmisAuthService = new OlmisAuthService(getApplication(), this);
        olmisAuthService.refreshToken();
    }


    @Override
    public void onTokenRefreshed(int loginResult) {
            //System.out.println("Wooorking...");
    }

    @Override
    public void onUserLoggedIn(JSONObject response) {

    }

    @Override
    public void onStockCardSummariesResponse(JSONObject stockCardSummaries) {

    }
}

