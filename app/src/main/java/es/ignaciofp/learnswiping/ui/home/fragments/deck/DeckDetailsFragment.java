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

    public static final int MODE_OWNER = 0;
    public static final int MODE_SHOP = 1;
    public static final int MODE_SUB = 2;

    public static final String ARG_DECK_ID = "DECK_ID";
    public static final String ARG_HAS_SUBSCRIPTION = "HAS_SUBSCRIPTION";
    public static final String ARG_MODE = "MODE";

    private Long deckId;
    private boolean hasSubscription;
    private int mode;

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
     * @param deckID Parameter 1.
     * @return A new instance of fragment DeckDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeckDetailsFragment newInstance(long deckID, boolean hasSubscription, int mode) {
        DeckDetailsFragment fragment = new DeckDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DECK_ID, deckID);
        args.putBoolean(ARG_HAS_SUBSCRIPTION, hasSubscription);
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deckId = getArguments().getLong(ARG_DECK_ID);
            hasSubscription = getArguments().getBoolean(ARG_HAS_SUBSCRIPTION);
            mode = getArguments().getInt(ARG_MODE);
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

//        switch (mode) {
//            case MODE_OWNER -> ;
//            case MODE_SHOP -> ;
//            case MODE_SUB -> ;
//        }

        toolbar.setNavigationOnClickListener((v) -> Navigation.findNavController(v).popBackStack());

    }
}