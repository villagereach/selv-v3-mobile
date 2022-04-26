package mz.org.selv.mobile.ui.sync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import mz.org.selv.mobile.R;
import mz.org.selv.mobile.auth.LoginHelper;
import mz.org.selv.mobile.model.Entity;

import static mz.org.selv.mobile.auth.LoginHelper.APP_SHARED_PREFS;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_PASSWORD;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_USERNAME;

public class SyncFragment extends Fragment {


    private SyncViewModel syncViewModel;
    private LoginHelper loginHelper;
    private ProgressDialog progressDialog;
    private static final String STATUS_CONNECTING = "connecting";
    private static final String STATUS_DOWNLOADING = "downloading";
    private static final String STATUS_SAVING = "saving";
    private static final String STATUS_FINISHED = "finished";
    private Button btSyncPrograms, btSyncOrderables, btSyncLots, btSyncFacilities, btSyncFacilityTypes, btSyncFtaps, btSyncReasons, btSyncValidReason, btSyncValidSources, btSyncValidDestrinations;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        syncViewModel = new ViewModelProvider(this).get(SyncViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sync, container, false);
        loginHelper = new LoginHelper(getContext());
        SharedPreferences sharedPrefs = getContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(KEY_USERNAME, "admin");
        editor.putString(KEY_PASSWORD, "password");
        editor.apply();

        btSyncPrograms = (Button) root.findViewById(R.id.bt_sync_programs);
        btSyncOrderables = (Button) root.findViewById(R.id.bt_sync_orderables);
        btSyncLots = (Button) root.findViewById(R.id.bt_sync_lots);
        btSyncFacilityTypes = (Button) root.findViewById(R.id.bt_sync_facility_types);
        btSyncFtaps = (Button) root.findViewById(R.id.bt_sync_ftaps);
        btSyncReasons = (Button) root.findViewById(R.id.bt_sync_reasons);
        btSyncValidReason = (Button) root.findViewById(R.id.bt_sync_valid_reaons);
        btSyncFacilities = (Button) root.findViewById(R.id.bt_sync_facilities);
        btSyncValidSources = (Button) root.findViewById(R.id.bt_sync_valid_sources);
        btSyncValidDestrinations = (Button) root.findViewById(R.id.bt_sync_valid_destinations);

        syncViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals(STATUS_CONNECTING)){
                    progressDialog.setMessage(getString(R.string.string_connecting));
                } else if(s.equals(STATUS_SAVING)){
                    progressDialog.setMessage(getString(R.string.string_saving));
                    System.out.println("saving");
                } else if(s.equals(STATUS_FINISHED)){
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                }

            }
        });

        btSyncPrograms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.PROGRAM);
            }
        });

        btSyncOrderables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.ORDERABLES);
            }
        });

        btSyncFacilityTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.FACILITY_TYPE);
            }
        });

        btSyncFtaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.FACILITY_TYPE_APPROVED_PRODUCTS);
            }
        });

        btSyncLots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.LOTS);
            }
        });

        btSyncReasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.REASON);
            }
        });

        btSyncValidReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.VALID_REASONS);
            }
        });

        btSyncValidSources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.VALID_SOURCES);
            }
        });

        btSyncFacilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.FACILITY);
            }
        });

        btSyncValidDestrinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.string_preparing));
                progressDialog.setCancelable(false);
                progressDialog.show();
                syncViewModel.sync(2, Entity.VALID_DESTINATION);
            }
        });

        return root;
    }

    //update progress dialog



    @Override
    public void onResume() {
        loginHelper = new LoginHelper(getActivity().getApplicationContext());
        loginHelper.obtainAccessToken(null);
        super.onResume();
    }

    public void syncEntity(){

    }
}


