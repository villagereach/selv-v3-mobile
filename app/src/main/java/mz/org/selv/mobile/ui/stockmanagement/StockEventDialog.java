package mz.org.selv.mobile.ui.stockmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import mz.org.selv.mobile.R;

public class StockEventDialog extends DialogFragment {
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
        View view = inflater.inflate(R.layout.dialog_new_event_item, container, false);

      //  Spinner spinnerVVM = view.findViewById();
      //  Spinner spinnerSourceDestination = view.findViewById();
      //  Spinner spinnerReason = view.findViewById();
       // EditText etSourceDestinationComments = view.findViewById();
       // EditText etReasonComments = view.findViewById();
//    //    EditText etDate = view.findViewById();
   //     TextView tvStockOnHand = view.findViewById();
   //     TextView tvExpirationDate = view.findViewById();


        return view;


    }
}
