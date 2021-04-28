package mz.org.selv.mobile.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import mz.org.selv.mobile.R;

public class AuthFragment extends Fragment {

  private AuthViewModel authViewModel;

  public View onCreateView(@NonNull LayoutInflater inflater,
      ViewGroup container, Bundle savedInsanceState) {
    authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    View root = inflater.inflate(R.layout.fragment_auth, container, false);

    Button btLogIn = root.findViewById(R.id.bt_log_in);
    btLogIn.setOnClickListener(v -> authViewModel.obtainAccessToken());

    return root;
  }
}
