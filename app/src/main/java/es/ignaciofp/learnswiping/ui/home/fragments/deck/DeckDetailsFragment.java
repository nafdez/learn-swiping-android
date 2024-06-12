package es.ignaciofp.learnswiping.ui.home.fragments.deck;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentDeckDetailsBinding;
import es.ignaciofp.learnswiping.managers.DeckManager;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.deck.Deck;
import es.ignaciofp.learnswiping.models.deck.DeckDetails;
import es.ignaciofp.learnswiping.ui.home.fragments.card.CardEditorFragment;

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
    private boolean hasSubscription = false;
    private int mode = -1;

    private Map<String, String> details;

    private DeckManager DECK_MANAGER;

    private MaterialToolbar toolbar;
    private ImageView imgDeck;
    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtRating;
    private Button btnSubscription;
    private FloatingActionButton fabAddCard;

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
        btnSubscription = binding.btnSubscriptionDeckDetails;
        fabAddCard = binding.fabAddCards;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DECK_MANAGER = DeckManager.getInstance();

        toolbar.setNavigationOnClickListener((v) -> Navigation.findNavController(v).popBackStack());
        setBaseState();
    }

    private void setBaseState() {
        fabAddCard.setVisibility(View.GONE);
        // TODO: terminar
        APICallback<DeckDetails> apiCallback = new APICallback<>(requireContext(), DeckDetails.class) {
            @Override
            public void call(DeckDetails deck) {
                ((Activity) CONTEXT).runOnUiThread(() -> {
                    txtTitle.setText(deck.getTitle());
                    txtDescription.setText(deck.getDescription());
                    hasSubscription = deck.isSusbcribed();
                    if (deck.getOwnerID() == UserManager.getInstance().getUser().getId())
                        fabAddCard.setVisibility(View.VISIBLE);
                    else
                        fabAddCard.setVisibility(View.GONE);

                    fabAddCard.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putLong(CardEditorFragment.ARG_DECK_ID, deckId);
                        bundle.putString(CardEditorFragment.ARG_DECK_TITLE, deck.getTitle());
                        Navigation.findNavController(v).navigate(R.id.action_deckDetailsFragment_to_cardEditorFragment, bundle);
                    });
                });
                DECK_MANAGER.deckPicture(deck.getPicID(), new APICallback<>(requireContext(), Bitmap.class) {
                    @Override
                    public void call(Bitmap bmp) {
                        ((Activity) CONTEXT).runOnUiThread(() -> {
                            imgDeck.setImageBitmap(bmp);
                        });
                    }

                    @Override
                    public void error(String error) {
                    }
                });
            }

            @Override
            public void error(String error) {
                showAlert("Unable to fetch details");
            }
        };

        switch (mode) {
            case MODE_OWNER -> DECK_MANAGER.deckDetailsOwner(requireContext(), deckId, apiCallback);
            case MODE_SHOP ->
                    DECK_MANAGER.deckDetailsShop(requireContext(), UserManager.getInstance().getUser().getUsername(), deckId, apiCallback);
            case MODE_SUB ->
                    DECK_MANAGER.deckDetailsSub(requireContext(), UserManager.getInstance().getUser().getUsername(), deckId, apiCallback);
        }

        if (hasSubscription) {
            btnSubscription.setText(R.string.deck_details_btn_unsubscribe);
            btnSubscription.setOnClickListener(DeckDetailsFragment.this::deckUnsubscribe);
        } else {
            btnSubscription.setText(R.string.deck_details_btn_subscribe);
            btnSubscription.setOnClickListener(DeckDetailsFragment.this::deckSubscribe);
        }
    }

    private void deckSubscribe(View v) {
        DeckManager.getInstance()
                .addDeckSubscription(
                        requireContext(),
                        deckId,
                        new APICallback<Void>(requireContext(), Void.class) {
                            @Override
                            public void call(Void obj) {
                                hasSubscription = true;
                                ((Activity) CONTEXT).runOnUiThread(() -> {
                                    setBaseState();
                                });
                            }

                            @Override
                            public void error(String error) {
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(requireContext(), String.format("%s %s", getString(R.string.home_toast_subscribe_error), txtTitle.getText()), Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                );
    }

    private void deckUnsubscribe(View v) {
        DeckManager.getInstance()
                .removeDeckSubscription(
                        requireContext(),
                        deckId,
                        new APICallback<>(requireContext(), Void.class) {
                            @Override
                            public void call(Void obj) {
                                hasSubscription = false;
                                ((Activity) CONTEXT).runOnUiThread(() -> {
                                    setBaseState();
                                });
                            }

                            @Override
                            public void error(String error) {
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(requireContext(), String.format("%s %s", getString(R.string.home_toast_unsubscribe_error), txtTitle.getText()), Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
    }

}