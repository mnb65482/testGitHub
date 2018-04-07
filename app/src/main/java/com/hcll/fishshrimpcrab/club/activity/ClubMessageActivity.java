package com.hcll.fishshrimpcrab.club.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.FSCApplication;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.adapter.ClubMessageAdapter;
import com.hcll.fishshrimpcrab.club.entity.ClubMessageEntity;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lWX524664 on 2018/3/7.
 */

public class ClubMessageActivity extends BaseActivity {
    private static final String TAG = ClubMessageActivity.class.getSimpleName();


    private TwinklingRefreshLayout refreshLayout;
    private ListView messageListView;
    private ClubApi mRetrofit;
    private ClubMessageAdapter adapter;
    //分页功能待后期封装
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private long loadMoreTime = 0;
    private Dialog mDialog;

    private enum HandleMessage {
        success(0, FSCApplication.getContext().getResources().getString(R.string.success)),
        failuer(1, FSCApplication.getContext().getResources().getString(R.string.failuer)),
        database(2, FSCApplication.getContext().getResources().getString(R.string.data_exception)),
        clubMax(3, "俱乐部满员"),
        messagehandle(4, "该消息已被处理"),
        notpower(5, "当前玩家已不在该俱乐部或已不是管理员"),
        clubDismiss(6, "当前战队已被解散");

        HandleMessage(int id, String desc) {
            this.desc = desc;
            this.id = id;
        }

        private int id;
        private String desc;

        public String getDesc() {
            return desc;
        }

        public int getId() {
            return id;
        }

        public static String getDescById(int id) {
            for (HandleMessage message : values()) {
                if (message.getId() == id) {
                    return message.getDesc();
                }
            }
            return "";
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_message);
        initTopBar();
        initView();
    }

    private void initView() {

        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.club_message_refresh);
        ProgressLayout headerView = new ProgressLayout(this);
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setOverScrollRefreshShow(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                isRefresh = true;
                isLoadMore = false;
                loadMoreTime = 0;
                requestList(loadMoreTime);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                isRefresh = false;
                isLoadMore = true;
                requestList(loadMoreTime);
            }
        });

        refreshLayout.startRefresh();

        messageListView = (ListView) findViewById(R.id.club_message_listview);
        mRetrofit = HttpUtils.createRetrofit(this, ClubApi.class);
        mDialog = DialogUtils.createProgressDialog(this, null);
        adapter = new ClubMessageAdapter(this, new ArrayList<ClubMessageEntity.ListBean>());
        messageListView.setAdapter(adapter);

        adapter.setOnHandleJoinClubCallBack(new ClubMessageAdapter.HandleJoinClubCallBack() {
            @Override
            public void handle(boolean isAgree, ClubMessageEntity.ListBean bean) {
                if (isAgree) {
                    agreeJoin(bean);
                } else {
                    rejectJoin(bean);
                }
            }

            @Override
            public void delete(ClubMessageEntity.ListBean bean) {
                deleteMsg(bean.getMsgID() + "");
            }
        });
    }

    /**
     * 请求列表信息
     *
     * @param time
     */
    private void requestList(long time) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("clear", "0");
        map.put("time", time);
        map.put("direction", 0);

        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity<ClubMessageEntity>> call = mRetrofit.getMessageList(body);
        call.enqueue(callback);
    }

    /**
     * 同意加入俱乐部请求
     *
     * @param bean
     */
    private void agreeJoin(ClubMessageEntity.ListBean bean) {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", bean.getSender());
        map.put("owner_id", AppCommonInfo.getUserid());
//        map.put("user_id", AppCommonInfo.getUserid());
//        map.put("owner_id", bean.getSender());
        map.put("club_id", bean.getClubID());
        map.put("msg_id", bean.getMsgID());

        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.agree(body);
        call.enqueue(handleCallback);
    }

    /**
     * 拒绝加入
     *
     * @param bean
     */
    private void rejectJoin(ClubMessageEntity.ListBean bean) {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", bean.getSender());
        map.put("operator_id", AppCommonInfo.getUserid());
        map.put("club_id", bean.getClubID());
        map.put("msg_id", bean.getMsgID());
        map.put("remark", "");

        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.reject(body);
        call.enqueue(handleCallback);
    }

    private void deleteMsg(String msgId) {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.getUserid());
        map.put("members", new String[]{msgId});
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.deleteMessage(body);
        call.enqueue(deleteCallback);
    }

    /**
     * 查询列表callback
     */
    private Callback<BaseResponseEntity<ClubMessageEntity>> callback = new Callback<BaseResponseEntity<ClubMessageEntity>>() {
        @Override
        public void onResponse(Call<BaseResponseEntity<ClubMessageEntity>> call, Response<BaseResponseEntity<ClubMessageEntity>> response) {
            BaseResponseEntity<ClubMessageEntity> body = response.body();
            if (body != null && body.getData() != null) {
                if (isRefresh) {
                    adapter.clear();
                    adapter.addAll(body.getData().getList());
                    refreshLayout.finishRefreshing();
                } else if (isLoadMore) {
                    adapter.addAll(body.getData().getList());
                    refreshLayout.finishLoadmore();
                }
                if (body.getData().getHasMore() == 0) {
                    refreshLayout.setEnableLoadmore(false);
                    loadMoreTime = 0;
                } else {
                    refreshLayout.setEnableLoadmore(true);
                    List<ClubMessageEntity.ListBean> list = body.getData().getList();
                    if (list != null && list.size() > 0) {
                        loadMoreTime = list.get(list.size() - 1).getTime();
                    }
                }
            } else {
                ToastUtils.showLong(R.string.search_fauiler);
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity<ClubMessageEntity>> call, Throwable t) {
            ToastUtils.showLong(R.string.search_fauiler);
        }
    };

    private Callback<BaseResponseEntity> handleCallback = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            mDialog.dismiss();
            BaseResponseEntity body = response.body();
            if (body != null) {
                ToastUtils.showLong(HandleMessage.getDescById(body.getStatus()));
                if (body.getStatus() == HandleMessage.success.getId()) {
                    refreshLayout.startRefresh();
                }
            } else {
                String desc = HandleMessage.failuer.getDesc();
                if (StringUtils.isEmpty(desc)) {
                    ToastUtils.showLong(R.string.failuer);
                } else {
                    ToastUtils.showLong(desc);
                }
            }

        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {

        }
    };

    Callback<BaseResponseEntity> deleteCallback = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            mDialog.dismiss();
            BaseResponseEntity body = response.body();
            if (body != null && body.getStatus() == 0) {
                ToastUtils.showLong(R.string.delete_success);
                refreshLayout.startRefresh();
            } else {
                ToastUtils.showLong(R.string.delete_failuer);
            }

        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            ToastUtils.showLong(R.string.delete_failuer);
        }
    };

    private void initTopBar() {
        showTopBar();
        getTopBar().setTitle(R.string.titlle_club_message);
    }


}
