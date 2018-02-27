package com.hcll.fishshrimpcrab.club;

import com.hcll.fishshrimpcrab.club.entity.ClubInfoEntity;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by hong on 2018/2/26.
 */

public interface ClubApi {

    /**
     * 获取俱乐部列表
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/list")
    Call<BaseResponseEntity<ClubInfoEntity>> getClubList(@Body RequestBody body);
}
