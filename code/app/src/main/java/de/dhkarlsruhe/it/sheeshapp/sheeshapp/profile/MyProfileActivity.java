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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.images.ImageHelper;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
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
    private ConstraintLayout mainLayout;
    private ConstraintLayout saveLayout;
    private UserSessionObject session;
    private ImageHelper imageHelper;
    private String userid;
    private Context context;
    private ProgressDialog dialog;
    private Intent dataIntent;
    private static final long  MEGABYTE = 1024L * 1024L;



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
        mainLayout = findViewById(R.id.layoutMyProfile);
        saveLayout = findViewById(R.id.layoutMyProfileSave);
        ColorDrawable[] color = {new ColorDrawable(Color.GRAY), new ColorDrawable(Color.BLACK)};
        TransitionDrawable trans = new TransitionDrawable(color);
        mainLayout.setBackground(trans);
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

    public static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE ;
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
                selectImage();
//                new MaterialFilePicker()
//                        .withActivity(MyProfileActivity.this)
//                        .withRequestCode(99)
//                        .withFilter(Pattern.compile("(.*/)*.+\\.(png|jpg|gif|jpeg|PNG|JPG|GIF)$"))
//                        .start();
            }
        });
        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndUploadImage();
            }
        });
    }

    private void saveAndUploadImage() {
        uploadFile();
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
        } else  {
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
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                bitmap = imageHelper.scaleBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = imageHelper.getThumbnailOfBitmap(bitmap,(int)(bitmap.getWidth()*0.3),(int)(bitmap.getHeight()*0.3));
            img.setImageBitmap(bitmap);
            //dataIntent = data;
            saveLayout.setVisibility(View.VISIBLE);
            //File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
        }
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    private void saveBitmap() {
        String response;
        if(imageHelper.saveBitmapToStorage(bitmap,userid)) {
            response = "Saved & Uploaded";
        } else {
            response = "Error saving picture";
        }
        Snackbar.make(mainLayout,response,Snackbar.LENGTH_LONG).show();
        dialog.dismiss();
    }

    private void uploadFile() {
        dialog = new ProgressDialog(context);
        dialog.setTitle("Uploading");
        dialog.setMessage("Please wait...");
        dialog.show();
        final String iconid = UUID.randomUUID().toString();
        final String fileName = userid+"_"+iconid+".png";
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(context.getCacheDir(), fileName);
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos ;
                try {
                    fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String content_type = getMimeType(f.getPath());
                String filePath = f.getAbsolutePath();
                OkHttpClient client = new OkHttpClient();
                RequestBody fileBody =  RequestBody.create(MediaType.parse(content_type),f);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type","image/png")
                        .addFormDataPart("file",filePath.substring(filePath.lastIndexOf("/")+1),fileBody)
                        .addFormDataPart("iconid", iconid)
                        .build();
                Request request = new Request.Builder()
                        .url(ServerConstants.URL_UPLOAD)
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        Snackbar.make(mainLayout,"ERROR",Snackbar.LENGTH_LONG).show();
                        throw new IOException("Error" + response);
                    } else {
                        saveBitmap();
                    }
                    response.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private File getFileFromBitmap(String format) {
        File f = new File(context.getCacheDir(), userid+format);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
}
