package de.dhkarlsruhe.it.sheeshapp.sheeshapp.signup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.Userdata;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

/**
 * Created by d0272129 on 04.05.18.
 */

public class SignUp {

    private Activity c;
    private String name = "", email="", password="";
    private ProgressDialog dialog;
    private Animation animEtShake;
    private Userdata userdata;

    public SignUp(Activity c) {
        this.c = c;
        userdata = new Userdata(c);
        animEtShake = AnimationUtils.loadAnimation(c, R.anim.anim_shake_et);
    }

    public boolean startSignup(final View layout, final boolean automatic) {
        prepareDialog();

        StringRequest request = new StringRequest(ServerConstants.URL_SIGN_UP+name+"&email="+email+"&password="+password, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                String response;
                if (string.equals("OK")) {
                    response = c.getString(R.string.check_your_emails);
                    userdata.save(automatic,email,password);
                    dialog.setMessage(response);
                    dialog.setTitle(c.getString(R.string.success));
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            c.finish();
                        }
                    });
                    return;
                } else if (string.equals("")) {
                    response = c.getString(R.string.error_connecting_to_server);
                } else {
                    response = c.getString(R.string.error_text);
                }
                dialog.dismiss();
                Snackbar.make(layout, response, Snackbar.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
                MyUtilities.getColoredSnackbar(layout,c.getString(R.string.no_internet_connection), c.getResources().getColor(R.color.redError)).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(c);
        rQueue.add(request);
        return true;
    }

    private void prepareDialog() {
        dialog = new ProgressDialog(c);
        dialog.setMessage(c.getString(R.string.loading_text));
        dialog.setTitle(c.getString(R.string.please_wait_text));
        dialog.show();
    }

    public boolean isValidUsername(String input, EditText et, ImageButton btn) {
        if (input.length() >= 3 && input.length() <= 15) {
            et.setBackgroundResource(R.drawable.et_background);
            et.setText(input);
            name = input;
            return true;
        } else {
            btn.setVisibility(View.VISIBLE);
            et.setBackgroundResource(R.drawable.et_background_invalidinput);
            et.startAnimation(animEtShake);
            return false;
        }
    }

    public boolean isValidEmail(String userEmail, EditText etEmail, ImageButton imgBtnEmail) {
        final String EMAIL_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userEmail);

        if (matcher.matches()) {
            etEmail.setBackgroundResource(R.drawable.et_background);
            email = userEmail;
            return true;
        } else {
            imgBtnEmail.setVisibility(View.VISIBLE);
            etEmail.setBackgroundResource(R.drawable.et_background_invalidinput);
            etEmail.startAnimation(animEtShake);
            return false;
        }
    }

    public boolean isValidPasswort(String userPassword, EditText etPassword, ImageButton imgBtnPassword) {

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$";

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(userPassword);
        if (matcher.matches()) {
            etPassword.setBackgroundResource(R.drawable.et_background);
            password = userPassword;
            return true;
        } else {
            imgBtnPassword.setVisibility(View.VISIBLE);
            etPassword.setBackgroundResource(R.drawable.et_background_invalidinput);
            etPassword.startAnimation(animEtShake);
            return false;
        }
    }

    public boolean isValidRepeat(String userPasswordRepeat, EditText etPasswordRepeat, ImageButton imgBtnRepeat) {
        if (userPasswordRepeat.equals(password)){
            etPasswordRepeat.setBackgroundResource(R.drawable.et_background);
            return true;
        } else {
            imgBtnRepeat.setVisibility(View.VISIBLE);
            etPasswordRepeat.setBackgroundResource(R.drawable.et_background_invalidinput);
            etPasswordRepeat.startAnimation(animEtShake);
            return false;
        }
    }
}
