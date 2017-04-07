package br.com.brasolia.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Yoshiharu Takuno on 4/20/16.
 */
public class AlertUtil {

    public static void show(Context context, String title, String message) {
        try {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(title);
            alert.setMessage(message);
            alert.setPositiveButton("OK", null);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void show(Context context, String title, String message, String positiveButton) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton(positiveButton, null);
        alert.show();
    }

    public static void show(Context context, String title, String message, String positiveButton, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.setPositiveButton(positiveButton, listener);
        alert.show();
    }

    public static void confirm(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.setNegativeButton("Não", listener);
        alert.setPositiveButton("Sim", listener);
        alert.show();
    }

    public static void confirm(Context context, String title, String message, String negativeButton,
                               String positiveButton, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setCancelable(false);
        alert.setPositiveButton(positiveButton, listener);
        alert.setNegativeButton(negativeButton, listener);
        alert.show();
    }

    public static void items(Context context, String title, CharSequence[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, listener);
        builder.show();
    }

}
