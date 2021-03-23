package mz.org.selv.mobile.ui.stockmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.ui.adapters.ProgramListAdapter;

public class ProgramListFragment extends Fragment {
    private ProgramListViewModel programListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        programListViewModel =
                new ViewModelProvider(this).get(ProgramListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_program_selector, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);

        ProgramListAdapter programListAdapter = new ProgramListAdapter(getContext(), programListViewModel.getPrograms());
        ListView lvProgramList = (ListView) root.findViewById(R.id.lv_program_selector_programs);
        lvProgramList.setAdapter(programListAdapter);
        return root;
    }
}
