package es.ignaciofp.learnswiping.service;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

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

    private static final String REGISTER = "auth/register";
    private static final String LOGIN = "auth/login";

    private UserAPIService() {
        CLIENT = new OkHttpClient();
    }

    public static UserAPIService getInstance() {
        if (instance == null) instance = new UserAPIService();
        return instance;
    }

    public void Register(User user, APICallback<User> callback) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\",\"name\":\"%s\"}", user.getUsername(), user.getPassword(), user.getEmail(), user.getName());

        CLIENT.newCall(makeJSONRequest(REGISTER, json)).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) return;
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, Constants.LOCAL_DATE_TIME_JSON_DESERIALIZER).create();
                callback.setObj(gson.fromJson(response.body().string(), User.class));
                callback.call();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.error();
            }
        });
    }

    public void Login(User user, APICallback<User> callback) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", user.getUsername(), user.getPassword());

        CLIENT.newCall(makeJSONRequest(LOGIN, json)).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) return;
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, Constants.LOCAL_DATE_TIME_JSON_DESERIALIZER).create();
                callback.setObj(gson.fromJson(response.body().string(), User.class));
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
