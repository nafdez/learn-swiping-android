package es.ignaciofp.learnswiping.callables;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import es.ignaciofp.learnswiping.R;
import okhttp3.Callback;

public abstract class APICallback<T> {

    protected final Context CONTEXT;
    private T obj;
    private List<T> list;

    public APICallback(Context CONTEXT) {
        this.CONTEXT = CONTEXT;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public T getObj() {
        return obj;
    }

    public List<T> getList() {
        return list;
    }

    public abstract void call();

    public abstract void error();

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
