package com.hcll.fishshrimpcrab.club;

import com.hcll.fishshrimpcrab.club.entity.ClubDetailEntity;
import com.hcll.fishshrimpcrab.club.entity.ClubGameEntity;
import com.hcll.fishshrimpcrab.club.entity.ClubInfoEntity;
import com.hcll.fishshrimpcrab.club.entity.ClubMemberEntity;
import com.hcll.fishshrimpcrab.club.entity.ClubMessageEntity;
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

    /**
     * 获取俱乐部成员列表
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/mlist")
    Call<BaseResponseEntity<List<ClubMemberEntity>>> getMemberList(@Body RequestBody body);

    /**
     * 获取俱乐部成员列表
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/set-role")
    Call<BaseResponseEntity> setMemberRole(@Body RequestBody body);

    /**
     * 获取俱乐部成员列表
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/msg-list")
    Call<BaseResponseEntity<ClubMessageEntity>> getMessageList(@Body RequestBody body);

    /**
     * 获取俱乐部游戏列表
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("imgroup/room-list")
    Call<BaseResponseEntity<ClubGameEntity>> getGameList(@Body RequestBody body);

    /**
     * 设置消息免打扰
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/set-push")
    Call<BaseResponseEntity> setPush(@Body RequestBody body);

    /**
     * 同意加入俱乐部
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/approve-join")
    Call<BaseResponseEntity> agree(@Body RequestBody body);

    /**
     * 拒绝加入俱乐部
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/reject-join")
    Call<BaseResponseEntity> reject(@Body RequestBody body);

    /**
     * 删除俱乐部消息
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/delete-msg")
    Call<BaseResponseEntity> deleteMessage(@Body RequestBody body);

    /**
     * 升级俱乐部人数
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/update-limit")
    Call<BaseResponseEntity> updateMax(@Body RequestBody body);

    /**
     * 俱乐部删除成员
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("club/remove")
    Call<BaseResponseEntity> deleteMember(@Body RequestBody body);

}
