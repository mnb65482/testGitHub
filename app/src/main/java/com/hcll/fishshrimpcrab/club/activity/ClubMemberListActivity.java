package com.hcll.fishshrimpcrab.club.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.adapter.ClubMemberListAdapter;
import com.hcll.fishshrimpcrab.club.entity.ClubMemberEntity;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 俱乐部成员页面
 * <p>
 * Created by hong on 2018/3/5.
 */

public class ClubMemberListActivity extends BaseActivity {

    private static final String EXTRA_CLUBID = "club_id";
    private static final String EXTRA_IS_CREATOR = "isCreator";

    @BindView(R.id.club_member_list_lv)
    ListView mMemberLv;
    private String clubId;
    private boolean isCreator;
    private ClubMemberListAdapter adapter;
    private Dialog mDialog;
    private ClubApi mRetrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_member_list);
        ButterKnife.bind(this);
        initParam();
        initTopBar();
        initView();
        initData();
    }

    private void initParam() {
        clubId = getIntent().getStringExtra(EXTRA_CLUBID);
        isCreator = getIntent().getBooleanExtra(EXTRA_IS_CREATOR, false);
    }

    private void initTopBar() {
        showTopBar();
        QMUITopBar topBar = getTopBar();
        QMUIAlphaImageButton leftBackImageButton = topBar.addLeftBackImageButton();
        leftBackImageButton.setImageResource(R.drawable.topbar_back_btn);
        leftBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView titleTV = topBar.setTitle(getString(R.string.club_member));
        titleTV.setTextColor(Color.WHITE);

        //非创建者不显示管理图标
        if (!isCreator) return;

        Button rightButton = topBar.addRightTextButton(getString(R.string.manager), View.generateViewId());
        rightButton.setTextColor(Color.WHITE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setEdit(!adapter.isEdit());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.club_member_list_lv);
        adapter = new ClubMemberListAdapter(this, new ArrayList<ClubMemberEntity>());
        listView.setAdapter(adapter);
        adapter.setOnManagerClickListener(new ClubMemberListAdapter.OnManagerClickListener() {
            @Override
            public void manage(int userId, int type, int position) {
                setMemberRole(userId, type, position);
            }
        });

        mDialog = DialogUtils.createProgressDialog(this, null);
        mRetrofit = HttpUtils.createRetrofit(this, ClubApi.class);
    }

    private void initData() {
        requestMemberList();
    }

    private void requestMemberList() {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity<List<ClubMemberEntity>>> call = mRetrofit.getMemberList(body);
        call.enqueue(listCallback);
    }

    private Callback<BaseResponseEntity<List<ClubMemberEntity>>> listCallback = new Callback<BaseResponseEntity<List<ClubMemberEntity>>>() {
        @Override
        public void onResponse(Call<BaseResponseEntity<List<ClubMemberEntity>>> call, Response<BaseResponseEntity<List<ClubMemberEntity>>> response) {
            mDialog.dismiss();
            List<ClubMemberEntity> respList = response.body().getData();
            if (response.body() != null && respList != null && respList.size() > 0) {
                Collections.sort(respList, new Comparator<ClubMemberEntity>() {
                    @Override
                    public int compare(ClubMemberEntity o1, ClubMemberEntity o2) {
                        if (o1.getType() == 1) return 1;
                        if (o2.getType() == 1) return -1;
                        return o1.getType() - o2.getType();
                    }
                });
                adapter.addAll(respList);
            } else {
                ToastUtils.showLong("获取俱乐部成员信息失败。");
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity<List<ClubMemberEntity>>> call, Throwable t) {
            mDialog.dismiss();

        }
    };

    private void setMemberRole(int userId, int type, int position) {
        mDialog.show();

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("club_id", clubId);
        map.put("type", type);
        map.put("creator_id", AppCommonInfo.getUserid());
        map.put("position", position);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.setMemberRole(body);
        call.enqueue(setRoleCallback);

    }

    private Callback<BaseResponseEntity> setRoleCallback = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            mDialog.dismiss();
            if (response.body() != null) {
                switch (response.body().getStatus()) {
                    case 0:
                        ToastUtils.showShort(R.string.success);
                        requestMemberList();
                        break;
                    case 1:
                        ToastUtils.showShort(R.string.failuer);
                        break;
                    case 2:
                        ToastUtils.showShort(R.string.data_exception);
                        break;
                    case 3:
                        ToastUtils.showLong("只有管理员才能设置管理员");
                        break;
                }
            }

        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            mDialog.dismiss();
        }
    };

    public static Intent createActivity(Context context, String club_id, boolean isCreator) {
        Intent intent = new Intent(context, ClubMemberListActivity.class);
        intent.putExtra(EXTRA_CLUBID, club_id);
        intent.putExtra(EXTRA_IS_CREATOR, isCreator);
        return intent;
    }
}
