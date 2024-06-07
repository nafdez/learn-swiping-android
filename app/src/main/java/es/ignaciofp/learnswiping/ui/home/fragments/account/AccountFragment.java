package es.ignaciofp.learnswiping.ui.home.fragments.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.time.format.DateTimeFormatter;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.adapters.VPAccountAdapter;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentAccountBinding;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.User;
import es.ignaciofp.learnswiping.ui.auth.AuthActivity;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        tabLayout = binding.tlAccount;
        viewPager2 = binding.vp2Account;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2.setAdapter(new VPAccountAdapter(requireActivity().getSupportFragmentManager(), getLifecycle()));
        viewPager2.setSaveFromParentEnabled(false);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}