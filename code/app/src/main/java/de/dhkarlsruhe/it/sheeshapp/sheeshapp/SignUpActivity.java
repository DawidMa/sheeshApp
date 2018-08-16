package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.SignUp;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

/**
 * Created by Informatik on 20.11.2017.
 */

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etPasswordRepeat, etUsername;
    private String userName, userPassword, userPasswordRepeat, userEmail;
    private ImageButton imgBtnUsername, imgBtnEmail, imgBtnPassword, imgBtnRepeat;
    private CheckBox cbSaveLogin;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Animation animEtShake;
    private boolean allDataValid[] = new boolean[4], checked = false;
    private View layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        layout = findViewById(R.id.layoutSignUp);

        etUsername = findViewById(R.id.edTUsername);
        etEmail = findViewById(R.id.edTEmail);
        etPassword = findViewById(R.id.edTPassword);
        etPasswordRepeat = findViewById(R.id.edTPasswordRepeat);

        etUsername.setFilters(new InputFilter[]{MyUtilities.EMOJI_FILTER});
        etEmail.setFilters(new InputFilter[]{MyUtilities.EMOJI_FILTER});
        etPassword.setFilters(new InputFilter[]{MyUtilities.EMOJI_FILTER});
        etPasswordRepeat.setFilters(new InputFilter[]{MyUtilities.EMOJI_FILTER});

        cbSaveLogin = findViewById(R.id.cbSaveLogin);

        imgBtnUsername = findViewById(R.id.imgBtnSigInfoUsername);
        imgBtnEmail = findViewById(R.id.imgBtnSigInfoEmail);
        imgBtnPassword = findViewById(R.id.imgBtnSigInfoPassword);
        imgBtnRepeat = findViewById(R.id.imgBtnSigInfoPasswordRepeat);

        pref = getSharedPreferences(SharedPrefConstants.NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

        animEtShake = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.anim_shake_et);
    }

    public void closeSignUp(View view) {
        this.finish();
    }
    public void submitSignUp(View view) {

        userName = etUsername.getText().toString();
        userEmail = etEmail.getText().toString();
        userPassword = etPassword.getText().toString();
        userPasswordRepeat = etPasswordRepeat.getText().toString();

        if (isValidUsername(userName)){
            etUsername.setBackgroundResource(R.drawable.et_background);
            allDataValid[0] = true;
        } else {
            imgBtnUsername.setVisibility(View.VISIBLE);
            etUsername.setBackgroundResource(R.drawable.et_background_invalidinput);
            etUsername.startAnimation(animEtShake);
            allDataValid[0]=false;
        }

        if (isValidEmail(userEmail)){
            allDataValid[1]=true;
            etEmail.setBackgroundResource(R.drawable.et_background);
        } else {
            imgBtnEmail.setVisibility(View.VISIBLE);
            etEmail.setBackgroundResource(R.drawable.et_background_invalidinput);
            etEmail.startAnimation(animEtShake);
            allDataValid[1]=false;
        }

        if (isValidPassword(userPassword)){
            allDataValid[2]=true;
            etPassword.setBackgroundResource(R.drawable.et_background);
        }else {
            imgBtnPassword.setVisibility(View.VISIBLE);
            etPassword.setBackgroundResource(R.drawable.et_background_invalidinput);
            etPassword.startAnimation(animEtShake);
            allDataValid[2]=false;
        }

        if (isValidRepeat(userPasswordRepeat,userPassword)){
            allDataValid[3]=true;
            etPasswordRepeat.setBackgroundResource(R.drawable.et_background);
        } else {
            imgBtnRepeat.setVisibility(View.VISIBLE);
            etPasswordRepeat.setBackgroundResource(R.drawable.et_background_invalidinput);
            etPasswordRepeat.startAnimation(animEtShake);
            allDataValid[3]=false;
        }

        int succses = 0;
        for (int i = 0; i < allDataValid.length ; i++) {

            if (allDataValid[i] == true){
                succses++;
            }
        }

        if (succses == allDataValid.length){
            saveData();
        } else {
            Toast.makeText(this, R.string.check_your_inout,Toast.LENGTH_SHORT).show();
        }
    }

    private void saveData() {

        editor.putString(SharedPrefConstants.EMAIL, userEmail);
        editor.putString(SharedPrefConstants.PASSWORD, userPassword);
        editor.putBoolean(SharedPrefConstants.AUTOMATIC_LOGIN,isCbAutoLoginChecked());
        editor.commit();

        SignUp signUp = new SignUp(userName,userEmail,userPassword,this);
        if (signUp.startSignup(layout)) {
            //this.finish();
        }
    }
    private boolean isCbAutoLoginChecked(){
        if (cbSaveLogin.isChecked())
            return true;
        else
            return false;
    }

    private static boolean isValidUsername(String userName) {
        if (userName.length() < 3 || userName.length() > 15) {
            return false;
        }
        return true;
    }

    private boolean isValidPassword(final String userPassword) {
        /*
        At least Min Characters 8 and Maximum Characters 15
        At least One Number and 1 special characters from (! @#$%^&*-=+?.);
        At least One lower case letter
        */
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(userPassword);
        return matcher.matches();
    }

    private static boolean isValidRepeat(final String userPasswordRepeat, final String userPassword){
        if (!(userPassword.equals(userPasswordRepeat))){
            return false;
        }
        return true;
    }
    public static boolean isValidEmail(String userEmail) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userEmail);
        return matcher.matches();
    }

    public void  showInfoUsername(View view) {
        MyAlert alert = new MyAlert(this,"Username","Number of letters between 3-15" );
        alert.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });alert.show();
    }

    public void showInfoEmail(View view) {
        MyAlert alert = new MyAlert(this,"Email","Your mail should have a pattern like this:\n" +
                "example@mail.com" );
        alert.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });alert.show();
    }
    public void showInfoPassword(View view) {
        MyAlert alert = new MyAlert(this,"Password","Your password needs:\n" +
                "number of letters between 8-15\n" +
                "at least one number 0-9\n" +
                "at least one lower case letter\n" +
                "at least one of this: !@#$%^&*+=?-");
        alert.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });alert.show();
    }
    public void showInfoPasswordRepeat(View view) {
        MyAlert alert = new MyAlert(this,"Password","Your passwords are not equal");
        alert.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });alert.show();
    }
}