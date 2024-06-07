package es.ignaciofp.learnswiping.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.Deck;
import es.ignaciofp.learnswiping.services.API.DeckAPIService;

public class DeckManager {

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
        API_SERVICE.create(getToken(ctx), deck, new APICallback<>(ctx) {
            @Override
            public void call() {
                if (img != null)
                    API_SERVICE.setDeckImage(getToken(ctx), getObj().getID(), img, imgCallback);
                callback.setObj(getObj());
                callback.call();
            }

            @Override
            public void error() {
                callback.error();
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

    private String getToken(Context ctx) {
        return ctx.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE).getString(Constants.TOKEN_KEY, "");
    }
}
