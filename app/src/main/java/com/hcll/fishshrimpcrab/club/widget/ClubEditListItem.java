package com.hcll.fishshrimpcrab.club.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.hcll.fishshrimpcrab.R;


/**
 * Created by hong on 2018/3/3.
 */

public class ClubEditListItem extends LinearLayout {

    private ImageView nextIv;
    private TextView nameTv;
    private TextView detailTv;
    private ViewGroup customFl;

    public ClubEditListItem(Context context) {
        this(context, null);
    }

    public ClubEditListItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClubEditListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(context, R.layout.view_club_edit_listitem, this);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ClubEditListItem);
        String name = typedArray.getString(R.styleable.ClubEditListItem_cel_name);
        String detail = typedArray.getString(R.styleable.ClubEditListItem_cel_detail);
        String hint = typedArray.getString(R.styleable.ClubEditListItem_cel_hint);
        boolean showNext = typedArray.getBoolean(R.styleable.ClubEditListItem_cel_shownext, false);
        typedArray.recycle();

        nameTv = (TextView) findViewById(R.id.club_edit_name_tv);
        setNotEmptyText(nameTv, name);
        detailTv = (TextView) findViewById(R.id.club_edit_detail_tv);
        setNotEmptyText(detailTv, detail);
        if (!StringUtils.isEmpty(hint)) {
            detailTv.setHint(hint);
        }
        nextIv = (ImageView) findViewById(R.id.club_edit_next_iv);
        if (showNext) {
            nextIv.setVisibility(VISIBLE);
        }

        customFl = (ViewGroup) findViewById(R.id.club_edit_custom_fl);
    }

    public void showNextView() {
        if (nextIv.getVisibility() != View.VISIBLE) {
            nextIv.setVisibility(VISIBLE);
        }
    }

    public void setName(String name) {
        nameTv.setText(name);
    }

    public void setDetail(String detail) {
        detailTv.setText(detail);
    }

    public void setCustomView(View view) {
        detailTv.setVisibility(GONE);
        nextIv.setVisibility(GONE);
        customFl.setVisibility(VISIBLE);
        customFl.addView(view);
    }

    private void setNotEmptyText(TextView textView, String str) {
        if (!StringUtils.isEmpty(str)) {
            textView.setText(str);
        }
    }

    public String getName() {
        if (StringUtils.isEmpty(nameTv.getText())) {
            return "";
        } else {
            return nameTv.getText().toString();
        }
    }

    public String getDetail() {
        if (StringUtils.isEmpty(detailTv.getText())) {
            return "";
        } else {
            return detailTv.getText().toString();
        }
    }
}
