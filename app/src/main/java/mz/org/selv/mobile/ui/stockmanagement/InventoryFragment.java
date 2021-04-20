package mz.org.selv.mobile.ui.stockmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.adapters.InventoryItemsAdapter;


public class InventoryFragment extends Fragment  implements InventoryAddProductDialog.DialogListener{
    private InventoryViewModel inventoryViewModel;
    private InventoryAddProductDialogViewModel inventoryAddProductDialogViewModel;
    private FloatingActionButton fbAddProduct;
    private String selectedProgram;
    private String selectedFacility;
    private ListView lvInventoryItems;
    private InventoryItemsAdapter inventoryItemsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        selectedProgram = getArguments().getString("programId");
        selectedFacility = getArguments().getString("facilityTypeId");

        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);


        View root = inflater.inflate(R.layout.fragment_inventory, container, false);
        lvInventoryItems = (ListView) root.findViewById(R.id.lv_stock_management_inventory_line_items);
        fbAddProduct = (FloatingActionButton) root.findViewById(R.id.fab_stock_management_inventory_add_product);

        fbAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("programId", selectedProgram);
                bundle.putString("facilityTypeId", selectedFacility);

                InventoryAddProductDialog inventoryAddProductDialog = InventoryAddProductDialog.newInstance();
                FragmentManager fm = getChildFragmentManager();
                inventoryAddProductDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_MaterialComponents_Light_Dialog);
                inventoryAddProductDialog.setArguments(getArguments());
                inventoryAddProductDialog.show(fm, "tag");
            }
        });

        inventoryViewModel.getLastLineUpdatedItem().observe(getViewLifecycleOwner(), new Observer<List<JSONObject>>() {
            @Override
            public void onChanged(List<JSONObject> jsonObjects) {
                    updateInventoryItemList(jsonObjects);
            }
        });

        return root;
    }

    public void addOrderableToList(String orderableName, String lotCode, int quantity, int soh, List adjustments) {
       inventoryViewModel.updateInventoryLineItems(orderableName, lotCode, quantity, -1, null);
    }

    public void updateInventoryItemList(List<JSONObject> lineItems){
        inventoryItemsAdapter = new InventoryItemsAdapter(getContext(), lineItems);
        lvInventoryItems.setAdapter(inventoryItemsAdapter);
    }

    @Override
    public void addProduct(String orderableName, String lotCode, int quantity, int soh, List adjustments) {
        addOrderableToList(orderableName, lotCode, quantity, soh, adjustments);
    }

}
