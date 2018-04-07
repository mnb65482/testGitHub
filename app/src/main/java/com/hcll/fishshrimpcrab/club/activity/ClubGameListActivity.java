package com.hcll.fishshrimpcrab.club.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.adapter.ClubGameAdapter;
import com.hcll.fishshrimpcrab.club.entity.ClubGameEntity;
import com.hcll.fishshrimpcrab.club.entity.ClubGameReq;
import com.hcll.fishshrimpcrab.club.widget.GameFilterView;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hong on 2018/3/8.
 */

public class ClubGameListActivity extends BaseActivity {

    private static final int REQUEST_CODE_DETAIL = 115;

    private static final String EXTRA_CLUB_ID = "club_id";
    private static final String EXTRA_CLUB_NAME = "club_name";

    @BindView(R.id.club_game_filter_tv)
    TextView filterTv;
    @BindView(R.id.club_game_list_lv)
    ListView gameListLv;
    @BindView(R.id.club_game_refresh)
    TwinklingRefreshLayout gameRefresh;
    @BindView(R.id.club_game_create_iv)
    ImageView clubGameCreateIv;
    @BindView(R.id.club_game_create_ll)
    LinearLayout createLl;
    @BindView(R.id.club_game_empty_tv)
    TextView emptyTv;

    private String clubId;
    private String clubName;
    private ClubGameAdapter adapter;
    private ClubApi mRetrofit;

    private boolean isRefresh = false;
    private boolean isLoadmore = false;
    private int mPageNum = 1;
    //用于保存筛选数据,上拉加载时使用
    private ClubGameReq req = null;
    //    private PopupWindow popupWindow;
    private GameFilterView filterView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_game_list);
        ButterKnife.bind(this);
        initParam();
        initTopBar();
        initView();
        initPopup();
    }

    private void initParam() {
        clubId = getIntent().getStringExtra(EXTRA_CLUB_ID);
        clubName = getIntent().getStringExtra(EXTRA_CLUB_NAME);
    }

    private void initTopBar() {
        showTopBar();
        QMUITopBar topBar = getTopBar();
        topBar.setTitle(clubName);
        QMUIAlphaImageButton rightImageButton = topBar.addRightImageButton(R.drawable.main_tab_club_select, View.generateViewId());
        rightImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(ClubDetailActivity.createActivity(ClubGameListActivity.this, clubId), REQUEST_CODE_DETAIL);
            }
        });
    }

    private void initView() {

        ProgressLayout headerView = new ProgressLayout(this);
        gameRefresh.setHeaderView(headerView);
        gameRefresh.setOverScrollRefreshShow(false);
        gameRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                isRefresh = true;
                isLoadmore = false;
                requestList(1, req);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                isRefresh = false;
                isLoadmore = true;
                requestList(0, req);
            }
        });

        gameRefresh.startRefresh();
        adapter = new ClubGameAdapter(this, new ArrayList<ClubGameEntity.RoomsBean>());
        gameListLv.setAdapter(adapter);
        mRetrofit = HttpUtils.createRetrofit(this, ClubApi.class);
    }

    /**
     * 请求列表
     *
     * @param direction 加载模式 0加载更多 1刷新  (后台要求传，多余)
     * @param req
     */
    private void requestList(int direction, ClubGameReq req) {
        ClubGameReq gameReq = null;
        if (req == null) {
            gameReq = new ClubGameReq();
        } else {
            gameReq = req;
        }
        gameReq.setUser_id(AppCommonInfo.userid);
        gameReq.setGid(clubId);
        gameReq.setPage_num(mPageNum);
        gameReq.setDirection(direction);

        Gson gson = new Gson();
        String json = gson.toJson(gameReq);
        RequestBody body = RequestBody.create(MediaType.parse("Content-Type,application/json"), json);
        Call<BaseResponseEntity<ClubGameEntity>> call = mRetrofit.getGameList(body);
        call.enqueue(callback);

    }

    private Callback<BaseResponseEntity<ClubGameEntity>> callback = new Callback<BaseResponseEntity<ClubGameEntity>>() {
        @Override
        public void onResponse(Call<BaseResponseEntity<ClubGameEntity>> call, Response<BaseResponseEntity<ClubGameEntity>> response) {

            BaseResponseEntity<ClubGameEntity> body = response.body();
            if (body != null && body.getData() != null) {
                List<ClubGameEntity.RoomsBean> rooms = body.getData().getRooms();
                if (rooms != null && rooms.size() > 0) {
                    showList();

                    if (isRefresh) {
                        adapter.clear();
                        adapter.addAll(rooms);
                        gameRefresh.finishRefreshing();
                    } else if (isLoadmore) {
                        adapter.addAll(rooms);
                        gameRefresh.finishLoadmore();
                    }
                } else {
                    showEmptyView();
                }

                if (body.getData().getTotalpage() <= mPageNum) {
                    gameRefresh.setEnableLoadmore(false);
                    mPageNum = 1;
                } else {
                    gameRefresh.setEnableLoadmore(true);
                    mPageNum++;
                }
            } else {
                ToastUtils.showLong(R.string.search_fauiler);
                showEmptyView();
            }

//            //测试逻辑
//            ClubGameEntity entity = new ClubGameEntity();
//            List<ClubGameEntity.RoomsBean> list = new ArrayList<>();
//            for (int i = 0; i < 10; i++) {
//                ClubGameEntity.RoomsBean bean = new ClubGameEntity.RoomsBean();
//                bean.setRoom_name(" 鱼虾蟹大合集" + i);
//                bean.setPlayer_count(20);
//                bean.setRoom_people(5);
//                bean.setInit_chip(1000);
//                bean.setReside_time(60 * 60 * 1000 + 30 * 60 * 1000);
//                bean.setGame_status(i % 2);
//                list.add(bean);
//            }
//            entity.setRooms(list);
//            entity.setTotalpage(10);
//
//            if (isRefresh) {
//                adapter.clear();
//                adapter.addAll(entity.getRooms());
//                gameRefresh.finishRefreshing();
//            } else if (isLoadmore) {
//                adapter.addAll(entity.getRooms());
//                gameRefresh.finishLoadmore();
//            }
//            if (entity.getTotalpage() == mPageNum) {
//                gameRefresh.setEnableLoadmore(false);
//                mPageNum = 1;
//            } else {
//                gameRefresh.setEnableLoadmore(true);
//                mPageNum++;
//            }

        }

        @Override
        public void onFailure(Call<BaseResponseEntity<ClubGameEntity>> call, Throwable t) {
            ToastUtils.showLong(R.string.search_fauiler);
        }
    };

    private void showEmptyView() {
        adapter.clear();
        gameRefresh.setVisibility(View.GONE);
        emptyTv.setVisibility(View.VISIBLE);
    }

    private void showList() {
        gameRefresh.setVisibility(View.VISIBLE);
        emptyTv.setVisibility(View.GONE);
    }


    @OnClick({R.id.club_game_filter_tv, R.id.club_game_create_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.club_game_filter_tv:
                filterView.show(filterTv);
                break;
            case R.id.club_game_create_iv:
                break;
        }
    }

    public static Intent createActivity(Context context, String clubId, String clubName) {
        Intent intent = new Intent(context, ClubGameListActivity.class);
        intent.putExtra(EXTRA_CLUB_ID, clubId);
        intent.putExtra(EXTRA_CLUB_NAME, clubName);
        return intent;
    }

    private void initPopup() {
        filterView = new GameFilterView(this);
        filterView.setOnFilterCallBack(new GameFilterView.OnFilterCallBack() {
            @Override
            public void filter(ClubGameReq req) {
                showList();
                gameRefresh.startRefresh();
                //由于没自动刷新，故手动调用
                ClubGameListActivity.this.req = req;
                requestList(1, req);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
