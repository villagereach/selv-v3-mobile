package mz.org.selv.mobile.service.openlmis;

import android.content.Context;
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
import mz.org.selv.mobile.model.Entity;
import mz.org.selv.mobile.model.referencedata.FacilityTypeApprovedProductAndProgram;
import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.referencedata.Orderable;
import mz.org.selv.mobile.model.referencedata.Program;
import mz.org.selv.mobile.model.referencedata.TradeItem;
import mz.org.selv.mobile.model.stockmanagement.Reason;
import mz.org.selv.mobile.model.stockmanagement.ValidReasons;

public class SyncEntities {

    private RequestQueue requestQueue;
    private Context context;

    public SyncEntities(Context context) {
        this.context = context;
    }

    public JSONObject submit(int type, String uri, JSONObject data) {

        JSONObject result = new JSONObject();
        requestQueue = Volley.newRequestQueue(context);
        int method;

        if (type == 1) {
            method = Request.Method.GET;
        } else {
            method = Request.Method.POST;
        }

        StringRequest stringRequest = new StringRequest(method, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("response: " + response);
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        result.put("status", 200);
                        result.put("data", jsonResponse);
                        result.put("message", context.getString(R.string.string_ok));
                        //       progressDialog.dismiss();
                    } catch (JSONException ex) {
                        Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse != null) {

                        result.put("status", error.networkResponse.statusCode);
                    } else {
                        result.put("status", -1);
                    }
                } catch (JSONException ex) {
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
                String auth = "Bearer 17eb1270-4870-478e-a9c5-afca0cbf3d3d";
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

    public void saveEntities(int entity, JSONArray objects) {
        List<Table> entities = readEntities(entity, objects);
        if (entities.size() > 0) {
            //remove entites saved
            System.out.println("Saving entities");
            saveEntitiesToDatabase(context, entities, false);
        } else {
            System.out.println("No entities");
        }
    }

    public List<Table> readEntities(int entity, JSONArray entities) {
        ArrayList<Table> result = new ArrayList();

        switch (entity) {
            case Entity.PROGRAM:
                for (int i = 0; i < entities.length(); i++) {
                    Program program = new Program();
                    try {
                        program.setUuid(entities.getJSONObject(i).getString(Database.Program.COLUMN_UUID));
                        program.setCode(entities.getJSONObject(i).getString(Database.Program.COLUMN_CODE));
                        program.setName(entities.getJSONObject(i).getString(Database.Program.COLUMN_NAME));
                        program.setActive(entities.getJSONObject(i).getString(Database.Program.COLUMN_ACTIVE));
                        program.setDescription(entities.getJSONObject(i).getString(Database.Program.COLUMN_DESCRIPTION));
                        result.add(program);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                break;

            case Entity.LOTS:
                for (int i = 0; i < entities.length(); i++) {
                    Lot lot = new Lot();
                    try {
                        System.out.println(entities.getJSONObject(i));
                        lot.setId(entities.getJSONObject(i).getString(Database.Lot.COLUMN_UUID));
                        lot.setOrderableId(entities.getJSONObject(i).getString(Database.Lot.COLUMN_TRADE_ITEM_ID));
                        lot.setLotCode(entities.getJSONObject(i).getString(Database.Lot.COLUMN_CODE));
                        lot.setExpirationDate(entities.getJSONObject(i).getString(Database.Lot.COLUMN_EXPIRATION_DATE));
                        result.add(lot);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                break;

            case Entity.FACILITY_TYPE_APPROVED_PRODUCTS:
                try {
                    for (int i = 0; i < entities.length(); i++) {
                        FacilityTypeApprovedProductAndProgram productAndProgram = new FacilityTypeApprovedProductAndProgram();
                        TradeItem tradeItem = new TradeItem();
                        productAndProgram.setId(entities.getJSONObject(i).getString(Database.FacilityTypeApprovedProductAndProgram.COLUMN_NAME_UUID));
                        //due to JSON Format
                        productAndProgram.setOrderableId(entities.getJSONObject(i).getJSONObject("orderable").getString(Database.Orderable.COLUMN_UUID));
                        productAndProgram.setProgramId(entities.getJSONObject(i).getJSONObject("program").getString(Database.Program.COLUMN_UUID));
                        productAndProgram.setFacilityTypeId(entities.getJSONObject(i).getJSONObject("facilityType").getString(Database.FacilityType.COLUMN_UUID));
                        tradeItem.setOrderableId(entities.getJSONObject(i).getJSONObject("orderable").getString(Database.Orderable.COLUMN_UUID));
                        tradeItem.setTradeItemId(entities.getJSONObject(i).getJSONObject("orderable").getJSONObject("identifiers").getString("tradeItem"));

                        result.add(productAndProgram);
                        result.add(tradeItem);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                break;

            case Entity.ORDERABLES:
                try {
                    for (int i = 0; i < entities.length(); i++) {
                        Orderable orderable = new Orderable();
                        orderable.setUuid(entities.getJSONObject(i).getString(Database.Orderable.COLUMN_UUID));
                        orderable.setName(entities.getJSONObject(i).getString(Database.Orderable.COLUMN_NAME));
                        orderable.setCode(entities.getJSONObject(i).getString(Database.Orderable.COLUMN_CODE));
                        result.add(orderable);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;

            case Entity.REASON:
                try {
                    for (int i = 0; i < entities.length(); i++) {
                        Reason reason = new Reason();
                        reason.setId(entities.getJSONObject(i).getString(Database.Reason.COLUMN_NAME_UUID));
                        reason.setCategory(entities.getJSONObject(i).getString(Database.Reason.COLUMN_NAME_CATEGORY));
                        reason.setName(entities.getJSONObject(i).getString(Database.Reason.COLUMN_NAME_NAME));
                        reason.setType(entities.getJSONObject(i).getString(Database.Reason.COLUMN_NAME_TYPE));
                        result.add(reason);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;

            case Entity.VALID_REASONS:
                try {
                    for (int i = 0; i < entities.length(); i++) {
                        ValidReasons validReason = new ValidReasons();
                        validReason.setId(entities.getJSONObject(i).getString(Database.ValidReasons.COLUMN_NAME_UUID));
                        validReason.setProgramId(entities.getJSONObject(i).getJSONObject("program").getString(Database.Program.COLUMN_UUID));
                        validReason.setFacilitytypeId(entities.getJSONObject(i).getJSONObject("facilityType").getString(Database.FacilityType.COLUMN_UUID));
                        validReason.setReasonId(entities.getJSONObject(i).getJSONObject("reason").getString(Database.Reason.COLUMN_NAME_UUID));
                        result.add(validReason);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
        }

        return result;
}

    public boolean saveEntitiesToDatabase(Context context, List<Table> entities, boolean isUpdate) {

        Database database = new Database(context);
        database.open();
        boolean inserted = true;
        if (isUpdate) {
            //
        } else {
            database.deleteAll(entities.get(0).getTableName());
            for (int i = 0; i < entities.size(); i++) {
                if (database.insert(entities.get(i)) < 0) {
                    inserted = false;
                } else {
                    //inserted
                }
            }
        }
        database.close();
        return inserted;
    }

}


