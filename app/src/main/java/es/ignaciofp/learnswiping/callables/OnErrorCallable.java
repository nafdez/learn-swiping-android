package es.ignaciofp.learnswiping.callables;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.concurrent.Callable;

import es.ignaciofp.learnswiping.models.User;

public class OnErrorCallable implements Callable<Void> {

    private final Context CONTEXT;

    public OnErrorCallable(Context context) {
        this.CONTEXT = context;
    }

    @Override
    public Void call() {
        return null;
    }

    public void showAlert(String msg) {
        ((Activity) CONTEXT).runOnUiThread(() -> {
            Toast.makeText(CONTEXT, msg, Toast.LENGTH_SHORT).show();
        });
    }

}
