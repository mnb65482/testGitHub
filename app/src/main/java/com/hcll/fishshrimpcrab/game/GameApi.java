package com.hcll.fishshrimpcrab.game;

import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.game.entity.GameListEntity;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by lWX385270 on 2018/3/28.
 */

public interface GameApi {
    /**
     * 牌局列表
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json")
    @POST("room/room-list")
    Call<BaseResponseEntity<GameListEntity>> getGameList(@Body RequestBody body);
}
