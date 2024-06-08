package es.ignaciofp.learnswiping.services.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.Deck;
import es.ignaciofp.learnswiping.models.rating.Rating;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
//        HTTP_CLIENT.newCall(makeRequest(DECK_ENDPOINT, token, METHOD_POST, APPLICATION_JSON, json)).enqueue(new Callback() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (!response.isSuccessful() || response.body() == null) {
//                    callback.error();
//                    return;
//                }
//
//                callback.setObj(BASIC_GSON.fromJson(response.body().string(), Deck.class));
//                callback.call();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                callback.error();
//            }
//        });
        HTTP_CLIENT.newCall(makeRequest(DECK_ENDPOINT, token, METHOD_POST, APPLICATION_JSON, json)).enqueue(callback);
    }

    public void delete(String token, long deckID, APICallback<Void> callback) {
        String endpoint = String.format("%s/%s", DECK_ENDPOINT, deckID);
        HTTP_CLIENT.newCall(makeRequest(endpoint, token, METHOD_DELETE, TEXT_PLAIN, "")).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) callback.call();
                else callback.error(String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error(e.getMessage());
            }
        });
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

        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.error(String.valueOf(response.code()));
                    return;
                }
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error(e.getMessage());
            }
        });

    }

    public void addDeckSubscription(String token, long deckID, APICallback<Void> callback) {
        String endpoint = String.format("%s/subs/%s", DECK_ENDPOINT, deckID);
        HTTP_CLIENT.newCall(makeRequest(endpoint, token, METHOD_POST, TEXT_PLAIN, "")).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) callback.call();
                else callback.error(String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error(e.getMessage());
            }
        });
    }

    public void removeDeckSubscription(String token, long deckID, APICallback<Void> callback) {
        String endpoint = String.format("%s/subs/%s", DECK_ENDPOINT, deckID);
        HTTP_CLIENT.newCall(makeRequest(endpoint, token, METHOD_DELETE, TEXT_PLAIN, "")).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) callback.call();
                else callback.error(String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error(e.getMessage());
            }
        });
    }

    public void deckPicture(String picID, APICallback<Bitmap> callback) {
        HTTP_CLIENT.newCall(makeRequest(String.format("%s/%s", Constants.PICTURE_ENDPOINT, picID), "", METHOD_GET, TEXT_PLAIN, "")).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error(String.valueOf(response.code()));
                    return;
                }
                callback.setObj(BitmapFactory.decodeStream(response.body().byteStream()));
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error(e.getMessage());
            }
        });
    }

    private void deckDetailsOwner(String token, String username, String deckID, APICallback<Deck> callback) {
        HTTP_CLIENT.newCall(makeRequest(String.format("%s/subs/%s/%s", DECK_ENDPOINT, username, deckID), token, "GET", TEXT_PLAIN, "")).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error(String.valueOf(response.code()));
                    return;
                }
                // Deck details
//                callback.setObj(BitmapFactory.decodeStream(response.body().byteStream()));
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
    }

    private void deckDetailsSub() {

    }

    private void rateDeck(String token, String deckID, int rating, APICallback<Void> callback) {
        HTTP_CLIENT.newCall(makeRequest(String.format("%s/%s/rating/%s", DECK_ENDPOINT, deckID, rating), token, "GET", TEXT_PLAIN, "")).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) callback.call();
                else callback.error(String.valueOf(response.code()));
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error(e.getMessage());
            }
        });
    }

    private void deckRating(String deckID, APICallback<Rating> callback) {
        HTTP_CLIENT.newCall(makeRequest(String.format("%s/%s/rating", DECK_ENDPOINT, deckID), "", "GET", TEXT_PLAIN, "")).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error(String.valueOf(response.code()));
                    return;
                }
                callback.setObj(new Gson().fromJson(response.body().string(), Rating.class));
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error(e.getMessage());
            }
        });
    }

    private Request makeRequest(String endpoint, String token, String method, String mediaTypeStr, String bodyStr) {
        Request.Builder request = new Request.Builder()
                .url(String.format("%s%s", Constants.BASE_URL, endpoint))
                .addHeader("Token", token);

        if (!method.equals(METHOD_GET)) {
            MediaType mediaType = MediaType.parse(mediaTypeStr);
            RequestBody body = RequestBody.create(bodyStr, mediaType);
            request.method(method, body);
        }

        return request.build();
    }

    private Request makeJSONRequest(String endpoint, String json, String token) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(json, mediaType);
        return new Request.Builder()
                .url(Constants.BASE_URL + endpoint)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Token", token)
                .build();
    }
}