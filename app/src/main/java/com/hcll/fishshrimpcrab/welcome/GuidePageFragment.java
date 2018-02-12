package com.hcll.fishshrimpcrab.welcome;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.common.Constant;
import com.hcll.fishshrimpcrab.login.activity.LoginActivity;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

/**
 * Created by hong on 2018/2/9.
 */

public class GuidePageFragment extends Fragment {

    public static final String KEY_TITLE = "title";
    public static final String KEY_DETAIL = "detail";
    public static final String KEY_ISLAST = "isLast";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String title = arguments.getString(KEY_TITLE);
        String detail = arguments.getString(KEY_DETAIL);
        boolean isLast = arguments.getBoolean(KEY_ISLAST);

        View view = inflater.inflate(R.layout.fragment_guide_page, container, false);

        TextView titleTv = (TextView) view.findViewById(R.id.guide_page_title);
        titleTv.setText(title);
        TextView detailTv = (TextView) view.findViewById(R.id.guide_page_detail);
        detailTv.setText(detail);
        QMUIRoundButton gotoNextBtn = (QMUIRoundButton) view.findViewById(R.id.goto_next_btn);
        if (isLast) {
            gotoNextBtn.setVisibility(View.VISIBLE);
            gotoNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SPUtils.getInstance().put(Constant.KEY_FIRST_LOGIN, false);
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
        return view;
    }


}
