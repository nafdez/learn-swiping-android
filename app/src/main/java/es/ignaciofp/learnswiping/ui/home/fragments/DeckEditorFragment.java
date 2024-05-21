package es.ignaciofp.learnswiping.ui.home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.databinding.FragmentDeckEditorBinding;
import es.ignaciofp.learnswiping.databinding.FragmentHomeBinding;
import es.ignaciofp.learnswiping.models.Deck;
import es.ignaciofp.learnswiping.ui.home.MainActivity;

public class DeckEditorFragment extends Fragment {

    private FragmentDeckEditorBinding binding;
    private static final String ARG_DECK_ID = "DECKID_EDIT";

    private int deckID = -1;

    private Deck deck;

    public DeckEditorFragment() {
    }

    /**
     * Receives a id of a deck to edit it. If no id is provided
     * then it creates a new one
     *
     * @param deckID id of the deck to edit, if any
     * @return A new instance of fragment DeckEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeckEditorFragment newInstance(int deckID) {
        DeckEditorFragment fragment = new DeckEditorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DECK_ID, deckID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deckID = getArguments().getInt(ARG_DECK_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDeckEditorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button goBack = binding.goBack;
        goBack.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_deckEditorFragment_to_navigation_home);
        });

        if (deckID == -1) {
            // EDIT MODE
        }

        // CREATE MODE

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }
}