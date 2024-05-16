package es.ignaciofp.learnswiping.ui.auth.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.OnErrorCallable;
import es.ignaciofp.learnswiping.databinding.FragmentLoginBinding;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.User;
import es.ignaciofp.learnswiping.ui.auth.AuthActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private FragmentLoginBinding binding;
    private AuthActivity authActivity;

    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASSWORD = "password";

    private String username;
    private String password;

    private EditText txtEditUsername;
    private EditText txtEditPassword;
    private OnErrorCallable onLoginError;

    public LoginFragment() {
    }

    /**
     *
     * @param username provided on sign up if any
     * @param password provided on sign up if any
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(String username, String password) {
        LoginFragment fragment = new LoginFragment();
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
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        authActivity = (AuthActivity) getActivity();

        txtEditUsername = binding.txtUsername;
        txtEditPassword = binding.txtPassword;
        Button loginButton = binding.loginBtn;
        Button signUpButton = binding.goSignUpBtn;

        loginButton.setOnClickListener(this);
        loginButton.setTag("loginButton");
        signUpButton.setOnClickListener(this);
        signUpButton.setTag("signUpButton");

        // Assigns what was introduced in sign up screen, if anything
        txtEditUsername.setText(username);
        txtEditPassword.setText(password);

        onLoginError = new OnErrorCallable(requireContext()) {
            @Override
            public Void call() {
                showAlert("Login failed");
                return null;
            }
        };

        return root;
    }

    @Override
    public void onClick(View v) {
        Log.d("PEPELUI", "PEPELUIII");
        switch (v.getTag().toString()) {
            case "loginButton":
                String username = txtEditUsername.getText().toString().replaceAll("\\s+", "");
                String password = txtEditPassword.getText().toString().trim();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                if (UserManager.getInstance().login(user, onLoginError)) {
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("No deck found")
                            .setMessage(String.format("Logged as: %s\nToken: %s\nSince: %s", user.getUsername(), user.getToken(), user.getSince()))
                            .setPositiveButton("OK", (dialog, which) -> {
                    }).show();
                }
                break;
            case "signUpButton":
                Log.d("PEPELUI", "spsdf");
                String usernme = txtEditUsername.getText().toString().replaceAll("\\s+", "");
                String passwd = txtEditPassword.getText().toString().trim();
                authActivity.changeFragment(new SignUpFragment(), usernme, passwd);
                break;
        }
    }
}