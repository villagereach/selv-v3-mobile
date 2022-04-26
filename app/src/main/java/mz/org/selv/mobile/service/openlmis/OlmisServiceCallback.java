package mz.org.selv.mobile.service.openlmis;

import org.json.JSONObject;

public interface OlmisServiceCallback {
    void onTokenRefreshed(int response);
    void onUserLoggedIn(JSONObject response);
    void onStockCardSummariesResponse(JSONObject stockCardSummaries);
}
