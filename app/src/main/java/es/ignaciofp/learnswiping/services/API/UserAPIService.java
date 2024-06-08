package es.ignaciofp.learnswiping.services.API;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.Deck;
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

        // Maybe APICallback should implement Callback class and reduce a lot of code
        // but this way we do not have to parse json on other classes when instantiating APICallback
//        HTTP_CLIENT.newCall(makeJSONRequest(REGISTER, json)).enqueue(new Callback() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (!response.isSuccessful() || response.body() == null) {
//                    callback.error(String.valueOf(response.code()));
//                    return;
//                }
//                callback.setObj(BASIC_GSON.fromJson(response.body().string(), User.class));
//                callback.call();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                callback.error(e.getMessage());
//            }
//        });
    }

    public void login(User user, APICallback<User> callback) {
        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", user.getUsername(), user.getPassword());

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(LOGIN, "", METHOD_POST, APPLICATION_JSON, json))
                .enqueue(callback);
//        HTTP_CLIENT.newCall(makeJSONRequest(LOGIN, json)).enqueue(new Callback() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (!response.isSuccessful() || response.body() == null) {
//                    callback.error(String.valueOf(response.code()));
//                    return;
//                }
//                callback.setObj(BASIC_GSON.fromJson(response.body().string(), User.class));
//                callback.call();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                callback.error(e.getMessage());
//            }
//        });
    }

    public void loginWithToken(String token, APICallback<User> callback) {
//        Request request = new Request.Builder()
//                .url(Constants.BASE_URL + LOGIN_TOKEN)
//                .addHeader("Token", token)
//                .build();
        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(LOGIN_TOKEN, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);
//        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (!response.isSuccessful() || response.body() == null) {
//                    callback.error(String.valueOf(response.code()));
//                    return;
//                }
//                callback.setObj(BASIC_GSON.fromJson(response.body().string(), User.class));
//                callback.call();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                callback.error(e.getMessage());
//            }
//        });
    }

    public void logout(String token) {
//        Request request = new Request.Builder()
//                .url(Constants.BASE_URL + LOGOUT)
//                .addHeader("Token", token)
//                .delete()
//                .build();


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
//        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) {
//            }
//        });
    }

    public void ownedDecks(String username, String token, APICallback<List<Deck>> callback) {
//        Request request = new Request.Builder()
//                .url(String.format("%susers/%s/decks", Constants.BASE_URL, username))
//                .addHeader("Token", token)
//                .build();

        String endpoint = String.format("users/%s/decks", username);

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);

//        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (!response.isSuccessful() || response.body() == null) {
//                    callback.error(String.valueOf(response.code()));
//                    return;
//                }
//
//                Type listType = new TypeToken<List<Deck>>(){}.getType();
//                List<Deck> deckList = BASIC_GSON.fromJson(response.body().string(), listType);
//                callback.setObj(deckList);
//                callback.call();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                callback.error(e.getMessage());
//            }
//        });
    }

    public void subscribedDecks(String username, String token, APICallback<List<Deck>> callback) {
//        Request request = new Request.Builder()
//                .url(String.format("%susers/%s/subscribed", Constants.BASE_URL, username))
//                .addHeader("Token", token)
//                .build();

        String endpoint = String.format("users/%s/subscribed", username);

        callback.setGson(BASIC_GSON);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, token, METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);

//        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (!response.isSuccessful() || response.body() == null) {
//                    callback.error(String.valueOf(response.code()));
//                    return;
//                }
//                Type listType = new TypeToken<List<Deck>>(){}.getType();
//                List<Deck> deckList = BASIC_GSON.fromJson(response.body().string(), listType);
//                callback.setObj(deckList);
//                callback.call();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                callback.error(e.getMessage());
//            }
//        });
    }

    public void userPicture(String picID, APICallback<Bitmap> callback) {
//        Request request = new Request.Builder()
//                .url(String.format("%s%s/%s", Constants.BASE_URL, Constants.PICTURE_ENDPOINT, picID)).build();

        String endpoint = String.format("%s/%s", Constants.PICTURE_ENDPOINT, picID);
        HTTP_CLIENT
                .newCall(makeRequest(endpoint, "", METHOD_GET, TEXT_PLAIN, ""))
                .enqueue(callback);

//        HTTP_CLIENT.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) {
//                if (!response.isSuccessful() || response.body() == null) {
//                    callback.error(String.valueOf(response.code()));
//                    return;
//                }
//                callback.setObj(BitmapFactory.decodeStream(response.body().byteStream()));
//                callback.call();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                callback.error(e.getMessage());
//            }
//        });
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
