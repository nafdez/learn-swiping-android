package es.ignaciofp.learnswiping.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import es.ignaciofp.learnswiping.ui.home.fragments.account.AccountDetailsFragment;
import es.ignaciofp.learnswiping.ui.home.fragments.account.AccountStatsFragment;
import es.ignaciofp.learnswiping.ui.home.fragments.account.OwnedDecksFragment;

public class VPAccountAdapter extends FragmentStateAdapter {

    public VPAccountAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return switch (position) {
            case 0 -> new AccountDetailsFragment();
            case 1 -> new AccountStatsFragment();
            case 2 -> new OwnedDecksFragment();
            default -> throw new IllegalStateException("Unexpected value: " + position);
        };
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
