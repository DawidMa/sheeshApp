package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Informatik on 24.11.2017.
 */

public class MyAlert extends AlertDialog.Builder {
    private Context context;
    private String title, message, positiv, negativ;

    public MyAlert(Context context,  String title, String message, String positiv, String negativ) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
        this.positiv = positiv;
        this.negativ = negativ;
        buildAlert();
    }

    public MyAlert(Context context, int themeResId, String title, String message, String positiv, String negativ) {
        super(context, themeResId);
        this.context = context;
        this.title = title;
        this.message = message;
        this.positiv = positiv;
        this.negativ = negativ;
        buildAlert();
    }

    private void  buildAlert(){
        this.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiv, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton(negativ, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


    }

}
