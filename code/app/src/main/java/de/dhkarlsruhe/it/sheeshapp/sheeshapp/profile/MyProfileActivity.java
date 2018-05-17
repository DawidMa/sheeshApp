package de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.images.ImageHelper;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;

/**
 * Created by d0272129 on 15.05.18.
 */

public class MyProfileActivity extends AppCompatActivity{

    private ImageView img;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    private Button btEdit;
    private boolean changedImage = false;
    private ConstraintLayout layout;
    private UserSessionObject session;
    private ImageHelper imageHelper;
    private String userid;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        this.setContentView(R.layout.activity_my_profile);
        session = new UserSessionObject(this);
        imageHelper = new ImageHelper(this);
        layout = findViewById(R.id.layoutMyProfile);
        ColorDrawable[] color = {new ColorDrawable(Color.GRAY), new ColorDrawable(Color.BLACK)};
        TransitionDrawable trans = new TransitionDrawable(color);
        layout.setBackground(trans);
        trans.startTransition(500);
        img = findViewById(R.id.imgMyProfile);
        userid = session.getUser_id()+"";
        btEdit = findViewById(R.id.btMyProfileEdit);
        btEdit.setAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));
        btEdit.animate();
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        setImage(imageHelper.loadImageFromStorage(userid));
        imageHelper.setChanged(false,userid);
    }

    private void setImage(Bitmap image) {
        if (image == null) {
            if (img != null) {
                Glide.with(getApplicationContext()).load(R.drawable.sheeshopa).into(img);
            }
        }else  {
            img.setImageBitmap(image);
        }

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode==RESULT_OK && data!=null) {
            Uri path = data.getData();
            try {
                bitmap = imageHelper.scaleBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(),path));
                img.setImageBitmap(bitmap);
                saveBitmap();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveBitmap() {
        String response;
        if(imageHelper.saveBitmapToStorage(bitmap,userid)) {
            response = "Saved";
        } else {
            response = "Error saving picture";
        }
        Snackbar.make(layout,response,Snackbar.LENGTH_LONG).show();
    }
}
