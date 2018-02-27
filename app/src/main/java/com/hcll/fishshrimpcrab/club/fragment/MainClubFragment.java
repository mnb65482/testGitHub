package com.hcll.fishshrimpcrab.club.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
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

public class MainClubFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = MainClubFragment.class.getSimpleName();


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

        requestClubList();


//        //设置可见性
//        clubContentSv.setVisibility(View.VISIBLE);
//        clubEmptyLl.setVisibility(View.GONE);
//
//        resetDynamicView();
//
//        List<ClubInfoEntity.ListBean> list = new ArrayList<>();
//        ClubInfoEntity.ListBean bean1 = new ClubInfoEntity.ListBean();
//        bean1.setHeader("0001100199991929371519628269733");
//        bean1.setAreaID("香港");
//        bean1.setName("休闲娱乐来一个");
//        bean1.setStatus(22);
//        bean1.setTotalCount(500);
//        bean1.setOnlineCount(10);
//        bean1.setType(2);
//
//
//        ClubInfoEntity.ListBean bean2 = new ClubInfoEntity.ListBean();
//        bean2.setHeader("0001100199991929371519628269733");
//        bean2.setAreaID("香港");
//        bean2.setName("休闲娱乐来一个");
//        bean2.setStatus(22);
//        bean2.setTotalCount(500);
//        bean2.setOnlineCount(10);
//        bean2.setType(2);
//
//        ClubInfoEntity.ListBean bean3 = new ClubInfoEntity.ListBean();
//        bean3.setHeader("0001100199991929371519628269733");
//        bean3.setAreaID("香港");
//        bean3.setName("休闲娱乐来一个");
//        bean3.setStatus(22);
//        bean3.setTotalCount(500);
//        bean3.setOnlineCount(10);
//        bean3.setType(2);
//
//        ClubInfoEntity.ListBean bean4 = new ClubInfoEntity.ListBean();
//        bean4.setHeader("0001100199991929371519628269733");
//        bean4.setAreaID("香港");
//        bean4.setName("休闲娱乐来一个");
//        bean4.setStatus(22);
//        bean4.setTotalCount(500);
//        bean4.setOnlineCount(10);
//        bean4.setType(2);
//
//
//        ClubInfoEntity.ListBean bean5 = new ClubInfoEntity.ListBean();
//        bean5.setHeader("0001100199991929371519628269733");
//        bean5.setAreaID("香港");
//        bean5.setName("休闲娱乐来一个");
//        bean5.setStatus(22);
//        bean5.setTotalCount(500);
//        bean5.setOnlineCount(10);
//        bean5.setType(2);
//
//        ClubInfoEntity.ListBean bean6 = new ClubInfoEntity.ListBean();
//        bean6.setHeader("0001100199991929371619628269733");
//        bean6.setAreaID("香港");
//        bean6.setName("休闲娱乐来一个");
//        bean6.setStatus(22);
//        bean6.setTotalCount(600);
//        bean6.setOnlineCount(10);
//        bean6.setType(2);
//
//        ClubInfoEntity.ListBean bean7 = new ClubInfoEntity.ListBean();
//        bean7.setHeader("0001100199991929371719628269733");
//        bean7.setAreaID("香港");
//        bean7.setName("休闲娱乐来一个");
//        bean7.setStatus(22);
//        bean7.setTotalCount(700);
//        bean7.setOnlineCount(10);
//        bean7.setType(2);
//
//        ClubInfoEntity.ListBean bean8 = new ClubInfoEntity.ListBean();
//        bean8.setHeader("0001100199991929371819628269733");
//        bean8.setAreaID("香港");
//        bean8.setName("休闲娱乐来一个");
//        bean8.setStatus(22);
//        bean8.setTotalCount(800);
//        bean8.setOnlineCount(10);
//        bean8.setType(2);
//
//        list.add(bean1);
//        list.add(bean2);
//        list.add(bean3);
//        list.add(bean4);
//        list.add(bean5);
//        list.add(bean6);
//        list.add(bean7);
//        list.add(bean8);
//
//
//        for (ClubInfoEntity.ListBean listBean : list) {
//            ClubItemView clubItemView = new ClubItemView(getContext());
//            clubItemView.init(listBean);
//            if (listBean.getType() == 1) {
//                if (clubMyTv.getVisibility() != View.VISIBLE) {
//                    clubMyTv.setVisibility(View.VISIBLE);
//                }
//                clubMyLl.addView(clubItemView);
//            } else {
//                if (clubJoinTv.getVisibility() != View.VISIBLE) {
//                    clubJoinTv.setVisibility(View.VISIBLE);
//                }
//                clubJoinLl.addView(clubItemView);
//            }
//        }

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
                ToastUtils.showLong("创建俱乐部！");
            }
        });

        joinTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                ToastUtils.showLong("加入俱乐部！");
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

                        List<ClubInfoEntity.ListBean> list = data.getList();
                        for (ClubInfoEntity.ListBean listBean : list) {
                            ClubItemView clubItemView = new ClubItemView(getContext());
                            clubItemView.init(listBean);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initTopBar() {
        QMUITopBar topBar = getTopBar();
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


        Log.e(TAG, "setUserVisibleHint: " + isVisibleToUser);
    }
}
