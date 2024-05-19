package es.ignaciofp.learnswiping.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.ActivityMainBinding;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.ui.auth.AuthActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // When opening the app user in UserManager will be null
        // so request a login via token (if any)
        // If rejected, then go to auth activity
        // TODO: login with token fails
        if (UserManager.getInstance().getUser() == null) {
            UserManager.getInstance().loginWithToken(this, new APICallback<>(this) {
                @Override
                public void call() {
                    UserManager.getInstance().setUser(getObj());
                }

                @Override
                public void error() {
                    // Not logged in, so going back to login activity
                    startActivity(new Intent(CONTEXT, AuthActivity.class));
                    finish();
                }
            });
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}