package es.ignaciofp.learnswiping.service;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Callable;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.OnErrorCallable;
import es.ignaciofp.learnswiping.models.User;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserAPIService {

    private static final String REGISTER = "auth/register";
    private static final String LOGIN = "auth/login";

    public static User Register(User user, Callable<Void> onError) {
        Log.d("USUARIOAPI", "HERE WE ARE");
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\",\"name\":\"%s\"}", user.getUsername(), user.getPassword(), user.getEmail(), user.getName());

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(Constants.API_URL + REGISTER)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
           if (response.isSuccessful() && response.body() != null) {
               Log.d("USUARIOAPI", "RESPONSE 200");
               Gson gson = new Gson();
               return gson.fromJson(response.body().toString(), User.class);
           }
           else {
               Log.d("USUARIOAPI", "ERROR");
               onError.call();
               return null;
           }
        } catch (Exception e) {
            Log.d("USUARIOAPI", Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public static User Login(User user, OnErrorCallable onError) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", user.getUsername(), user.getPassword());

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(Constants.API_URL + LOGIN)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                Gson gson = new Gson();
                return gson.fromJson(response.body().toString(), User.class);
            }
            else {
                onError.call();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
