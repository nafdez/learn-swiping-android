package es.ignaciofp.learnswiping.ui.auth.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentSignUpBinding;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.User;
import es.ignaciofp.learnswiping.ui.auth.AuthActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    private FragmentSignUpBinding binding;
    private AuthActivity authActivity;

    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASSWORD = "password";

    private String username;
    private String password;

    private EditText txtEditEmail;
    private EditText txtEditName;
    private EditText txtEditUsername;
    private EditText txtEditPassword;
    private APICallback<User> signUpCallback;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param username provided on login if any
     * @param password provided on login if any
     * @return A new instance of fragment SignUpFragment.
     */
    public static SignUpFragment newInstance(String username, String password) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME, "");
            password = getArguments().getString(ARG_PASSWORD, "");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        authActivity = (AuthActivity) getActivity();

        txtEditEmail= binding.txtRegEmail;
        txtEditName = binding.txtRegName;
        txtEditUsername = binding.txtRegUsername;
        txtEditPassword = binding.txtRegPassword;

        Button btnSignUp = binding.btnSignUp;
        Button btnGoBack = binding.btnGoBack;

        btnSignUp.setOnClickListener(this);
        btnSignUp.setTag("signUpButton");
        btnGoBack.setOnClickListener(this);
        btnGoBack.setTag("goBackButton");

        // Assigns what was introduced in login screen, if anything
        txtEditUsername.setText(username);
        txtEditPassword.setText(password);

        signUpCallback = new APICallback<User>(requireContext()) {
            @Override
            public void call() {
                showAlert(CONTEXT.getString(R.string.auth_alert_sign_up_ok));
                goToLoginFragment();
            }

            @Override
            public void error(String error) {
                showAlert(CONTEXT.getString(R.string.global_generic_error));
            }
        };

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()) {
            case "signUpButton":
                String email = txtEditEmail.getText().toString().trim();
                String name = txtEditName.getText().toString().trim();
                username = txtEditUsername.getText().toString().replaceAll("\\s+", "");
                password = txtEditPassword.getText().toString().trim();

                if (email.isEmpty() && name.isEmpty() && username.isEmpty() && password.isEmpty()) return;

                User user = new User();
                user.setEmail(email);
                user.setName(name);
                user.setUsername(username);
                user.setPassword(password);

                UserManager.getInstance().register(user, signUpCallback);

                break;
            case "goBackButton":
                goToLoginFragment();
                break;
        }
    }

    private void goToLoginFragment() {
        username = txtEditUsername.getText().toString().replaceAll("\\s+", "");
        password = txtEditPassword.getText().toString().trim();
        authActivity.changeFragment(new LoginFragment(), username, password);
    }
}