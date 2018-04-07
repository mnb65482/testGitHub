package com.hcll.fishshrimpcrab.game.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BasePageAdapter;
import com.hcll.fishshrimpcrab.club.adapter.ClubGameAdapter;
import com.hcll.fishshrimpcrab.club.entity.ClubGameEntity;
import com.hcll.fishshrimpcrab.common.utils.HttpFileUtils;
import com.hcll.fishshrimpcrab.game.entity.GameListEntity;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.List;

/**
 * Created by lWX385270 on 2018/3/28.
 */

public class GameListAdapter extends BaseAdapter implements BasePageAdapter<GameListEntity.RoomsBean> {

    private Context context;
    private List<GameListEntity.RoomsBean> list;
    private LayoutInflater inflater;

    public GameListAdapter(Context context, List<GameListEntity.RoomsBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GameListEntity.RoomsBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_club_game, parent, false);
            viewHolder.iconIv = convertView.findViewById(R.id.item_cgame_head_iv);
            viewHolder.nameTv = convertView.findViewById(R.id.item_cgame_name_tv);
            viewHolder.countTv = convertView.findViewById(R.id.item_cgame_count_tv);
            viewHolder.diamondsTv = convertView.findViewById(R.id.item_cgame_diamonds_tv);
            viewHolder.timeTv = convertView.findViewById(R.id.item_cgame_time_tv);
            viewHolder.stateTv = convertView.findViewById(R.id.item_cgame_state_tv);
            viewHolder.codeLl = convertView.findViewById(R.id.item_cgame_code_ll);
            viewHolder.codeTv = convertView.findViewById(R.id.item_cgame_code_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.codeLl.setVisibility(View.VISIBLE);

        GameListEntity.RoomsBean item = getItem(position);
        HttpFileUtils.loadImage2View(context, item.getOnwer_head(), viewHolder.iconIv);
        //todo 邀请码待确认
        viewHolder.codeTv.setText(item.getId());
        viewHolder.nameTv.setText(item.getRoom_name());
        viewHolder.countTv.setText(String.format("%s/%s", item.getRoom_people() + "", item.getPlayer_count() + ""));
        viewHolder.diamondsTv.setText(item.getInit_chip() + "");
        long currentTimeMillis = System.currentTimeMillis();
        viewHolder.timeTv.setText(TimeUtils.getFitTimeSpan(currentTimeMillis + item.getReside_time(),
                currentTimeMillis, 3));

        if (item.getGame_status() == 0) {
            viewHolder.stateTv.setTextColor(context.getResources().getColor(R.color.common_text_color));
            setDrawabelTop(viewHolder.stateTv, R.drawable.game_waitting);
            viewHolder.stateTv.setText(context.getString(R.string.club_game_waiting));
        } else {
            viewHolder.stateTv.setTextColor(context.getResources().getColor(R.color.text_green));
            setDrawabelTop(viewHolder.stateTv, R.drawable.game_playing);
            viewHolder.stateTv.setText(context.getString(R.string.club_game_palying));
        }
        return convertView;
    }

    private class ViewHolder {
        QMUIRadiusImageView iconIv;
        TextView nameTv;
        TextView countTv;
        TextView diamondsTv;
        TextView timeTv;
        TextView stateTv;
        LinearLayout codeLl;
        TextView codeTv;
    }


    private void setDrawabelTop(TextView textView, @DrawableRes int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);

    }

    @Override
    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<GameListEntity.RoomsBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
