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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;
import mz.org.selv.mobile.MainActivity;
import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.StockEventViewModel;

public class StockEventFragment extends Fragment {



    private StockEventViewModel mViewModel;


    public static StockEventFragment newInstance() {
        return new StockEventFragment();
    }

    FloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPrefs = requireActivity().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        String homeFacilityName = sharedPrefs.getString(KEY_HOME_FACILITY_NAME, "");
        assert getArguments() != null;
        String programName = getArguments().getString("programName");
        Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar())
            .setTitle(homeFacilityName + " - " + programName);
        View root =  inflater.inflate(R.layout.fragment_stock_event, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

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