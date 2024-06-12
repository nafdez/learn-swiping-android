package es.ignaciofp.learnswiping.managers;

import android.content.Context;

import es.ignaciofp.learnswiping.Constants;

public abstract class Manager {

    protected String getToken(Context ctx) {
        return ctx
                .getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)
                .getString(Constants.TOKEN_KEY, "");
    }
}
