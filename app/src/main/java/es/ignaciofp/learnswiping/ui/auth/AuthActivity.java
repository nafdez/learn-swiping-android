package es.ignaciofp.learnswiping.ui.auth;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.ui.auth.fragments.SignUpFragment;

public class AuthActivity extends AppCompatActivity {

    private int authFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        authFragmentId = R.id.frgCntAuth;
    }

    public void changeFragment(Fragment fragment, String username, String password) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("password", password);
        fragment.setArguments(bundle);

        transaction.replace(authFragmentId, fragment);
        transaction.commit();
    }
}