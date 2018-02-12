package com.hcll.fishshrimpcrab.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hcll.fishshrimpcrab.R;
import com.qmuiteam.qmui.widget.QMUILoadingView;

/**
 * 对话框管理
 */
public class DialogUtils {

    /**
     * 创建进度框
     *
     * @param context
     * @param listener
     * @return
     */
    public static Dialog createProgressDialog(Context context, DialogInterface.OnCancelListener listener) {
        Dialog dialog = new Dialog(context, R.style.dialog);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        QMUILoadingView loadingView = new QMUILoadingView(context);
        dialog.addContentView(loadingView, params);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(listener);
        return dialog;
    }


}
