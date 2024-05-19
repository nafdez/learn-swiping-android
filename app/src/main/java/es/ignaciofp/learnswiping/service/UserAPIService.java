package es.ignaciofp.learnswiping.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserAPIService {

    private static UserAPIService instance;
    private final OkHttpClient CLIENT;

    private final String REGISTER = "auth/register";
    private final String LOGIN = "auth/login";
    private final String LOGIN_TOKEN = "auth/token";
    private final String LOGOUT = "auth/logout";

    private final Gson GSON_ACCOUNT = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, Constants.LOCAL_DATE_TIME_JSON_DESERIALIZER).create();

    private UserAPIService() {
        CLIENT = new OkHttpClient();
    }

    public static UserAPIService getInstance() {
        if (instance == null) instance = new UserAPIService();
        return instance;
    }

    public void register(User user, APICallback<User> callback) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\",\"name\":\"%s\"}", user.getUsername(), user.getPassword(), user.getEmail(), user.getName());

        // Maybe APICallback should implement Callback class and reduce a lot of code
        // but this way we do not have to parse json on other classes when instantiating APICallback
        CLIENT.newCall(makeJSONRequest(REGISTER, json)).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error();
                    return;
                }
                callback.setObj(GSON_ACCOUNT.fromJson(response.body().string(), User.class));
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
    }

    public void login(User user, APICallback<User> callback) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", user.getUsername(), user.getPassword());

        CLIENT.newCall(makeJSONRequest(LOGIN, json)).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error();
                    return;
                }
                callback.setObj(GSON_ACCOUNT.fromJson(response.body().string(), User.class));
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
    }

    public void loginWithToken(String token, APICallback<User> callback) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + LOGIN_TOKEN)
                .addHeader("Token", token)
                .build();

        CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.error();
                    return;
                }
                callback.setObj(GSON_ACCOUNT.fromJson(response.body().string(), User.class));
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
    }

    public void logout(String token) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + LOGOUT)
                .addHeader("Token", token)
                .delete()
                .build();

        // We don't give a fuck if successful or not. The API just updates the token to ensure
        // that nobody has access to the account. But we also delete the token here so shouldn't
        // really be a problem
        CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
            }
        });
    }

    public void userPicture(String picID, APICallback<Bitmap> callback) {
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
