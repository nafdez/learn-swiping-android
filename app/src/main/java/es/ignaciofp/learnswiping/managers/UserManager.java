package es.ignaciofp.learnswiping.managers;

import java.util.concurrent.Callable;

import es.ignaciofp.learnswiping.callables.OnErrorCallable;
import es.ignaciofp.learnswiping.models.User;
import es.ignaciofp.learnswiping.service.UserAPIService;

public class UserManager {

    private static UserManager instance;
    private User user;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public boolean register(User user, OnErrorCallable onError) {
        user = UserAPIService.Register(user, onError);
        return user != null;
    }

    public boolean login(User user, OnErrorCallable onError) {
        user = UserAPIService.Login(user, onError);
        return user != null;
    }
}
