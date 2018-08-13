package de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.util.List;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.DelayAutoCompleteTextView;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.FriendAutoCompleteAdapter;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;

/**
 * Created by d0272129 on 10.08.18.
 */

public class MyUtilities {
    static DelayAutoCompleteTextView autoCompleteTextView;

    public static String getChooseFriendsAsString(List<ChooseFriendObject> list) {
        String ok = "";
        for(int i = 0; i < list.size(); i++) {
            if(i==list.size()-1) {
                ok+=(list.get(i).getName()+".");
            } else {
                ok+=(list.get(i).getName()+", ");
            }
        }
        return ok;
    }

    public static void openAddFriendPopUp(Activity context) {

        View promptView = context.getLayoutInflater().inflate(R.layout.auto_et_with_icons, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        AlertDialog b = alertDialogBuilder.create();

        autoCompleteTextView = promptView.findViewById(R.id.autoAddName);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(new FriendAutoCompleteAdapter(context,b)); // 'this' is Activity instance
        autoCompleteTextView.setLoadingIndicator((android.widget.ProgressBar) promptView.findViewById(R.id.pb_loading_indicator));
        Button btnDelete = promptView.findViewById(R.id.btnAddDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(b.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        lp.softInputMode = (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        lp.windowAnimations = R.style.popupAnimation;
        b.show();
        b.getWindow().setAttributes(lp);
        b.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
