package com.hcll.fishshrimpcrab.club.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.entity.ClubInfoEntity;
import com.hcll.fishshrimpcrab.club.widget.ClubEditListItem;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.http.entity.FileUploadEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.HttpFileUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.hcll.fishshrimpcrab.login.activity.ImageSelectActivity;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;

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
 * Created by hong on 2018/3/3.
 */

public class CreateClubActivity extends BaseActivity {

    private static final int REQUEST_CODE_CAMERA = 107;
    private static final int REQUEST_CODE_NAME = 109;
    private static final int REQUEST_CODE_INTRO = 111;
    private static final int REQUEST_CODE_REGION = 113;

    @BindView(R.id.club_create_bgimage_iv)
    ImageView clubCreateBgimageIv;
    @BindView(R.id.club_create_icon)
    QMUIRadiusImageView clubCreateIcon;
    @BindView(R.id.club_create_name_celi)
    ClubEditListItem clubCreateNameCeli;
    @BindView(R.id.club_create_region_celi)
    ClubEditListItem clubCreateRegionCeli;
    @BindView(R.id.club_create_intro_celi)
    ClubEditListItem clubCreateIntroCeli;
    @BindView(R.id.club_create_fee_tv1)
    TextView clubCreateFeeTv1;
    @BindView(R.id.club_create_fee_tv2)
    TextView clubCreateFeeTv2;
    @BindView(R.id.club_create_sure_tv)
    TextView clubCreateSureTv;
    private String imagePath;

    private String name;
    private String region;
    private String intro;
    private Dialog mDialog;
    private String picName;
    private ClubApi retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        ButterKnife.bind(this);
        initTopBar();
        initView();
    }

    private void initView() {
        clubCreateFeeTv1.setText(AppCommonInfo.clubCreateFee);
        clubCreateFeeTv2.setText(String.valueOf(AppCommonInfo.getUserDiamonds()));
        mDialog = DialogUtils.createProgressDialog(this, null);
        retrofit = HttpUtils.createRetrofit(this, ClubApi.class);
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
        TextView title = topBar.setTitle(getString(R.string.create_to_club));
        title.setTextColor(Color.WHITE);
    }

    public static Intent createActivity(Context context) {
        return new Intent(context, CreateClubActivity.class);
    }

    @OnClick({R.id.club_create_icon, R.id.club_create_name_celi, R.id.club_create_region_celi, R.id.club_create_intro_celi, R.id.club_create_sure_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.club_create_icon:
                Intent cameraIntent = new Intent(this, ImageSelectActivity.class);
                startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                break;
            case R.id.club_create_name_celi:
                startActivityForResult(ClubInfoEditActivity.createActivity(this,
                        getString(R.string.club_edit_name), name), REQUEST_CODE_NAME);
                break;
            case R.id.club_create_region_celi:
                startActivityForResult(ClubInfoEditActivity.createActivity(this,
                        getString(R.string.club_edit_region), region), REQUEST_CODE_REGION);
                break;
            case R.id.club_create_intro_celi:
                startActivityForResult(ClubInfoEditActivity.createActivity(this,
                        getString(R.string.club_edit_intro), intro), REQUEST_CODE_INTRO);
                break;
            case R.id.club_create_sure_tv:
                save();
                break;
        }
    }

    private void save() {
        if (checkNotNull()) {
            mDialog.show();
            if (!StringUtils.isEmpty(imagePath)) {
                HttpFileUtils.uploadImage(this, imagePath, uploadCallback);
            } else {
                commit();
            }
        }
    }

    private void commit() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        if (!StringUtils.isEmpty(picName)) {
            map.put("club_head", picName);
        }
        map.put("club_name", name);
        map.put("area_id", region);
        map.put("desc", intro);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity<ClubInfoEntity>> call = retrofit.createClub(body);
        call.enqueue(commitCallback);
    }

    private Callback<BaseResponseEntity<FileUploadEntity>> uploadCallback = new Callback<BaseResponseEntity<FileUploadEntity>>() {
        @Override
        public void onResponse(Call<BaseResponseEntity<FileUploadEntity>> call, Response<BaseResponseEntity<FileUploadEntity>> response) {
            BaseResponseEntity<FileUploadEntity> body = response.body();
            if (body != null && body.isSuccessed()) {
                FileUploadEntity data = body.getData();
                picName = data.getPic_name();
            } else {
                if (response.body() != null) {
                    ToastUtils.showLong(response.body().getMsg());
                } else {
                    ToastUtils.showLong("图片上传失败！");
                }
            }
            commit();
        }

        @Override
        public void onFailure(Call<BaseResponseEntity<FileUploadEntity>> call, Throwable t) {
            ToastUtils.showLong("图片上传失败！");
        }
    };

    private Callback<BaseResponseEntity<ClubInfoEntity>> commitCallback = new Callback<BaseResponseEntity<ClubInfoEntity>>() {
        @Override
        public void onResponse(Call<BaseResponseEntity<ClubInfoEntity>> call, Response<BaseResponseEntity<ClubInfoEntity>> response) {
            mDialog.dismiss();
            BaseResponseEntity body = response.body();
            if (body != null && body.isSuccessed()) {
                if (body.getData() instanceof ClubInfoEntity) {
                    ClubInfoEntity entity = (ClubInfoEntity) body.getData();
                    AppCommonInfo.setUserDiamonds(entity.getIdou());
                    ToastUtils.showShort(R.string.create_to_club_success);
                    setResult(RESULT_OK);
                    CreateClubActivity.this.finish();
                }
            } else {
                if (body != null) {
                    ToastUtils.showLong(body.getMsg());
                } else {
                    ToastUtils.showLong(R.string.create_to_club_failuer);
                }
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity<ClubInfoEntity>> call, Throwable t) {
            mDialog.dismiss();
            ToastUtils.showLong(R.string.create_to_club_failuer);
        }
    };


    private boolean checkNotNull() {
        if (StringUtils.isEmpty(clubCreateNameCeli.getDetail())) {
            ToastUtils.showLong("请" + getString(R.string.club_edit_name));
            return false;
        }

        if (StringUtils.isEmpty(clubCreateRegionCeli.getDetail())) {
            ToastUtils.showLong("请" + getString(R.string.club_edit_region));
            return false;
        }

        if (StringUtils.isEmpty(clubCreateIntroCeli.getDetail())) {
            ToastUtils.showLong("请" + getString(R.string.club_edit_intro));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                imagePath = data.getStringExtra(ImageSelectActivity.EXTRA_CAMERA);
                clubCreateIcon.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                break;
            case REQUEST_CODE_NAME:
                name = data.getStringExtra(ClubInfoEditActivity.EXTAR_CONTENT);
                clubCreateNameCeli.setDetail(name);
                break;
            case REQUEST_CODE_INTRO:
                intro = data.getStringExtra(ClubInfoEditActivity.EXTAR_CONTENT);
                clubCreateIntroCeli.setDetail(intro);
                break;
            case REQUEST_CODE_REGION:
                region = data.getStringExtra(ClubInfoEditActivity.EXTAR_CONTENT);
                clubCreateRegionCeli.setDetail(region);
                break;
        }

    }
}
