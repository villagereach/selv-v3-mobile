package mz.org.selv.mobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mz.org.selv.mobile.R;

public class InventoryItemsAdapter extends ArrayAdapter<JSONObject> {

    public InventoryItemsAdapter(@NonNull Context context, @NonNull List<JSONObject> lineItems) {
        super(context, 0, lineItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        JSONObject lineItem = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stock_management_inventory_line_item, parent, false);
        }

        TextView tvProduct = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_line_item_product);
        TextView tvStockOnHand = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_line_item_soh);
        TextView tvLot = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_line_item_lot);
        TextView tvExpirationDate = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_linte_item_expiration_date);
        TextView tvPhysicalStock = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_line_item_physical_stock);
        TextView tvBalance = (TextView)convertView.findViewById(R.id.tv_stock_management_inventory_linte_item_stock_balance);
        Button btAdjustments = (Button) convertView.findViewById(R.id.bt_stock_management_inventory_linte_item_adjustments);

        //tvBalance.setText(lineItem.);
        try{
            tvExpirationDate.setText(lineItem.getString("expirationDate"));
            tvLot.setText(lineItem.getString("lotCode"));
            tvProduct.setText(lineItem.getString("orderableName"));
            tvPhysicalStock.setText(lineItem.getInt("physicalStock")+"");


            if (lineItem.getInt("stockOnHand") < 0){
                tvStockOnHand.setText("");
                tvBalance.setText("");
                btAdjustments.setText(R.string.string_adjustments);
                btAdjustments.setEnabled(false);
            }
        } catch (JSONException ex){

        }

        return convertView;
    }
}
