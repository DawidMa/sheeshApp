package de.dhkarlsruhe.it.sheeshapp.sheeshapp.images;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.SharedPrefConstants;

/**
 * Created by d0272129 on 16.05.18.
 */

public class ImageHelper {

    private Context c;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public ImageHelper(Context c) {
        this.c = c;
        pref = c.getSharedPreferences(SharedPrefConstants.IMAGES, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public boolean saveBitmapToStorage(Bitmap bitmap, String name) {
        boolean ok = false;
        ContextWrapper cw = new ContextWrapper(c.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("profile_images", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name+".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            editor.putString(SharedPrefConstants.I_NR+name, directory.getAbsolutePath());
            editor.commit();
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
        String path = pref.getString(SharedPrefConstants.I_NR + name, "empty");
        if (!path.equals("empty")) {
            try {
                File f = new File(path, name+".png");
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public Bitmap getThumbnailOfBitmap(Bitmap bitmap, int width, int height) {
        return ThumbnailUtils.extractThumbnail((bitmap), width, height);
    }

    public RoundedBitmapDrawable getRoundedBitmap(Bitmap thumbnail) {
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(c.getResources(),thumbnail );
        circularBitmapDrawable.setCircular(true);
        return circularBitmapDrawable;
    }

    public Bitmap scaleBitmap(Bitmap bitmap) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager= ((Activity) c).getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;

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
}
