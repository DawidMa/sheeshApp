package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Informatik on 24.11.2017.
 */

public class MyAlert extends AlertDialog.Builder {
    private Context context;
    private String title, message;
    private boolean withTheme = false;
    private int anzBtn, themeResId;

    public MyAlert(Context context, String title, String message) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
        this.anzBtn = anzBtn;
        buildAlert();

    }

    public MyAlert(Context context, int themeResId, String title, String message) {
        super(context, themeResId);
        this.context = context;
        this.title = title;
        this.themeResId = themeResId;
        this.message = message;
        withTheme = true;
        buildAlert();
    }

    private void buildAlert() {
            this.setTitle(title)
                    .setMessage(message);
            if (withTheme){
                this.setView(R.layout.fragment_friends);
            }

        }
}