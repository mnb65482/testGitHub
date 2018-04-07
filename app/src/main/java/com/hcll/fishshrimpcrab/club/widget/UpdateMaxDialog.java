package com.hcll.fishshrimpcrab.club.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;

/**
 * Created by lWX524664 on 2018/3/12.
 */

public class UpdateMaxDialog extends Dialog implements View.OnClickListener {

    /**
     * 当前俱乐部人数
     */
    private int currentCount;
    private TextView inputTv;
    private TextView feeTv;
    private ImageView lessIv;
    private ImageView addIv;

    public UpdateMaxDialog(@NonNull Context context, int currentCount) {
        super(context, R.style.Verfication_info_style);
        this.currentCount = currentCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_max);
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
        ImageView closeIv = (ImageView) findViewById(R.id.update_max_close_iv);
        lessIv = (ImageView) findViewById(R.id.update_max_less_iv);
        lessIv.setOnClickListener(this);
        inputTv = (TextView) findViewById(R.id.update_max_input_tv);
        addIv = (ImageView) findViewById(R.id.update_max_add_iv);
        addIv.setOnClickListener(this);
        feeTv = (TextView) findViewById(R.id.update_max_fee_tv);
        TextView sureTv = (TextView) findViewById(R.id.update_max_sure_tv);

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateMaxDialog.this.dismiss();
            }
        });

        inputTv.setText(String.valueOf(currentCount));
        feeTv.setText("0");

        if (currentCount >= AppCommonInfo.clubMaxCount) {
            addIv.setImageResource(R.drawable.comm_add_disable_ic);
        }

        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = parseInt(inputTv.getText().toString());
                int fee = parseInt(feeTv.getText().toString());
                if (onUpdateCallBack != null) {
                    onUpdateCallBack.update(count - currentCount, fee);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_max_less_iv: {
                int count = parseInt(inputTv.getText().toString());
                int fee = parseInt(feeTv.getText().toString());
                if (count > currentCount) {
                    inputTv.setText(String.valueOf(count - AppCommonInfo.clubUpdateCount));
                    feeTv.setText(String.valueOf(fee - AppCommonInfo.clubUpdateFee));
                    addIv.setImageResource(R.drawable.comm_add_ic);
                    if ((count - 50) == currentCount) {
                        lessIv.setImageResource(R.drawable.comm_less_disable_ic);
                    }
                }
            }
            break;
            case R.id.update_max_add_iv:

                int count = parseInt(inputTv.getText().toString());
                int fee = parseInt(feeTv.getText().toString());
                if (count >= AppCommonInfo.clubMaxCount) {
                    return;
                }

                inputTv.setText(String.valueOf(count + AppCommonInfo.clubUpdateCount));
                feeTv.setText(String.valueOf(fee + AppCommonInfo.clubUpdateFee));
                lessIv.setImageResource(R.drawable.comm_less_ic);
                if (parseInt(inputTv.getText().toString()) >= AppCommonInfo.clubMaxCount) {
                    addIv.setImageResource(R.drawable.comm_add_disable_ic);
                }
                break;
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public interface OnUpdateCallBack {
        void update(int num, int idou);
    }

    private OnUpdateCallBack onUpdateCallBack;

    public void setOnUpdateCallBack(OnUpdateCallBack onUpdateCallBack) {
        this.onUpdateCallBack = onUpdateCallBack;
    }
}
