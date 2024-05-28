package es.ignaciofp.learnswiping.ui.home.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.adapters.AdapterDeck;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentHomeBinding;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.Deck;
import es.ignaciofp.learnswiping.services.HomeService;
import es.ignaciofp.learnswiping.ui.home.MainActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private MainActivity mainActivity;

    private final HomeService HOME_SERVICE = HomeService.getInstance();

    private List<Deck> deckList;
    private AdapterDeck adapterDeck;
    private RecyclerView rcrDeck;
    private APICallback<List<Deck>> deckListCallback;

    private ConstraintLayout lytSelectedDeck;

    private FloatingActionButton fabAddDeck;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mainActivity = (MainActivity) getActivity();

        lytSelectedDeck = binding.lytSelectedDeck;
        fabAddDeck = binding.fabAddDeck;
        fabAddDeck.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_deckEditorFragment));
        fabAddDeck.setTag("fabAddDeck");

        // HomeFragment is the first fragment the user sees
        // It is first loaded when loading MainActivity, meaning
        // we're checking for login status in MainActivity while
        // attempting to use the user in this fragment
        if (UserManager.getInstance().getUser() == null) return root;

        if (HOME_SERVICE.getWorkingDeckID() == HomeService.CLEAR_SELECTION) lytSelectedDeck.setVisibility(View.GONE);

        deckListCallback = new APICallback<>(requireContext()) {
            @Override
            public void call() {
//                deckList.addAll(getObj());
                deckList.addAll(getObj().stream()
                        .sorted(Comparator.comparing(Deck::getUpdatedAt).reversed())
                        .collect(Collectors.toList()));
            }

            @Override
            public void error() {
                // Nothing, just don't add them
            }
        };

        rcrDeck = binding.rcrDeck;
        rcrDeck.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        rcrDeck.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 16;
                outRect.bottom = 16;
            }
        });


        deckList = new LinkedList<>();


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // deckList get cleared when rotating the device, so populating again on Start
        // We're going to show both decks on this view
        UserManager.getInstance().ownedDecks(requireContext(), deckListCallback);
        UserManager.getInstance().subscribedDecks(requireContext(), deckListCallback);

        adapterDeck = new AdapterDeck(deckList);
        rcrDeck.setAdapter(adapterDeck);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().toString().equals("fabAddDeck")) {
            mainActivity.openFormFragment(new DeckEditorFragment());
        }
    }
}