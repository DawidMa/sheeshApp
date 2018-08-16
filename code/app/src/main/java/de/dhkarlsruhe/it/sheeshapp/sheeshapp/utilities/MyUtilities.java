package de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.MainActivity;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.friend.Friend;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.DelayAutoCompleteTextView;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.myAutoComplete.FriendAutoCompleteAdapter;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ChooseFriendObject;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.FriendlistObject;

/**
 * Created by d0272129 on 10.08.18.
 */

public class MyUtilities {
    static DelayAutoCompleteTextView autoCompleteTextView;

    public static String getChooseFriendsAsString(List<ChooseFriendObject> list) {
        String ok = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                ok += (list.get(i).getName() + ".");
            } else {
                ok += (list.get(i).getName() + ", ");
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
        autoCompleteTextView.setAdapter(new FriendAutoCompleteAdapter(context, b)); // 'this' is Activity instance
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

    public static void openLeaveDialog(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.leave_question));
        builder.setCancelable(true);
        builder.setPositiveButton(context.getString(R.string.yes_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.finish();
            }
        });
        builder.setNegativeButton(context.getString(R.string.no_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, context.getString(R.string.sheeeesh_text), Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void openHTMLDialog(Activity context, String window) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(true);

        if (window.equals("flaticon")) {
            alert.setTitle(context.getString(R.string.license_text));
            String message = "<div>Icons made by <a href=\"https://www.flaticon.com/authors/chanut\" title=\"Chanut\">Chanut</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a> is licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC 3.0 BY</a></div>";
            alert.setMessage(Html.fromHtml(message));
        } else if (window.equals("legal")) {
            alert.setTitle(context.getString(R.string.legal_text));
            WebView web = new WebView(context);
            web.loadUrl("file:///android_res/raw/datenschutzerklaerung.html");
            web.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            alert.setView(web);
        }
        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    public static boolean etOK(EditText et) {
        String text = et.getText().toString().trim();
        return (!text.matches(""));
    }
    public static boolean stringOK(String string) {
        String text = string.trim();
        return (!text.matches(""));
    }


    public static void configureEtMax(final EditText et, final TextView textView, final int max, boolean emoji_filter) {
        textView.setVisibility(View.VISIBLE);
        textView.setText(max+"");
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        if (emoji_filter) {
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max),EMOJI_FILTER});
        } else {
            et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
        }
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                textView.setText(String.valueOf(max - et.length()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {
            }

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
            }
        });
    }

    public static List<ChooseFriendObject> getOfflineFriends(Friend friend) {
        List<ChooseFriendObject> objects = new ArrayList<>();
        if (friend.actualInformationAvailable()) {
            Gson json = new Gson();
            String offlineData = friend.getOfflineData();
            Type listType = new TypeToken<List<FriendlistObject>>() {
            }.getType();
            List<FriendlistObject> friendlistObject = json.fromJson(offlineData, listType);
            for (FriendlistObject o : friendlistObject) {
                ChooseFriendObject object = new ChooseFriendObject(o.getName(), o.getFriend_id());
                objects.add(object);
            }
        }
        return objects;
    }

    public static String durationAsString(long duration) {
        String time = "";
        if (duration==0) {
            time = "00:00:00";
        } else {
            int seconds = (int) (duration / 1000) % 60 ;
            int minutes = (int) ((duration / (1000*60)) % 60);
            int hours   = (int) ((duration / (1000*60*60)) % 24);
            time = String.format("%02d:%02d:%02d",hours,minutes,seconds );
        }
        return time;
    }
    public static InputFilter EMOJI_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                int type = Character.getType(source.charAt(index));
                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };

    public static String calendarToString(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = (calendar.get(Calendar.MONTH)+1);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return String.format("%02d.%02d.%04d  %02d:%02d",day,month,year,hour,minute);
    }
}
