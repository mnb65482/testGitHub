package com.hcll.fishshrimpcrab.club.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseFragment;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.activity.ClubDetailActivity;
import com.hcll.fishshrimpcrab.club.activity.ClubGameListActivity;
import com.hcll.fishshrimpcrab.club.activity.ClubMessageActivity;
import com.hcll.fishshrimpcrab.club.activity.CreateClubActivity;
import com.hcll.fishshrimpcrab.club.activity.JoinClubActivity;
import com.hcll.fishshrimpcrab.club.entity.ClubInfoEntity;
import com.hcll.fishshrimpcrab.club.widget.ClubItemView;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;

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
 * Created by hong on 2018/2/23.
 */

public class MainClubFragment extends BaseFragment implements View.OnClickListener, ClubItemView.OnItemClickListener {

    private static final String TAG = MainClubFragment.class.getSimpleName();
    private static final int REQUEST_CODE_CREATE = 105;

    @BindView(R.id.main_club_empty_ll)
    LinearLayout clubEmptyLl;
    @BindView(R.id.main_club_my_tv)
    TextView clubMyTv;
    @BindView(R.id.main_club_my_ll)
    LinearLayout clubMyLl;
    @BindView(R.id.main_club_join_tv)
    TextView clubJoinTv;
    @BindView(R.id.main_club_join_ll)
    LinearLayout clubJoinLl;
    @BindView(R.id.main_club_content_sv)
    ScrollView clubContentSv;

    private int topBarId;
    private Dialog dialog;
    private ClubApi retrofit;
    private QMUIAlphaImageButton rightImageButton;
    private PopupWindow popupWindow;

    @Override
    public void onInitView() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View fragView = getFragView();
        ButterKnife.bind(this, fragView);
        initView();
        return view;

    }

    private void initView() {
        initTopBar();
        initPopup();

        dialog = DialogUtils.createProgressDialog(getContext(), null);
        retrofit = HttpUtils.createRetrofit(getContext(), ClubApi.class);
    }

    private void initPopup() {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.view_club_popup, null);
        TextView createTv = (TextView) popupView.findViewById(R.id.club_popup_create_tv);
        TextView joinTv = (TextView) popupView.findViewById(R.id.club_popup_join_tv);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 点击其他区域关闭Pw，必须给pw设置背景
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置让pw获得焦点
        popupWindow.setFocusable(true);

        createTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivityForResult(CreateClubActivity.createActivity(getContext()), REQUEST_CODE_CREATE);
            }
        });

        joinTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startActivityForResult(new Intent(getContext(), JoinClubActivity.class), REQUEST_CODE_CREATE);
            }
        });
    }

    /**
     * 发送请求
     */
    private void requestClubList() {
        dialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        RequestBody requestBody = JsonUtils.createJsonRequestBody(map);

        Call<BaseResponseEntity<ClubInfoEntity>> call = retrofit.getClubList(requestBody);
        call.enqueue(new Callback<BaseResponseEntity<ClubInfoEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseEntity<ClubInfoEntity>> call, Response<BaseResponseEntity<ClubInfoEntity>> response) {
                dialog.dismiss();
                BaseResponseEntity<ClubInfoEntity> body = response.body();
                if (body.isSuccessed()) {
                    ClubInfoEntity data = body.getData();
                    if (data != null && data.getList().size() > 0) {
                        //设置可见性
                        clubContentSv.setVisibility(View.VISIBLE);
                        clubEmptyLl.setVisibility(View.GONE);
                        //重置控件
                        resetDynamicView();

                        //动态加载控件
                        List<ClubInfoEntity.ListBean> list = data.getList();
                        for (ClubInfoEntity.ListBean listBean : list) {
                            ClubItemView clubItemView = new ClubItemView(getContext());
                            clubItemView.init(listBean);
                            clubItemView.setOnItemClickListener(MainClubFragment.this);
                            if (listBean.getType() == 1) {
                                if (clubMyTv.getVisibility() != View.VISIBLE) {
                                    clubMyTv.setVisibility(View.VISIBLE);
                                }
                                clubMyLl.addView(clubItemView);
                            } else {
                                if (clubJoinTv.getVisibility() != View.VISIBLE) {
                                    clubJoinTv.setVisibility(View.VISIBLE);
                                }
                                clubJoinLl.addView(clubItemView);
                            }
                        }
                    } else {
                        clubContentSv.setVisibility(View.GONE);
                        clubEmptyLl.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseEntity<ClubInfoEntity>> call, Throwable t) {
                dialog.dismiss();
                ToastUtils.showLong(t.getMessage());

            }
        });

    }

    /**
     * 重置动态控件
     */
    public void resetDynamicView() {
        //把动态控件全部移除
        clubMyLl.removeAllViews();
        clubJoinLl.removeAllViews();

        clubMyTv.setVisibility(View.GONE);
        clubJoinTv.setVisibility(View.GONE);
    }

    private void initTopBar() {
        QMUITopBar topBar = getTopBar();
        QMUIAlphaImageButton leftBackImageButton = topBar.addLeftBackImageButton();
        leftBackImageButton.setImageResource(R.drawable.comm_message_ic);
        leftBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ClubMessageActivity.class));
            }
        });
        topBarId = View.generateViewId();
        rightImageButton = topBar.addRightImageButton(R.drawable.main_add_club_ic, topBarId);
        rightImageButton.setOnClickListener(this);
        TextView title = topBar.setTitle(getString(R.string.tab2));
        title.setTextColor(Color.WHITE);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_main_club;
    }

    @Override
    public void onClick(View view) {
        if (topBarId == view.getId()) {
            // 使pw显示在“+”的下方
            popupWindow.showAsDropDown(rightImageButton, -180, 0);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        if (isVisibleToUser) {
            requestClubList();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //由于先跳转到游戏列表，在跳转到详情列表，详情列表可对俱乐部信息进行编辑，需刷新。
//        requestClubList();
//        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_CODE_CREATE) {
            requestClubList();
        }

    }

    @Override
    public void clickItem(ClubInfoEntity.ListBean listBean) {
//        startActivityForResult(ClubDetailActivity.createActivity(getContext(), listBean.getId()), REQUEST_CODE_CREATE);
        startActivityForResult(ClubGameListActivity.createActivity(getContext(), listBean.getId(), listBean.getName()), REQUEST_CODE_CREATE);
    }
}
