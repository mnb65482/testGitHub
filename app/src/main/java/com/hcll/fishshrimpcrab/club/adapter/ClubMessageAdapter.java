package com.hcll.fishshrimpcrab.club.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.club.entity.ClubMessageEntity;
import com.hcll.fishshrimpcrab.common.utils.HttpFileUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.List;

/**
 * Created by lWX524664 on 2018/3/7.
 */

public class ClubMessageAdapter extends BaseAdapter {

    private Context context;
    private List<ClubMessageEntity.ListBean> list;
    private LayoutInflater inflater;

    public ClubMessageAdapter(Context context, List<ClubMessageEntity.ListBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ClubMessageEntity.ListBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_club_message_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.iconIv = convertView.findViewById(R.id.club_message_icon_iv);
            viewHolder.nameTv = convertView.findViewById(R.id.club_message_name_tv);
            viewHolder.timeTv = convertView.findViewById(R.id.club_message_time_tv);
            viewHolder.contentTv = convertView.findViewById(R.id.club_message_content_tv);
            viewHolder.bottomLl = convertView.findViewById(R.id.club_handle_bottom_ll);
            viewHolder.handleTv = convertView.findViewById(R.id.club_handle_message_tv);
            viewHolder.dealLl = convertView.findViewById(R.id.club_message_deal_ll);
            viewHolder.refuseTv = convertView.findViewById(R.id.club_handle_refuse_tv);
            viewHolder.agreeTv = convertView.findViewById(R.id.club_handle_agree_tv);
            viewHolder.deleteBtn = convertView.findViewById(R.id.club_message_delete_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ClubMessageEntity.ListBean bean = getItem(position);
        switch (bean.getType()) {
            case ClubMessageEntity.ListBean.TYPE_TOACCEPT:
                viewHolder.bottomLl.setVisibility(View.VISIBLE);
                viewHolder.handleTv.setVisibility(View.GONE);
                viewHolder.dealLl.setVisibility(View.VISIBLE);

                viewHolder.refuseTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onHandleJoinClubCallBack != null) {
                            onHandleJoinClubCallBack.handle(false, bean);
                        }
                    }
                });

                viewHolder.agreeTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onHandleJoinClubCallBack.handle(true, bean);
                    }
                });

                break;
            case ClubMessageEntity.ListBean.TYPE_ACCEPTED:
                viewHolder.bottomLl.setVisibility(View.VISIBLE);
                viewHolder.handleTv.setVisibility(View.VISIBLE);
                viewHolder.dealLl.setVisibility(View.GONE);
                viewHolder.handleTv.setText("已同意");
                viewHolder.handleTv.setTextColor(context.getResources().getColor(R.color.club_message_agree));
                setDrawableLeft(viewHolder.handleTv, R.drawable.comm_check_yello_ic);
                break;
            case ClubMessageEntity.ListBean.TYPE_REFUSE_APPLY:
                viewHolder.bottomLl.setVisibility(View.VISIBLE);
                viewHolder.handleTv.setVisibility(View.VISIBLE);
                viewHolder.dealLl.setVisibility(View.GONE);
                viewHolder.handleTv.setText("已拒绝");
                viewHolder.handleTv.setTextColor(context.getResources().getColor(R.color.close_red));
                setDrawableLeft(viewHolder.handleTv, R.drawable.comm_close_red_ic);

                break;
            default:
                viewHolder.bottomLl.setVisibility(View.GONE);
                break;
        }

        HttpFileUtils.loadImage2View(context, bean.getHeader(), viewHolder.iconIv);
//        viewHolder.nameTv.setText(bean.getContent());
//        viewHolder.contentTv.setText(bean.getRemark());
        viewHolder.nameTv.setText(bean.getTitle());
        viewHolder.contentTv.setText(getContent(bean));
        viewHolder.timeTv.setText(TimeUtils.getFriendlyTimeSpanByNow(bean.getTime() * 1000));
        final View finalConvertView = convertView;
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onHandleJoinClubCallBack != null) {
                    onHandleJoinClubCallBack.delete(bean);
                    ((SwipeMenuLayout) finalConvertView).quickClose();
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        QMUIRadiusImageView iconIv;
        TextView nameTv;
        TextView timeTv;
        TextView contentTv;
        LinearLayout bottomLl;
        TextView handleTv;
        LinearLayout dealLl;
        TextView refuseTv;
        TextView agreeTv;
        Button deleteBtn;
    }

    public void addAll(List<ClubMessageEntity.ListBean> listBeans) {
        list.addAll(listBeans);
        notifyDataSetChanged();
    }

    private void setDrawableLeft(TextView textView, @DrawableRes int drawableId) {
        Drawable drawable = context.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * 根据状态返回提示文案
     *
     * @param bean
     * @return
     */
    public String getContent(@NonNull ClubMessageEntity.ListBean bean) {
        switch (bean.getType()) {
            case ClubMessageEntity.ListBean.TYPE_NOTICE:
                return bean.getContent();
            case ClubMessageEntity.ListBean.TYPE_ACCEPTED:
//                self.remarkLabel.text = String( @ "%@申请加入俱乐部:%@\n申请消息:%@", msg.title, msg.content, msg.remark);
                return String.format("申请加入俱乐部:%s \n申请信息:%s", bean.getContent(), bean.getRemark());
//                self.dealtLabel.text = @ "已同意";
            case ClubMessageEntity.ListBean.TYPE_TOACCEPT:
//                self.remarkLabel.text = String( @ "%@申请加入俱乐部:%@\n申请消息:%@", msg.title, msg.content, msg.remark);
//                break;
                return String.format("申请加入俱乐部:%s \n申请信息:%s", bean.getContent(), bean.getRemark());
            case ClubMessageEntity.ListBean.TYPE_ACCEPT_APPLY:
                return "同意了你的加入请求";
            case ClubMessageEntity.ListBean.TYPE_EXIT_CLUB:
//                self.remarkLabel.text = String( @ "退出了你的俱乐部");
//                break;
                return "退出了你的俱乐部";
            case ClubMessageEntity.ListBean.TYPE_REMOVE_CLUB:
//                self.remarkLabel.text = String( @ "你已被管理员移出俱乐部");
//                break;
                return "你已被管理员移出俱乐部";
            case ClubMessageEntity.ListBean.TYPE_DISMISS_CLUB:
//                self.remarkLabel.text = String( @ "该俱乐部已经被解散");
                return "该俱乐部已经被解散";
            case ClubMessageEntity.ListBean.TYPE_SET_MANAGER:
//                self.remarkLabel.text = String( @ "你被设置为管理员");
//                break;
                return "你被设置为管理员";
            case ClubMessageEntity.ListBean.TYPE_REMOVE_MANAGER:
//                self.remarkLabel.text = String( @ "你被取消管理员");
//                break;
                return "你被取消管理员";
            case ClubMessageEntity.ListBean.TYPE_REFUSE_APPLY:
//                self.remarkLabel.text = String( @ "%@申请加入俱乐部:%@\n申请消息:%@", msg.title, msg.content, msg.remark);
//                self.dealtLabel.text = @ "已拒绝";
//                break;
                return String.format("申请加入俱乐部:%s \n申请信息:%s", bean.getContent(), bean.getRemark());
            case ClubMessageEntity.ListBean.TYPE_NOTICE_REFUSE:
//                self.remarkLabel.text = String( @ "拒绝了你的加入请求");
//                break;
                return "拒绝了你的加入请求";
            default:
                ToastUtils.showShort("error club msg type");
                return "";
        }
    }

    public interface HandleJoinClubCallBack {
        void handle(boolean isAgree, ClubMessageEntity.ListBean bean);

        void delete(ClubMessageEntity.ListBean bean);
    }

    private HandleJoinClubCallBack onHandleJoinClubCallBack;

    public void setOnHandleJoinClubCallBack(HandleJoinClubCallBack onHandleJoinClubCallBack) {
        this.onHandleJoinClubCallBack = onHandleJoinClubCallBack;
    }
}
