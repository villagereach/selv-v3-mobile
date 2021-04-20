package mz.org.selv.mobile.ui.stockmanagement;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mz.org.selv.mobile.R;

public class StockOnHandFragment extends Fragment {

    private StockOnHandViewModel mViewModel;

    public static StockOnHandFragment newInstance() {
        return new StockOnHandFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_on_hand_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StockOnHandViewModel.class);
        // TODO: Use the ViewModel
    }

}