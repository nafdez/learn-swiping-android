package es.ignaciofp.learnswiping.callables;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import es.ignaciofp.learnswiping.R;
import es.ignaciofp.learnswiping.models.Deck;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class APICallback<T> implements Callback {

    protected final Context CONTEXT;
    private T obj;
    private Class<T> tClass;
    private Class<?> eClass; // In case of a list, the element's class
    private Gson gson;

    public APICallback(Context context, Class<T> tClass) {
        this(context, tClass, null);
    }

    public APICallback(Context context, Class<T> tClass, Class<?> eClass) {
        this.CONTEXT = context;
        this.tClass = tClass;
        this.eClass = eClass;
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

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        if (!response.isSuccessful() || response.body() == null) {
            error(String.valueOf(response.code()));
            return;
        }

        if (tClass.equals(Void.class)) {
            call(obj);
            return;
        }

        try {
            byte[] rspBytes = response.body().bytes();
            String responseStr = new String(rspBytes);
            if (responseStr.isEmpty()) {
                error("empty body");
                return;
            }

            if (tClass.equals(Bitmap.class)) {
                call((T) BitmapFactory.decodeByteArray(rspBytes, 0, rspBytes.length));
                return;
            }

            if (tClass.equals(List.class)) { // guarrada a la vista
                Type listType = TypeToken.getParameterized(tClass, eClass).getType();
                call(gson.fromJson(responseStr, listType));
                return;
            }

            Log.d("PRUEAS", responseStr);
            call(gson.fromJson(responseStr, tClass));


        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        error(e.getMessage());
    }

    public abstract void call(T obj);

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
