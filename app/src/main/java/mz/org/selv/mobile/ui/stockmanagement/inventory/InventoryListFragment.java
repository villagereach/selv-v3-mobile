package mz.org.selv.mobile.ui.stockmanagement.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.adapters.InventoryListAdapter;
import mz.org.selv.mobile.ui.stockmanagement.inventory.InventoryFragment;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.InventoryListViewModel;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.ProgramListViewModel;

public class InventoryListFragment extends Fragment {
    private InventoryListViewModel inventoryListViewModel;
    private Button btCreate;
    private ListView lvInventoryList;
    private LiveData<String> programId;
    private String selectedProgramId;
    private String selectedFacilityTypeId;
    private String selectedFacilityId;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        inventoryListViewModel = new ViewModelProvider(this).get(InventoryListViewModel.class);
        ProgramListViewModel programListViewModel = new ViewModelProvider(this).get(ProgramListViewModel.class);

        View root = inflater.inflate(R.layout.fragment_inventory_list, container, false);

        selectedProgramId = getArguments().getString("programId");
        selectedFacilityId = getArguments().getString("facilityId");
        selectedFacilityTypeId = getArguments().getString("facilityTypeId");
        lvInventoryList = (ListView) root.findViewById(R.id.lv_stock_management_inventory_list_row_item);
        btCreate = (Button) root.findViewById(R.id.bt_stock_management_inventory_create);
        InventoryListAdapter inventoryListAdapter = new InventoryListAdapter(getContext(), inventoryListViewModel.getInventories(selectedProgramId, selectedFacilityId));

        lvInventoryList.setAdapter(inventoryListAdapter);

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("programId", selectedProgramId);
                bundle.putString("facilityTypeId", selectedFacilityTypeId);
                bundle.putString("facilityId", selectedFacilityId);
                FragmentManager fragmentManager = getParentFragmentManager();
                InventoryFragment inventoryFragment = new InventoryFragment();
                inventoryFragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        replace(R.id.nav_host_fragment, inventoryFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        lvInventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return root;
    }
}
