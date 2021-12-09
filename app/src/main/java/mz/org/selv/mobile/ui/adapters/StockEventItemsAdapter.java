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

public class StockEventItemsAdapter extends ArrayAdapter<JSONObject> {
    private Context context;
    private String facilityId;
    private String programId;
    private String action;
    private List lineItems;

    public StockEventItemsAdapter(@NonNull Context context, @NonNull List<JSONObject> lineItems, String action) {
        super(context, 0, lineItems);
        this.action = action;
        this.lineItems = lineItems;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        JSONObject lineItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_row_item, parent, false);
        }

        TextView tvProduct = convertView.findViewById(R.id.tv_stock_event_product);
        TextView tvLotCode = convertView.findViewById(R.id.tv_stock_event_lot_code);
        TextView tvQuantity = convertView.findViewById(R.id.tv_stock_event_row_item_quantity);
        TextView tvStockOnHand = convertView.findViewById(R.id.tv_stock_event_row_item_quantity);
        TextView tvReason = convertView.findViewById(R.id.tv_stock_event_row_item_reason);
        TextView tvSourceOrDestination = convertView.findViewById(R.id.tv_stock_event_row_item_source_or_destination);
        TextView tvOccurredDate = convertView.findViewById(R.id.tv_stock_event_row_item_occurred_date);
        TextView labelSourceOrDestination = convertView.findViewById(R.id.label_event_row_item_source_or_destination);
        ImageView ivStatus = convertView.findViewById(R.id.iv_event_row_item_status);

        try{
            tvProduct.setText(lineItem.getString("orderableName"));
            tvLotCode.setText(lineItem.getString("lotCode"));
            tvOccurredDate.setText(lineItem.getString("occurredDate"));
            tvQuantity.setText(lineItem.getString("quantity"));

            if(lineItem.has("reasonComments")){
                tvReason.setText(lineItem.getString("reasonName")+": "+lineItem.getString("reasonComments"));
            }  else {
                tvReason.setText(lineItem.getString("reasonName"));
            }

            if(action.equals("receive")){
                labelSourceOrDestination.setText(R.string.string_source);
            } else if (action.equals("issue")){
                labelSourceOrDestination.setText(R.string.string_destination);
            }

            if(lineItem.has("sourceOrDestinationComments")){
                tvSourceOrDestination.setText(lineItem.getString("sourceOrDestinationName")+" "+lineItem.getString("sourceOrDestinationComments"));
            }  else {
                tvSourceOrDestination.setText(lineItem.getString("sourceOrDestinationName"));
            }

            if(lineItem.has("status")){

            }

            if(lineItem.has("stockOnHand")){
                tvStockOnHand.setText(lineItem.getString("stockOnHand"));
            }

            if(lineItem.has("quantity")){
                tvQuantity.setText(lineItem.getString("quantity"));
            }

        } catch (JSONException exception){
            exception.printStackTrace();
        }

        return convertView;
    }

    public List getItems(){
        return lineItems;
    }
}
