package es.ignaciofp.learnswiping.ui.home.fragments.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import es.ignaciofp.learnswiping.databinding.FragmentAccountDetailsBinding;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.User;
import es.ignaciofp.learnswiping.ui.auth.AuthActivity;

public class AccountDetailsFragment extends Fragment implements View.OnClickListener{

    private final UserManager USER_MANAGER = UserManager.getInstance();
    private final User USER = USER_MANAGER.getUser();

    private FragmentAccountDetailsBinding binding;

    private TextView txtName;
    private TextView txtUsername;
    private TextView txtEmail;
    private TextView txtSince;
    private ImageView imgProfile;
    private Button btnLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountDetailsBinding.inflate(inflater, container, false);

        txtName = binding.txtAccName;
        txtUsername = binding.txtAccUsername;
        txtEmail = binding.txtAccEmail;
        txtSince = binding.txtAccSince;
        imgProfile = binding.imgDeckImgDetails;
        btnLogOut = binding.btnLogOut;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            public void error(String error) {
                ((Activity) CONTEXT).runOnUiThread(() ->{
                    imgProfile.setImageDrawable(AppCompatResources.getDrawable(CONTEXT, R.drawable.ic_fallback));
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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