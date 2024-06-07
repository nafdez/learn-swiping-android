package es.ignaciofp.learnswiping.ui.home.fragments.deck;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

import es.ignaciofp.learnswiping.databinding.FragmentDeckDetailsBinding;
import es.ignaciofp.learnswiping.managers.DeckManager;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.Deck;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeckDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeckDetailsFragment extends Fragment {

    private FragmentDeckDetailsBinding binding;

    private static final String ARG_DECK_ID = "DECK_ID";
    private static final String ARG_HAS_SUBSCRIPTION = "HAS_SUBSCRIPTION";

    private String deckId;
    private boolean hasSubscription;

    private Deck deck;

    private DeckManager DECK_MANAGER;

    private MaterialToolbar toolbar;
    private ImageView imgDeck;
    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtRating;

    public DeckDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param deck_id Parameter 1.
     * @return A new instance of fragment DeckDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeckDetailsFragment newInstance(String deck_id, boolean hasSubscription) {
        DeckDetailsFragment fragment = new DeckDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DECK_ID, deck_id);
        args.putBoolean(ARG_HAS_SUBSCRIPTION, hasSubscription);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deckId = getArguments().getString(ARG_DECK_ID);
            hasSubscription = getArguments().getBoolean(ARG_HAS_SUBSCRIPTION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeckDetailsBinding.inflate(inflater, container, false);

        toolbar = binding.tlbDeckDetails;
        imgDeck = binding.imgDeckImgDetails;
        txtTitle = binding.txtDeckTitleDetails;
        txtDescription = binding.txtDeckDescriptionDetails;
        txtRating = binding.txtRatingDeckDetails;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DECK_MANAGER = DeckManager.getInstance();

        if (deck.getOwnerID() == UserManager.getInstance().getUser().getId()) {
            // Is Owner
        } else {
            // It isn't
        }

        toolbar.setNavigationOnClickListener((v) -> Navigation.findNavController(v).popBackStack());
    }
}