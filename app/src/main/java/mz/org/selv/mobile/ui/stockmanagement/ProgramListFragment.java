package mz.org.selv.mobile.ui.stockmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import mz.org.selv.mobile.R;
import mz.org.selv.mobile.model.referencedata.Program;
import mz.org.selv.mobile.ui.adapters.ProgramListAdapter;
import mz.org.selv.mobile.ui.stockmanagement.viewmodel.ProgramListViewModel;

public class ProgramListFragment extends Fragment {
    private ProgramListViewModel programListViewModel;
    String action;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        programListViewModel =
                new ViewModelProvider(this).get(ProgramListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_program_selector, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);

        if (getArguments() != null) {
            action = getArguments().getString("action");
            System.out.println("action:"+ action);
            if (action.equals("issue")) {

                getActivity().setTitle(R.string.string_adjustments);
                 //  actionBar.setTitle(R.string.string_issue);
            } else if (action.equals("receive")) {
                getActivity().setTitle(R.string.string_adjustments);
                //  actionBar.setTitle(R.string.string_receive);
            } else if (action.equals("inventory")) {
                getActivity().setTitle(R.string.string_adjustments);
                // actionBar.setTitle(R.string.string_inventory);
            } else if (action.equals("soh")) {

                getActivity().setTitle(R.string.string_adjustments);
                //   actionBar.setTitle(R.string.string_soh);
            } else if (action.equals("adjustment")) {

                getActivity().setTitle(R.string.string_adjustments);
                //   actionBar.setTitle(R.string.string_adjustments);
            }


        }
        ProgramListAdapter programListAdapter = new ProgramListAdapter(getContext(), programListViewModel.getPrograms());
        ListView lvProgramList = (ListView) root.findViewById(R.id.lv_program_selector_programs);
        lvProgramList.setAdapter(programListAdapter);

        lvProgramList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Program program = (Program) lvProgramList.getAdapter().getItem(position);
                //programListViewModel.getProgramId().setValue(program.getUuid());

                if (action.equals("receive") || action.equals("issue") || action.equals("adjustment")) {

                    Bundle bundle = new Bundle();
                    bundle.putString("action", action);
                    bundle.putString("facilityTypeId", "b5cd5c54-9cc1-4395-82e6-d9f9eb117950");
                    bundle.putString("programId", program.getUuid());
                    StockEventFragment stockEventFragment = new StockEventFragment();
                    stockEventFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getParentFragmentManager();

                    fragmentManager.beginTransaction().
                            replace(R.id.nav_host_fragment, stockEventFragment, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                } else if(action.equals("inventory")){
                    Bundle bundle = new Bundle();
                    bundle.putString("action", action);
                    bundle.putString("facilityTypeId", "b5cd5c54-9cc1-4395-82e6-d9f9eb117950");
                    bundle.putString("facilityId", "b5cd5c54-9cc1-4395-82e6-d9f9eb117950");
                    bundle.putString("programId", program.getUuid());
                    InventoryListFragment inventoryListFragment = new InventoryListFragment();
                    inventoryListFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction().
                            replace(R.id.nav_host_fragment, inventoryListFragment, null)
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .commit();
                }
            }

        });

        return root;
    }


}
