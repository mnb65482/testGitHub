package com.hcll.fishshrimpcrab.club.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.club.entity.ClubMemberEntity;
import com.hcll.fishshrimpcrab.club.enums.MemberCommEnum;
import com.hcll.fishshrimpcrab.club.enums.MemberEditEnum;
import com.hcll.fishshrimpcrab.common.utils.HttpFileUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.List;

/**
 * Created by hong on 2018/3/5.
 */

public class ClubMemberListAdapter extends BaseAdapter {

    private List<ClubMemberEntity> list;
    private Context context;
    private LayoutInflater inflater;
    private boolean isEdit;
    private boolean isCreate;

    public ClubMemberListAdapter(Context context, List<ClubMemberEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ClubMemberEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_club_member, parent, false);
            viewHolder.lableTv = convertView.findViewById(R.id.club_member_creator_tv);
            viewHolder.iconIv = convertView.findViewById(R.id.club_member_icon_iv);
            viewHolder.nameTv = convertView.findViewById(R.id.club_member_name_tv);
            viewHolder.powerTv = convertView.findViewById(R.id.club_member_power_tv);
            viewHolder.deleteBtn = convertView.findViewById(R.id.club_member_delete_btn);
            viewHolder.fillview = convertView.findViewById(R.id.club_member_fill_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) convertView.getTag());
        }

        final ClubMemberEntity entity = getItem(position);

        if (position > 1) {
            viewHolder.lableTv.setVisibility(View.GONE);
        } else {
            viewHolder.lableTv.setVisibility(View.VISIBLE);
            viewHolder.lableTv.setText(position == 0 ? context.getString(R.string.club_creater) :
                    context.getString(R.string.club_member));
        }

        HttpFileUtils.loadImage2View(context, entity.getHead(), viewHolder.iconIv);
        viewHolder.nameTv.setText(entity.getNick());
        if (isEdit) {
            viewHolder.powerTv.setText(MemberEditEnum.getDescById(entity.getType()));
            viewHolder.powerTv.setTextColor(context.getResources()
                    .getColor(MemberEditEnum.getColorById(entity.getType())));
        } else {
            viewHolder.powerTv.setText(MemberCommEnum.getDescById(entity.getType()));
            viewHolder.powerTv.setTextColor(context.getResources().getColor(MemberCommEnum
                    .getColorById(entity.getType())));
        }

        viewHolder.powerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit && position != 0) {
                    onManagerClickListener.manage(entity.getUid(), MemberEditEnum.getChangeTypeById(entity.getType()), MemberEditEnum.getControlCodeById(entity.getType()));
                }
            }
        });

        //创建者不能够被删除
        if (position == 0) {
            ((SwipeMenuLayout) convertView).setSwipeEnable(false);
        } else {
            //创建者进成员列表有删除角色的功能
            if (isCreate) {
                ((SwipeMenuLayout) convertView).setSwipeEnable(true);
            } else {
                ((SwipeMenuLayout) convertView).setSwipeEnable(false);
            }
        }

        if (position == 1) {
            viewHolder.fillview.setVisibility(View.VISIBLE);
        } else {
            viewHolder.fillview.setVisibility(View.GONE);
        }


        final View finalConvertView = convertView;
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteListener != null) {
                    ((SwipeMenuLayout) finalConvertView).quickClose();
                    onDeleteListener.delete(entity);
                }
            }
        });


        return convertView;
    }

    private class ViewHolder {
        TextView lableTv;
        QMUIRadiusImageView iconIv;
        TextView nameTv;
        TextView powerTv;
        Button deleteBtn;
        View fillview;

    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }


    public interface OnManagerClickListener {
        void manage(int userId, int type, int position);
    }

    private OnManagerClickListener onManagerClickListener;

    public void setOnManagerClickListener(OnManagerClickListener onManagerClickListener) {
        this.onManagerClickListener = onManagerClickListener;
    }

    public interface OnDeleteListener {
        void delete(ClubMemberEntity entity);
    }

    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public void addAll(List<ClubMemberEntity> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }
}
