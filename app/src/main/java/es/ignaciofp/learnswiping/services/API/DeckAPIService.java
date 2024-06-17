package es.ignaciofp.learnswiping.services.API;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.List;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.deck.Deck;
import es.ignaciofp.learnswiping.models.deck.DeckDetails;
import es.ignaciofp.learnswiping.models.rating.Rating;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DeckAPIService extends APIService {

    private static DeckAPIService instance;

    private final String DECK_ENDPOINT = "decks";

    private DeckAPIService() {
    }

    public static DeckAPIService getInstance() {
        if (instance == null) instance = new DeckAPIService();
        return instance;
    }

    public void create(String token, Deck deck, APICallback<Deck> callback) {
        String json = String.format("{\n\"title\": \"%s\",\n\"description\": \"%s\",\n\"visible\": %s}", deck.getTitle(), deck.getDescription(), deck.isVisible());

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(DECK_ENDPOINT, token, METHOD_POST, APPLICATION_JSON, json))
                .enqueue(callback);
    }

    public void delete(String token, long deckID, APICallback<Void> callback) {
        String endpoint = String.format("%s/%s", DECK_ENDPOINT, deckID);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_DELETE, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void setDeckImage(String token, long deckID, Bitmap image, APICallback<Bitmap> callback) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
//        image.recycle();

        MediaType mediaType = MediaType.parse("image/png");

        RequestBody reqBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("picture", "pepito.png", RequestBody.create(imageBytes, mediaType)).build();

        Request request = new Request.Builder()
                .url(String.format("%sdecks/%s", Constants.BASE_URL, deckID))
                .addHeader("Token", token)
                .put(reqBody)
                .build();

        HTTP_CLIENT
                .newCall(request)
                .enqueue(callback);
    }

    public void addDeckSubscription(String token, long deckID, APICallback<Void> callback) {
        String endpoint = String.format("%s/subs/%s", DECK_ENDPOINT, deckID);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_POST, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void removeDeckSubscription(String token, long deckID, APICallback<Void> callback) {
        String endpoint = String.format("%s/subs/%s", DECK_ENDPOINT, deckID);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_DELETE, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void deckPicture(String picID, APICallback<Bitmap> callback) {
        String endpoint = String.format("%s/%s", Constants.PICTURE_ENDPOINT, picID);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, "", METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void shopTopN(String token, int quantity, APICallback<List<Deck>> callback) {
        String endpoint = String.format("shop/top/%s", quantity);

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void deckDetailsOwner(String token, long deckID, APICallback<DeckDetails> callback) {
        String endpoint = String.format("%s/%s", DECK_ENDPOINT, deckID);

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void deckDetailsSubs(String token, String username, long deckID, APICallback<DeckDetails> callback) {
        String endpoint = String.format("%s/subs/%s/%s", DECK_ENDPOINT, username, deckID);
        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void deckDetailsShop(long deckID, APICallback<DeckDetails> callback) {
        String endpoint = String.format("shop/%s", deckID);
        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, "", METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void rateDeck(String token, long deckID, int rating, APICallback<Void> callback) {
        String endpoint = String.format("%s/%s/rating/%s", DECK_ENDPOINT, deckID, rating);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void deckRating(long deckID, APICallback<Rating> callback) {
        String endpoint = String.format("%s/%s/rating", DECK_ENDPOINT, deckID);
        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, "", METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void deleteDeckRating(String token, long deckID, APICallback<Void> callback) {
        String endpoint = String.format("%s/%s/rating", DECK_ENDPOINT, deckID);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_DELETE, TEXT_PLAIN, ""))
                .enqueue(callback);
    }
}