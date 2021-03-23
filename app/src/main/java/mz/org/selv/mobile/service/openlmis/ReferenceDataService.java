package mz.org.selv.mobile.service.openlmis;

import android.content.Context;
import android.content.Entity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.database.Table;
import mz.org.selv.mobile.model.referencedata.Program;


public class ReferenceDataService {
    private Context context;
    private RequestQueue requestQueue;
    public ReferenceDataService(Context context){
        this.context = context;
    }


    public int syncPrograms(){
        syncEntities("https://www.selv.org.mz/api/programs");
        return 1;
    }



    public List<Program> preparePrograms(JSONObject programsJSON){
        List<Program> programs = null;
        try{
            if(programsJSON.getJSONArray("data").length() > 0){

            }
        } catch (JSONException ex){
            return null;
        }
        return programs;
    }

    public int syncFacilities(){
        final String apiEndpoint = "/api/facilities";

        return 1;
    }

    public int syncOrderables(){


        return 1;
    }

    public int syncProcessingPeriods(){
        return 1;
    }


    public int syncAll(){
        syncPrograms();
        return 1;
    }

    public void syncEntities(String uri){

        requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try{
                        JSONArray jsonResponse = new JSONArray(response);
                        new SyncEntities(context).saveEntities(1, jsonResponse);
                        //       progressDialog.dismiss();
                    } catch (JSONException ex){
                        Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    }
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
                //String credentials = String.format("%s:%s", "admin", "password");
                String auth = "Bearer 7f4e1d1b-e513-4044-9664-dedf5e408111";
                //String auth = "Bearer 9f81cba6-0462-42e7-8e32-9fbbd2941b57";
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "client_credentials");
                params.put("username", "admin");
                params.put("password", "password");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
