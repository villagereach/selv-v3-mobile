package mz.org.selv.mobile.ui.stockmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.adapters.InventoryItemAdjustmentAdapter;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.InventoryViewModel;

public class InventoryAdjustmentsDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {


    private InventoryViewModel inventoryViewModel;
    private TextView tvLotNumber;
    private TextView tvProduct;
    private AutoCompleteTextView acReason;
    private EditText etQuantity;
    private TextView tvBalance;
    private TextView tvAdjustedStock;
    private ListView lvAdjustments;
    private Button btSave;
    private Button btCancel;
    private Button btAddReason;

    public static InventoryAdjustmentsDialog newInstance() {
        return new InventoryAdjustmentsDialog();
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

        btSave = (Button) view.findViewById(R.id.bt_stock_management_inventory_adjustments_save);
        btAddReason = (Button) view.findViewById(R.id.bt_stock_management_inventory_adjustments_add);
        btCancel = (Button) view.findViewById(R.id.bt_stock_management_inventory_adjustments_cancel);
        etQuantity = (EditText) view.findViewById(R.id.et_stock_management_inventory_adjustments_quantity);
        tvProduct = (TextView) view.findViewById(R.id.tv_stock_management_inventory_adjustments_product);
        tvLotNumber = (TextView) view.findViewById(R.id.tv_stock_management_inventory_adjustments_lot);
        tvBalance = (TextView) view.findViewById(R.id.tv_stock_management_inventory_adjustments_balance);
        tvAdjustedStock = (TextView) view.findViewById(R.id.tv_stock_management_inventory_adjustments_total);
        acReason = (AutoCompleteTextView) view.findViewById(R.id.ac_stock_management_inventory_adjustments_reason);
        lvAdjustments = (ListView) view.findViewById(R.id.lv_stock_management_inventory_adjustments_list);
        //AC Adapters

        if (getArguments() != null) {
            tvProduct.setText(getArguments().getString("orderableName"));
            tvLotNumber.setText(getArguments().getString("lotCode"));
            tvBalance.setText("" + (getArguments().getInt("physicalStock") - getArguments().getInt("stockOnHand")));
            List reasons = inventoryViewModel.getReasonNames(getArguments().getString("facilityTypeId"), getArguments().getString("programId"));
            ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, reasons);
            reasonAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            acReason.setAdapter(reasonAdapter);

        }


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAdjustment(tvProduct.getText().toString(), tvLotNumber.getText().toString(), ((InventoryItemAdjustmentAdapter) lvAdjustments.getAdapter()).getItems());
                //dismiss();
            }
        });

        btAddReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateBalance(acReason.getText().toString(), Integer.parseInt(etQuantity.getText().toString()));
                addAdjustment(acReason.getText().toString(), Integer.parseInt(etQuantity.getText().toString()));
                acReason.setText("");
                etQuantity.setText("");
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

    public interface DialogListener {
        void addAdjustments(String orderable, String lotCode, List adjustments);
    }

    public void saveAdjustment(String orderableName, String lotCode, List adjustments) {
        DialogListener listener = (DialogListener) getParentFragmentManager();
        listener.addAdjustments(orderableName, lotCode, adjustments);
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
}
