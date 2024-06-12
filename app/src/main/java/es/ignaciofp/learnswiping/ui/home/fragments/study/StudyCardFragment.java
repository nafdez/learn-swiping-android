package es.ignaciofp.learnswiping.ui.home.fragments.study;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Text;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentStudyCardBinding;
import es.ignaciofp.learnswiping.managers.CardManager;
import es.ignaciofp.learnswiping.models.card.Card;
import es.ignaciofp.learnswiping.models.card.Progress;
import es.ignaciofp.learnswiping.services.StudyService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyCardFragment extends Fragment {

    private FragmentStudyCardBinding binding;

    private final CardManager CARD_MANAGER = CardManager.getInstance();
    private final StudyService STUDY_SERVICE = StudyService.getInstance();

    public static final String ARG_DECK_ID = "DECK_ID";
    public static final String ARG_CARD_ID = "CARD_ID";

    private final int EASY_OFFSET = 5;
    private final int HARD_OFFSET = 2;

    private long deckID;
    private long cardID;

    private Card card;

    private MaterialToolbar toolbar;

    private TextView txtFront;
    private TextView txtBack;
    private ConstraintLayout cntLytFront;
    private ConstraintLayout cntLytBack;

    private Button btnFlip;
    private Button btnAgain;
    private Button btnHard;
    private Button btnGood;
    private Button btnEasy;

    private Progress progress;
    private APICallback<Progress> onProgressCallback;
    private APICallback<Void> onNextCardCallback;

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

        toolbar = binding.tlbCard;
//        txtCardTitle = binding.txtStudyCardTitle;
        txtFront = binding.txtStudyCardFront;
        txtBack = binding.txtStudyCardBack;

        cntLytFront = binding.cntLytButtonFrontCard;
        cntLytBack = binding.cntLytButtonBackCard;

        btnFlip = binding.btnCardFlip;
        btnAgain = binding.btnCardAgain;
        btnHard = binding.btnCardHard;
        btnGood = binding.btnCardGood;
        btnEasy = binding.btnCardEasy;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        toolbar.setOnMenuItemClickListener(this::onMenuClick);

        btnFlip.setOnClickListener(this::swapUI);
        btnEasy.setOnClickListener(this::easy);
        btnGood.setOnClickListener(this::good);
        btnHard.setOnClickListener(this::hard);
        btnAgain.setOnClickListener(this::again);

        onProgressCallback = new APICallback<Progress>(requireContext(), Progress.class) {
            @Override
            public void call(Progress progress) {
                StudyCardFragment.this.progress = progress;
            }

            @Override
            public void error(String error) {
            }
        };

        onNextCardCallback = new APICallback<>(requireContext(), Void.class) {
            @Override
            public void call(Void obj) {
            }

            @Override
            public void error(String error) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Connection to the server has failed", Toast.LENGTH_SHORT).show());
            }
        };


        loadCard();
    }

    private void loadCard() {
        CARD_MANAGER.card(
                requireContext(),
                deckID,
                cardID,
                new APICallback<>(requireContext(), Card.class) {
                    @Override
                    public void call(Card card) {
                        StudyCardFragment.this.card = card;
                        CARD_MANAGER.progress(requireContext(), cardID, onProgressCallback);
                        requireActivity().runOnUiThread(() -> {
                            toolbar.setTitle(card.getTitle());
//                            txtCardTitle.setText(card.getTitle());
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

    private boolean onMenuClick(MenuItem m) {
        if (m.getItemId() == R.id.tbCardFlip) swapUI(m.getActionView());

        return true;
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

    private void easy(View v) {
        next(-15);
    }

    private void good(View v) {
        next(0);
    }

    private void hard(View v) {
        next(15);
    }

    private void again(View v) {
        next(20, true);
    }

    private void next(int easePercentage) {
        next(easePercentage, false);
    }

    private void next(int easePercentage, boolean relearnMode) {
        if (progress == null) progress = new Progress();
        progress.setCardID(cardID);
        float newEaseResult = progress.getEase() * ((progress.getEase() * easePercentage) / 100);
        progress.setEase(newEaseResult >= 1 ? newEaseResult : 1); // Prevent 0 or negative days
        progress.setDaysHidden((int) Math.ceil(progress.getEase()));
        progress.setWatchCount(progress.getWatchCount() + 1);
        progress.setCorrectCount(relearnMode ? progress.getCorrectCount() : progress.getCorrectCount() + 1);
        progress.setRelearning(relearnMode);
        CARD_MANAGER.updateProgress(requireContext(), progress, onNextCardCallback);
        progress = new Progress();

        card = STUDY_SERVICE.next();
        if (card == null) {
            Navigation.findNavController(requireView()).popBackStack();
            Toast.makeText(requireContext(), "No cards left!", Toast.LENGTH_SHORT).show();
            return;
        }

        cardID = card.getCardID();

        loadCard();
        swapUI(requireView());
    }
}