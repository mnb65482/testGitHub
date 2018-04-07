package com.hcll.fishshrimpcrab.game.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hcll.fishshrimpcrab.club.activity.ClubGameListActivity;
import com.hcll.fishshrimpcrab.club.entity.ClubGameReq;
import com.hcll.fishshrimpcrab.club.widget.GameFilterView;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.hcll.fishshrimpcrab.game.GameApi;
import com.hcll.fishshrimpcrab.game.adapter.GameListAdapter;
import com.hcll.fishshrimpcrab.game.entity.GameListEntity;
import com.hcll.fishshrimpcrab.game.entity.RoomListReq;
import com.hcll.fishshrimpcrab.game.enums.GameListType;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by hong on 2018/3/28.
 */

public class GameHallFragment extends BaseGamePageFragment {

    private final String TAG = GameHallFragment.class.getSimpleName();

    private GameApi mRetrofit;
    private RoomListReq req = new RoomListReq();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //base里已经进行网络请求，故需先定义Retrofit
        mRetrofit = HttpUtils.createRetrofit(getContext(), GameApi.class);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void request(int currentPage) {
        req.setPage_num(currentPage);
        req.setList_type(GameListType.hall.getId());
        RequestBody body = JsonUtils.createJsonRequestBody(req);
        Call<BaseResponseEntity<GameListEntity>> call = mRetrofit.getGameList(body);
        call.enqueue(getCallback());
    }

    @Override
    protected BaseAdapter getAdapter() {
        return new GameListAdapter(getContext(), new ArrayList<GameListEntity.RoomsBean>());
    }

    protected void setRequestParam(RoomListReq req) {
        try {
            this.req = req.clone();
        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
            Log.e(TAG, "setRequestParam: 请求参数clone异常！");
        }

        doRequest();
    }
}
