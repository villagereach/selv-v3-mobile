package mz.org.selv.mobile.ui.stockmanagement;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.StockEventLineItemViewModel;

public class StockEventLineItemDialog extends DialogFragment {
    private StockEventLineItemViewModel stockEventLineItemViewModel;

    private AutoCompleteTextView acLotNumber;
    private Button btAdd;
    private Button btCancel;

    public static StockEventLineItemDialog newInstance(){
        return new StockEventLineItemDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(StockEventLineItemDialog.STYLE_NORMAL, R.style.Theme_MaterialComponents_Light_DialogWhenLarge);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        stockEventLineItemViewModel = new ViewModelProvider(this).get(StockEventLineItemViewModel.class);
        View view = inflater.inflate(R.layout.dialog_new_stock_event_line_item, container, false);

        AutoCompleteTextView acProduct = view.findViewById(R.id.ac_stock_event_line_item_product);
        List orderables = stockEventLineItemViewModel
            .getOrderables(getArguments().getString("programId"), getArguments().getString("facilityTypeId"));
        ArrayAdapter<String> orderableAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, orderables);
        acProduct.setAdapter(orderableAdapter);

        acLotNumber = view.findViewById(R.id.ac_stock_event_line_item_lot);

        TextView tvStockOnHand = view.findViewById(R.id.tv_stock_event_line_item_soh);
        tvStockOnHand.setText("");
        TextView tvExpirationDate = view.findViewById(R.id.tv_stock_event_line_item_expiration_date);
        tvExpirationDate.setText("");

        AutoCompleteTextView acSourceDestination = view.findViewById(R.id.ac_stock_event_line_item_source_destination);
        EditText etSourceDestinationComments = view.findViewById(R.id.et_stock_event_line_item_source_destination_comments);

        AutoCompleteTextView acReason = view.findViewById(R.id.ac_stock_event_line_item_reason);
        List reasonNames = stockEventLineItemViewModel
            .getReasonNames(getArguments().getString("facilityTypeId"), getArguments().getString("programId"));
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, reasonNames);
        acReason.setAdapter(reasonAdapter);

        EditText etReasonComments = view.findViewById(R.id.et_stock_event_line_item_reason_comment);

        EditText etQuantity = view.findViewById(R.id.et_stock_event_line_item_quantity);

        AutoCompleteTextView acVVM = view.findViewById(R.id.ac_stock_event_line_item_vvm);

        EditText etOccurredDate = view.findViewById(R.id.et_stock_event_line_item_occurred_date);

        btAdd = view.findViewById(R.id.bt_stock_event_line_item_add);
        btCancel = view.findViewById(R.id.bt_stock_event_line_item_cancel);

        acProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    acLotNumber.setText("");
                    List lots = stockEventLineItemViewModel
                        .getLotCodes(parent.getItemAtPosition(position).toString());
                    ArrayAdapter<String> lotAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, lots);
                    acLotNumber.setAdapter(lotAdapter);
                }
            }
        });

        btAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //
        assert getArguments() != null;
        if (getArguments().getString("action").equals("adjustment")) {
            TextInputLayout sourceDestinationLayout = view.findViewById(R.id.til_stock_event_line_item_source_destination);
            TextInputLayout sourceDestinationCommentLayout = view.findViewById(R.id.til_stock_event_line_item_source_destination_comment);
            sourceDestinationLayout.setVisibility(View.GONE);
            sourceDestinationCommentLayout.setVisibility(View.GONE);
            sourceDestinationLayout.setVisibility(View.GONE);
            acSourceDestination.setVisibility(View.GONE);
            etSourceDestinationComments.setVisibility(View.GONE);
        }
        return view;

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
