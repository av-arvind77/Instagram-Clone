/*
Copyright 2016 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yellowseed.avatar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yellowseed.webservices.response.avatar.AvatarCategory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generate a bitmap based on avatar parts
 */
public class AvatarGenerator extends AsyncTask<Long, Void, Bitmap> {
    private final Context mContext;
    private final AvatarPartDb mAvatarPartDb;
    private final int mSize;
    private final WeakReference<ImageView> mImageViewReference;
    private final Random mRandom = new Random();

    private final int AVATAR_FULL_SIZE = 1200;
//    private final String AVATAR_PARTS_DIR = "parts";
//    private final String AVATAR_PARTS_DIR = "male";
    public static final String AVATAR_PARTS_DIR_MALE = "male";
    public static final String AVATAR_PARTS_DIR_FEMALE = "female";
    private final ArrayList<AvatarPart> mAvatarParts;
    private List<AvatarCategory> avatarCategories;

    public AvatarGenerator(Context context, AvatarPartDb avatarPartDb, ImageView imageView, int size, ArrayList<AvatarPart> avatarParts, List<AvatarCategory> avatarCategories) {
        mContext = context;
        mAvatarPartDb = avatarPartDb;
        mSize = size;
        mImageViewReference = new WeakReference<>(imageView);
        mAvatarParts = avatarParts;
        this.avatarCategories = avatarCategories;
    }

    @Override
    protected Bitmap doInBackground(Long... params) {
//        long seed = params[0];
//        mRandom.setSeed(seed);

        int width = mSize;// - (mSize/3);
        Bitmap bitmap = Bitmap.createBitmap(width, mSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Matrix matrix = new Matrix();
        float hratio = mSize / AVATAR_FULL_SIZE;
        float wratio = width / AVATAR_FULL_SIZE;
        matrix.setScale(hratio, hratio);
        canvas.setMatrix(matrix);

        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

        canvas.drawColor(Color.TRANSPARENT);
        int i=0;
        for (AvatarPart avatarCategory : mAvatarParts) {
            if (isCancelled()) {
                return null;
            }


           // int idx = mRandom.nextInt(mAvatarPartDb.getPartCount(avatarCategory));
            AvatarPart part = mAvatarParts.get(i);//mAvatarPartDb.getPart(partName, idx);
            if (part == null) {
                continue;
            }

            String filePath = null;
            try {
                filePath = mAvatarParts.get(i).filename;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(filePath)) {
                continue;
            }
            Bitmap partBitmap = null;
          /*  try {
                *//*InputStream stream;
                stream = mContext.getAssets().open(filePath);
*//*                partBitmap = Glide.with(mContext).load(Uri.parse())
               // stream.close();
            } catch (IOException e) {
                continue;
            }*/
            try {
                URL url = new URL(filePath);
                partBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                System.out.println(e);
                continue;
            }

            int x = (canvas.getWidth()/2) - (partBitmap.getScaledWidth(canvas)/2);
            canvas.drawBitmap(partBitmap, x, part.y, paint);
            i++;
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView view = mImageViewReference.get();
        if (view != null && bitmap != null) {
            view.setImageBitmap(bitmap);
        }
    }
}
