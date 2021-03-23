package mz.org.selv.mobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.model.referencedata.Program;

public class ProgramListAdapter extends ArrayAdapter<Program> {


    public ProgramListAdapter(@NonNull Context context,  @NonNull List<Program> programList) {
        super(context, 0, programList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Program program = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.program_selector_row_item, parent, false);
        }

        TextView tvProgramName = (TextView) convertView.findViewById(R.id.tv_program_selector_item_program);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tv_program_selector_item_description);
        ImageView tvLastSync = (ImageView) convertView.findViewById(R.id.iv_program_selector_status);

        tvProgramName.setText(program.getName());
        tvDescription.setText(program.getDescription());
        return convertView;
    }
}
