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


}
