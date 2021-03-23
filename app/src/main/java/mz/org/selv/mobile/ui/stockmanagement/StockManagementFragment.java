package mz.org.selv.mobile.ui.stockmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;

import mz.org.selv.mobile.R;

public class StockManagementFragment extends Fragment {

    private StockManagementViewModel stockManagementViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        stockManagementViewModel =
                new ViewModelProvider(this).get(StockManagementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stockmanagement, container, false);
        final MaterialCardView cvInventory = root.findViewById(R.id.cv_stockmanagement_inventory);

        //onclick events
        cvInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().
                        replace(R.id.nav_host_fragment, ProgramListFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });
        stockManagementViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s){
            }
        });
        return root;
    }
}