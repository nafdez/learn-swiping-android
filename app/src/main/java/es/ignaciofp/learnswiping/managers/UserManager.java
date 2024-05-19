package es.ignaciofp.learnswiping.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import es.ignaciofp.learnswiping.Constants;
import es.ignaciofp.learnswiping.callables.APICallback;
import es.ignaciofp.learnswiping.models.User;
import es.ignaciofp.learnswiping.service.UserAPIService;

public class UserManager {

    private static UserManager instance;
    private final UserAPIService API_SERVICE = UserAPIService.getInstance();
    private User user;

    private UserManager() {

    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void register(User user, APICallback<User> callback) {
        API_SERVICE.register(user, callback);
    }

    public void login(User user, APICallback<User> callback) {
        API_SERVICE.login(user, callback);
    }

    // Used when starting the application, if login is successful, then continue normally, if not
    // then go back to login screen (Callback handled by MainActivity)
    public void loginWithToken(Context ctx, APICallback<User> callback) {
        String token = getToken(ctx);
        if (token.isEmpty()) {
            callback.error();
            return;
        }
        API_SERVICE.loginWithToken(getToken(ctx), callback);
    }

    public void logout(Context ctx) {
        String token = getToken(ctx);
        if (token.isEmpty()) return;
        API_SERVICE.logout(token);
        user = null;
        deleteToken(ctx);
    }

    public void userPicture(APICallback<Bitmap> callback) {
        API_SERVICE.userPicture(user.getPicId(), callback);
    }

    // Doing this as a public function bc the way we handle login with callbacks
    // doesn't allow to retrieve user in this class
    public void storeToken(Context ctx, String token) {
        ctx.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE).edit().putString("token", token).apply();
    }

    private String getToken(Context ctx) {
        return ctx.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE).getString("token", "");
    }

    private void deleteToken(Context ctx) {
        ctx.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE).edit().remove("token").apply();
    }
}
