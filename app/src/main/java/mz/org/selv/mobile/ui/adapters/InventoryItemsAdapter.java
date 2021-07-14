package mz.org.selv.mobile.ui.adapters;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.stockmanagement.InventoryItemDialog;

public class InventoryItemsAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private String selectedFacilityId;
    private String selectedProgramId;
    private List lineItems;
    public InventoryItemsAdapter(@NonNull Context context, @NonNull List<JSONObject> lineItems, String selectedFacilityId, String selectedProgramId) {
        super(context, 0, lineItems);
        this.selectedFacilityId = selectedFacilityId;
        this.selectedProgramId = selectedProgramId;
        this.lineItems = lineItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        JSONObject lineItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stock_management_inventory_line_item, parent, false);
        }

        TextView tvProduct = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_line_item_product);
        TextView tvStockOnHand = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_line_item_soh);
        TextView tvLot = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_line_item_lot);
        TextView tvExpirationDate = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_linte_item_expiration_date);
        TextView tvPhysicalStock = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_line_item_physical_stock);
        TextView tvBalance = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_linte_item_stock_balance);

        try {
            tvExpirationDate.setText(lineItem.getString("expirationDate"));
            tvLot.setText(lineItem.getString("lotCode"));
            tvProduct.setText(lineItem.getString("orderableName"));
            tvPhysicalStock.setText(lineItem.getString("physicalStock"));
            if (lineItem.getString("stockOnHand").isEmpty()) {
                tvStockOnHand.setText("");
                tvBalance.setText("");
            } else if (lineItem.getInt("stockOnHand") >= 0) {
                tvStockOnHand.setText("" + lineItem.getInt("stockOnHand"));
                if (!lineItem.getString("physicalStock").equals("")) {
                    tvBalance.setText("" + (lineItem.getInt("physicalStock") - lineItem.getInt("stockOnHand")));
                    if(lineItem.getInt("stockOnHand") != lineItem.getInt("physicalStock")){

                    }
                }
            }

            if(lineItem.has("adjustments")){
                JSONArray adjustments = lineItem.getJSONArray("adjustments");

                if(adjustments != null && adjustments.length() > 0){
                    for(int i = 0; i< adjustments.length(); i++){
                        JSONObject adjustment = new JSONObject();
                        //adjustment.put("reason", )
                    }
                }
            }


        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return convertView;

    }

    public List getItems(){
        return lineItems;
    }
}
