package mz.org.selv.mobile.ui.stockmanagement;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.StockEventDialogViewModel;

public class StockEventDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {
    private StockEventDialogViewModel stockEventDialogViewModel;
    private AutoCompleteTextView acLotNumber;
    private String action;
    public static StockEventDialog newInstance(){
        return new StockEventDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(StockEventDialog.STYLE_NORMAL, R.style.Theme_MaterialComponents_Light_DialogWhenLarge);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        stockEventDialogViewModel = new ViewModelProvider(this).get(StockEventDialogViewModel.class);
        View view = inflater.inflate(R.layout.dialog_new_event_item, container, false);

        List orderables = stockEventDialogViewModel.getOrderables(getArguments().getString("programId"), getArguments().getString("facilityTypeId"));
        List reasonNames = stockEventDialogViewModel.getReasonNames(getArguments().getString("facilityTypeId"), getArguments().getString("programId"));

        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, reasonNames);
        ArrayAdapter<String> orderableAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, orderables);
        reasonAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        orderableAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        AutoCompleteTextView acVVM = view.findViewById(R.id.ac_stock_event_vvm);
        AutoCompleteTextView acProduct = view.findViewById(R.id.ac_stock_management_inventory_add_product_product);
        acProduct.setAdapter(orderableAdapter);

        AutoCompleteTextView acSourceDestination = view.findViewById(R.id.ac_stock_event_source_destination);
        AutoCompleteTextView acReason = view.findViewById(R.id.ac_stock_event_reason);
        acReason.setAdapter(reasonAdapter);
        EditText etSourceDestinationComments = view.findViewById(R.id.et_stock_event_source_destination_comments);
        EditText etReasonComments = view.findViewById(R.id.et_stock_event_reason_comment);
        EditText etDate = view.findViewById(R.id.tv_stock_event_date);
        TextView tvStockOnHand = view.findViewById(R.id.tv_stock_event_soh);
        TextView tvExpirationDate = view.findViewById(R.id.tv_stock_management_inventory_add_product_expiration_date);
        acLotNumber = (AutoCompleteTextView) view.findViewById(R.id.ac_stock_management_inventory_add_product_lot);
        acProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position > 0){
                        List lots = stockEventDialogViewModel.getLots(parent.getItemAtPosition(position).toString());
                        System.out.println(lots);
                        ArrayAdapter<String> lotAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, lots);
                        acLotNumber.setAdapter(lotAdapter);
                    } else {

                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //
        if(getArguments().getString("action").equals("adjustment")){
            TextInputLayout sourceDestinationLayout = (TextInputLayout) view.findViewById(R.id.til_stock_event_dialog_source_destination);
            TextInputLayout sourceDestinationCommentLayout = (TextInputLayout) view.findViewById(R.id.til_stock_event_dialog_source_destination_comment);
            sourceDestinationLayout.setVisibility(View.GONE);
            sourceDestinationCommentLayout.setVisibility(View.GONE);
            sourceDestinationLayout.setVisibility(View.GONE);
            acSourceDestination.setVisibility(View.GONE);
            etSourceDestinationComments.setVisibility(View.GONE);
        }
        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            //dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }


}
