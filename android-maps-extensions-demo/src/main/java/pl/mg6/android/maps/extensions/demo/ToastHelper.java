package pl.mg6.android.maps.extensions.demo;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

    private ToastHelper() {
    }

    public static void showToast(Context context, String text) {
        if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }
}
