package es.ignaciofp.learnswiping.managers;

import android.content.Context;

import java.util.List;

import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.card.Card;
import es.ignaciofp.learnswiping.models.card.Progress;
import es.ignaciofp.learnswiping.services.API.CardAPIService;

public class CardManager extends Manager {

    private static CardManager instance;
    private final CardAPIService API_SERVICE = CardAPIService.getInstance();

    private CardManager() {

    }

    public static CardManager getInstance() {
        if (instance == null) instance = new CardManager();
        return instance;
    }

    public void create(Context ctx, Card card, APICallback<Void> callback) {
        API_SERVICE.create(getToken(ctx), card, callback);
    }

    public void cards(Context ctx, long deckID, APICallback<List<Card>> callback) {
        API_SERVICE.cards(getToken(ctx), deckID, callback);
    }

    public void pending(Context ctx, long deckID, APICallback<List<Card>> callback) {
        API_SERVICE.pending(getToken(ctx), deckID, callback);
    }

    public void card(Context ctx, long deckID, long cardID, APICallback<Card> callback) {
        API_SERVICE.card(getToken(ctx), deckID, cardID, callback);
    }


    public void delete(Context ctx, long deckID, long cardID, APICallback<Void> callback) {
        API_SERVICE.delete(getToken(ctx), deckID, cardID, callback);
    }

    public void progress(Context ctx, long cardID, APICallback<Progress> callback) {
        API_SERVICE.progress(getToken(ctx), cardID, callback);
    }

    public void updateProgress(Context ctx, Progress progress, APICallback<Void> callback) {
        API_SERVICE.updateProgress(getToken(ctx), progress, callback);
    }
}
