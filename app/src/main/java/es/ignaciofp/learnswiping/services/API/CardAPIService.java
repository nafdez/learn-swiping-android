package es.ignaciofp.learnswiping.services.API;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.card.Card;
import es.ignaciofp.learnswiping.models.card.Progress;

public class CardAPIService extends APIService {

    private static CardAPIService instance;

    private final String DECK_ENDPOINT = "decks";

    private CardAPIService() {

    }

    public static CardAPIService getInstance() {
        if (instance == null) instance = new CardAPIService();
        return instance;
    }

    public void create(String token, Card card, APICallback<Void> callback) {
        String json = String.format(
                "{\"title\":\"%s\",\"front\":\"%s\",\"back\":\"%s\",\"question\":\"%s\",\"answer\":\"%s\",\"wrong\":[{\"answer\":\"%s\"},{\"answer\":\"%s\"},{\"answer\":\"%s\"}]}",
                card.getTitle(),
                card.getFront(),
                card.getBack(),
                card.getQuestion(),
                card.getAnswer(),
                card.getWrongAnswers().get(0).getAnswer(),
                card.getWrongAnswers().get(1).getAnswer(),
                card.getWrongAnswers().get(2).getAnswer());

        String endpoint = String.format("%s/%s", DECK_ENDPOINT, card.getDeckID());
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_POST, APPLICATION_JSON, json))
                .enqueue(callback);

    }

    public void card(String token, long deckID, long cardID, APICallback<Card> callback) {
        String endpoint = String.format("%s/%s/%s", DECK_ENDPOINT, deckID, cardID);
        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void cards(String token, long deckID, APICallback<List<Card>> callback) {
        String endpoint = String.format("%s/%s/cards", DECK_ENDPOINT, deckID);
        callback.setGson(BASIC_GSON);

        // If token is provided with this request, is gonna retrieve pending cards, not all cards
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, "", METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void pending(String token, long deckID, APICallback<List<Card>> callback) {
        String endpoint = String.format("%s/%s/cards", DECK_ENDPOINT, deckID);
        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void update(Card card, long deckID, APICallback<Void> callback) {
    }

    public void delete(String token, long deckID, long cardID, APICallback<Void> callback) {
        String endpoint = String.format("%s/%s/%s", DECK_ENDPOINT, deckID, cardID);
        if (callback != null)
            callback.setGson(BASIC_GSON);
        HTTP_CLIENT
            .newCall(makeRequest(endpoint, token, METHOD_DELETE, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void progress(String token, long cardID, APICallback<Progress> callback) {
        String endpoint = String.format("progress/%s", cardID);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint , token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void saveProgress(String token, Progress progress, APICallback<Void> callback) {
        HTTP_CLIENT
                .newCall(makeRequest("progress", token, METHOD_PUT, APPLICATION_JSON, BASIC_GSON.toJson(progress)))
                .enqueue(callback);
    }
}
