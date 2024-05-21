package es.ignaciofp.learnswiping.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import java.io.IOException;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeckAPIService {

    private static DeckAPIService instance;

    private final OkHttpClient CLIENT;

    private DeckAPIService() {
       CLIENT = new OkHttpClient();
    }

    public static DeckAPIService getInstance() {
        if (instance == null) instance = new DeckAPIService();
        return instance;
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
}
