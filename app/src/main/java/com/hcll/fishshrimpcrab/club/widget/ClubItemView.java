package com.hcll.fishshrimpcrab.club.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.club.entity.ClubInfoEntity;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by hong on 2018/2/26.
 */

public class ClubItemView extends LinearLayout {

    private QMUIRadiusImageView iconIv;
    private TextView nameTv;
    private TextView countTv;
    private TextView locationTv;
    private TextView gameTv;

    private Activity mActivity;

    private ClubInfoEntity.ListBean listBean;

    public ClubItemView(Context context) {
        this(context, null);
    }

    public ClubItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClubItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_club_item, this);

        iconIv = (QMUIRadiusImageView) findViewById(R.id.club_item_icon);
        nameTv = (TextView) findViewById(R.id.club_item_name);
        countTv = (TextView) findViewById(R.id.club_item_count);
        locationTv = (TextView) findViewById(R.id.club_item_location);
        gameTv = (TextView) findViewById(R.id.club_item_game);

        if (context instanceof Activity) {
            mActivity = ((Activity) context);
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listBean != null && mActivity != null) {
//                    mActivity.startActivity(ClubDetailActivity.createActivity(mActivity, listBean.getId()));
//                    ToastUtils.showLong(listBean.getName());
                    if (onItemClickListener != null) {
                        onItemClickListener.clickItem(listBean);
                    }

                }
            }
        });

    }

    public void init(ClubInfoEntity.ListBean listBean) {
        this.listBean = listBean;
        Picasso.with(getContext()).load(AppCommonInfo.ImageDownLoadPath + listBean.getHeader()).error(R.drawable.login_logo).into(iconIv);

        nameTv.setText(listBean.getName());
        countTv.setText(String.format("%s/%s", listBean.getOnlineCount() + "", listBean.getTotalCount() + ""));
        locationTv.setText(listBean.getAreaID());
        gameTv.setText(listBean.getStatus() + "桌");
    }

    public interface OnItemClickListener {
        void clickItem(ClubInfoEntity.ListBean listBean);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
