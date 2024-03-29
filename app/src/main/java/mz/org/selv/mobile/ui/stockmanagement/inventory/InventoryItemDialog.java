package mz.org.selv.mobile.ui.stockmanagement.inventory;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.ui.adapters.InventoryItemAdjustmentAdapter;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.InventoryViewModel;

public class InventoryItemDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {


    private InventoryViewModel inventoryViewModel;
    private AutoCompleteTextView acReason;
    private AutoCompleteTextView acProduct;
    private AutoCompleteTextView acLotNumber;
    private EditText etPhysicalStock;
    private EditText etAdjustmentQuantity;
    private EditText etExpirationDate;
    private EditText etTheoricStock;
    private TextView tvBalance;
    private TextView tvAdjustedStock;
    private ListView lvAdjustments;
    private Button btSave;
    private Button btCancel;
    private Button btAddReason;
    private LinearLayout llStockBalanceInfo;
    private TextInputLayout tilTheoricStock;
    private int itemPosition;



    public static InventoryItemDialog newInstance() {
        return new InventoryItemDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        View view = inflater.inflate(R.layout.dialog_inventory_item, container, false);

        btSave = (Button) view.findViewById(R.id.bt_stock_management_inventory_item_dialog_save);
        tilTheoricStock = (TextInputLayout) view.findViewById(R.id.til_stock_management_inventory_dialog_theoric_stock);
        btAddReason = (Button) view.findViewById(R.id.bt_stock_management_inventory_item_dialog_add_reason);
        btCancel = (Button) view.findViewById(R.id.bt_stock_management_inventory_item_dialog_cancel);
        etPhysicalStock = (EditText) view.findViewById(R.id.et_stock_management_inventory_dialog_physical_stock);
        acProduct = (AutoCompleteTextView) view.findViewById(R.id.ac_stock_management_inventory_item_dialog_product);
        etExpirationDate = (EditText) view.findViewById(R.id.et_stock_management_inventory_item_dialog_expiration_date);
        etAdjustmentQuantity = (EditText) view.findViewById(R.id.et_stock_management_inventory_item_dialog_adjustment_quantity);
        acLotNumber = (AutoCompleteTextView) view.findViewById(R.id.ac_stock_management_inventory_item_dialog_lot);
        tvBalance = (TextView) view.findViewById(R.id.tv_stock_management_inventory_item_dialog_balance);
        tvAdjustedStock = (TextView) view.findViewById(R.id.tv_stock_management_inventory_item_dialog_adjusted_quantity);
        acReason = (AutoCompleteTextView) view.findViewById(R.id.ac_stock_management_inventory_item_dialog_reason);
        lvAdjustments = (ListView) view.findViewById(R.id.lv_stock_management_inventory_item_dialog_reason);
        llStockBalanceInfo = (LinearLayout) view.findViewById(R.id.ll_stock_management_inventory_dialog_stock_balance_info);

        //AC Adapters

        List reasons = inventoryViewModel.getReasonNames(getArguments().getString("facilityTypeId"), getArguments().getString("programId"));
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, reasons);
        reasonAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        acReason.setAdapter(reasonAdapter);

        if(getArguments().getBoolean("newItem")){ // add product dialog
            List orderables = inventoryViewModel.getOrderables( getArguments().getString("programId"), getArguments().getString("facilityTypeId"));
            ArrayAdapter<String> orderableAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, orderables);
            acProduct.setAdapter(orderableAdapter);
            acProduct.setEnabled(true);
            tilTheoricStock.setVisibility(View.GONE);
            etExpirationDate.setEnabled(false);
            llStockBalanceInfo.setVisibility(View.GONE);

        } else { // view product dialog
            acProduct.setText(getArguments().getString("orderableName"));
            acProduct.setEnabled(false);
            acLotNumber.setText(getArguments().getString("lotCode"));
            acLotNumber.setEnabled(false);
            //tvBalance.setText("" + (getArguments().getString("physicalStock") - getArguments().getString("stockOnHand")));
            etTheoricStock.setText(""+getArguments().getString("stockOnHand"));
            tilTheoricStock.setVisibility(View.VISIBLE);
            etExpirationDate.setText(getArguments().getString("expirationDate"));
            etExpirationDate.setEnabled(false);
            etPhysicalStock.setText(getArguments().getString("physicalStock"));
            llStockBalanceInfo.setVisibility(View.VISIBLE);
            if(getArguments().containsKey("adjustments")){
                try {

                    List<JSONObject> adjustmentsList = new ArrayList<>();

                    JSONArray adjustments = new JSONArray(getArguments().getString("adjustments"));
                    for (int i = 0; i < adjustments.length(); i++){
                        adjustmentsList.add(adjustments.getJSONObject(i));
                    }
                    InventoryItemAdjustmentAdapter adjustmentAdapter = new InventoryItemAdjustmentAdapter(getActivity(), R.layout.support_simple_spinner_dropdown_item, adjustmentsList);
                    lvAdjustments.setAdapter(adjustmentAdapter);

                } catch (JSONException exception) {
                    exception.printStackTrace();
                }

            }
        }

        etPhysicalStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!getArguments().getBoolean("newItem") && !s.toString().isEmpty()){

                    int theoricStock = 0;

                    if(!etTheoricStock.getText().toString().equals("")){
                        theoricStock = Integer.parseInt(etTheoricStock.getText().toString());
                    }

                    int currentAdjustedQuantity = Integer.parseInt(tvAdjustedStock.getText().toString());
                    int currentBalance =  Integer.parseInt(s.toString()) - currentAdjustedQuantity - theoricStock;
                    tvBalance.setText(""+currentBalance);
                } else {
                    tvBalance.setText("");
                }
            }
        });

        acProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List lotCodes = inventoryViewModel.getLots(parent.getItemAtPosition(position).toString());
                acLotNumber.setText("");
                ArrayAdapter<String> lotAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, lotCodes);
                acLotNumber.setAdapter(lotAdapter);
            }
        });

        acLotNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lot lot = inventoryViewModel.getLotByCode(parent.getItemAtPosition(position).toString());
                etExpirationDate.setText(lot.getExpirationDate());
            }
        });


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray adjustments = new JSONArray();
                InventoryItemAdjustmentAdapter adjustmentAdapter = (InventoryItemAdjustmentAdapter) lvAdjustments.getAdapter();
                if(adjustmentAdapter != null && adjustmentAdapter.getCount() > 0){

                    for(int i = 0; i < adjustmentAdapter.getCount(); i++){
                        JSONObject adjustment = new JSONObject();
                        try{

                            adjustment.put("reasonName", adjustmentAdapter.getItem(i).getString("reasonName"));
                            adjustment.put("quantity", adjustmentAdapter.getItem(i).getInt("quantity"));
                            adjustments.put(adjustment);
                        } catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    }
                }

                if(getArguments().getBoolean("newItem")){
                    saveProduct(acProduct.getText().toString(),
                            acLotNumber.getText().toString(),
                            etPhysicalStock.getText().toString(),
                            "",
                            adjustments,
                            -1 //
                    );
                } else {
                    saveProduct(acProduct.getText().toString(),
                            acLotNumber.getText().toString(),
                            etPhysicalStock.getText().toString(),
                            etTheoricStock.getText().toString(),
                            adjustments,
                            getArguments().getInt("itemPosition")
                    );
                }


                dismiss();
            }
        });

        btAddReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateBalance(acReason.getText().toString(), Integer.parseInt(etAdjustmentQuantity.getText().toString()));
                addAdjustment(acReason.getText().toString(), Integer.parseInt(etAdjustmentQuantity.getText().toString()));
                acReason.setText("");
                etAdjustmentQuantity.setText("");
            }
        });

        //Observe line items
        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updateBalance(String reasonName, int quantity) {

        if (inventoryViewModel.getReasonType(reasonName).equals("CREDIT")) {

            tvBalance.setText("" + (Integer.parseInt(tvBalance.getText().toString()) + quantity));
            tvAdjustedStock.setText(""+(Integer.parseInt(tvAdjustedStock.getText().toString())+quantity));
        } else {
            tvBalance.setText("" + (Integer.parseInt(tvBalance.getText().toString()) - quantity));
            tvAdjustedStock.setText(""+(Integer.parseInt(tvAdjustedStock.getText().toString())-quantity));
        }
    }

    public void addAdjustment(String reasonName, int quantity) {
        InventoryItemAdjustmentAdapter adjustmentsAdapter = (InventoryItemAdjustmentAdapter) lvAdjustments.getAdapter();

        try {
            JSONObject adjustment = new JSONObject();
            adjustment.put("reasonName", reasonName);
            adjustment.put("quantity", quantity);
            if (adjustmentsAdapter != null) {
                adjustmentsAdapter.add(adjustment);
                adjustmentsAdapter.notifyDataSetChanged();
            } else {
                List<JSONObject> items = new ArrayList<>();
                items.add(adjustment);
                adjustmentsAdapter = new InventoryItemAdjustmentAdapter(getContext(),0, items);
                lvAdjustments.setAdapter(adjustmentsAdapter);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public interface DialogListener {
        void saveProduct(String orderableName, String lotCode, String quantity, String soh, JSONArray adjustments, int position);
    }

    public void saveProduct(String orderableName, String lotCode, String quantity, String soh, JSONArray adjustments, int position) {
        InventoryItemDialog.DialogListener listener = (InventoryItemDialog.DialogListener) getParentFragment();
        listener.saveProduct(orderableName, lotCode, quantity, soh, adjustments,position);
    }
}
