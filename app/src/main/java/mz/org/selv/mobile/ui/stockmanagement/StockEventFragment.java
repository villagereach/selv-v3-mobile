package mz.org.selv.mobile.ui.stockmanagement;

import static mz.org.selv.mobile.auth.LoginHelper.APP_SHARED_PREFS;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_HOME_FACILITY_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;
import mz.org.selv.mobile.MainActivity;
import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.adapters.StockEventItemsAdapter;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.StockEventViewModel;

public class StockEventFragment extends Fragment {



    private StockEventViewModel mViewModel;


    public static StockEventFragment newInstance() {
        return new StockEventFragment();
    }

    private ListView lvStockEventLineItems;
    private StockEventViewModel stockEventViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        String homeFacilityName = sharedPrefs.getString(KEY_HOME_FACILITY_NAME, "");
        String actionName = "";
        if(getArguments().getString("action").equals("receive")){
            actionName = getString(R.string.string_receive);
        } else if(getArguments().getString("action").equals("issue")){
            actionName = getString(R.string.string_issue);
        } else if(getArguments().getString("action").equals("adjustment")){
            actionName = getString(R.string.string_adjustments);
        } else if(getArguments().getString("action").equals("inventory")){
            actionName = getString(R.string.string_inventory);
        } else {
            actionName = getString(R.string.string_soh);
        }
        String programName = getArguments().getString("programName");
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
                .setTitle(actionName + " - " + programName + " ("+ homeFacilityName + ")");
        View root =  inflater.inflate(R.layout.fragment_stock_event, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fab);
        lvStockEventLineItems = root.findViewById(R.id.lv_stock_event_line_items);
        stockEventViewModel = new ViewModelProvider(this).get(StockEventViewModel.class);

        assert getArguments() != null;
        if(getArguments().getString("action").equals("receive")){


            StockEventItemsAdapter eventItemsAdapter = new StockEventItemsAdapter(getContext(), stockEventViewModel.getEventLineItems(getArguments().getString("facilityId"),
                        getArguments().getString("programId"), getArguments().getString("action")), getArguments().getString("action"));
            lvStockEventLineItems.setAdapter(eventItemsAdapter);

        } else if(getArguments().getString("action").equals("issue")){

            StockEventItemsAdapter eventItemsAdapter = new StockEventItemsAdapter(getContext(), stockEventViewModel.getEventLineItems(getArguments().getString("facilityId"),
                    getArguments().getString("programId"), getArguments().getString("action")), getArguments().getString("action"));
            lvStockEventLineItems.setAdapter(eventItemsAdapter);

        } else if(getArguments().getString("action").equals("adjustments")){

            StockEventItemsAdapter eventItemsAdapter = new StockEventItemsAdapter(getContext(), stockEventViewModel.getEventLineItems(getArguments().getString("facilityId"),
                    getArguments().getString("programId"), getArguments().getString("action")), getArguments().getString("action"));
            lvStockEventLineItems.setAdapter(eventItemsAdapter);

        } else if(getArguments().getString("action").equals("inventory")){
            StockEventItemsAdapter eventItemsAdapter = new StockEventItemsAdapter(getContext(), stockEventViewModel.getEventLineItems(getArguments().getString("facilityId"),
                    getArguments().getString("programId"), getArguments().getString("action")), getArguments().getString("action"));
            lvStockEventLineItems.setAdapter(eventItemsAdapter);
            actionName = getString(R.string.string_inventory);
        } else if(getArguments().getString("action").equals("soh")){
            actionName = getString(R.string.string_soh);
        }

                fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockEventLineItemDialog stockEventLineItemDialog = StockEventLineItemDialog.newInstance();
                stockEventLineItemDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_SELVMobile_StockEventDialog);
                stockEventLineItemDialog.setArguments(getArguments());
                stockEventLineItemDialog.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StockEventViewModel.class);
        // TODO: Use the ViewModel
    }

}