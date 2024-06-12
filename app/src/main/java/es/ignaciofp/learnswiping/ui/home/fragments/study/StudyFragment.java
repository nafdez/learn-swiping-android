package es.ignaciofp.learnswiping.ui.home.fragments.study;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.adapters.AdapterCard;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentStudyBinding;
import es.ignaciofp.learnswiping.managers.CardManager;
import es.ignaciofp.learnswiping.managers.DeckManager;
import es.ignaciofp.learnswiping.managers.UserManager;
import es.ignaciofp.learnswiping.models.card.Card;
import es.ignaciofp.learnswiping.models.deck.Deck;
import es.ignaciofp.learnswiping.services.HomeService;
import es.ignaciofp.learnswiping.services.StudyService;
import es.ignaciofp.learnswiping.ui.home.fragments.HomeFragment;
import es.ignaciofp.learnswiping.ui.home.fragments.card.CardEditorFragment;
import es.ignaciofp.learnswiping.ui.home.fragments.deck.DeckDetailsFragment;

public class StudyFragment extends Fragment {

    private FragmentStudyBinding binding;

    private final CardManager CARD_MANAGER = CardManager.getInstance();
    private final StudyService STUDY_SERVICE = StudyService.getInstance();

    private Deck deck;

    private RecyclerView rvCard;
    private AdapterCard adapterCard;
    private List<Card> cardList;

    private TextView txtDeckTitle;

    private FloatingActionButton fabStudy;

    private APICallback<List<Card>> cardListCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudyBinding.inflate(inflater, container, false);

        txtDeckTitle = binding.txtStudyScreenDeckTitle;
        fabStudy = binding.fabStartStudying;

        rvCard = binding.rvStudyCard;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deck = HomeService.getInstance().getWorkingDeck();
        if (deck == null) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error")
                    .setMessage("No deck selected")
                    .setPositiveButton(requireContext().getResources().getString(R.string.global_dialog_positive),
                            (d, which) -> Navigation.findNavController(requireView()).popBackStack())
                    .show();
            return;
        }

        fabStudy.setOnClickListener(this::startStudying);

        txtDeckTitle.setText(deck.getTitle());

        rvCard.setHasFixedSize(true);
        rvCard.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        rvCard.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });

        cardList = new ArrayList<>();
        cardListCallback = new APICallback<>(requireContext(), (Class<List<Card>>) (Object) List.class, Card.class) {
            @Override
            public void call(List<Card> cards) {
                cards.stream()
                        .sorted(Comparator.comparing(Card::getTitle))
                        .forEachOrdered(cardList::add);

                ((Activity) CONTEXT).runOnUiThread(() -> {
                    adapterCard = new AdapterCard(
                            cardList,
                            StudyFragment.this::onCardClick,
                            StudyFragment.this::onPopupMenuClick);
                    rvCard.setAdapter(adapterCard);
                });
            }

            @Override
            public void error(String error) {
            }
        };

        CARD_MANAGER.cards(requireContext(), deck.getID(), cardListCallback);
    }

    private void onCardClick(int position) {
        Bundle b = new Bundle();
        b.putLong(StudyCardFragment.ARG_DECK_ID, deck.getID());
        b.putLong(StudyCardFragment.ARG_CARD_ID, cardList.get(position).getCardID());
        Navigation
                .findNavController(requireView())
                .navigate(R.id.action_navigation_study_to_studyCardFragment, b);
    }

    private void onPopupMenuClick(int position) {
        if (!(deck.getOwnerID() == UserManager.getInstance().getUser().getId())) return;

        RecyclerView.ViewHolder viewHolder = rvCard.findViewHolderForAdapterPosition(position);
        if (viewHolder == null) {
            return;
        }
        var popupMenu = new PopupMenu(requireContext(), viewHolder.itemView, Gravity.END);
        popupMenu.inflate(R.menu.menu_item_card);

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menuItemEdit) {
                Bundle b = new Bundle();
                b.putString(CardEditorFragment.ARG_DECK_TITLE, deck.getTitle());
                b.putLong(CardEditorFragment.ARG_DECK_ID, deck.getID());
                b.putLong(CardEditorFragment.ARG_CARD_ID, cardList.get(position).getCardID());
                b.putBoolean(CardEditorFragment.ARG_EDIT_MODE, true);
                Navigation
                        .findNavController(requireView())
                        .navigate(R.id.action_navigation_study_to_cardEditorFragment, b);
            } else if (item.getItemId() == R.id.menuItemDelete) {
                CARD_MANAGER.delete(
                        requireContext(),
                        deck.getID(),
                        cardList.get(position).getCardID(),
                        new APICallback<>(requireContext(), Void.class) {
                            @Override
                            public void call(Void obj) {
                                cardList.remove(position);
                                requireActivity().runOnUiThread(() -> {
                                    adapterCard.notifyItemRemoved(position);
                                });
                            }

                            @Override
                            public void error(String error) {
                                Toast.makeText(requireContext(), "Unable to delete card", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            return false;
        });
        popupMenu.show();
    }

    private void startStudying(View v) {
        CARD_MANAGER.pending(
                requireContext(),
                deck.getID(),
                new APICallback<>(requireContext(), (Class<List<Card>>) (Object) List.class, Card.class) {
                    @Override
                    public void call(List<Card> cards) {
                        requireActivity().runOnUiThread(() -> {
                            STUDY_SERVICE.feedCardList(cards);
                            Card currCard = STUDY_SERVICE.next();
                            if (currCard == null) {
                                error("");
                                return;
                            }
                            Bundle b = new Bundle();
                            b.putLong(StudyCardFragment.ARG_DECK_ID, deck.getID());
                            b.putLong(StudyCardFragment.ARG_CARD_ID, currCard.getCardID());
                            Navigation
                                    .findNavController(requireView())
                                    .navigate(R.id.action_navigation_study_to_studyCardFragment, b);
                        });
                    }

                    @Override
                    public void error(String error) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "No cards left!", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }
}