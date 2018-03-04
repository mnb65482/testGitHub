package com.hcll.fishshrimpcrab.club.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.entity.ClubEditType;
import com.hcll.fishshrimpcrab.club.entity.ClubDetailEntity;
import com.hcll.fishshrimpcrab.club.widget.ClubEditListItem;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.HttpFileUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zcw.togglebutton.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hong on 2018/3/4.
 */

public class ClubDetailActivity extends BaseActivity {
    private static final String TAG = ClubInfoEditActivity.class.getSimpleName();
    private static final String REQUEST_CODE_CLUBKEY = "clubkey";
    private static final int REQUEST_CODE_Modify = 112;

    @BindView(R.id.club_detail_bgimage_iv)
    ImageView imageIv;
    @BindView(R.id.club_detail_head_iv)
    QMUIRadiusImageView headIv;
    @BindView(R.id.club_detail_name_tv)
    TextView nameTv;
    @BindView(R.id.club_detail_id_tv)
    TextView idTv;
    @BindView(R.id.club_detail_creater_celi)
    ClubEditListItem creatorCeli;
    @BindView(R.id.club_detail_notinfo_celi)
    ClubEditListItem notinfoCeli;
    @BindView(R.id.club_detail_region_celi)
    ClubEditListItem regionCeli;
    @BindView(R.id.club_detail_member_celi)
    ClubEditListItem memberCeli;
    @BindView(R.id.club_detail_intro)
    TextView introTv;
    @BindView(R.id.club_detail_createtime_tv)
    TextView createtimeTv;
    @BindView(R.id.club_detail_exit_tv)
    TextView exitTv;
    @BindView(R.id.club_detail_name_celi)
    ClubEditListItem nameCeli;
    @BindView(R.id.club_detail_intro_celi)
    ClubEditListItem introCeli;
    @BindView(R.id.club_detail_intro_ll)
    LinearLayout introLl;
    private ClubApi mRetrofit;
    private Dialog mDialog;
    private String clubId;
    private QMUIDialog exitDialog;
    //是否为俱乐部创建者
    private boolean isCreator = false;
    private int creatorID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_club);
        ButterKnife.bind(this);
        initParam();
        initTopBar();
        initView();

        mRetrofit = HttpUtils.createRetrofit(this, ClubApi.class);
        mDialog = DialogUtils.createProgressDialog(this, null);
        request();
    }

    private void initView() {

        ToggleButton button = (ToggleButton) LayoutInflater.from(this).inflate(R.layout.view_comm_togglebtn, null);
        button.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                //todo 请求消息免打扰功能
                if (on) {
                    Log.w(TAG, "消息免打扰 ");
                } else {
                    Log.w(TAG, "取消消息免打扰 ");
                }
            }
        });

        notinfoCeli.setCustomView(button);
    }

    private void initParam() {
        clubId = getIntent().getStringExtra(REQUEST_CODE_CLUBKEY);
    }

    private void request() {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity<ClubDetailEntity>> call = mRetrofit.getClubDetail(body);
        call.enqueue(callback);
    }

    private void exitClubRequest() {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.exitClub(body);
        call.enqueue(exitClubCallBack);
    }

    private void dismissClubRequest() {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.dismissClub(body);
        call.enqueue(dismissCallback);
    }


    Callback<BaseResponseEntity<ClubDetailEntity>> callback = new Callback<BaseResponseEntity<ClubDetailEntity>>() {
        @Override
        public void onResponse(Call<BaseResponseEntity<ClubDetailEntity>> call, Response<BaseResponseEntity<ClubDetailEntity>> response) {
            mDialog.dismiss();
            if (response.body() != null) {
                ClubDetailEntity data = response.body().getData();
                fillView(data);
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity<ClubDetailEntity>> call, Throwable t) {
            mDialog.dismiss();
        }
    };

    Callback<BaseResponseEntity> dismissCallback = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            mDialog.dismiss();
            if (response.body() != null) {
                switch (response.body().getStatus()) {
                    case 0:
                        ToastUtils.showShort("解散成功！");
                        exitDialog.dismiss();
                        setResult(RESULT_OK);
                        ClubDetailActivity.this.finish();
                        break;
                    case 1:
                        ToastUtils.showShort("解散失败: " + response.body().getMsg());
                        break;
                    case 2:
                        ToastUtils.showShort("存在未结束的牌局！");
                        exitDialog.dismiss();
                        break;
                    case 5:
                        ToastUtils.showLong("只有创建者才可以解散战队!");
                        exitDialog.dismiss();
                    case 6:
                        ToastUtils.showShort("俱乐部不存在！");
                        exitDialog.dismiss();
                        break;
                }
            } else {
                ToastUtils.showShort("解散失败！");
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            mDialog.dismiss();
            ToastUtils.showShort("解散失败: " + t.getMessage());
        }
    };

    Callback<BaseResponseEntity> exitClubCallBack = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            mDialog.dismiss();
            if (response.body() != null) {
                switch (response.body().getStatus()) {
                    case 0:
                        ToastUtils.showShort("退出成功！");
                        exitDialog.dismiss();
                        setResult(RESULT_OK);
                        ClubDetailActivity.this.finish();
                        break;
                    case 1:
                        ToastUtils.showShort("退出失败: " + response.body().getMsg());
                        break;
                    case 6:
                        ToastUtils.showShort("俱乐部不存在！");
                        exitDialog.dismiss();
                        break;
                }
            } else {
                ToastUtils.showShort("退出失败！");
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            ToastUtils.showShort("退出失败！");
        }
    };

    private void fillView(ClubDetailEntity data) {
        if (data == null) return;

        if (data.getType() == 1) isCreator = true;
        creatorID = data.getCreatorID();

        HttpFileUtils.loadImage2View(this, data.getHeader(), headIv);
        nameTv.setText(data.getName());
        idTv.setText(String.format("ID:%S", data.getClubID()));
        creatorCeli.setDetail(data.getCreator());
        regionCeli.setDetail(data.getAreaID());
        memberCeli.setDetail(String.valueOf(data.getOnlineCount()));
        introTv.setText(StringUtils.isEmpty(data.getDesc()) ? "暂无简介" : data.getDesc());
        createtimeTv.setText("创建于" + TimeUtils.date2String(new Date(data.getCreateTime()),
                new SimpleDateFormat("yyyy-MM-dd")));

        String message = "确定要退出俱乐部？";
        String btnText = "退出俱乐部";

        if (isCreator) {
            nameCeli.setVisibility(View.VISIBLE);
            nameCeli.setDetail(data.getName());
            creatorCeli.showNextView();
            regionCeli.showNextView();
            introLl.setVisibility(View.GONE);
            introCeli.setVisibility(View.VISIBLE);
            introCeli.setDetail(StringUtils.isEmpty(data.getDesc()) ? "暂无简介" : data.getDesc());
            exitTv.setText("解散");

            message = "确定要解散俱乐部？";
            btnText = "解散俱乐部";

        }

        exitDialog = new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage(message)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, btnText, QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        if (isCreator) {
                            dismissClubRequest();
                        } else {
                            exitClubRequest();
                        }
                    }
                }).create();
    }


    private void initTopBar() {
        showTopBar();
        QMUITopBar topBar = getTopBar();
        QMUIAlphaImageButton leftBackImageButton = topBar.addLeftBackImageButton();
        leftBackImageButton.setImageResource(R.drawable.topbar_back_btn);
        leftBackImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = topBar.setTitle(getString(R.string.club_detail));
        title.setTextColor(Color.WHITE);
    }

    public static Intent createActivity(Context context, String clubKey) {
        Intent intent = new Intent(context, ClubDetailActivity.class);
        intent.putExtra(REQUEST_CODE_CLUBKEY, clubKey);
        return intent;
    }

    @OnClick({R.id.club_detail_name_celi, R.id.club_detail_creater_celi, R.id.club_detail_region_celi, R.id.club_detail_member_celi, R.id.club_detail_intro_celi, R.id.club_detail_exit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.club_detail_member_celi:
                break;
            case R.id.club_detail_exit_tv:
                exitDialog.show();
                break;
            case R.id.club_detail_creater_celi:
//                ToastUtils.showLong("创建者");
                startActivity(PersonDetailActivity.createAvtivity(this, creatorID));
                break;
        }

        if (isCreator) {
            switch (view.getId()) {
                case R.id.club_detail_intro_celi:
                    startActivityForResult(ClubInfoEditActivity.createActivity(this,
                            getString(R.string.club_edit_intro), introCeli.getDetail(),
                            true, ClubEditType.ClubDesc.getId(), clubId), REQUEST_CODE_Modify);
                    break;
                case R.id.club_detail_name_celi:
                    startActivityForResult(ClubInfoEditActivity.createActivity(this,
                            getString(R.string.club_edit_name), nameCeli.getDetail(),
                            true, ClubEditType.ClubName.getId(), clubId), REQUEST_CODE_Modify);
                    break;
                case R.id.club_detail_region_celi:
                    startActivityForResult(ClubInfoEditActivity.createActivity(this,
                            getString(R.string.club_edit_region), regionCeli.getDetail(),
                            true, ClubEditType.ClubRegion.getId(), clubId), REQUEST_CODE_Modify);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //进行详情字段修改时，出现setResult(RESULT_OK)不响应的问题，故先屏蔽调此判断
//        if (resultCode == RESULT_OK) return;
        if (requestCode == REQUEST_CODE_Modify) {
            request();
        }
    }
}
