package mz.org.selv.mobile.ui.stockmanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.adapters.InventoryItemsAdapter;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.InventoryAddProductDialogViewModel;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.InventoryViewModel;


public class InventoryFragment extends Fragment  implements  InventoryItemDialog.DialogListener{
    private InventoryViewModel inventoryViewModel;
    private InventoryAddProductDialogViewModel inventoryAddProductDialogViewModel;
    private Button addProduct;
    private Button btSaveInventory;
    private TextInputLayout tilOccuredDate;
    private TextInputLayout tilSignature;
    private EditText etSignature;
    private EditText etOccurredDate;
    private Button btReturn;
    private String selectedProgram;
    private String selectedFacility;
    private ListView lvInventoryItems;
    private int year, month, day;

    private InventoryItemsAdapter inventoryItemsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        selectedProgram = getArguments().getString("programId");
        selectedFacility = getArguments().getString("facilityTypeId");

        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_inventory, container, false);
        lvInventoryItems = (ListView) root.findViewById(R.id.lv_stock_management_inventory_line_items);
        addProduct = (Button) root.findViewById(R.id.bt_stock_management_inventory_add_product);
        btSaveInventory = (Button) root.findViewById(R.id.bt_stock_management_inventory_save) ;
        btReturn = (Button) root.findViewById(R.id.bt_stock_management_inventory_return);
        etOccurredDate = (EditText) root.findViewById(R.id.et_stock_management_inventory_occurred_date);
        etSignature = (EditText) root.findViewById(R.id.et_stock_management_inventory_signature);
        tilOccuredDate = (TextInputLayout) root.findViewById(R.id.til_stock_management_inventory_occurred_date);
        tilSignature = (TextInputLayout) root.findViewById(R.id.til_stock_management_inventory_signature) ;

        createInventory(selectedFacility, selectedProgram);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("programId", selectedProgram);
                bundle.putString("facilityTypeId", selectedFacility);
                bundle.putBoolean("newItem", true);

                InventoryItemDialog inventoryItemDialog = InventoryItemDialog.newInstance();
                FragmentManager fm = getChildFragmentManager();
                inventoryItemDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_MaterialComponents_Light_Dialog);
                inventoryItemDialog.setArguments(bundle);
                inventoryItemDialog.show(fm, "tag");
            }
        });

        inventoryViewModel.getLastLineUpdatedItem().observe(getViewLifecycleOwner(), new Observer<List<JSONObject>>() {
            @Override
            public void onChanged(List<JSONObject> jsonObjects) {
                    updateInventoryItemList(jsonObjects);
            }
        });

        btSaveInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lvInventoryItems.getVisibility() == View.VISIBLE){
                    btSaveInventory.setText(R.string.string_save);
                    btReturn.setVisibility(View.VISIBLE);
                    lvInventoryItems.setVisibility(View.GONE);
                    addProduct.setVisibility(View.GONE);
                    tilOccuredDate.setVisibility(View.VISIBLE);
                    tilSignature.setVisibility(View.VISIBLE);
                } else {
                    List lineItems = ((InventoryItemsAdapter) lvInventoryItems.getAdapter()).getItems();
                    String date = etOccurredDate.getText().toString();
                    String signature = etSignature.getText().toString();
                    inventoryViewModel.saveInventory(signature, selectedProgram, selectedFacility, date, lineItems);

                    //submit inventory
                }
            }
        });

        etOccurredDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etOccurredDate.getText().toString().equals("")){
                    Calendar calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_YEAR);
                } else {
                    year = Integer.parseInt(etOccurredDate.getText().toString().substring(6));
                    month = Integer.parseInt(etOccurredDate.getText().toString().substring(3, 5))-1;
                    day = Integer.parseInt(etOccurredDate.getText().toString().substring(0,2));
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etOccurredDate.setText(dayOfMonth+"-"+(String.format("%02d",month+1))+"-"+year);
                    }
                }, year, month, day );
                datePickerDialog.show();
            }
        });

        btReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btSaveInventory.setText(R.string.string_proceed);
                btReturn.setVisibility(View.GONE);
                lvInventoryItems.setVisibility(View.VISIBLE);
                addProduct.setVisibility(View.VISIBLE);
                tilOccuredDate.setVisibility(View.GONE);
                etOccurredDate.setText("");
                etSignature.setText("");
                tilSignature.setVisibility(View.GONE);
            }
        });

        lvInventoryItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("programId", selectedProgram);
                bundle.putString("facilityTypeId", selectedFacility);
                bundle.putBoolean("newItem", false);
                JSONObject selectedItem = (JSONObject) parent.getItemAtPosition(position);
                System.out.println(selectedItem);
                try{
                    bundle.putBoolean("newItem", false);
                    bundle.putString("orderableName", selectedItem.getString("orderableName"));
                    bundle.putString("lotCode", selectedItem.getString("lotCode"));
                    bundle.putString("expirationDate", selectedItem.getString("expirationDate"));
                    bundle.putString("stockOnHand", selectedItem.getString("stockOnHand"));
                    bundle.putString("physicalStock", selectedItem.getString("physicalStock"));
                    if(selectedItem.has("adjustments")){
                        bundle.putString("adjustments", selectedItem.getJSONArray("adjustments").toString());
                    }
                    bundle.putInt("itemPosition", position);
                } catch (JSONException exception){
                    exception.printStackTrace();
                }

                InventoryItemDialog inventoryItemDialog = InventoryItemDialog.newInstance();
                FragmentManager fm = getChildFragmentManager();
                inventoryItemDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_MaterialComponents_Light_Dialog);
                inventoryItemDialog.setArguments(bundle);
                inventoryItemDialog.show(fm, "tag");
            }
        });

        return root;
    }


    public void saveInventoryLineItem(String orderableName, String lotCode, String quantity, String soh, JSONArray adjustments,int position) {
       inventoryViewModel.updateInventoryLineItems(orderableName, lotCode, quantity, soh, position,adjustments);
    }



    public void updateInventoryItemList(List<JSONObject> lineItems){
        inventoryItemsAdapter = new InventoryItemsAdapter(getContext(), lineItems, selectedFacility, selectedProgram);
        lvInventoryItems.setAdapter(inventoryItemsAdapter);
    }

    public void createInventory(String facilityId, String selectedProgramId){
        inventoryViewModel.getInventoryLineItems(selectedProgramId, facilityId);
    }

    @Override
    public void saveProduct(String orderableName, String lotCode, String quantity, String soh, JSONArray adjustments, int position) {
        saveInventoryLineItem(orderableName, lotCode, quantity, soh, adjustments, position);
    }
}
