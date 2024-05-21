package es.ignaciofp.learnswiping.managers;

import android.graphics.Bitmap;

import java.util.List;

import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.Deck;
import es.ignaciofp.learnswiping.service.DeckAPIService;

public class DeckManager {

    private static DeckManager instance;
    private final DeckAPIService API_SERVICE = DeckAPIService.getInstance();

    private DeckManager() {

    }

    public static DeckManager getInstance() {
        if (instance == null) instance = new DeckManager();
        return instance;
    }

    public void deckPicture(String picID, APICallback<Bitmap> callback) {
        API_SERVICE.deckPicture(picID, callback);
    }
}
