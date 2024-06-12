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
import es.ignaciofp.learnswiping.models.card.Card;
import es.ignaciofp.learnswiping.models.deck.Deck;

public class AdapterCard extends RecyclerView.Adapter<AdapterCard.ViewHolderCard> {

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public interface PopupMenuOnClickListener {
        void onPopupMenuClicked(int position);
    }

    final List<Card> cardList;
    final OnItemClickListener onItemClickListener;
    final PopupMenuOnClickListener popupMenuOnClickListener;

    public AdapterCard(List<Card> cardList, OnItemClickListener onItemClickListener, PopupMenuOnClickListener popupMenuOnClickListener) {
        this.cardList = cardList;
        this.onItemClickListener = onItemClickListener;
        this.popupMenuOnClickListener = popupMenuOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolderCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCard(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false), onItemClickListener, popupMenuOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCard holder, int position) {
        holder.bind(cardList.get(position));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class ViewHolderCard extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView question;

        public ViewHolderCard(@NonNull View itemView, OnItemClickListener onItemClickListener, PopupMenuOnClickListener popupMenuOnClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.txtItemCardTitle);
            question = itemView.findViewById(R.id.txtItemCardQuestion);

            itemView.setOnClickListener(v -> onItemClickListener.onItemClicked(getAdapterPosition()));

            itemView.setOnLongClickListener(v -> {
                popupMenuOnClickListener.onPopupMenuClicked(getAdapterPosition());
                return false;
            });
        }

        public void bind(@NonNull Card card) {
            title.setText(card.getTitle());
            question.setText(card.getQuestion());
        }
    }
}
