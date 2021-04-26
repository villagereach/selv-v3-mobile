package mz.org.selv.mobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mz.org.selv.mobile.R;


public class InventoryListAdapter extends ArrayAdapter<JSONObject> {
    public InventoryListAdapter(@NonNull Context context, @NonNull List<JSONObject> lineItems) {
        super(context, 0, lineItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.inventory_row_item, parent, false);
        }

        TextView tvProgram = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_list_row_item_program);
        TextView tvOccurredDate = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_list_row_item_date);
        TextView tvSignature = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_list_row_item_signature);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_list_row_item_status);
        ImageView ivStatus = (ImageView) convertView.findViewById(R.id.iv_stock_management_inventory_list_row_item_status);

        try{
            tvProgram.setText(getItem(position).getString("programName"));
            tvOccurredDate.setText(getItem(position).getString("occurredaDate"));
            tvSignature.setText(getItem(position).getString("signature"));
           // tvStatus.setText(getItem(position).getString("status"));
        } catch (JSONException ex){
            ex.printStackTrace();
        }

        return convertView;
    }
}
