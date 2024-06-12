package es.ignaciofp.learnswiping.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.ActivityMainBinding;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.User;
import es.ignaciofp.learnswiping.ui.auth.AuthActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private BottomNavigationView navView;
    private FragmentContainerView navHost;
    private FragmentContainerView frgCntWholeActivityScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // When opening the app user in UserManager will be null
        // so request a login via token (if any)
        // If rejected, then go to auth activity
        if (UserManager.getInstance().getUser() == null) {
            UserManager.getInstance().loginWithToken(this, new APICallback<>(this, User.class) {
                @Override
                public void call(User user) {
                    UserManager.getInstance().setUser(user);
                }

                @Override
                public void error(String error) {
                    // Not logged in, so going back to login activity
                    startActivity(new Intent(CONTEXT, AuthActivity.class));
                    finish();
                }
            });
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navView = findViewById(R.id.nav_view);
        navHost = findViewById(R.id.nav_host_fragment_activity_main);

        frgCntWholeActivityScreen = binding.frmCntWholeActivityScreen;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}