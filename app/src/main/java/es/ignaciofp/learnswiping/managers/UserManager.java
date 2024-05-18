package es.ignaciofp.learnswiping.managers;

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

    public void register(User user, APICallback<User> callback) {
        API_SERVICE.Register(user, callback);
    }

    public void login(User user, APICallback<User> callback) {
        API_SERVICE.Login(user, callback);
    }
}
