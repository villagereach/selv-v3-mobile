package mz.org.selv.mobile.ui.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import mz.org.selv.mobile.R;
import mz.org.selv.mobile.auth.LoginHelper;

import static mz.org.selv.mobile.auth.LoginHelper.APP_SHARED_PREFS;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_PASSWORD;
import static mz.org.selv.mobile.auth.LoginHelper.KEY_USERNAME;

public class SyncFragment extends Fragment {

    private SyncViewModel syncViewModel;
    private LoginHelper loginHelper;


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

        Button btSync = (Button) root.findViewById(R.id.bt_sync_metadata);

        btSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginHelper.refreshToken();
                //syncViewModel.sync(1);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        loginHelper = new LoginHelper(getActivity().getApplicationContext());
        loginHelper.obtainAccessToken(null);
        super.onResume();
    }
}


