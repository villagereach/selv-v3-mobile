package mz.org.selv.mobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mz.org.selv.mobile.R;

public class InventoryItemAdjustmentAdapter extends ArrayAdapter<JSONObject> {

    private TextView tvReasonName;
    private TextView tvQuantity;
    private Button btDelete;
    private List<JSONObject> adjustments;

    public InventoryItemAdjustmentAdapter(@NonNull Context context, int resource, @NonNull List<JSONObject> adjustments) {
        super(context, 0, adjustments);
        this.adjustments = adjustments;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stock_managment_inventory_adjustments_row_item, parent, false);
        }

        tvReasonName = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_adjustments_item_reason);
        tvQuantity = (TextView) convertView.findViewById(R.id.tv_stock_management_inventory_adjustments_item_quantity);
        btDelete = (Button) convertView.findViewById(R.id.bt_stock_management_inventory_adjustments_item_delete);
        try{
            tvReasonName.setText(getItem(position).getString("reasonName"));
            tvQuantity.setText(getItem(position).getString("quantity"));
        } catch (JSONException ex){
            ex.printStackTrace();
        }

        return convertView;
    }

    @Override
    public void add(@Nullable JSONObject object) {

        super.add(object);
        adjustments.add(object);

    }

    public List getItems(){
       // return
        return adjustments;
    }

}
