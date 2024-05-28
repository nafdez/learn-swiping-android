package es.ignaciofp.learnswiping.ui.home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.File;
import java.io.IOException;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentDeckEditorBinding;
import es.ignaciofp.learnswiping.managers.DeckManager;
import es.ignaciofp.learnswiping.models.Deck;

public class DeckEditorFragment extends Fragment implements View.OnClickListener {

    private FragmentDeckEditorBinding binding;
    private static final String ARG_DECK_ID = "DECKID_EDIT";
    private boolean isInEditMode = false;

    private ImageView imgPicture;
    private EditText txtEditTitle;
    private EditText txtEditDescription;
    private CheckBox chbIsPublic;

    private final String UPLOAD_PICTURE_TAG = "UploadPicture";
    private final String SAVE_TAG = "Save";
    private final String BACK_TAG = "Back";

    private Bitmap deckPicture;

    private int deckID = -1;

    private Deck deck;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMediaDeckPicture;

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
            isInEditMode = true;
        }

        pickMediaDeckPicture = registerForActivityResult(new PickVisualMedia(), uri -> {
            if (uri != null) {
                imgPicture.setImageURI(uri);
                try {
                    deckPicture = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().getContentResolver(), uri));
                } catch (IOException e) {
                    Toast.makeText(getContext(), getString(R.string.deck_editor_toast_image_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDeckEditorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imgPicture = binding.imgDeckEditorPicture;
        txtEditTitle = binding.txtEditDeckEditorTitle;
        txtEditDescription = binding.txtEditDeckEditorDescription;
        chbIsPublic = binding.chbDeckEditorIsPublic;
        Button btnUploadPicture = binding.btnDeckEditorUploadPicture;
        Button btnSave = binding.btnDeckEditorSave;
        Button btnBack = binding.btnDeckEditorBack;

        btnUploadPicture.setOnClickListener(this);
        btnUploadPicture.setTag(UPLOAD_PICTURE_TAG);

        btnSave.setOnClickListener(this);
        btnSave.setTag(SAVE_TAG);

        btnBack.setOnClickListener(this);
        btnBack.setTag(BACK_TAG);

        if (isInEditMode) {
            binding.txtDeckEditorScreenTitle.setText(getString(R.string.deck_editor_txt_screen_title_edit));
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

    @Override
    public void onClick(View v) {
        switch (v.getTag().toString()) {
            case UPLOAD_PICTURE_TAG:
                pickMediaDeckPicture.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
                break;
            case SAVE_TAG:
                save();
                break;
            case BACK_TAG:
                Navigation.findNavController(v).navigate(R.id.action_deckEditorFragment_to_navigation_home);
                break;
        }
    }

    private void save() {
        Deck deck = new Deck();
        deck.setTitle(txtEditTitle.getText().toString().trim());
        deck.setDescription(txtEditDescription.getText().toString().trim());
        deck.setVisible(chbIsPublic.isChecked());

        if (deck.getTitle().isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.deck_editor_toast_title_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        APICallback<Deck> deckCallback = new APICallback<>(requireContext()) {
            @Override
            public void call() {
                ((Activity) CONTEXT).runOnUiThread(() -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_deckEditorFragment_to_navigation_home);
                });
            }

            @Override
            public void error() {
                ((Activity) CONTEXT).runOnUiThread(() -> {
                    Toast.makeText(requireContext(), getString(R.string.deck_editor_toast_save_error), Toast.LENGTH_SHORT).show();
                });
            }
        };

        APICallback<Bitmap> imgCallback = new APICallback<>(requireContext()) {
            @Override
            public void call() {
            }

            @Override
            public void error() {
                ((Activity) CONTEXT).runOnUiThread(() -> {
                    Toast.makeText(requireContext(), getString(R.string.deck_editor_toast_image_error), Toast.LENGTH_SHORT).show();
                });
            }
        };

        DeckManager.getInstance().create(requireContext(), deck, deckPicture, deckCallback, imgCallback);
    }

}