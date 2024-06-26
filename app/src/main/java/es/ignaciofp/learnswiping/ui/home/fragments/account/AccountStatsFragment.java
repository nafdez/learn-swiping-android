package es.ignaciofp.learnswiping.ui.home.fragments.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.databinding.FragmentAccountStatsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountStatsFragment extends Fragment {

    private FragmentAccountStatsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountStatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}