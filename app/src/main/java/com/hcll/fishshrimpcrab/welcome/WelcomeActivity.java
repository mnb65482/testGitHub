package com.hcll.fishshrimpcrab.welcome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.Constant;
import com.hcll.fishshrimpcrab.common.utils.NotificationUtils;
import com.hcll.fishshrimpcrab.login.activity.LoginActivity;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import butterknife.ButterKnife;

/**
 * 欢迎页 校验版本信息等
 * <p>
 * Created by hong on 2018/2/1.
 */
public class WelcomeActivity extends AppCompatActivity {
    private QMUIDialog settingDialog;
    private QMUIDialog successDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        createSettingDialog();
        createSuccessDialog();
        if (!NotificationUtils.isNotificationEnabled(this)) {
            settingDialog.show();
        } else {
            startActivity(new Intent(this, GuidePageActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NotificationUtils.isNotificationEnabled(this)) {
            settingDialog.dismiss();
            successDialog.show();
        }
    }

    private void createSettingDialog() {
        QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("物2斗想给您发送通知")
                .setMessage("通知可能包括提示、声音和图标，这些可在设置中开启。")
                .addAction("马上去设置", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        NotificationUtils.getAppDetailSettingIntent(WelcomeActivity.this);
                    }
                });
        settingDialog = builder.create();
        settingDialog.setCanceledOnTouchOutside(false);
        settingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                WelcomeActivity.this.finish();
            }
        });
    }

    private void createSuccessDialog() {
        QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("通知权限设置成功")
                .addAction("马上开始游戏", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
//                        NotificationUtils.getAppDetailSettingIntent(WelcomeActivity.this);
                        Intent intent = new Intent(WelcomeActivity.this, GuidePageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        successDialog = builder.create();
        successDialog.setCanceledOnTouchOutside(false);
        successDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                WelcomeActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
