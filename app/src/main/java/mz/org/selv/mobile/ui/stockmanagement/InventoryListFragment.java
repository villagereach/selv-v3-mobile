package mz.org.selv.mobile.ui.stockmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import mz.org.selv.mobile.R;

public class InventoryListFragment extends Fragment {
    private InventoryListViewModel inventoryListViewModel;
    private Button btCreate;
    private LiveData<String> programId;
    private String selectedProgramId;
    private String selectedFacilityTypeId;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        inventoryListViewModel = new ViewModelProvider(this).get(InventoryListViewModel.class);
        ProgramListViewModel programListViewModel = new ViewModelProvider(this).get(ProgramListViewModel.class);

        View root = inflater.inflate(R.layout.fragment_inventory_list, container, false);

        selectedProgramId = getArguments().getString("programId");
        selectedFacilityTypeId = getArguments().getString("facilityTypeId");

        btCreate = (Button) root.findViewById(R.id.bt_stock_management_inventory_create);
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("programId", selectedProgramId);
                bundle.putString("facilityTypeId", selectedFacilityTypeId);
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
        return root;
    }
}
