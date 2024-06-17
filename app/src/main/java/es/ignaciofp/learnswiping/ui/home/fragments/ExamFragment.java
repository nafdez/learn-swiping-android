package es.ignaciofp.learnswiping.ui.home.fragments;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.adapters.AdapterDeck;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentExamBinding;
import es.ignaciofp.learnswiping.managers.DeckManager;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.deck.Deck;
import es.ignaciofp.learnswiping.ui.home.fragments.deck.DeckDetailsFragment;

public class ExamFragment extends Fragment {

    private FragmentExamBinding binding;
    private final DeckManager DECK_MANAGER = DeckManager.getInstance();

    private List<Deck> deckList;
    private AdapterDeck adapterDeck;
    private RecyclerView rvDeck;
    private APICallback<List<Deck>> deckListCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExamBinding.inflate(inflater, container, false);

        rvDeck = binding.rvShopDecks;

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
                // Nothing, just don't add them
            }
        };

        DECK_MANAGER.shopTopN(requireContext(), 50, deckListCallback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvDeck.setAdapter(null);
        adapterDeck = null;
        rvDeck = null;
        binding = null;
    }

    private void onRvItemClick(int position) {
        // Open deck details fragment
        Bundle bundle = new Bundle();
        bundle.putLong(DeckDetailsFragment.ARG_DECK_ID, deckList.get(position).getID());
//        bundle.putBoolean(DeckDetailsFragment.ARG_HAS_SUBSCRIPTION, );
        bundle.putInt(DeckDetailsFragment.ARG_MODE, DeckDetailsFragment.MODE_SHOP);
        Navigation.findNavController(requireView()).navigate(R.id.action_navigation_exam_to_deckDetailsFragment, bundle);
    }

    private void onPopupMenuClick(int position) {
    }

}