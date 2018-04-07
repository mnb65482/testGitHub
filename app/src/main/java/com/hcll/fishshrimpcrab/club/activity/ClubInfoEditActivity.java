package com.hcll.fishshrimpcrab.club.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
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
 * Created by hong on 2018/3/3.
 */

public class ClubInfoEditActivity extends BaseActivity {
    private static final String EXTAR_TITLE = "title";
    public static final String EXTAR_CONTENT = "content";
    public static final String EXTAR_UPDATE = "update";
    public static final String EXTAR_PARAM_KEY = "paramkey";
    public static final String EXTAR_CLUB_ID = "clubId";

    @BindView(R.id.club_info_edit_et)
    EditText clubInfoEditEt;
    private String title;
    private String content;
    private boolean isUpdate = false;
    private String clubId;
    private ClubApi mRetrofit;
    private Dialog mDialog;
    private String key;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_info_edit);
        ButterKnife.bind(this);
        initParam();
        initTopBar();
        initView();

        mRetrofit = HttpUtils.createRetrofit(this, ClubApi.class);
        mDialog = DialogUtils.createProgressDialog(this, null);
    }


    private void initParam() {
        title = getIntent().getStringExtra(EXTAR_TITLE);
        content = getIntent().getStringExtra(EXTAR_CONTENT);
        isUpdate = getIntent().getBooleanExtra(EXTAR_UPDATE, false);
        clubId = getIntent().getStringExtra(EXTAR_CLUB_ID);
        key = getIntent().getStringExtra(EXTAR_PARAM_KEY);

    }

    private void initTopBar() {
        showTopBar();
        QMUITopBar topBar = getTopBar();
        topBar.setTitle(title);
//        QMUIAlphaImageButton leftBackImageButton = topBar.addLeftBackImageButton();
//        leftBackImageButton.setImageResource(R.drawable.topbar_back_btn);
//        leftBackImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        TextView titleTV = topBar.setTitle(title);
//        titleTV.setTextColor(Color.WHITE);
        Button rightButton = topBar.addRightTextButton(getString(R.string.club_edit_save), View.generateViewId());
        rightButton.setTextColor(Color.WHITE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是修改信息页面，则请求
                if (isUpdate) {
                    requestUpdate();
                    return;
                }

                if (!StringUtils.isEmpty(clubInfoEditEt.getText())) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTAR_CONTENT, clubInfoEditEt.getText().toString());
                    ClubInfoEditActivity.this.setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showLong("请" + title);
                }
            }
        });
    }

    private void initView() {
        if (!StringUtils.isEmpty(content)) {
            clubInfoEditEt.setText(content);
        }
    }


    public static Intent createActivity(Context context, @NonNull String title, String content) {
        return createActivity(context, title, content, false, "", "");
    }

    public static Intent createActivity(Context context, @NonNull String title, String content, boolean isUpdate, String key, String clubId) {
        Intent intent = new Intent(context, ClubInfoEditActivity.class);
        intent.putExtra(EXTAR_TITLE, title);
        intent.putExtra(EXTAR_CONTENT, content);
        intent.putExtra(EXTAR_UPDATE, isUpdate);
        intent.putExtra(EXTAR_PARAM_KEY, key);
        intent.putExtra(EXTAR_CLUB_ID, clubId);
        return intent;
    }

    private void requestUpdate() {
        mDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        map.put("key", key);
        map.put("value", clubInfoEditEt.getText().toString());
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = mRetrofit.updateClub(body);
        call.enqueue(updateCallBack);
    }

    private Callback<BaseResponseEntity> updateCallBack = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            mDialog.dismiss();
            if (response.body() != null) {
                switch (response.body().getStatus()) {
                    case 0:
//                        ClubInfoEditActivity.this.setResult(RESULT_OK);
                        ToastUtils.showShort(R.string.club_update_Success);
                        ClubInfoEditActivity.this.finish();
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

}
