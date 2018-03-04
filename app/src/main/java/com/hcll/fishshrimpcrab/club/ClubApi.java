package com.hcll.fishshrimpcrab.club;

import com.hcll.fishshrimpcrab.club.entity.ClubDetailEntity;
import com.hcll.fishshrimpcrab.club.entity.ClubInfoEntity;
import com.hcll.fishshrimpcrab.club.entity.SearchClubEntity;
import com.hcll.fishshrimpcrab.club.entity.UserInfoEntity;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;

import java.util.List;

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

    /**
     * 创建俱乐部
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/new")
    Call<BaseResponseEntity<ClubInfoEntity>> createClub(@Body RequestBody body);

    /**
     * 搜索
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/search")
    Call<BaseResponseEntity<List<SearchClubEntity>>> searchClub(@Body RequestBody body);

    /**
     * 申请加入俱乐部
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/apply-join")
    Call<BaseResponseEntity> applyJoinClub(@Body RequestBody body);

    /**
     * 查询俱乐部详情
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/view")
    Call<BaseResponseEntity<ClubDetailEntity>> getClubDetail(@Body RequestBody body);

    /**
     * 退出俱乐部
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/leave")
    Call<BaseResponseEntity> exitClub(@Body RequestBody body);

    /**
     * 解散俱乐部
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/dismiss")
    Call<BaseResponseEntity> dismissClub(@Body RequestBody body);

    /**
     * 更新俱乐部
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/update")
    Call<BaseResponseEntity> updateClub(@Body RequestBody body);

    /**
     * 获取用户资料
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("customer/detail")
    Call<BaseResponseEntity<UserInfoEntity>> getUserDetail(@Body RequestBody body);

}
