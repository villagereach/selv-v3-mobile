package mz.org.selv.mobile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import mz.org.selv.mobile.ui.stockmanagement.StockEventDialog;

public class StockEventFragment extends Fragment {



    private StockEventViewModel mViewModel;


    public static StockEventFragment newInstance() {
        return new StockEventFragment();
    }

    FloatingActionButton fab;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_stock_event, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StockEventDialog stockEventDialog = StockEventDialog.newInstance();
                stockEventDialog.show(getActivity().getSupportFragmentManager(), "tag");
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