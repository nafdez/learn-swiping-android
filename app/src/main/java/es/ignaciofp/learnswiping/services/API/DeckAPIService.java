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


    private DeckAPIService() {
    }

    public static DeckAPIService getInstance() {
        if (instance == null) instance = new DeckAPIService();
        return instance;
    }

    public void create(String token, Deck deck, APICallback<Deck> callback) {
        String json = String.format("{\n\"title\": \"%s\",\n\"description\": \"%s\",\n\"visible\": %s}", deck.getTitle(), deck.getDescription(), deck.isVisible());

        HTTP_CLIENT.newCall(makeJSONRequest("decks", json, token)).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error();
                    return;
                }

                callback.setObj(BASIC_GSON.fromJson(response.body().string(), Deck.class));
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
    }

    public void delete(String token, long deckID, APICallback<Void> callback) {
        Request request = new Request.Builder()
                .url(String.format("%s%s/%s", Constants.BASE_URL, "decks", deckID))
                .delete()
                .addHeader("Token", token)
                .build();
        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) callback.call();
                else callback.error();
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
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
                    callback.error();
                    return;
                }
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });

    }

    public void addDeckSubscription(String token, long deckID, APICallback<Void> callback) {
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request request = new Request.Builder()
                .url(String.format("%s%s/%s", Constants.BASE_URL, "decks/subs", deckID))
                .post(body)
                .addHeader("Token", token)
                .build();
        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) callback.call();
                else callback.error();
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
    }

    public void removeDeckSubscription(String token, long deckID, APICallback<Void> callback) {
        Request request = new Request.Builder()
                .url(String.format("%s%s/%s", Constants.BASE_URL, "decks/subs", deckID))
                .delete()
                .addHeader("Token", token)
                .build();
        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) callback.call();
                else callback.error();
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
    }

    public void deckPicture(String picID, APICallback<Bitmap> callback) {
        Request request = new Request.Builder()
                .url(String.format("%s%s/%s", Constants.BASE_URL, Constants.PICTURE_ENDPOINT, picID)).build();

        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error();
                    return;
                }
                callback.setObj(BitmapFactory.decodeStream(response.body().byteStream()));
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
    }

    private void rateDeck(String token, String deckID, int rating, APICallback<Void> callback) {
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request request = new Request.Builder()
                .url(String.format("%sdecks/%s/rating/%s", Constants.BASE_URL, deckID, rating))
                .method("GET", body)
                .addHeader("Token", token)
                .build();
        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) callback.call();
                else callback.error();
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
    }

    private void deckRating(String deckID, APICallback<Rating> callback) {
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create("", mediaType);
        Request request = new Request.Builder()
                .url(String.format("%sdecks/%s/rating", Constants.BASE_URL, deckID))
                .method("GET", body)
                .build();
        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error();
                    return;
                }
                callback.setObj(new Gson().fromJson(response.body().string(), Rating.class));
                callback.call();
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
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
