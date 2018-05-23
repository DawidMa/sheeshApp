package de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.images.ImageHelper;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.session.UserSessionObject;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private Context context;
    private ProgressDialog dialog;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        this.setContentView(R.layout.activity_my_profile);
        context = this;
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
        setImage(imageHelper.loadImageFromStorage(userid));
        imageHelper.setChanged(false,userid);
        checkPerms();
    }

    private void checkPerms() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }
        enableButton();

    }

    private void enableButton() {
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();

                new MaterialFilePicker()
                        .withActivity(MyProfileActivity.this)
                        .withRequestCode(99)
                        .withFilter(Pattern.compile("(.*/)*.+\\.(png|jpg|gif|jpeg|PNG|JPG|GIF)$"))
                        .start();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            enableButton();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }

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
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
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
        } else if (requestCode == 99 && resultCode == RESULT_OK) {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Uploading");
            dialog.setMessage("Please wait...");
            dialog.show();


            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type = getMimeType(f.getPath());

                    String filePath = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody fileBody =  RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("file",filePath.substring(filePath.lastIndexOf("/")+1),fileBody)
                            .build();

                    Request request = new Request.Builder()
                            .url("http://sheeshapp.it.dh-karlsruhe.de:8080/upload/file")
                            .post(requestBody)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            Snackbar.make(layout,"ERROR",Snackbar.LENGTH_LONG).show();
                            throw new IOException("Error" + response);
                        } else {
                            Snackbar.make(layout,"HOCHGELADEN",Snackbar.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
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
