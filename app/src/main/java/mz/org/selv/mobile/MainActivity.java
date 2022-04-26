package mz.org.selv.mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;

import mz.org.selv.mobile.ui.home.HomeViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ProgressDialog progressDialog;
    MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_requisition, R.id.nav_shipment, R.id.nav_stockmanagement,
            R.id.nav_sync, R.id.nav_log_out)
            .setDrawerLayout(drawer)
            .build();
        NavController navController = Navigation
            .findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setupApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setupApplication(){
            //progressDialog = new ProgressDialog(getApplication());
           // progressDialog.setMessage("Preparando...");
          //  progressDialog.show();
            mainActivityViewModel.setupApplication();
         //   progressDialog.dismiss();
    }
}