package com.hcll.fishshrimpcrab.club.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.entity.ClubDetailEntity;
import com.hcll.fishshrimpcrab.club.enums.ClubEditType;
import com.hcll.fishshrimpcrab.club.widget.ClubEditListItem;
import com.hcll.fishshrimpcrab.club.widget.UpdateMaxDialog;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.http.entity.FileUploadEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.HttpFileUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.hcll.fishshrimpcrab.login.activity.ImageSelectActivity;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zcw.togglebutton.CustomToggleButton;

import java.io.File;
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
    private static final int REQUEST_CODE_CAMERA = 121;

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
    @BindView(R.id.club_detail_update_celi)
    ClubEditListItem updateCeli;
    private ClubApi mRetrofit;
    private Dialog mDialog;
    private String clubId;
    private QMUIDialog exitDialog;
    //是否为俱乐部创建者
    private boolean isCreator = false;
    private int creatorID;
    private CustomToggleButton pushButton;
    private String imagePath;
    private int currentCount;
    private UpdateMaxDialog updateMaxDialog;

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

        pushButton = (CustomToggleButton) LayoutInflater.from(this).inflate(R.layout.view_comm_togglebtn, null);
        pushButton.setOnToggleChanged(new CustomToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    pushRequest(1);
                } else {
//                    Log.w(TAG, "取消消息免打扰 ");
                    pushRequest(0);
                }
            }
        });

        notinfoCeli.setCustomView(pushButton);
    }

    private void initParam() {
        clubId = getIntent().getStringExtra(REQUEST_CODE_CLUBKEY);
    }

    /**
     * 请求俱乐部详情信息
     */
    private void request() {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity<ClubDetailEntity>> call = mRetrofit.getClubDetail(body);
        call.enqueue(callback);
    }

    /**
     * 退出俱乐部
     */
    private void exitClubRequest() {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.exitClub(body);
        call.enqueue(exitClubCallBack);
    }

    /**
     * 解散俱乐部
     */
    private void dismissClubRequest() {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.dismissClub(body);
        call.enqueue(dismissCallback);
    }

    /**
     * 设置推送请求
     *
     * @param state
     */
    private void pushRequest(int state) {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        map.put("push_status", state);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.setPush(body);
        call.enqueue(pushCallBack);
    }

    /**
     * 上传头像图片
     *
     * @param imagePath
     */
    private void uploadImage(String imagePath) {
        mDialog.show();
        File file = new File(imagePath);
        if (file.exists()) {
            HttpFileUtils.uploadImage(this, imagePath, uploadCallback);
        }
    }

    /**
     * 更新俱乐部信息(更新头像信息)
     *
     * @param header
     */
    private void requestUpdate(String header) {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        map.put("key", "header");
        map.put("value", header);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.updateClub(body);
        call.enqueue(updateCallBack);
    }

    /**
     * 升级人数
     */
    private void updateCount(int num, int idou) {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        map.put("idou", String.valueOf(idou));
        map.put("num", num);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.updateMax(body);
        call.enqueue(updateMaxCallBack);
    }


    /**
     * 请求详情 callback
     */
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

    /**
     * 解散俱乐部
     */
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

    /**
     * 重置免打扰按钮
     */
    private void resetPushButton() {
        if (pushButton.isToggleOn()) {
            pushButton.setToggleOff();
        } else {
            pushButton.setToggleOn();
        }
    }

    /**
     * 退出俱乐部callback
     */
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

    /**
     * 设置是否免打扰 回调类
     */
    Callback<BaseResponseEntity> pushCallBack = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            mDialog.dismiss();
            BaseResponseEntity body = response.body();
            if (body != null) {
                switch (body.getStatus()) {
                    case 0:
                        ToastUtils.showLong(R.string.success);
                        break;
                    case 1:
                        ToastUtils.showLong(R.string.failuer);
                        resetPushButton();
                        break;
                    case 2:
                        ToastUtils.showLong(R.string.system_erro);
                        resetPushButton();
                        break;
                    case 3:
                        ToastUtils.showLong("玩家不在该俱乐部！");
                        resetPushButton();
                        break;
                }
            } else {
                ToastUtils.showLong("设置失败！");

            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            mDialog.dismiss();
            ToastUtils.showLong("设置失败！");
            resetPushButton();
        }
    };

    /**
     * 上传图片回调
     */
    private Callback<BaseResponseEntity<FileUploadEntity>> uploadCallback = new Callback<BaseResponseEntity<FileUploadEntity>>() {
        @Override
        public void onResponse(Call<BaseResponseEntity<FileUploadEntity>> call, Response<BaseResponseEntity<FileUploadEntity>> response) {
            mDialog.dismiss();
            BaseResponseEntity<FileUploadEntity> body = response.body();
            if (body != null && body.isSuccessed()) {
                FileUploadEntity data = body.getData();
                String picName = data.getPic_name();
                requestUpdate(picName);

            } else {
                if (response.body() != null) {
                    ToastUtils.showLong(response.body().getMsg());
                } else {
                    ToastUtils.showLong("图片上传失败！");
                }
            }

        }

        @Override
        public void onFailure(Call<BaseResponseEntity<FileUploadEntity>> call, Throwable t) {

        }
    };

    /**
     * 更新头像
     */
    private Callback<BaseResponseEntity> updateCallBack = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            mDialog.dismiss();
            if (response.body() != null) {
                switch (response.body().getStatus()) {
                    case 0:
                        ToastUtils.showShort(R.string.club_update_Success);
                        headIv.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                        break;
                    case 1:
                        ToastUtils.showShort(getString(R.string.club_update_failuer) + response.body().getMsg());
                        break;
                    case 2:
                        ToastUtils.showShort(R.string.system_erro);
                        break;
                    case 5:
                        ToastUtils.showLong(R.string.club_repeat);
                    case 6:
                        ToastUtils.showShort(R.string.club_not_exitence);
                        break;
                }
            } else {
                ToastUtils.showShort(R.string.club_update_failuer);
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            mDialog.dismiss();
            ToastUtils.showShort(getString(R.string.club_update_failuer) + t.getMessage());
        }
    };

    /**
     * 增加上限制人数
     */
    private Callback<BaseResponseEntity> updateMaxCallBack = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            mDialog.dismiss();
            if (response.body() != null) {
                switch (response.body().getStatus()) {
                    case 0:
                        ToastUtils.showShort(R.string.update_max_success);
                        updateMaxDialog.dismiss();
                        request();
                        break;
                    case 1:
                        ToastUtils.showShort(getString(R.string.update_max_failuer));
                        break;
                    case 2:
                        ToastUtils.showShort(R.string.system_erro);
                        break;
                    case 3:
                        ToastUtils.showLong(R.string.update_max_request_erro);
                    case 4:
                        ToastUtils.showShort(R.string.update_max_not_idou);
                        break;
                    case 5:
                        ToastUtils.showLong(R.string.update_max_too_much);
                        break;
                }
            } else {
                ToastUtils.showShort(R.string.club_update_failuer);
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            mDialog.dismiss();
            ToastUtils.showShort(getString(R.string.club_update_failuer) + t.getMessage());
        }
    };


    private void fillView(ClubDetailEntity data) {
        if (data == null) return;

        if (data.getType() == 1) isCreator = true;
        creatorID = data.getCreatorID();
        currentCount = data.getTotalCount();

        HttpFileUtils.loadImage2View(this, data.getHeader(), R.drawable.club_empty_info, headIv);
        nameTv.setText(data.getName());
        String showId = data.getShowId();
        if (showId.contains("_")) {
            String substring = showId.substring(showId.indexOf("_") + 1, showId.length());
            idTv.setText(String.format("ID:%S", substring));
        } else {
            idTv.setText(String.format("ID:%S", showId));
        }


        creatorCeli.setDetail(data.getCreator());
        regionCeli.setDetail(data.getAreaID());
        memberCeli.setDetail(String.valueOf(data.getOnlineCount()));
        introTv.setText(StringUtils.isEmpty(data.getDesc()) ? "暂无简介" : data.getDesc());

        createtimeTv.setText("创建于" + TimeUtils.getFriendlyTimeSpanByNow(data.getCreateTime() * 1000));
        if (data.getPushStatus() == 1) {
            pushButton.setToggleOn();
        } else {
            pushButton.setToggleOff();
        }


        String message = getString(R.string.sure_exit_club);
        String btnText = getString(R.string.exit_club);

        //创建者  设置字段可修改
        if (isCreator) {
            nameCeli.setVisibility(View.VISIBLE);
            nameCeli.setDetail(data.getName());
            creatorCeli.showNextView();
            regionCeli.showNextView();
            introLl.setVisibility(View.GONE);
            introCeli.setVisibility(View.VISIBLE);
            introCeli.setDetail(StringUtils.isEmpty(data.getDesc()) ? "暂无简介" : data.getDesc());
            exitTv.setText(getString(R.string.dismiss));

            //当前为创建者的话，可升级人数
            updateCeli.setVisibility(View.VISIBLE);
            updateCeli.setDetail(data.getOnlineCount() + "/" + data.getTotalCount());
//            TextView detailView = updateCeli.getDetailView();
//            detailView.setText(String.valueOf(data.getIdou()));
//            Drawable drawable = getResources().getDrawable(R.drawable.comm_diamonds_ic);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            detailView.setCompoundDrawables(drawable, null, null, null);

            message = getString(R.string.sure_dismiss_club);
            btnText = getString(R.string.dismiss_club);

        }

        exitDialog = new QMUIDialog.MessageDialogBuilder(this)
                .setTitle(getString(R.string.prompt))
                .setMessage(message)
                .addAction(getString(R.string.cancel), new QMUIDialogAction.ActionListener() {
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

        if (data.getType() != 1 && data.getType() != 2 && data.getType() != 4) {
            exitTv.setVisibility(View.GONE);
        }

    }


    private void initTopBar() {
        showTopBar();
        getTopBar().setTitle(getString(R.string.club_detail));
    }

    public static Intent createActivity(Context context, String clubKey) {
        Intent intent = new Intent(context, ClubDetailActivity.class);
        intent.putExtra(REQUEST_CODE_CLUBKEY, clubKey);
        return intent;
    }

    @OnClick({R.id.club_detail_name_celi, R.id.club_detail_creater_celi, R.id.club_detail_region_celi, R.id.club_detail_member_celi, R.id.club_detail_intro_celi, R.id.club_detail_exit_tv, R.id.club_detail_head_iv, R.id.club_detail_update_celi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.club_detail_member_celi:
                startActivityForResult(ClubMemberListActivity.createActivity(this, clubId, isCreator), REQUEST_CODE_Modify);
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
                case R.id.club_detail_head_iv:
                    Intent cameraIntent = new Intent(this, ImageSelectActivity.class);
                    startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                    break;

                case R.id.club_detail_update_celi:
                    updateMaxDialog = new UpdateMaxDialog(this, currentCount);
                    updateMaxDialog.setOnUpdateCallBack(new UpdateMaxDialog.OnUpdateCallBack() {
                        @Override
                        public void update(int num, int idou) {
                            updateCount(num, idou);
                        }
                    });
                    updateMaxDialog.show();

                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //进行详情字段修改时，出现setResult(RESULT_OK)不响应的问题，故先屏蔽调此判断
//        if (resultCode == RESULT_OK) return;
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            imagePath = data.getStringExtra(ImageSelectActivity.EXTRA_CAMERA);
            uploadImage(imagePath);
//            perfectCameraIv.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }

        if (requestCode == REQUEST_CODE_Modify) {
            request();
        }
    }
}
