package com.hcll.fishshrimpcrab.login.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.utils.FileUtils;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;


public class ImageSelectActivity extends BaseTransparentActivity implements TakePhoto.TakeResultListener, InvokeListener {

    private static final String TAG = ImageSelectActivity.class.getSimpleName();

    private static final int REQUEST_CODE_CAMERA = 101;
    public static final String EXTRA_CAMERA = "imagePath";
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;
    private String caropCacheFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        configCompress(getTakePhoto());
        configTakePhotoOption(getTakePhoto());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected String getFirstBtnName() {
        return getString(R.string.take_photo);
    }

    @Override
    protected String getSecondBtnName() {
        return getString(R.string.select_system_image);
    }

    @Override
    protected void firstBtnClick() {
        File file = new File(FileUtils.createCaptureFilePath());
        Uri imageUri = Uri.fromFile(file);
        getTakePhoto().onPickFromCaptureWithCrop(imageUri, getCropOptions());
    }

    @Override
    protected void secondBtnClick() {
        caropCacheFilePath = FileUtils.createCaptureFilePath();
        File file = new File(caropCacheFilePath);
        Uri imageUri = Uri.fromFile(file);
        getTakePhoto().onPickFromDocumentsWithCrop(imageUri, getCropOptions());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }


    @Override
    public void takeSuccess(TResult result) {

        Log.w(TAG, "takeSuccess: OriginalPath = " + result.getImage().getOriginalPath());
        Log.w(TAG, "takeSuccess: CompressPath = " + result.getImage().getCompressPath());

        if (!StringUtils.isEmpty(caropCacheFilePath)) {
            File file = new File(caropCacheFilePath);
            if (file.exists()) {
                FileUtils.deleteFile(file);
            }
        }


        Intent intent = new Intent();
        intent.putExtra(EXTRA_CAMERA, result.getImage().getCompressPath());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(com.jph.takephoto.R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        //使用TakePhoto自带相册
        builder.setWithOwnGallery(false);
        //是否纠正拍照旋转角度
        builder.setCorrectImage(false);
        takePhoto.setTakePhotoOptions(builder.create());

    }

    private void configCompress(TakePhoto takePhoto) {
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(AppCommonInfo.maxSize)
                .setMaxPixel(AppCommonInfo.maxPixel)
                .enableReserveRaw(false)//压缩后是否保存原图
                .create();
        takePhoto.onEnableCompress(config, false);//压缩时是否显示进度条
    }

    private CropOptions getCropOptions() {

        CropOptions.Builder builder = new CropOptions.Builder();

        builder.setAspectX(AppCommonInfo.maxPixel).setAspectY(AppCommonInfo.maxPixel);
//        builder.setOutputX(AppCommonInfo.maxPixel).setOutputY(AppCommonInfo.maxPixel);
        //是否使用自带的图片裁剪工具
        builder.setWithOwnCrop(false);
        return builder.create();
    }
}
