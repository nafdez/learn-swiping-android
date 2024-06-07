package es.ignaciofp.learnswiping.ui.home.fragments;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.adapters.AdapterDeck;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentHomeBinding;
import es.ignaciofp.learnswiping.managers.DeckManager;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.Deck;
import es.ignaciofp.learnswiping.services.HomeService;
import es.ignaciofp.learnswiping.ui.home.MainActivity;
import es.ignaciofp.learnswiping.ui.home.fragments.deck.DeckDetailsFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MainActivity mainActivity;

    private final HomeService HOME_SERVICE = HomeService.getInstance();

    private List<Deck> deckList;
    private AdapterDeck adapterDeck;
    private RecyclerView rvDeck;
    private APICallback<List<Deck>> deckListCallback;

    private ConstraintLayout lytSelectedDeck;
    private TextView txtSelectedDeck;

    private FloatingActionButton fabAddDeck;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        mainActivity = (MainActivity) getActivity();

        lytSelectedDeck = binding.lytSelectedDeck;
        txtSelectedDeck = binding.txtSelectedDeck;
        fabAddDeck = binding.fabAddDeck;
        rvDeck = binding.rvSubDecks;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabAddDeck.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_deckCreatorFragment));
        fabAddDeck.setTag("fabAddDeck");

        // HomeFragment is the first fragment the user sees
        // It is first loaded when loading MainActivity, meaning
        // we're checking for login status in MainActivity while
        // attempting to use the user in this fragment
        if (UserManager.getInstance().getUser() == null) return;

        // Hide information about selected deck (Restored later when one is selected)
        if (HOME_SERVICE.getWorkingDeck() == null)
            lytSelectedDeck.setVisibility(View.GONE);
        else
            txtSelectedDeck.setText(HOME_SERVICE.getWorkingDeck().getTitle());

        /* RECYCLER VIEW */
        rvDeck.setHasFixedSize(true);
        rvDeck.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        rvDeck.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 16;
                outRect.bottom = 16;
            }
        });

        deckList = new ArrayList<>();
        deckListCallback = new APICallback<>(requireContext()) {
            @Override
            public void call() {
                getObj().stream()
                        .sorted(Comparator.comparing(Deck::getUpdatedAt).reversed())
                        .forEachOrdered(deckList::add);

                ((Activity) CONTEXT).runOnUiThread(() -> {
                    adapterDeck = new AdapterDeck(deckList,
                            position -> onRvItemClick(position),
                            position -> onPopupMenuClick(position));
                    rvDeck.setAdapter(adapterDeck);
                });
            }

            @Override
            public void error() {
                // Nothing, just don't add them
            }
        };

        UserManager.getInstance().subscribedDecks(requireContext(), deckListCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvDeck.setAdapter(null);
        adapterDeck = null;
        rvDeck = null;
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void onRvItemClick(int position) {
        HOME_SERVICE.setWorkingDeck(deckList.get(position));
        txtSelectedDeck.setText(deckList.get(position).getTitle()); // Information for User
        lytSelectedDeck.setVisibility(View.VISIBLE);
    }

    private void onPopupMenuClick(int position) {
        // Cannot use getChildAt() bc RecyclerView calculates the position based on scroll offset
        // and the position is being provided by the adapter, who just gives the position on list
        RecyclerView.ViewHolder viewHolder = rvDeck.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            return;
        }
        var popupMenu = new PopupMenu(requireContext(), viewHolder.itemView, Gravity.END);
        popupMenu.inflate(R.menu.menu_item_deck);

        // We're only showing already subscribed decks so no subscribe nor delete option
        popupMenu.getMenu().removeItem(R.id.menuItemSelectDeck);
        popupMenu.getMenu().removeItem(R.id.menuItemSubscribe);
        popupMenu.getMenu().removeItem(R.id.menuItemDelete);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menuItemDetails) {
                // Open deck details fragment
                Bundle bundle = new Bundle();
                bundle.putLong(DeckDetailsFragment.ARG_DECK_ID, deckList.get(position).getID());
                bundle.putBoolean(DeckDetailsFragment.ARG_HAS_SUBSCRIPTION, false);
                bundle.putInt(DeckDetailsFragment.ARG_MODE, DeckDetailsFragment.MODE_SUB);
                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_home_to_deckDetailsFragment);
            } else if (item.getItemId() == R.id.menuItemUnsubscribe) {
                // Unsubscribe and remove from rv if successful
                DeckManager.getInstance()
                        .removeDeckSubscription(
                                requireContext(),
                                deckList.get(position).getID(),
                                new APICallback<>(requireContext()) {
                                    @Override
                                    public void call() {
                                        deckList.remove(position);
                                        requireActivity().runOnUiThread(() -> {
                                           adapterDeck.notifyItemRemoved(position);
                                        });
                                    }

                                    @Override
                                    public void error() {
                                        requireActivity().runOnUiThread(() -> {
                                            Toast.makeText(requireContext(), String.format("%s %s", getString(R.string.home_toast_unsubscribe_error), deckList.get(position).getTitle()), Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                });
            }
            return false;
        });
        popupMenu.show();
    }
}