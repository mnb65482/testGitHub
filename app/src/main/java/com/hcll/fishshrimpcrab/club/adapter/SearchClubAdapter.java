package com.hcll.fishshrimpcrab.club.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.entity.SearchClubEntity;
import com.hcll.fishshrimpcrab.club.widget.VerficationCodeDialog;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.HttpFileUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.key;

/**
 * Created by hong on 2018/3/3.
 */

public class SearchClubAdapter extends BaseAdapter {
    private List<SearchClubEntity> list;
    private Context context;
    private ClubApi retrofit;
    private Dialog progressDialog;
    private VerficationCodeDialog dialog;
    private String clubId = "";

    public SearchClubAdapter(Context context, List<SearchClubEntity> list) {
        this.context = context;
        this.list = list;
        retrofit = HttpUtils.createRetrofit(context, ClubApi.class);
        progressDialog = DialogUtils.createProgressDialog(context, null);
        dialog = new VerficationCodeDialog(context);
        dialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(dialog.getText())) {
                    ToastUtils.showLong("请输入验证信息！");
                } else {
                    requestApply();
                }
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SearchClubEntity getItem(int position) {
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
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_join_club, parent, false);
            viewHolder.headImage = convertView.findViewById(R.id.search_item_image_iv);
            viewHolder.nameTv = convertView.findViewById(R.id.search_item_name_tv);
            viewHolder.countTv = convertView.findViewById(R.id.search_item_count_tv);
            viewHolder.locationTv = convertView.findViewById(R.id.search_item_location_tv);
            viewHolder.clickTv = convertView.findViewById(R.id.search_item_join_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final SearchClubEntity entity = getItem(position);
        HttpFileUtils.loadImage2View(context, entity.getHeader(), viewHolder.headImage);
        viewHolder.nameTv.setText(entity.getName());
        viewHolder.countTv.setText(String.format("%s/%s", String.valueOf(entity.getOnlineCount()),
                String.valueOf(entity.getTotalCount())));
        viewHolder.locationTv.setText(entity.getAreaID());
        viewHolder.clickTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clubId = entity.getId();
                dialog.show();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        QMUIRadiusImageView headImage;
        TextView nameTv;
        TextView countTv;
        TextView locationTv;
        TextView clickTv;
    }

    public void addAll(List<SearchClubEntity> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    private void requestApply() {
        progressDialog.show();

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("club_id", clubId);
        map.put("remark", dialog.getText());
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        Call<BaseResponseEntity> call = retrofit.applyJoinClub(body);
        call.enqueue(applyCallback);
    }

    private Callback<BaseResponseEntity> applyCallback = new Callback<BaseResponseEntity>() {
        @Override
        public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
            progressDialog.dismiss();
            if (response.body() != null) {
                dialog.dismiss();
                switch (response.body().getStatus()) {
                    case 0:
                        ToastUtils.showLong(R.string.apply_club_succsee);
                        break;
                    case 1:
                        ToastUtils.showLong(R.string.apply_club_failuer);
                        break;
                    case 2:
                        ToastUtils.showLong(R.string.data_exception);
                        break;
                    case 4:
                        ToastUtils.showLong(R.string.club_contain);
                        break;
                    case 6:
                        ToastUtils.showLong(R.string.club_disband);
                        break;
                }
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
            progressDialog.dismiss();
        }
    };
}
