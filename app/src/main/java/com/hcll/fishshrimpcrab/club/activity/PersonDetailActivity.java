package com.hcll.fishshrimpcrab.club.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.entity.GenderEnum;
import com.hcll.fishshrimpcrab.club.entity.UserInfoEntity;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.HttpFileUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hong on 2018/3/5.
 */

public class PersonDetailActivity extends BaseActivity {

    private static final String REQUEST_CODE_CREATOR_ID = "creatorID";

    @BindView(R.id.person_detail_icon_iv)
    QMUIRadiusImageView iconIv;
    @BindView(R.id.person_detail_name_tv)
    TextView nameTv;
    @BindView(R.id.person_detail_sex_iv)
    ImageView sexIv;
    @BindView(R.id.person_detail_id_tv)
    TextView idTv;
    private int creatorId;
    private ClubApi retrofit;
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        ButterKnife.bind(this);
        initParam();
        initTopBar();

        retrofit = HttpUtils.createRetrofit(this, ClubApi.class);
        mDialog = DialogUtils.createProgressDialog(this, null);

        request();
    }

    private void request() {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", creatorId);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity<UserInfoEntity>> call = retrofit.getUserDetail(body);
        call.enqueue(new Callback<BaseResponseEntity<UserInfoEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseEntity<UserInfoEntity>> call, Response<BaseResponseEntity<UserInfoEntity>> response) {
                mDialog.dismiss();
                if (response.body() != null && response.body().isSuccessed()) {
                    UserInfoEntity entity = response.body().getData();
                    if (entity != null) {
                        HttpFileUtils.loadImage2View(PersonDetailActivity.this, entity.getHead_pic_name(), iconIv);
                        nameTv.setText(entity.getNick());
                        sexIv.setImageResource(GenderEnum.getDrawablebyId(entity.getSex()));
                        idTv.setText("ID:" + entity.getRandomNum());
                    }
                } else {
                    ToastUtils.showLong("获取用户信息失败！");
                }
            }

            @Override
            public void onFailure(Call<BaseResponseEntity<UserInfoEntity>> call, Throwable t) {
                mDialog.dismiss();
                ToastUtils.showLong("获取用户信息失败！");
            }
        });
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
        TextView titleTV = topBar.setTitle(R.string.title_person_detail);
        titleTV.setTextColor(Color.WHITE);
    }

    private void initParam() {
        creatorId = getIntent().getIntExtra(REQUEST_CODE_CREATOR_ID, 0);
    }

    public static Intent createAvtivity(Context context, int creatorID) {
        Intent intent = new Intent(context, PersonDetailActivity.class);
        intent.putExtra(REQUEST_CODE_CREATOR_ID, creatorID);
        return intent;
    }
}
