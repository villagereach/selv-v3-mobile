package mz.org.selv.mobile.ui.adapters;

import android.content.ContentValues;
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
        TextView tvExpirationDate = convertView.findViewById(R.id.tv_stock_event_row_item_expiration_date);
        TextView tvQuantity = convertView.findViewById(R.id.tv_stock_event_row_item_quantity);
        TextView tvStockOnHand = convertView.findViewById(R.id.tv_stock_event_row_item_soh);
        TextView tvReason = convertView.findViewById(R.id.tv_stock_event_row_item_reason);
        TextView tvSourceOrDestination = convertView.findViewById(R.id.tv_stock_event_row_item_source_or_destination);
        TextView tvOccurredDate = convertView.findViewById(R.id.tv_stock_event_row_item_occurred_date);
        ImageView ivStatus = convertView.findViewById(R.id.iv_sotck_event_row_item_status);

        try{
            System.out.println(lineItem.toString());
            tvProduct.setText(lineItem.getString("orderableName"));
            tvLotCode.setText(lineItem.getString("lotCode"));
            tvExpirationDate.setText(lineItem.getString("expirationDate"));
            tvOccurredDate.setText(lineItem.getString("occurredDate"));
            tvQuantity.setText(lineItem.getString("quantity"));

            if(lineItem.has("reasonComments")){
                tvReason.setText(lineItem.getString("reasonName")+": "+lineItem.getString("reasonComments"));
            }  else {
                tvReason.setText(lineItem.getString("reasonName"));
            }

            if(lineItem.has("sourceOrDestinationComments")){
                tvSourceOrDestination.setText(lineItem.getString("sourceOrDestinationName")+" "+lineItem.getString("sourceOrDestinationComments"));
            }  else {
                tvSourceOrDestination.setText(lineItem.getString("sourceOrDestinationName"));
            }

            tvStockOnHand.setText(lineItem.getString("stockOnHand"));
            tvQuantity.setText(lineItem.getString("quantity"));
        } catch (JSONException exception){
            exception.printStackTrace();
        }

        return convertView;
    }

    public List getItems(){
        return lineItems;
    }
}
