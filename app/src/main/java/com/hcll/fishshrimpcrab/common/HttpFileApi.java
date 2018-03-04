package com.hcll.fishshrimpcrab.common;

import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.http.entity.FileUploadEntity;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by hong on 2018/3/3.
 */

public interface HttpFileApi {

    @Multipart
    @POST("upload/head")
    Call<BaseResponseEntity<FileUploadEntity>> saveFile(@Part MultipartBody.Part file,
                                                        @Query("user_id") int user_id);
}
