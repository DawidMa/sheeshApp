package de.dhkarlsruhe.it.sheeshapp.sheeshapp.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.FriendsFragment;
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
import uk.co.senab.photoview.PhotoView;

/**
 * Created by d0272129 on 15.05.18.
 */

public class MyProfileActivity extends AppCompatActivity{

    private PhotoView img;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    private ConstraintLayout mainLayout;
    private ConstraintLayout saveLayout;
    private UserSessionObject session;
    private ImageHelper imageHelper;
    private String userid;
    private Context context;
    private ProgressDialog dialog;
    private MenuItem editMenu;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(0,0,0));
        this.setContentView(R.layout.activity_my_profile);
        context = this;
        session = new UserSessionObject(this);
        imageHelper = new ImageHelper(this);
        setTitle(getString(R.string.profile_image_text));
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        mainLayout = findViewById(R.id.layoutMyProfile);
        saveLayout = findViewById(R.id.layoutMyProfileSave);
        ColorDrawable[] color = {new ColorDrawable(Color.GRAY), new ColorDrawable(Color.BLACK)};
        TransitionDrawable trans = new TransitionDrawable(color);
        mainLayout.setBackground(trans);
        trans.startTransition(500);
        userid = session.getUser_id()+"";
        img = findViewById(R.id.imgMyProfile);
       /* btEdit = findViewById(R.id.btMyProfileEdit);
        btEdit.setAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));
        btEdit.animate();*/
        imageHelper.setRectImage(userid,img);
        imageHelper.setChanged(false,userid);
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

        editMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                editMenu.setVisible(false);
                openPopup(img);
                setTitle(getString(R.string.edit_text));
                return false;
            }
        });
/*
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btEdit.setVisibility(View.GONE);
                openPopup(img);
                setTitle("Edit");
            }
        });*/
        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

    }

    private void openPopup(View anchor) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_image_edit, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       // popupWindow.setAnimationStyle(R.style.popupAnimation);
        popupWindow.setFocusable(true);
        ConstraintLayout layoutGallery = popupView.findViewById(R.id.layoutPopupGallery);
        ConstraintLayout layoutDelete= popupView.findViewById(R.id.layoutPopupDelete);

        layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(ServerConstants.URL_DELTE + userid+".png", new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String string) {
                        if (string.equals("OK")) {
                            if (imageHelper.deleteFromStorage(userid)) {
                                Toast.makeText(context, R.string.deleted_text,Toast.LENGTH_SHORT).show();
                                imageHelper.setChanged(true,userid);
                                popupWindow.dismiss();
                            } else {
                                Toast.makeText(context,"Couldnt delete offline",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context,"Couldnt delete online",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, R.string.error_text,Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue rQueue = Volley.newRequestQueue(MyProfileActivity.this);
                rQueue.add(request);
            }
        });

        layoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(MyProfileActivity.this);
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                editMenu.setVisible(true);
               // btEdit.setVisibility(View.VISIBLE);
                setTitle(getString(R.string.profile_image_text));
            }
        });
        popupWindow.showAsDropDown(anchor);
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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode == Crop.REQUEST_PICK && data != null) {
            /*Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                bitmap = imageHelper.scaleBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = imageHelper.getThumbnailOfBitmap(bitmap,(int)(bitmap.getWidth()*0.3),(int)(bitmap.getHeight()*0.3));
            img.setImageBitmap(bitmap);
            //dataIntent = data;
            saveLayout.setVisibility(View.VISIBLE);*/
            Uri path = data.getData();
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
                Crop.of(path, destination).asSquare().start(this);
                img.setImageURI(Crop.getOutput(data));
                //File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode,data);
            }
        }
    }

    private void handleCrop(int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {
            img.setImageURI(Crop.getOutput(data));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),Crop.getOutput(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveLayout.setVisibility(View.VISIBLE);
            //btEdit.setVisibility(View.GONE);
            editMenu.setVisible(false);
            setTitle(getString(R.string.save_image_question));
        } else if(resultCode == Crop.RESULT_ERROR) {

        }
    }

    private void uploadFile() {
        dialog = new ProgressDialog(context);
        dialog.setTitle(getString(R.string.uploading_text));
        dialog.setMessage(getString(R.string.please_wait_text));
        dialog.setCancelable(true);
        dialog.show();

        final String iconid = UUID.randomUUID().toString();
        final String fileName = userid+".png";
        final Thread t = new Thread(new Runnable() {
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
                        Snackbar.make(mainLayout,getString(R.string.error_text),Snackbar.LENGTH_LONG).show();
                        throw new IOException("Error" + response);
                    } else {
                        saveBitmap(iconid);
                    }
                    response.body().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                t.interrupt();
            }
        });
    }
    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    private void saveBitmap(String iconid) {
        String response;
        if(imageHelper.saveBitmapToStorage(bitmap,userid,iconid)) {
            response = getString(R.string.saved_uploaded_text);
        } else {
            response = getString(R.string.error_save_picture);
        }
        Snackbar.make(mainLayout,response,Snackbar.LENGTH_LONG).show();
        dialog.dismiss();
       // btEdit.setVisibility(View.VISIBLE);
        editMenu.setVisible(true);
        setTitle(getString(R.string.profile_image_text));
        saveLayout.setVisibility(View.GONE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_myprofile, menu);
        editMenu = menu.getItem(0);
        checkPerms();

        return true;
    }
}
