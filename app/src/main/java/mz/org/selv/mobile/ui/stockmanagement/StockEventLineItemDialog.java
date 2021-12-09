package mz.org.selv.mobile.ui.stockmanagement;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.model.referencedata.Lot;
import mz.org.selv.mobile.model.stockmanagement.ValidDestination;
import mz.org.selv.mobile.model.stockmanagement.ValidSource;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.StockEventLineItemViewModel;

public class StockEventLineItemDialog extends DialogFragment {
    private StockEventLineItemViewModel stockEventLineItemViewModel;

    private AutoCompleteTextView acLotNumber;
    private AutoCompleteTextView acReason;
    TextInputLayout sourceDestinationLayout;
    TextInputLayout sourceDestinationCommentLayout;
    private Button btAdd;
    private Button btCancel;
    private int year, month, day;

    public static StockEventLineItemDialog newInstance() {
        return new StockEventLineItemDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(StockEventLineItemDialog.STYLE_NORMAL, R.style.Theme_MaterialComponents_Light_DialogWhenLarge);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        stockEventLineItemViewModel = new ViewModelProvider(this).get(StockEventLineItemViewModel.class);
        View view = inflater.inflate(R.layout.dialog_new_stock_event_line_item, container, false);

        AutoCompleteTextView acProduct = view.findViewById(R.id.ac_stock_event_line_item_product);
        String[] vvmStatus = getResources().getStringArray(R.array.string_array_vvm);

        acLotNumber = view.findViewById(R.id.ac_stock_event_line_item_lot);

        TextView tvStockOnHand = view.findViewById(R.id.tv_stock_event_line_item_soh);
        tvStockOnHand.setText("");
        TextView tvExpirationDate = view.findViewById(R.id.tv_stock_event_line_item_expiration_date);
        tvExpirationDate.setText("");

        AutoCompleteTextView acSourceDestination = view.findViewById(R.id.ac_stock_event_line_item_source_destination);
        acReason = view.findViewById(R.id.ac_stock_event_line_item_reason);

        EditText etSourceDestinationComments = view.findViewById(R.id.et_stock_event_line_item_source_destination_comments);

        EditText etReasonComments = view.findViewById(R.id.et_stock_event_line_item_reason_comment);

        EditText etQuantity = view.findViewById(R.id.et_stock_event_line_item_quantity);

        AutoCompleteTextView acVVM = view.findViewById(R.id.ac_stock_event_line_item_vvm);


        EditText etOccurredDate = view.findViewById(R.id.et_stock_event_line_item_occurred_date);

        btAdd = view.findViewById(R.id.bt_stock_event_line_item_add);
        btCancel = view.findViewById(R.id.bt_stock_event_line_item_cancel);

        //fill VVM Status Dropdown
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, vvmStatus);
        acVVM.setAdapter(adapter);

        acProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {

                    acLotNumber.setText("");
                    List lots = stockEventLineItemViewModel
                            .getLotCodes(parent.getItemAtPosition(position).toString(), getArguments().getString("facilityId"), getArguments().getString("programId"), getArguments().getString("action"));
                    ArrayAdapter<String> lotAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, lots);
                    acLotNumber.setAdapter(lotAdapter);
                }
            }
        });

        acLotNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {
                    stockEventLineItemViewModel.setSelectedLot(parent.getItemAtPosition(position).toString());
                }
            }
        });

        acSourceDestination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (getArguments().getString("action").equals("receive")) {
                        stockEventLineItemViewModel.setSelectedSource(getArguments().getString("facilityTypeId"), getArguments().getString("programId"), parent.getItemAtPosition(position).toString());

                    } else {
                        stockEventLineItemViewModel.setSelectedDestination(getArguments().getString("facilityTypeId"), getArguments().getString("programId"), parent.getItemAtPosition(position).toString());
                    }
                }
            }
        });

        //Datepicker
        etOccurredDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOccurredDate.getText().toString().equals("")) {
                    Calendar calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_YEAR);
                } else {
                    year = Integer.parseInt(etOccurredDate.getText().toString().substring(6));
                    month = Integer.parseInt(etOccurredDate.getText().toString().substring(3, 5)) - 1;
                    day = Integer.parseInt(etOccurredDate.getText().toString().substring(0, 2));
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etOccurredDate.setText(year+"-"+(String.format("%02d", month+1)) + "-" + (String.format("%02d", dayOfMonth)));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stockEventLineItemViewModel.saveEvent(
                        getArguments().getString("action"),
                        getArguments().getString("facilityId"),
                        getArguments().getString("facilityTypeId"),
                        getArguments().getString("programId"),
                        acProduct.getText().toString(),
                        acLotNumber.getText().toString(),
                        acSourceDestination.getText().toString(),
                        etSourceDestinationComments.getText().toString(),
                        acReason.getText().toString(),
                        etReasonComments.getText().toString(),
                        Integer.parseInt(etQuantity.getText().toString()),
                        acVVM.getText().toString(),
                        etOccurredDate.getText().toString()
                );
                dismiss();
            }
        });

        btCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //observable
        stockEventLineItemViewModel.getSelectedValidSource().observe(getViewLifecycleOwner(), new Observer<ValidSource>() {
            @Override
            public void onChanged(ValidSource validSource) {
                if (validSource.getIsFreeTextAllowed().equals("true")) {
                    etSourceDestinationComments.setText("");
                    sourceDestinationCommentLayout.setVisibility(View.VISIBLE);
                } else {
                    etSourceDestinationComments.setText("");
                    sourceDestinationCommentLayout.setVisibility(View.GONE);
                }
            }
        });

        stockEventLineItemViewModel.getSelectedValidDestination().observe(getViewLifecycleOwner(), new Observer<ValidDestination>() {
            @Override
            public void onChanged(ValidDestination validDestination) {
                if (validDestination.getIsFreeTextAllowed().equals("true")) {

                    etSourceDestinationComments.setText("");
                    sourceDestinationCommentLayout.setVisibility(View.VISIBLE);
                } else {
                    etSourceDestinationComments.setText("");
                    sourceDestinationCommentLayout.setVisibility(View.GONE);
                }
            }
        });

        stockEventLineItemViewModel.getSelectedLot().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> lot) {
                tvExpirationDate.setText(lot.get("expirationDate"));
                tvStockOnHand.setText(lot.get("stockOnHand"));
            }
        });

        //
        assert getArguments() != null;
        if (getArguments().getString("action").equals("adjustment")) {
            sourceDestinationLayout = view.findViewById(R.id.til_stock_event_line_item_source_destination);
            sourceDestinationCommentLayout = view.findViewById(R.id.til_stock_event_line_item_source_destination_comment);
            sourceDestinationLayout.setVisibility(View.GONE);
            sourceDestinationCommentLayout.setVisibility(View.GONE);
            sourceDestinationLayout.setVisibility(View.GONE);
            acSourceDestination.setVisibility(View.GONE);
            etSourceDestinationComments.setVisibility(View.GONE);
            AutoCompleteTextView acReason = view.findViewById(R.id.ac_stock_event_line_item_reason);
            List reasonNames = stockEventLineItemViewModel
                    .getReasonNames(getArguments().getString("facilityTypeId"), getArguments().getString("programId"), null, null);
            ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, reasonNames);
            acReason.setAdapter(reasonAdapter);

            List orderables = stockEventLineItemViewModel
                    .getOrderables(getArguments().getString("programId"), getArguments().getString("facilityTypeId"), getArguments().getString("facilityId"), getArguments().getString("action"));
            ArrayAdapter<String> orderableAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, orderables);
            acProduct.setAdapter(orderableAdapter);

        } else if (getArguments().getString("action").equals("receive")) {

            sourceDestinationLayout = view.findViewById(R.id.til_stock_event_line_item_source_destination);
            sourceDestinationLayout.setHint(R.string.string_source);
            sourceDestinationCommentLayout = view.findViewById(R.id.til_stock_event_line_item_source_destination_comment);
            sourceDestinationCommentLayout.setHint(R.string.string_comments);
            sourceDestinationLayout.setVisibility(View.VISIBLE);
            sourceDestinationCommentLayout.setVisibility(View.VISIBLE);
            acSourceDestination.setVisibility(View.VISIBLE);
            etSourceDestinationComments.setVisibility(View.VISIBLE);

            // get valid sources
            List validSources = stockEventLineItemViewModel
                    .getValidSources(getArguments().getString("facilityTypeId"), getArguments().getString("programId"));
            ArrayAdapter<String> validSourcesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, validSources);
            acSourceDestination.setAdapter(validSourcesAdapter);

            // valid reasons
            List reasonNames = stockEventLineItemViewModel
                    .getReasonNames(getArguments().getString("facilityTypeId"), getArguments().getString("programId"), "TRANSFER", "CREDIT");
            ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, reasonNames);
            acReason.setAdapter(reasonAdapter);

            // get orderables
            List orderables = stockEventLineItemViewModel
                    .getOrderables(getArguments().getString("programId"), getArguments().getString("facilityTypeId"), getArguments().getString("facilityId"), getArguments().getString("action"));
            ArrayAdapter<String> orderableAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, orderables);
            acProduct.setAdapter(orderableAdapter);

        } else if (getArguments().getString("action").equals("issue")) {
            sourceDestinationLayout = view.findViewById(R.id.til_stock_event_line_item_source_destination);
            sourceDestinationLayout.setHint(R.string.string_destination);
            sourceDestinationCommentLayout = view.findViewById(R.id.til_stock_event_line_item_source_destination_comment);
            sourceDestinationCommentLayout.setHint(R.string.string_comments);
            sourceDestinationLayout.setVisibility(View.VISIBLE);
            sourceDestinationCommentLayout.setVisibility(View.VISIBLE);
            acSourceDestination.setVisibility(View.VISIBLE);
            etSourceDestinationComments.setVisibility(View.VISIBLE);

            List orderables = stockEventLineItemViewModel
                    .getOrderables(getArguments().getString("programId"), getArguments().getString("facilityTypeId"), getArguments().getString("facilityId"), getArguments().getString("action"));
            ArrayAdapter<String> orderableAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, orderables);
            acProduct.setAdapter(orderableAdapter);

            // get valid sources
            List validDestination = stockEventLineItemViewModel
                    .getValidDestinations(getArguments().getString("facilityTypeId"), getArguments().getString("programId"));
            ArrayAdapter<String> validSourcesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, validDestination);
            acSourceDestination.setAdapter(validSourcesAdapter);

            // valid reasons
            List reasonNames = stockEventLineItemViewModel
                    .getReasonNames(getArguments().getString("facilityTypeId"), getArguments().getString("programId"), "TRANSFER", "DEBIT");
            ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, reasonNames);
            acReason.setAdapter(reasonAdapter);
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
