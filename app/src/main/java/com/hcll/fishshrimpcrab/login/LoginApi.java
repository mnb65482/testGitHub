package com.hcll.fishshrimpcrab.login;

import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.http.entity.FileUploadEntity;
import com.hcll.fishshrimpcrab.login.entity.UserIdEntity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by hong on 2018/2/10.
 */

public interface LoginApi {

    //注册
    public int RESET = 148511;
    //重置
    public int REGISTER = 148510;

    /**
     * 注册
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("user/register")
    Call<BaseResponseEntity<UserIdEntity>> register(@Body RequestBody body);

    /**
     * 短信验证码
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("user/register-code")
    Call<BaseResponseEntity> getSms(@Body RequestBody body);

    /**
     * 忘记(重置)密码
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("user/modify-pw")
    Call<BaseResponseEntity> resetPsw(@Body RequestBody body);

    /**
     * 编辑用户信息
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("customer/update")
    Call<BaseResponseEntity> updateUser(@Body RequestBody body);


    /**
     * 上传头像
     *
     * @param file
     * @param user_id
     * @return
     */
    @Multipart
    @POST("upload/head")
    Call<BaseResponseEntity<FileUploadEntity>> saveFile(@Part MultipartBody.Part file,
                                                        @Query("user_id") int user_id);


}
