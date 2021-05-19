package mz.org.selv.mobile.ui.stockmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.StockManagementViewModel;

public class StockManagementFragment extends Fragment {

    private StockManagementViewModel stockManagementViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        stockManagementViewModel =
                new ViewModelProvider(this).get(StockManagementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stockmanagement, container, false);
        final MaterialCardView cvInventory = root.findViewById(R.id.cv_stockmanagement_inventory);
        final MaterialCardView cvReceive = root.findViewById(R.id.cv_stockmanagement_receive);
        final MaterialCardView cvIssue = root.findViewById(R.id.cv_stockmanagement_issue);
        final MaterialCardView cvAdjustment = root.findViewById(R.id.cv_stockmanagement_adjustments);
        final MaterialCardView cvStockOnHand = root.findViewById(R.id.cv_stockmanagement_soh);


        //onclick events
        cvInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("action", "inventory");
                ProgramListFragment programListFragment = new ProgramListFragment();
                programListFragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        replace(R.id.nav_host_fragment, programListFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        cvAdjustment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("action", "adjustment");
                FragmentManager fragmentManager = getParentFragmentManager();
                ProgramListFragment programListFragment = new ProgramListFragment();
                programListFragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        replace(R.id.nav_host_fragment, programListFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        cvIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("action", "issue");
                FragmentManager fragmentManager = getParentFragmentManager();
                ProgramListFragment programListFragment = new ProgramListFragment();
                programListFragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        replace(R.id.nav_host_fragment, programListFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        cvReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("action", "receive");
                FragmentManager fragmentManager = getParentFragmentManager();
                ProgramListFragment programListFragment = new ProgramListFragment();
                programListFragment.setArguments(bundle);
                fragmentManager.beginTransaction().
                        replace(R.id.nav_host_fragment, programListFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });

        cvStockOnHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("action", "soh");
                ProgramListFragment programListFragment = new ProgramListFragment();
                programListFragment.setArguments(bundle);
                FragmentManager fragmentManager = getParentFragmentManager();

                fragmentManager.beginTransaction().
                        replace(R.id.nav_host_fragment, programListFragment, null)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return root;
    }
}