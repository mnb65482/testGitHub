package com.hcll.fishshrimpcrab.club.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.hcll.fishshrimpcrab.R;

/**
 * Created by hong on 2018/3/3.
 */

public class VerficationCodeDialog extends Dialog {

    private ImageView closeIv;
    private EditText infoEt;
    private TextView sureTv;
    private View.OnClickListener onPositiveClickListener;

    public VerficationCodeDialog(Context context) {
        super(context, R.style.Verfication_info_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input_verification_info);
        initDialog();
        initView();
    }

    private void initDialog() {
        //设置dialog的大小
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth() - 100; //设置dialog的宽度为当前手机屏幕的宽度-100
        getWindow().setAttributes(p);
    }

    private void initView() {
        closeIv = (ImageView) findViewById(R.id.verification_info_close_iv);
        infoEt = (EditText) findViewById(R.id.verification_info_et);
        sureTv = (TextView) findViewById(R.id.verification_info_sure_tv);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerficationCodeDialog.this.dismiss();
            }
        });

        if (onPositiveClickListener != null) {
            sureTv.setOnClickListener(onPositiveClickListener);
        }
    }

    /**
     * 确定按钮
     */
    public void setOnPositiveListener(View.OnClickListener onPositiveClickListener) {
//        sureTv.setOnClickListener(onPositiveClickListener);
        this.onPositiveClickListener = onPositiveClickListener;
    }

    public void setText(String text) {
        infoEt.setText(text);
    }

    public String getText() {
        Editable text = infoEt.getText();
        return StringUtils.isEmpty(text) ? "" : text.toString();
    }

    @Override
    public void dismiss() {
        infoEt.setText("");
        super.dismiss();
    }
}
