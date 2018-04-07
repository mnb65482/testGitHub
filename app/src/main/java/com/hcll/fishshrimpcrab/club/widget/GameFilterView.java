package com.hcll.fishshrimpcrab.club.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.club.entity.ClubGameReq;
import com.hcll.fishshrimpcrab.club.enums.GameBankerEnum;
import com.hcll.fishshrimpcrab.club.enums.GameFilterInterface;
import com.hcll.fishshrimpcrab.club.enums.GameModelEnums;
import com.hcll.fishshrimpcrab.club.enums.GamePeopleEnum;
import com.hcll.fishshrimpcrab.club.enums.GameScoreEnum;
import com.hcll.fishshrimpcrab.club.enums.GameSeatEnums;
import com.hcll.fishshrimpcrab.club.enums.GameTypeEnums;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hong on 2018/3/11.
 */
public class GameFilterView {

    private ClubGameFilterView typeCgfv;
    private ClubGameFilterView modelCgfv;
    private ClubGameFilterView scoreCgfv;
    private ClubGameFilterView bankerCgfv;
    private ClubGameFilterView peopleCgfv;
    private ClubGameFilterView seatCgfv;
    private PopupWindow popupWindow;

    public GameFilterView(Context context) {

        View popupView = LayoutInflater.from(context).inflate(R.layout.view_game_filter_popup, null);
        typeCgfv = (ClubGameFilterView) popupView.findViewById(R.id.game_filter_type_cgfv);
        modelCgfv = (ClubGameFilterView) popupView.findViewById(R.id.game_filter_model_cgfv);
        scoreCgfv = (ClubGameFilterView) popupView.findViewById(R.id.game_filter_bscore_cgfv);
        bankerCgfv = (ClubGameFilterView) popupView.findViewById(R.id.game_filter_banker_cgfv);
        peopleCgfv = (ClubGameFilterView) popupView.findViewById(R.id.game_filter_people_cgfv);
        seatCgfv = (ClubGameFilterView) popupView.findViewById(R.id.game_filter_seat_cgfv);
        TextView okTv = (TextView) popupView.findViewById(R.id.game_filter_ok_tv);
        typeCgfv.init(enum2Bean(GameTypeEnums.values()));
        modelCgfv.init(enum2Bean(GameModelEnums.values()));
        scoreCgfv.init(enum2Bean(GameScoreEnum.values()));
        bankerCgfv.init(enum2Bean(GameBankerEnum.values()));
        peopleCgfv.init(enum2Bean(GamePeopleEnum.values()));
        seatCgfv.init(enum2Bean(GameSeatEnums.values()));

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 点击其他区域关闭Pw，必须给pw设置背景
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置让pw获得焦点
        popupWindow.setFocusable(true);
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ClubGameReq req = new ClubGameReq();
                req.setRoom_path(typeCgfv.getSelectId());
                req.setHave_seat(seatCgfv.getSelectId());
                req.setRoom_type(bankerCgfv.getSelectId());
                req.setNum_type(peopleCgfv.getSelectId());
                req.setScore_type(scoreCgfv.getSelectId());
                req.setGame_type(modelCgfv.getSelectId());
                if (onFilterCallBack != null) {
                    onFilterCallBack.filter(req);
                }
            }
        });
    }

    private List<ClubGameFilterView.SelectBean> enum2Bean(GameFilterInterface[] imps) {
        List<ClubGameFilterView.SelectBean> been = new ArrayList<>();
        for (GameFilterInterface imp : imps) {
            ClubGameFilterView.SelectBean selectBean =
                    new ClubGameFilterView.SelectBean(imp.getEnumId(), imp.getEnumName());
            been.add(selectBean);
        }
        return been;
    }

    public void show(View anchor) {
        popupWindow.showAsDropDown(anchor);

    }

    public interface OnFilterCallBack {
        void filter(ClubGameReq req);
    }

    private OnFilterCallBack onFilterCallBack;

    public void setOnFilterCallBack(OnFilterCallBack onFilterCallBack) {
        this.onFilterCallBack = onFilterCallBack;
    }
}
