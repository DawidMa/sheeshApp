package de.dhkarlsruhe.it.sheeshapp.sheeshapp.images;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.R;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;
import de.dhkarlsruhe.it.sheeshapp.sheeshapp.server.ServerConstants;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by d0272129 on 16.05.18.
 */

public class ImageHelper {

    private Context c;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String IMAGE_DIRECTORY = "profile_images";
    private static final String PNG_FORMAT = ".png";
    private Handler handler;
    private ProgressDialog progressDialog;

    public ImageHelper(Context c) {
        this.c = c;
        pref = c.getSharedPreferences(SharedPrefConstants.IMAGES, Context.MODE_PRIVATE);
        editor = pref.edit();
        ContextWrapper cw = new ContextWrapper(c.getApplicationContext());
        File directory = cw.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        editor.putString(SharedPrefConstants.I_PATH,directory.getAbsolutePath());
        editor.commit();
    }

    public boolean saveBitmapToStorage(Bitmap bitmap, String name, String iconid) {
        boolean ok = false;
        ContextWrapper cw = new ContextWrapper(c.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name+PNG_FORMAT);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            editor.putString(SharedPrefConstants.I_NR+name, iconid);
            editor.commit();
            setChanged(true,name);
            ok = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ok;
    }

    public Bitmap loadImageFromStorage(String name) {
        Bitmap bitmap = null;
        String path = pref.getString(SharedPrefConstants.I_PATH, "empty");
        if (!path.equals("empty")) {
            try {
                File f = new File(path, name+PNG_FORMAT);
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return bitmap;
    }

    public Bitmap getThumbnailOfBitmap(Bitmap bitmap, int width, int height) {
        return ThumbnailUtils.extractThumbnail((bitmap), width, height);
    }

    public Bitmap scaleBitmap(Bitmap bitmap) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager= ((Activity) c).getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;

        Bitmap Objbitmap = bitmap;

        if (bitmap != null) {
            int heightofBitMap = Objbitmap.getHeight();
            int widthofBitMap = Objbitmap.getWidth();
            heightofBitMap = screenWidth * heightofBitMap / widthofBitMap;
            widthofBitMap = screenWidth;
            bitmap = Bitmap.createScaledBitmap(bitmap, widthofBitMap, heightofBitMap, true);
        }

        return bitmap;

    }

    public void setChanged(boolean b, String userid) {
        editor.putBoolean(SharedPrefConstants.I_NR_CHANGED+userid,b);
        editor.commit();
    }

    public boolean getChanged(String userid) {
        return pref.getBoolean(SharedPrefConstants.I_NR_CHANGED+userid,false);
    }

    public void setRectImage(String userid, ImageView img) {
        Bitmap bitmap = loadImageFromStorage(userid);
        if (bitmap==null) {
            Glide.with(c.getApplicationContext()).load(R.drawable.sheeshopa).into(img);
        } else {
            img.setImageBitmap(bitmap);
        }
    }

    public String getIconId(long friendid) {
        return pref.getString(SharedPrefConstants.I_NR+friendid,"empty");
    }

    public void setRoundImageWithId(ImageView imgFriends, long friendid) {
        Bitmap bitmap = loadImageFromStorage(friendid+"");
        if (bitmap==null) {
            setRoundImageDefault(imgFriends);
        } else {
            setRoundImageWithBitmap(imgFriends,bitmap);
        }
    }

    public void setRoundImageWithBitmap(ImageView imgFriends, Bitmap bitmap) {
        Glide.with(c).load(bitmap).apply(RequestOptions.circleCropTransform()).into(imgFriends);
    }

    public void setRoundImageDefault(ImageView imgFriends) {
        Glide.with(c).load(R.drawable.sheeshopa).apply(RequestOptions.circleCropTransform()).into(imgFriends);
    }

    public String getImagePath() {
        return pref.getString(SharedPrefConstants.I_PATH,"empty");
    }

    public void setNewIconId(long userid, String iconid) {
        editor.putString(SharedPrefConstants.I_NR+userid, iconid);
        editor.commit();
    }
    public void loadFileFromServer(final long userid, final ImageView imageView, final String iconid) {

        prepareProgressDialog();
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(ServerConstants.URL_DOWNLOAD + userid+".png").build();
                okhttp3.Response response = null;
                try {
                    response = client.newCall(request).execute();
                    //String fileName = response.body().toString();
                    float fileSize = response.body().contentLength();
                    BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());
                    String path = getImagePath();
                    if (path.equals("empty")) {
                        Toast.makeText(c,"Problem with Image Storage",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String fullFilePath = path +"/"+ userid+ ".png";
                    OutputStream outputStream = new FileOutputStream(fullFilePath);
                    byte[] data = new byte[8192];
                    float total = 0;
                    int readedBytes = 0;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show();
                        }
                    });

                    while ((readedBytes = inputStream.read(data)) != -1) {
                        total = total + readedBytes;
                        outputStream.write(data, 0, readedBytes);
                        progressDialog.setProgress((int) ((total / fileSize) * 100));
                    }
                    progressDialog.dismiss();
                    outputStream.flush();
                    outputStream.close();
                    response.body().close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setNewIconId(userid,iconid);
                            setRoundImageWithId(imageView,userid);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void prepareProgressDialog() {
        handler = new Handler();
        progressDialog = new ProgressDialog(c);
        progressDialog.setTitle("Downloading...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    public boolean deleteFromStorage(String userid) {
        String path = pref.getString(SharedPrefConstants.I_PATH, "empty");
        if (!path.equals("empty")) {
            File f = new File(path, userid+PNG_FORMAT);
            return f.delete();
        }
        return false;
    }
}
