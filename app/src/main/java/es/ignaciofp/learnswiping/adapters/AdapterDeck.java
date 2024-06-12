package es.ignaciofp.learnswiping.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.managers.DeckManager;
import es.ignaciofp.learnswiping.models.deck.Deck;

public class AdapterDeck extends RecyclerView.Adapter<AdapterDeck.ViewHolderDeck> {

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public interface PopupMenuOnClickListener {
        void onPopupMenuClicked(int position);
    }

    final List<Deck> deckList;
    final OnItemClickListener onItemClickListener;
    final PopupMenuOnClickListener popupMenuOnClickListener;

    public AdapterDeck(List<Deck> deckList, OnItemClickListener onItemClickListener, PopupMenuOnClickListener popupMenuOnClickListener) {
        this.deckList = deckList;
        this.onItemClickListener = onItemClickListener;
        this.popupMenuOnClickListener = popupMenuOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolderDeck onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderDeck(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deck, parent, false), onItemClickListener, popupMenuOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDeck holder, int position) {
        holder.bind(deckList.get(position));
    }

    @Override
    public int getItemCount() {
        return deckList.size();
    }

    public static class ViewHolderDeck extends RecyclerView.ViewHolder {

        final View VIEW;
        final TextView title;
        final TextView description;
        final ImageView image;

        public ViewHolderDeck(@NonNull View itemView, OnItemClickListener onItemClickListener, PopupMenuOnClickListener popupMenuOnClickListener) {
            super(itemView);
            VIEW = itemView;
            title = itemView.findViewById(R.id.txtDeckTitle);
            description = itemView.findViewById(R.id.txtDeckDesc);
            image = itemView.findViewById(R.id.imgDeckPic);

            // TODO: Select deck when simple touch
            itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(getAdapterPosition()));

            // TODO: menu actions
            itemView.setOnLongClickListener(v -> {
                popupMenuOnClickListener.onPopupMenuClicked(getAdapterPosition());
                return false;
            });
        }

        public void bind(@NonNull Deck deck) {
            title.setText(deck.getTitle());
            description.setText(deck.getDescription());

            // Don't waste time making requests, if no picID is provided
            // for whatever reason then just set the fallback image
            // To reduce even more the requests, if the picture is the default one
            // then fallback onto the default one stored in this device
            if (deck.getPicID() == null || deck.getPicID().isEmpty() || deck.getPicID().equals("default_deck_pic_1.png")) {
                image.setImageDrawable(AppCompatResources.getDrawable(VIEW.getContext(), R.drawable.ic_deck_default));
                return;
            }

            DeckManager.getInstance().deckPicture(deck.getPicID(), new APICallback<>(VIEW.getContext(), Bitmap.class) {
                @Override
                public void call(Bitmap bitmap) {
                    ((Activity) VIEW.getContext()).runOnUiThread(() -> {
                        image.setImageBitmap(bitmap);
                    });
                }

                @Override
                public void error(String error) {
                    ((Activity) VIEW.getContext()).runOnUiThread(() -> {
                        image.setImageDrawable(AppCompatResources.getDrawable(VIEW.getContext(), R.drawable.ic_deck_default));
                    });
                }
            });
        }
    }
}
