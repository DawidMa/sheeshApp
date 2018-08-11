package de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;

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

    public static void openAddFriendPopUp(Activity context, TabLayout tabLayout) {

        View popupView = context.getLayoutInflater().inflate(R.layout.auto_et_with_icons, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        Button btnDelete;
        autoCompleteTextView = popupView.findViewById(R.id.autoAddName);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(new FriendAutoCompleteAdapter(context,popupWindow)); // 'this' is Activity instance
        autoCompleteTextView.setLoadingIndicator((android.widget.ProgressBar) popupView.findViewById(R.id.pb_loading_indicator));

        btnDelete = popupView.findViewById(R.id.btnAddDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });
        popupWindow.showAsDropDown(tabLayout);
    }
}
