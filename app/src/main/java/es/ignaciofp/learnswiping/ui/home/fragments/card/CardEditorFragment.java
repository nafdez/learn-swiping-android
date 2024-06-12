package es.ignaciofp.learnswiping.ui.home.fragments.card;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentCardEditorBinding;
import es.ignaciofp.learnswiping.managers.CardManager;
import es.ignaciofp.learnswiping.models.card.Card;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardEditorFragment extends Fragment {

    private FragmentCardEditorBinding binding;

    private final CardManager CARD_MANAGER = CardManager.getInstance();

    public static final String ARG_DECK_ID = "DECK_ID";
    public static final String ARG_DECK_TITLE = "DECK_TITLE";
    public static final String ARG_CARD_ID = "CARD_ID";
    public static final String ARG_EDIT_MODE = "EDIT_MODE";

    private long deckID;
    private String deckTitle;
    private long cardID;
    private boolean isInEditMode;

    private MaterialToolbar toolbar;
    private TextView txtDeckTitle;
    private EditText txtEditTitle;
    private EditText txtEditFront;
    private EditText txtEditBack;
    private EditText txtEditQuestion;
    private EditText txtEditAnswer;
    private EditText txtEditWrong1;
    private EditText txtEditWrong2;
    private EditText txtEditWrong3;

    public CardEditorFragment() {
        // Required empty public constructor
    }

    public static CardEditorFragment newInstance(long deckID, String deckTitle, long cardID, boolean editMode) {
        CardEditorFragment fragment = new CardEditorFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DECK_ID, deckID);
        args.putString(ARG_DECK_TITLE, deckTitle);
        args.putLong(ARG_CARD_ID, cardID);
        args.putBoolean(ARG_EDIT_MODE, editMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deckID = getArguments().getLong(ARG_DECK_ID);
            deckTitle = getArguments().getString(ARG_DECK_TITLE);
            cardID = getArguments().getLong(ARG_CARD_ID);
            isInEditMode = getArguments().getBoolean(ARG_EDIT_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardEditorBinding.inflate(inflater, container, false);

        toolbar = binding.tlbCardEditor;
        txtDeckTitle = binding.txtCardEditorScreenDeckTitle;
        txtEditTitle = binding.txtEditCardEditorTitle;
        txtEditFront = binding.txtEditCardEditorFront;
        txtEditBack = binding.txtEditCardEditorBack;
        txtEditQuestion = binding.txtEditCardEditorQuestion;
        txtEditAnswer = binding.txtEditCardEditorAnswer;
        txtEditWrong1 = binding.txtEditCardEditorWrongOne;
        txtEditWrong2 = binding.txtEditCardEditorWrongTwo;
        txtEditWrong3 = binding.txtEditCardEditorWrongThree;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(this::toPreviousNav);
        toolbar.setOnMenuItemClickListener(this::onMenuClick);

        txtDeckTitle.setText(deckTitle);

        if (isInEditMode) {
            CARD_MANAGER.card(requireContext(), deckID, cardID, new APICallback<>(requireContext(), Card.class) {
                @Override
                public void call(Card card) {
                    requireActivity().runOnUiThread(() -> {
                        txtEditTitle.setText(card.getTitle());
                        txtEditFront.setText(card.getFront());
                        txtEditBack.setText(card.getBack());
                        txtEditQuestion.setText(card.getQuestion());
                        txtEditAnswer.setText(card.getAnswer());
                        txtEditWrong1.setText(card.getWrongAnswers().get(0).getAnswer());
                        txtEditWrong2.setText(card.getWrongAnswers().get(1).getAnswer());
                        txtEditWrong3.setText(card.getWrongAnswers().get(2).getAnswer());
                    });
                }

                @Override
                public void error(String error) {

                }
            });
        }
    }

    private boolean onMenuClick(MenuItem m) {
        if (m.getItemId() == R.id.tbDeckSave) {
            if (isInEditMode) updateCard();
            else saveCard();
        } else if (m.getItemId() == R.id.tbDeckDelete) deleteCard();

        return true;
    }

    private void saveCard() {
        Card card = new Card();
        card.setDeckID(deckID);
        card.setTitle(txtEditTitle.getText().toString().trim());
        card.setFront(txtEditFront.getText().toString().trim());
        card.setBack(txtEditBack.getText().toString().trim());
        card.setQuestion(txtEditQuestion.getText().toString().trim());
        card.setAnswer(txtEditAnswer.getText().toString().trim());
        card.setWrongAnswers(List.of(
                new Card.WrongAnswer(txtEditWrong1.getText().toString().trim()),
                new Card.WrongAnswer(txtEditWrong2.getText().toString().trim()),
                new Card.WrongAnswer(txtEditWrong3.getText().toString().trim())
        ));

        if (card.getTitle().isEmpty() || card.getFront().isEmpty() || card.getBack().isEmpty() ||
                card.getQuestion().isEmpty() || card.getAnswer().isEmpty() ||
                card.getWrongAnswers().stream().anyMatch(wrongAnswer -> wrongAnswer.getAnswer().isEmpty())) {
            return;
        }

        CARD_MANAGER.create(requireContext(), card, new APICallback<>(requireContext(), Void.class) {
            @Override
            public void call(Void obj) {
                ((Activity) CONTEXT).runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Saved correctly!", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void error(String error) {
                ((Activity) CONTEXT).runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Unable to save: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    private void updateCard() {

    }

    private void deleteCard() {
        CARD_MANAGER.delete(requireContext(), deckID, cardID, null);
    }

    private void toPreviousNav(View v) {
        Navigation.findNavController(v).popBackStack();
    }
}