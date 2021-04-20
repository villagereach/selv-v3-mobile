package mz.org.selv.mobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.model.referencedata.Orderable;

public class ProgramSpinnerAdapter extends ArrayAdapter<Orderable> {
    public ProgramSpinnerAdapter(@NonNull Context context,  @NonNull List<Orderable> orderables) {
        super(context, 0, orderables);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Orderable orderable = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.program_selector_row_item, parent, false);
        }

        TextView tvOrderable = (TextView) convertView.findViewById(R.id.tv_product_row_item_name);
        tvOrderable.setText(orderable.getName());
        return  convertView;
    }
}
