package es.ignaciofp.learnswiping.ui.home.fragments.deck;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.databinding.FragmentDeckCreatorBinding;
import es.ignaciofp.learnswiping.managers.DeckManager;
import es.ignaciofp.learnswiping.models.deck.Deck;

public class DeckCreatorFragment extends Fragment implements View.OnClickListener {

    private FragmentDeckCreatorBinding binding;

    private final DeckManager DECK_MANAGER = DeckManager.getInstance();

    private ImageView imgPicture;
    private EditText txtEditTitle;
    private EditText txtEditDescription;
    private CheckBox chbIsPublic;
    private Button btnUploadPicture;
    private Button btnSave;
    private Button btnBack;

    private final String UPLOAD_PICTURE_TAG = "UploadPicture";
    private final String SAVE_TAG = "Save";
    private final String BACK_TAG = "Back";

    private Bitmap deckPicture;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMediaDeckPicture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDeckCreatorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imgPicture = binding.imgDeckEditorPicture;
        txtEditTitle = binding.txtEditDeckEditorTitle;
        txtEditDescription = binding.txtEditDeckEditorDescription;
        chbIsPublic = binding.chbDeckEditorIsPublic;
        btnUploadPicture = binding.btnDeckEditorUploadPicture;
        btnSave = binding.btnDeckEditorSave;
        btnBack = binding.btnDeckEditorBack;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnUploadPicture.setOnClickListener(this);
        btnUploadPicture.setTag(UPLOAD_PICTURE_TAG);

        btnSave.setOnClickListener(this);
        btnSave.setTag(SAVE_TAG);

        btnBack.setOnClickListener(this);
        btnBack.setTag(BACK_TAG);

        pickMediaDeckPicture = registerForActivityResult(new PickVisualMedia(), uri -> {
            if (uri != null) {
                deckPicture = compressImageFromUri(uri);
                imgPicture.setImageBitmap(deckPicture);
                Log.d("","");
            }
        });
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

        APICallback<Deck> deckCallback = new APICallback<>(requireContext(), Deck.class) {
            @Override
            public void call(Deck deck) {
                ((Activity) CONTEXT).runOnUiThread(() -> {
                    Navigation.findNavController(requireView()).navigate(R.id.action_deckEditorFragment_to_navigation_home);
                });
            }

            @Override
            public void error(String error) {
                ((Activity) CONTEXT).runOnUiThread(() -> {
                    Toast.makeText(requireContext(), getString(R.string.deck_editor_toast_save_error), Toast.LENGTH_SHORT).show();
                });
            }
        };

        APICallback<Bitmap> imgCallback = new APICallback<>(requireContext(), Bitmap.class) {
            @Override
            public void call(Bitmap bitmap) {
            }

            @Override
            public void error(String error) {
                ((Activity) CONTEXT).runOnUiThread(() -> {
                    Toast.makeText(requireContext(), getString(R.string.deck_editor_toast_image_error), Toast.LENGTH_SHORT).show();
                });
            }
        };

        DECK_MANAGER.create(requireContext(), deck, deckPicture, deckCallback, imgCallback);
    }

    public Bitmap compressImageFromUri(Uri uri) {
        Bitmap bitmap = null;
        File outputFile = null;
        try {
            // Create a new file in your application's storage
            outputFile = new File(requireContext().getExternalFilesDir(null), "compressed.png");
            OutputStream outStream = new FileOutputStream(outputFile);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;
            bitmap = BitmapFactory.decodeStream(requireContext().getContentResolver().openInputStream(uri), null, bmOptions);

            Objects.requireNonNull(bitmap).compress(Bitmap.CompressFormat.JPEG, 50, Objects.requireNonNull(outStream));

            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            Toast.makeText(requireContext(), getString(R.string.deck_editor_toast_image_error), Toast.LENGTH_SHORT).show();
        } finally {
            if (outputFile != null) {
                outputFile.delete();  // Delete the file
            }
        }
        return bitmap;
    }
}