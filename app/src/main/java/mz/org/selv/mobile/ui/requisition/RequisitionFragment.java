package mz.org.selv.mobile.ui.requisition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import mz.org.selv.mobile.R;

public class RequisitionFragment extends Fragment {

    private RequisitionViewModel requisitionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        requisitionViewModel =
                new ViewModelProvider(this).get(RequisitionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_requisition, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        requisitionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}