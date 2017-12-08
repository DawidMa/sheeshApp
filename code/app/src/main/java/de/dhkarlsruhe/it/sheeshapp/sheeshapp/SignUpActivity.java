package de.dhkarlsruhe.it.sheeshapp.sheeshapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Animation animEtShake, fadeInanim;
    private boolean allDataValid[] = new boolean[4], checked = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        fadeInanim = AnimationUtils.loadAnimation(this,R.anim.anim_move_from_left);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        etUsername = findViewById(R.id.edTUsername);
        etEmail = findViewById(R.id.edTEmail);
        etPassword = findViewById(R.id.edTPassword);
        etPasswordRepeat = findViewById(R.id.edTPasswordRepeat);

        imgBtnUsername = findViewById(R.id.imgBtnSigInfoUsername);
        imgBtnEmail = findViewById(R.id.imgBtnSigInfoEmail);
        imgBtnPassword = findViewById(R.id.imgBtnSigInfoPassword);
        imgBtnRepeat = findViewById(R.id.imgBtnSigInfoPasswordRepeat);
        cbSaveLogin = findViewById(R.id.cbSaveLogin);

        pref = getSharedPreferences("com.preferences.sheeshapp", Context.MODE_PRIVATE);
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

        }else{
            imgBtnUsername.setVisibility(View.VISIBLE);
            etUsername.setBackgroundResource(R.drawable.et_background_invalidinput);
            etUsername.startAnimation(animEtShake);
            allDataValid[0]=false;

        }

        if (isValidEmail(userEmail)){
            allDataValid[1]=true;
            etEmail.setBackgroundResource(R.drawable.et_background);

        }else {
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

        }else {
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
            this.finish();
        } else {
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    private void saveData() {
        editor.putString("savedUsername", userName);
        editor.putString("savedEmail", userEmail);
        editor.putString("savedPassword", userPassword);
        editor.putString("savedPasswordRepeat", userPasswordRepeat);

        if (isCbAutoLoginChecked()){
            editor.putBoolean("saveLogin",true);
        } else
            editor.putBoolean("saveLogin",false);
        editor.commit();
    }
    private boolean isCbAutoLoginChecked(){
        if (cbSaveLogin.isChecked())
                return true;
        else
            return false;
    }
    public static boolean isValidUsername(String userName) {
        if (userName.length() < 3 || userName.length() > 15) {
            return false;
        }
        return true;
    }
    public static boolean isValidPassword(final String userPassword) {
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
    public static boolean isValidRepeat(final String userPasswordRepeat, final String userPassword){
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