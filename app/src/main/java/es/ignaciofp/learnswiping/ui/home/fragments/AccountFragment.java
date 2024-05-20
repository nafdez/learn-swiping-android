package es.ignaciofp.learnswiping.ui.home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentAccountBinding;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.User;
import es.ignaciofp.learnswiping.ui.auth.AuthActivity;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private FragmentAccountBinding binding;

    private final UserManager USER_MANAGER = UserManager.getInstance();
    private final User USER = USER_MANAGER.getUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView txtName = binding.txtAccName;
        TextView txtUsername = binding.txtAccUsername;
        TextView txtEmail = binding.txtAccEmail;
        TextView txtSince = binding.txtAccSince;
        ImageView imgProfile = binding.imgAccProfile;
        Button btnLogOut = binding.btnLogOut;

        txtName.setText(USER.getName());
        txtUsername.setText(USER.getUsername());
        txtEmail.setText(USER.getEmail());
        txtSince.setText(String.format("Since: %s", USER.getSince().format(DateTimeFormatter.ISO_DATE).replaceAll("-", "/")));
        btnLogOut.setOnClickListener(this);
        btnLogOut.setTag("logout");

        USER_MANAGER.userPicture(new APICallback<>(requireContext()) {
            @Override
            public void call() {
                ((Activity) CONTEXT).runOnUiThread(() ->{
                    imgProfile.setImageBitmap(getObj());
                });
            }

            @Override
            public void error() {
                ((Activity) CONTEXT).runOnUiThread(() ->{
                    imgProfile.setImageDrawable(AppCompatResources.getDrawable(CONTEXT, R.drawable.ic_fallback));
                });
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().toString().equals("logout")) {
            USER_MANAGER.logout(requireContext());
            startActivity(new Intent(requireContext(), AuthActivity.class));
            requireActivity().finish();
        }
    }
}