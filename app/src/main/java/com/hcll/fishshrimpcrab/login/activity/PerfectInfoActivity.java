package com.hcll.fishshrimpcrab.login.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.http.entity.FileUploadEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.hcll.fishshrimpcrab.login.LoginApi;
import com.hcll.fishshrimpcrab.login.entity.UserInfoEntity;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.path;
import static com.hcll.fishshrimpcrab.login.activity.RegisterActivity.REQUEST_CODE_PERFECT;

public class PerfectInfoActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CAMERA = 103;
    private static final int REQUEST_CODE_SEX = 105;

    public static final String EXTRA_USER_ID = "userid";

    @BindView(R.id.perfect_topbar)
    QMUITopBar perfectTopbar;
    @BindView(R.id.perfect_camera_tv)
    TextView perfectCameraTv;
    @BindView(R.id.perfect_camera_iv)
    ImageView perfectCameraIv;
    @BindView(R.id.perfect_info_name)
    EditText perfectInfoName;
    @BindView(R.id.perfect_info_sex)
    EditText perfectInfoSex;
    @BindView(R.id.perfect_info_commit_tv)
    TextView perfectInfoCommitTv;
    private String imagePath;
    private LoginApi retrofit;
    private int userId;
    private Dialog dialog;
    private String picName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_perfect_info);
        ButterKnife.bind(this);
        initTopBar();
        userId = getIntent().getIntExtra(EXTRA_USER_ID, 0);

        retrofit = HttpUtils.createRetrofit(this, LoginApi.class);
        dialog = DialogUtils.createProgressDialog(this, null);

    }

    private void initTopBar() {
        perfectTopbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        QMUIAlphaImageButton backBtn = perfectTopbar.addLeftBackImageButton();
        backBtn.setImageResource(R.drawable.topbar_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_still, R.anim.slide_out_right);
            }
        });
        TextView title = perfectTopbar.setTitle(getString(R.string.title_perfect_info));
        title.setTextColor(Color.WHITE);
    }

    @OnClick({R.id.perfect_camera_iv, R.id.perfect_camera_tv, R.id.perfect_info_sex, R.id.perfect_info_commit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.perfect_camera_iv:
            case R.id.perfect_camera_tv:
                Intent cameraIntent = new Intent(this, ImageSelectActivity.class);
                startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
                break;
            case R.id.perfect_info_sex:
                Intent sexIntent = new Intent(this, SexSelectActivity.class);
                startActivityForResult(sexIntent, REQUEST_CODE_SEX);
                break;
            case R.id.perfect_info_commit_tv:
                if (checkNotNull()) {
                    dialog.show();

                    File file = new File(imagePath);
                    if (file.exists()) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type,image/jpg"), file);
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                        Call<BaseResponseEntity<FileUploadEntity>> call = retrofit.saveFile(filePart, userId);
                        call.enqueue(uploadCallback);
                    } else {
                        commit();
                    }
                }

                break;
        }
    }

    private void commit() {
        Map<String, Object> map = new HashMap<>();
        map.put("nick_name", perfectInfoName.getText().toString());
        if (!StringUtils.isEmpty(picName)) {
            map.put("head", picName);
        }
        map.put("sex", ((Integer) perfectInfoSex.getTag()));
        map.put("sign", "");
        map.put("user_id", userId);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = retrofit.updateUser(body);
        call.enqueue(updateCallback);
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
            ToastUtils.showLong(t.getMessage());
            commit();
        }
    };

    private Callback<BaseResponseEntity> updateCallback = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            dialog.dismiss();
            BaseResponseEntity entity = response.body();
            if (entity != null) {
                switch (entity.getStatus()) {
                    //更新信息成功
                    case 0:
//                        Object data = entity.getData();
//                        if (data instanceof UserInfoEntity) {
//                            UserInfoEntity userinfo = (UserInfoEntity) data;
//                            Intent intent = PerfectInfoActivity.createActivity(RegisterActivity.this, userinfo.getUserid());
//                            startActivityForResult(intent, REQUEST_CODE_PERFECT);
//                        }

                        break;
                    //昵称重复
                    case 1:
                        ToastUtils.showLong(R.string.perfect_info_repeat);
                        break;
                    //暂无该用户
                    case 2:
                        ToastUtils.showLong(R.string.perfect_info_nouser);
                        break;
                    //钻石不足
                    case 3:
                        ToastUtils.showLong(R.string.perfect_info_nodiomand);
                        break;
                    //失败
                    case 4:
                        ToastUtils.showLong(R.string.perfect_info_faiure);
                        break;
                    //包含非法字符
                    case 5:
                        ToastUtils.showLong(R.string.perfect_info_illegal);
                        break;
                    default:
                        break;
                }
            }

        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            dialog.dismiss();
            ToastUtils.showLong(t.getMessage());
        }
    };


    private boolean checkNotNull() {
        if (StringUtils.isEmpty(perfectInfoName.getText())) {
            ToastUtils.showLong("请填写昵称！");
            return false;
        }
        if (perfectInfoSex.getTag() == null) {
            ToastUtils.showLong("请选择性别！");
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CODE_CAMERA) {
            imagePath = data.getStringExtra(ImageSelectActivity.EXTRA_CAMERA);
            perfectCameraTv.setVisibility(View.GONE);
            perfectCameraIv.setVisibility(View.VISIBLE);
            perfectCameraIv.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        } else if (requestCode == REQUEST_CODE_SEX) {
            int sex = data.getIntExtra(SexSelectActivity.EXTRA_SEX_SELECT, 0);
            perfectInfoSex.setText(SexSelectActivity.map.get(sex));
            perfectInfoSex.setTag(sex);
        }
    }

    public static Intent createActivity(Context context, int userid) {
        Intent intent = new Intent(context, PerfectInfoActivity.class);
        intent.putExtra(EXTRA_USER_ID, userid);
        return intent;
    }
}
