package es.ignaciofp.learnswiping.ui.home.fragments.study;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentStudyCardBinding;
import es.ignaciofp.learnswiping.managers.CardManager;
import es.ignaciofp.learnswiping.models.card.Card;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyCardFragment extends Fragment {

    private FragmentStudyCardBinding binding;

    private final CardManager CARD_MANAGER = CardManager.getInstance();

    public static final String ARG_DECK_ID = "DECK_ID";
    public static final String ARG_CARD_ID = "CARD_ID";

    private long deckID;
    private long cardID;

    private Card card;

    private TextView txtCardTitle;
    private TextView txtFront;
    private TextView txtBack;
    private ConstraintLayout cntLytFront;
    private ConstraintLayout cntLytBack;

    private Button btnFlip;
    private Button btnBury;
    private Button btnHard;
    private Button btnEasy;


    public StudyCardFragment() {
        // Required empty public constructor
    }

    public static StudyCardFragment newInstance(long deckID, long cardID) {
        StudyCardFragment fragment = new StudyCardFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DECK_ID, deckID);
        args.putLong(ARG_CARD_ID, cardID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deckID = getArguments().getLong(ARG_DECK_ID);
            cardID = getArguments().getLong(ARG_CARD_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudyCardBinding.inflate(inflater, container, false);

        txtCardTitle = binding.txtStudyCardTitle;
        txtFront = binding.txtStudyCardFront;
        txtBack = binding.txtStudyCardBack;

        cntLytFront = binding.cntLytButtonFrontCard;
        cntLytBack = binding.cntLytButtonBackCard;

        btnFlip = binding.btnCardFlip;
        btnBury = binding.btnCardBury;
        btnHard = binding.btnCardHard;
        btnEasy = binding.btnCardEasy;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnFlip.setOnClickListener(this::swapUI);

        CARD_MANAGER.card(
                requireContext(),
                deckID,
                cardID,
                new APICallback<>(requireContext(), Card.class) {
                    @Override
                    public void call(Card card) {
                        StudyCardFragment.this.card = card;
                        requireActivity().runOnUiThread(() -> {
                            txtCardTitle.setText(card.getTitle());
                            txtFront.setText(card.getFront());
                            txtBack.setText(card.getBack());
                        });
                    }

                    @Override
                    public void error(String error) {
                        requireActivity().runOnUiThread(() -> {
                            Navigation.findNavController(requireView()).popBackStack();
                        });
                    }
                });
    }

    private void swapUI(View v) {
        if (txtFront.getVisibility() == View.VISIBLE) {
            txtFront.setVisibility(View.GONE);
            cntLytFront.setVisibility(View.GONE);

            txtBack.setVisibility(View.VISIBLE);
            cntLytBack.setVisibility(View.VISIBLE);
        } else {
            txtBack.setVisibility(View.GONE);
            cntLytBack.setVisibility(View.GONE);

            txtFront.setVisibility(View.VISIBLE);
            cntLytFront.setVisibility(View.VISIBLE);
        }
    }
}