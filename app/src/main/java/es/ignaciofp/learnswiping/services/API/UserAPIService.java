package es.ignaciofp.learnswiping.services.API;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.deck.Deck;
import es.ignaciofp.learnswiping.models.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserAPIService extends APIService {

    private static UserAPIService instance;

    private final String REGISTER = "auth/register";
    private final String LOGIN = "auth/login";
    private final String LOGIN_TOKEN = "auth/token";
    private final String LOGOUT = "auth/logout";

    private UserAPIService() {
    }

    public static UserAPIService getInstance() {
        if (instance == null) instance = new UserAPIService();
        return instance;
    }

    public void register(User user, APICallback<User> callback) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\",\"name\":\"%s\"}", user.getUsername(), user.getPassword(), user.getEmail(), user.getName());

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(REGISTER, "", METHOD_POST, APPLICATION_JSON, json))
                .enqueue(callback);
    }

    public void login(User user, APICallback<User> callback) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", user.getUsername(), user.getPassword());

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(LOGIN, "", METHOD_POST, APPLICATION_JSON, json))
                .enqueue(callback);
    }

    public void loginWithToken(String token, APICallback<User> callback) {
        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(LOGIN_TOKEN, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void logout(String token) {
        // We don't give a fuck if successful or not. The API just updates the token to ensure
        // that nobody has access to the account. But we also delete the token here so shouldn't
        // really be a problem
        HTTP_CLIENT
                .newCall(makeRequest(LOGOUT, token, METHOD_DELETE, TEXT_PLAIN, ""))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    }
                });
    }

    public void ownedDecks(String username, String token, APICallback<List<Deck>> callback) {
        String endpoint = String.format("users/%s/decks", username);

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void subscribedDecks(String username, String token, APICallback<List<Deck>> callback) {
        String endpoint = String.format("users/%s/subscribed", username);

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }

    public void userPicture(String picID, APICallback<Bitmap> callback) {
        String endpoint = String.format("%s/%s", Constants.PICTURE_ENDPOINT, picID);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, "", METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
    }
}
