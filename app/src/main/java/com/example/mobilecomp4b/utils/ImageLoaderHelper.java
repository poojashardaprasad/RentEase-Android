package com.example.mobilecomp4b.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.example.mobilecomp4b.R;

import java.io.File;

public class ImageLoaderHelper {

    public static void loadPropertyImage(ImageView imageView, String imageName) {
        if (imageView == null) return;

        if (imageName == null || imageName.isEmpty()) {
            imageView.setImageResource(R.drawable.img_default_property);
            return;
        }

        // Case 1: Uploaded local file path
        if (imageName.startsWith("/")) {
            File file = new File(imageName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    return;
                }
            }
        }

        // Case 2: Content or File URI
        if (imageName.startsWith("content:") || imageName.startsWith("file:")) {
            try {
                imageView.setImageURI(Uri.parse(imageName));
                return;
            } catch (Exception ignored) {}
        }

        // Case 3: Built-in drawable resource name (e.g. img_property_1)
        Context context = imageView.getContext();
        try {
            Resources resources = context.getResources();
            int resId = resources.getIdentifier(imageName, "drawable", context.getPackageName());
            if (resId != 0) {
                imageView.setImageResource(resId);
                return;
            }
        } catch (Exception ignored) {}

        // Fallback default
        imageView.setImageResource(R.drawable.img_default_property);
    }
}
