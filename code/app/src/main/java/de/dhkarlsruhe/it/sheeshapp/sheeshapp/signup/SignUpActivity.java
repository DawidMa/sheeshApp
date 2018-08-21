package de.dhkarlsruhe.it.sheeshapp.sheeshapp.signup;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.MyAlert;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.utilities.MyUtilities;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etPasswordRepeat, etUsername;
    private String userName, userPassword, userPasswordRepeat, userEmail;
    private ImageButton imgBtnUsername, imgBtnEmail, imgBtnPassword, imgBtnRepeat;
    private CheckBox cbSaveLogin;
    private boolean allDataValid[] = new boolean[4];
    private View layout;
    private SignUp signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        signUp = new SignUp(this);

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
    }

    public void closeSignUp(View view) {
        this.finish();
    }

    public void submitSignUp(View view) {

        userName = etUsername.getText().toString().trim();
        userEmail = etEmail.getText().toString().trim();
        userPassword = etPassword.getText().toString().trim();
        userPasswordRepeat = etPasswordRepeat.getText().toString().trim();

        allDataValid[0] = signUp.isValidUsername(userName, etUsername, imgBtnUsername);
        allDataValid[1] = signUp.isValidEmail(userEmail, etEmail, imgBtnEmail);
        allDataValid[2] = signUp.isValidPasswort(userPassword, etPassword, imgBtnPassword);
        allDataValid[3] = signUp.isValidRepeat(userPasswordRepeat, etPasswordRepeat, imgBtnRepeat);

        int success = 0;
        for (Boolean b : allDataValid) {
            if (b) success++;
        }

        if (success == allDataValid.length) {
            signUp.startSignup(layout,cbSaveLogin.isChecked());
        } else {
            MyUtilities.getColoredSnackbar(layout,getString(R.string.check_your_inout), getResources().getColor(R.color.redError)).show();
        }
    }

    public void showInfoUsername(View view) {
        MyAlert alert = new MyAlert(this, getString(R.string.username_text), "Number of letters between 3-15");
        alert.setNeutralButton(getString(R.string.ok_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public void showInfoEmail(View view) {
        MyAlert alert = new MyAlert(this, "Email", "Your mail should have a pattern like this:\n" +
                "example@mail.com");
        alert.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public void showInfoPassword(View view) {
        MyAlert alert = new MyAlert(this, "Password", "Your password needs:\n" +
                "number of letters between 6-20\n" +
                "at least one number 0-9\n" +
                "at least one lower case letter\n" +
                "at least one upper case letter");
        alert.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public void showInfoPasswordRepeat(View view) {
        MyAlert alert = new MyAlert(this, "Password", "Your passwords are not equal");
        alert.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

}