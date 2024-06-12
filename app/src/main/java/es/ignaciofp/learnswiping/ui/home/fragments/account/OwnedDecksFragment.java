package es.ignaciofp.learnswiping.ui.home.fragments.account;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.adapters.AdapterDeck;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentOwnedDecksBinding;
import es.ignaciofp.learnswiping.managers.DeckManager;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.deck.Deck;
import es.ignaciofp.learnswiping.ui.home.fragments.deck.DeckDetailsFragment;

public class OwnedDecksFragment extends Fragment {

    private FragmentOwnedDecksBinding binding;

    private RecyclerView rvDeck;
    private AdapterDeck adapterDeck;
    private List<Deck> deckList;
    private APICallback<List<Deck>> deckListCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOwnedDecksBinding.inflate(inflater, container, false);

        rvDeck = binding.rvOwnedDecks;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        deckListCallback = new APICallback<>(requireContext(), (Class<List<Deck>>)(Object)List.class, Deck.class) {
            @Override
            public void call(List<Deck> decks) {
                decks.stream()
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
            public void error(String error) {
            }
        };

        UserManager.getInstance().ownedDecks(requireContext(), deckListCallback);
    }

    private void onRvItemClick(int position) {
        // Probably moving open details here
        Bundle bundle = new Bundle();
        bundle.putLong(DeckDetailsFragment.ARG_DECK_ID, deckList.get(position).getID());
        bundle.putBoolean(DeckDetailsFragment.ARG_HAS_SUBSCRIPTION, false);
        bundle.putInt(DeckDetailsFragment.ARG_MODE, DeckDetailsFragment.MODE_OWNER);
        Navigation.findNavController(requireView()).navigate(R.id.action_navigation_account_to_deckDetailsFragment, bundle);
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
        popupMenu.getMenu().removeItem(R.id.menuItemUnsubscribe);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menuItemSubscribe) {
                DeckManager.getInstance()
                        .addDeckSubscription(
                                requireContext(),
                                deckList.get(position).getID(),
                                new APICallback<>(requireContext(), Void.class) {
                                    @Override
                                    public void call(Void obj) {
                                    }

                                    @Override
                                    public void error(String error) {
                                        requireActivity().runOnUiThread(() -> {
                                            Toast.makeText(requireContext(), String.format("%s %s", getString(R.string.owned_decks_toast_subscribe_error), deckList.get(position).getTitle()), Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                });
            } else if(item.getItemId() == R.id.menuItemDelete) {
                DeckManager.getInstance().delete(requireContext(),
                        deckList.get(position).getID(),
                        new APICallback<>(requireContext(), Void.class) {
                            @Override
                            public void call(Void obj) {
                                deckList.remove(position);
                                requireActivity().runOnUiThread(() -> {
                                    adapterDeck.notifyItemRemoved(position);
                                });
                            }

                            @Override
                            public void error(String error) {
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(requireContext(), String.format("%s %s", getString(R.string.owned_decks_toast_delete_error), deckList.get(position).getTitle()), Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvDeck.setAdapter(null);
        adapterDeck = null;
        rvDeck = null;
        binding = null;
    }
}