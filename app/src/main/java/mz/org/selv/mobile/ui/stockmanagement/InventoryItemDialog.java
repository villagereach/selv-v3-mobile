package mz.org.selv.mobile.ui.stockmanagement;

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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.adapters.InventoryItemAdjustmentAdapter;
import mz.org.selv.mobile.ui.adapters.InventoryItemsAdapter;
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
        View view = inflater.inflate(R.layout.dialog_inventory_adjustements, container, false);

        btSave = (Button) view.findViewById(R.id.bt_stock_management_inventory_item_dialog_save);
        etTheoricStock = (EditText) view.findViewById(R.id.et_stock_management_inventory_dialog_theoric_stock);
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

        //AC Adapters

        List reasons = inventoryViewModel.getReasonNames(getArguments().getString("facilityTypeId"), getArguments().getString("programId"));
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, reasons);
        reasonAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        acReason.setAdapter(reasonAdapter);

        if(getArguments().getBoolean("newItem")){ // add product dialog

        } else { // view product dialog
            acProduct.setText(getArguments().getString("orderableName"));
            acProduct.setEnabled(false);
            acLotNumber.setText(getArguments().getString("lotCode"));
            acLotNumber.setEnabled(false);
            //tvBalance.setText("" + (getArguments().getString("physicalStock") - getArguments().getString("stockOnHand")));
            etTheoricStock.setText(""+getArguments().getString("stockOnHand"));
            etTheoricStock.setEnabled(false);
            etExpirationDate.setText(getArguments().getString("expirationDate"));
            etExpirationDate.setEnabled(false);
            etPhysicalStock.setText(getArguments().getString("physicalStock"));
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
                if(!s.toString().isEmpty()){

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

                saveProduct(acProduct.getText().toString(),
                            acLotNumber.getText().toString(),
                            etPhysicalStock.getText().toString(),
                            etTheoricStock.getText().toString(),
                            adjustments,
                            getArguments().getInt("itemPosition")
                           );

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
        System.out.println("Item selected");
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
