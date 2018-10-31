package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public abstract class ToastMaker {

    public static void defaultTestToast(Context context) {
        Toast.makeText(context, R.string.toast_default, Toast.LENGTH_SHORT).show();
    }

    public static void shortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
