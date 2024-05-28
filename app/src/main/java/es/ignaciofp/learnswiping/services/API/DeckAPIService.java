package es.ignaciofp.learnswiping.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.Deck;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeckAPIService {

    private static DeckAPIService instance;

    private final OkHttpClient CLIENT;
    private final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, Constants.LOCAL_DATE_TIME_JSON_DESERIALIZER).create();

    private DeckAPIService() {
       CLIENT = new OkHttpClient();
    }

    public static DeckAPIService getInstance() {
        if (instance == null) instance = new DeckAPIService();
        return instance;
    }

    public void create(Deck deck, APICallback<Deck> callback) {
        String json = String.format("{\n\"title\": \"%s\",\n\"description\": \"%s\",\n\"visible\": %s}", deck.getTitle(), deck.getDescription(), deck.isVisible());

        CLIENT.newCall(makeJSONRequest("decks", json)).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error();
                    return;
                }
                callback.setObj(GSON.fromJson(response.body().string(), Deck.class));
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
    }

    public void setDeckImage(int deckID, Bitmap image, APICallback<Bitmap> callback) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody reqBody = RequestBody.create(image.getRowBytes())
        Request request = new Request.Builder()
                .url(String.format("%sdecks/%s", deckID))
                        .put()

        Constants.HTTP_CLIENT.newCall()

    }

    public void deckPicture(String picID, APICallback<Bitmap> callback) {
        Request request = new Request.Builder()
                .url(String.format("%s%s/%s", Constants.BASE_URL, Constants.PICTURE_ENDPOINT, picID)).build();

        CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
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

    private Request makeJSONRequest(String endpoint, String json) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(json, mediaType);
        return new Request.Builder()
                .url(Constants.BASE_URL + endpoint)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
    }
}
