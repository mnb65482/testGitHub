package com.hcll.fishshrimpcrab.game.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BasePageAdapter;
import com.hcll.fishshrimpcrab.base.inter.PageParams;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.game.entity.GameListEntity;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hong on 2018/3/25.
 */

public abstract class BaseGamePageFragment extends Fragment {

    protected TwinklingRefreshLayout refreshLayout;
    private int page = 1;
    private ListView listView;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private TextView emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.comm_base_page_list, container, false);

        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.base_page_refresh);
        ProgressLayout headerView = new ProgressLayout(getContext());
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setOverScrollRefreshShow(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                isRefresh = true;
                isLoadMore = false;
                page = 1;
                request(page);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                isRefresh = false;
                isLoadMore = true;
                request(page);
            }
        });


        listView = (ListView) view.findViewById(R.id.base_page_listview);
        listView.setAdapter(getAdapter());
        emptyView = (TextView) view.findViewById(R.id.base_game_empty_tv);

        //由于   refreshLayout.requestLayout();没自动刷新，故手动调用
        doRequest();
        return view;
    }

    protected abstract void request(int currentPage);

    protected abstract BaseAdapter getAdapter();


    private Callback<BaseResponseEntity<GameListEntity>> callback = new Callback<BaseResponseEntity<GameListEntity>>() {
        @Override
        public void onResponse(Call<BaseResponseEntity<GameListEntity>> call, Response<BaseResponseEntity<GameListEntity>> response) {
            BaseResponseEntity<GameListEntity> body = response.body();
            if (body != null && body.isSuccessed()) {
                ListAdapter adapter = listView.getAdapter();
                if (adapter instanceof BasePageAdapter) {
                    BasePageAdapter<GameListEntity.RoomsBean> pageAdapter = (BasePageAdapter) adapter;
                    List<GameListEntity.RoomsBean> list = body.getData().getList();

                    if (isRefresh) {
                        pageAdapter.clearAll();
                        if (list != null && list.size() > 0) {
                            showList();
                            pageAdapter.addAll(list);
                            page++;
                        } else {
                            showEmpty();
                        }
                        refreshLayout.finishRefreshing();

                    } else {
                        page++;
                        pageAdapter.addAll(list);
                        refreshLayout.finishLoadmore();
                    }
                }

                if (body.getData().hasMore()) {
                    refreshLayout.setEnableLoadmore(true);
                } else {
                    refreshLayout.setEnableLoadmore(false);
                }

            } else {
                ToastUtils.showLong(R.string.search_fauiler);
                showEmpty();
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity<GameListEntity>> call, Throwable t) {
            ToastUtils.showLong(R.string.search_fauiler);
            showEmpty();
        }
    };

    private void showList() {
        emptyView.setVisibility(View.GONE);
//        refreshLayout.setVisibility(View.VISIBLE);
    }

    private void showEmpty() {
        emptyView.setVisibility(View.VISIBLE);
//        refreshLayout.setVisibility(View.GONE);
    }

    protected Callback<BaseResponseEntity<GameListEntity>> getCallback() {
        return callback;
    }

    protected void doRequest() {
        refreshLayout.startRefresh();
        isRefresh = true;
        isLoadMore = false;
        page = 1;
        request(page);
    }
}
