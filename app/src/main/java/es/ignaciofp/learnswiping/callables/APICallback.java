package es.ignaciofp.learnswiping.callables;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import es.ignaciofp.learnswiping.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class APICallback<T> implements Callback {

    protected final Context CONTEXT;
    private T obj;
    private Class<T> tClass;
    private Gson gson;
    private boolean isExpectingReturn;

    public APICallback(Context CONTEXT) {
        this(CONTEXT, true);
    }

    public APICallback(Context CONTEXT, boolean isExpectingReturn) {
        this.CONTEXT = CONTEXT;
        this.isExpectingReturn = isExpectingReturn;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }

    public void setGson(com.google.gson.Gson gson) {
        this.gson = gson;
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        if (!response.isSuccessful() || response.body() == null) {
            error(String.valueOf(response.code()));
            return;
        }
        try {
            String responseStr = response.body().string();
            if (isExpectingReturn && !responseStr.isEmpty()) {
                setObj(gson.fromJson(responseStr, (Type) obj.getClass()));
            }
        } catch (IOException e) {
            error(e.getMessage());
            return;
        }
        call();
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        error(e.getMessage());
    }

    public abstract void call();

//    public abstract void error();
    public abstract void error(String error);

    /**
     * Shows default dialog (Positive button without action, no negative button)
     *
     * @param title
     * @param msg
     */
    public void showDialog(String title, String msg) {
        showDialog(title, msg, false, null, null);
    }

    public void showDialog(String title, String msg, boolean hasNegative, DialogInterface.OnClickListener onPositiveListener, DialogInterface.OnClickListener onNegativeListener) {
        ((Activity) CONTEXT).runOnUiThread(() -> {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(CONTEXT)
                    .setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton(CONTEXT.getResources().getString(R.string.global_dialog_positive), onPositiveListener);

            if (hasNegative)
                dialog.setNegativeButton(CONTEXT.getResources().getString(R.string.global_dialog_negative), onNegativeListener);

            dialog.show();
        });
    }

    public void showAlert(String msg) {
        ((Activity) CONTEXT).runOnUiThread(() -> {
            Toast.makeText(CONTEXT, msg, Toast.LENGTH_SHORT).show();
        });
    }
}
