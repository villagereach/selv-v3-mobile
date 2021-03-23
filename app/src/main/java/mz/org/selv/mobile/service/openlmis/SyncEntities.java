package mz.org.selv.mobile.service.openlmis;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.database.Database;
import mz.org.selv.mobile.database.Table;
import mz.org.selv.mobile.model.referencedata.Program;

public class SyncEntities {

    private RequestQueue requestQueue;
    private Context context;

    public SyncEntities(Context context) {
        this.context = context;
    }

    public JSONObject submit(int type, String uri, JSONObject data){

        //Progresss Dialog
     //   ProgressDialog progressDialog = new ProgressDialog(context);
    //    progressDialog.setCancelable(false);
      //  progressDialog.setMessage("Conectando...");

        JSONObject result = new JSONObject();
        requestQueue = Volley.newRequestQueue(context);
        int method;

        if(type == 1){
            method = Request.Method.GET;
        } else {
            method = Request.Method.POST;
        }

        StringRequest stringRequest = new StringRequest(method, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("response: "+response);
                if (!response.equals(null)) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        result.put("status", 200);
                        result.put("data", jsonResponse);
                        result.put("message", context.getString(R.string.string_ok));
                 //       progressDialog.dismiss();
                    } catch (JSONException ex){
                        Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    if(error.networkResponse != null){

                        result.put("status", error.networkResponse.statusCode);
                    } else {
                        result.put("status", -1);
                    }
                } catch (JSONException ex){
                    //
                }
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                //String credentials = String.format("%s:%s", "admin", "password");
                String auth = "Bearer 59ae27fe-21ca-4fcd-8e8c-77d93085b61c";
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
        return result;
    }

    public void saveEntities(int entity, JSONArray objects){
        List<Table> entities = readEntities(entity, objects);
        if(entities.size() > 0){
            //remove entites saved
            saveEntitiesToDatabase(context, entities, false);
        }
    }

    public List<Table> readEntities(int entity, JSONArray entities){
        List<Table> entitiesTable = new ArrayList<Table>();
        for(int i = 0; i < entities.length(); i++){
            try{
                Program program = new Program();

                program.setCode(entities.getJSONObject(i).getString(Database.Program.COLUMN_CODE));
                program.setName(entities.getJSONObject(i).getString(Database.Program.COLUMN_CODE));
                program.setDescription(entities.getJSONObject(i).getString(Database.Program.COLUMN_CODE));
                program.setUuid(entities.getJSONObject(i).getString("id"));
                program.setActive(entities.getJSONObject(i).getBoolean(Database.Program.COLUMN_ACTIVE));
                entitiesTable.add(program);
            } catch (JSONException ex){
                ex.printStackTrace();
            }
        }

        return entitiesTable;
    }

    public boolean saveEntitiesToDatabase(Context context, List<Table> entities, boolean isUpdate){

            Database database = new Database(context);
            database.open();
            boolean inserted = true;
            if(isUpdate){
                //
            } else {
                database.deleteAll(entities.get(0).getTableName());
                for(int i = 0; i < entities.size(); i++){
                    if(database.insert(entities.get(i)) < 0){
                        inserted = false;
                    }
                }
            }
            database.close();
        return inserted;
    }

}


