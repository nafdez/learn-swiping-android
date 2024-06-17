package es.ignaciofp.learnswiping.managers;

import android.content.ContentQueryMap;
import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.deck.Deck;
import es.ignaciofp.learnswiping.models.deck.DeckDetails;
import es.ignaciofp.learnswiping.models.rating.Rating;
import es.ignaciofp.learnswiping.services.API.DeckAPIService;

public class DeckManager extends Manager {

    private static DeckManager instance;
    private final DeckAPIService API_SERVICE = DeckAPIService.getInstance();

    private DeckManager() {

    }

    public static DeckManager getInstance() {
        if (instance == null) instance = new DeckManager();
        return instance;
    }

    // TODO: fix this shit
    public void create(Context ctx, Deck deck, Bitmap img, APICallback<Deck> callback, APICallback<Bitmap> imgCallback) {
        // This is ugly af. Next time sure I'm going to plan things before haha
        API_SERVICE.create(getToken(ctx), deck, new APICallback<>(ctx, Deck.class) {
            @Override
            public void call(Deck deck) {
                if (img != null && deck != null)
                    API_SERVICE.setDeckImage(getToken(ctx), deck.getID(), img, imgCallback);
                callback.setObj(deck);
                callback.call(deck);
            }

            @Override
            public void error(String error) {
                callback.error(error);
            }
        });
    }

    public void delete(Context ctx, long deckID, APICallback<Void> callback) {
        API_SERVICE.delete(getToken(ctx), deckID, callback);
    }

    public void deckPicture(String picID, APICallback<Bitmap> callback) {
        API_SERVICE.deckPicture(picID, callback);
    }

    public void addDeckSubscription(Context ctx, long deckID, APICallback<Void> callback) {
        API_SERVICE.addDeckSubscription(getToken(ctx), deckID, callback);
    }

    public void removeDeckSubscription(Context ctx, long deckID, APICallback<Void> callback) {
        API_SERVICE.removeDeckSubscription(getToken(ctx), deckID, callback);
    }

    public void shopTopN(Context ctx, int quantity, APICallback<List<Deck>> callback) {
        API_SERVICE.shopTopN(getToken(ctx), quantity, callback);
    }

    public void deckDetailsOwner(Context ctx, long deckID, APICallback<DeckDetails> callback) {
        API_SERVICE.deckDetailsOwner(getToken(ctx), deckID, callback);
    }

    public void deckDetailsSub(Context ctx, String username, long deckID, APICallback<DeckDetails> callback) {
        API_SERVICE.deckDetailsSubs(getToken(ctx), username, deckID, callback);
    }

    public void deckDetailsShop(Context ctx, String username, long deckID, APICallback<DeckDetails> callback) {
        API_SERVICE.deckDetailsShop(deckID, callback);
    }

    public void rateDeck(Context ctx, long deckID, int rating, APICallback<Void> callback) {
        API_SERVICE.rateDeck(getToken(ctx), deckID, rating, callback);
    }

    public void deckRating(long deckID, APICallback<Rating> callback) {
        API_SERVICE.deckRating(deckID, callback);
    }

    public void deleteDeckRating(Context ctx, long deckID, APICallback<Void> callback) {
        API_SERVICE.deleteDeckRating(getToken(ctx), deckID, callback);
    }
}
