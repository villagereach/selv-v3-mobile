package mz.org.selv.mobile.ui.stockmanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.model.referencedata.Lot;

public class InventoryAddProductDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private InventoryViewModel inventoryViewModel;
    private AutoCompleteTextView acProduct;
    private AutoCompleteTextView acLotNumber;
    private AutoCompleteTextView acReason;
    private EditText etQuantity;
    private TextView product;
    private TextView tvExpirationDate;
    private Button btSave;

    public static InventoryAddProductDialog newInstance() {
        return new InventoryAddProductDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        View view = inflater.inflate(R.layout.dialog_inventory_new_item, container, false);

        btSave = (Button) view.findViewById(R.id.bt_stock_management_inventory_add_product_add);
        etQuantity = (EditText) view.findViewById(R.id.et_stock_management_inventory_add_product_quantity);
        acProduct = (AutoCompleteTextView) view.findViewById(R.id.ac_stock_management_inventory_add_product_product);
        acLotNumber = (AutoCompleteTextView) view.findViewById(R.id.ac_stock_management_inventory_add_product_lot);
        tvExpirationDate = (TextView) view.findViewById(R.id.tv_stock_management_inventory_add_product_expiration_date);
        //AC Adapters
        List orderables = inventoryViewModel.getOrderables(getArguments().getString("programId"), getArguments().getString("facilityTypeId"));
        ArrayAdapter<String> orderableAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, orderables);
        orderableAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        acProduct.setAdapter(orderableAdapter);


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct(acProduct.getText().toString(), acLotNumber.getText().toString(), Integer.parseInt(etQuantity.getText().toString()), -1);
            }
        });

        acProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List lots = inventoryViewModel.getLots(parent.getItemAtPosition(position).toString());
                acLotNumber.setText("");
                ArrayAdapter<String> lotAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, lots);
                acLotNumber.setAdapter(lotAdapter);
            }
        });

        acLotNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lot lot = inventoryViewModel.getLotByCode(parent.getItemAtPosition(position).toString());
                tvExpirationDate.setText(lot.getExpirationDate());
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
        void addProduct(String orderableName, String lotCode, int quantity, int soh);
        void addLineItemAdjustmets(String orderable, String lotCode, List adjustments);
    }

    public void saveProduct(String orderableName, String lotCode, int quantity, int soh) {
        DialogListener listener = (DialogListener) getParentFragment();
        listener.addProduct(orderableName, lotCode, quantity, soh);
    }

    public void saveAdjustment(String orderableName, String lotCode, int quantity, int soh) {
        DialogListener listener = (DialogListener) getParentFragment();
        listener.addProduct(orderableName, lotCode, quantity, soh);
    }
}
