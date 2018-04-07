package com.hcll.fishshrimpcrab.common.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.HttpFileApi;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.http.entity.FileUploadEntity;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hong on 2018/3/3.
 */

public class HttpFileUtils {

    public static void uploadImage(Context context, String imagePath,
                                   Callback<BaseResponseEntity<FileUploadEntity>> uploadCallback) {
        File file = new File(imagePath);
        if (file.exists()) {
            HttpFileApi retrofit = HttpUtils.createRetrofit(context, HttpFileApi.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type,image/jpg"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            Call<BaseResponseEntity<FileUploadEntity>> call = retrofit.saveFile(filePart, AppCommonInfo.userid);
            call.enqueue(uploadCallback);
        }
    }

    public static void loadImage2View(Context context, String httpid, ImageView imageView) {
        Picasso.with(context).load(AppCommonInfo.ImageDownLoadPath + httpid).error(R.drawable.default_head_ic).into(imageView);
    }

    public static void loadImage2View(Context context, String httpid, @DrawableRes int erroDrawableId, ImageView imageView) {
        Picasso.with(context).load(AppCommonInfo.ImageDownLoadPath + httpid).error(erroDrawableId).into(imageView);
    }

}
