package com.hcll.fishshrimpcrab.club.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseActivity;
import com.hcll.fishshrimpcrab.club.ClubApi;
import com.hcll.fishshrimpcrab.club.adapter.SearchClubAdapter;
import com.hcll.fishshrimpcrab.club.entity.SearchClubEntity;
import com.hcll.fishshrimpcrab.common.AppCommonInfo;
import com.hcll.fishshrimpcrab.common.http.HttpUtils;
import com.hcll.fishshrimpcrab.common.http.entity.BaseResponseEntity;
import com.hcll.fishshrimpcrab.common.utils.DialogUtils;
import com.hcll.fishshrimpcrab.common.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hong on 2018/3/3.
 */

public class JoinClubActivity extends BaseActivity {

    private static final String TAG = JoinClubActivity.class.getSimpleName();

    @BindView(R.id.join_club_search_et)
    EditText searchEt;
    @BindView(R.id.club_seach_result_lv)
    ListView resultLv;
    private ClubApi retrofit;
    private SearchClubAdapter adapter;
    private Dialog progressDialog;
    private Call<BaseResponseEntity<List<SearchClubEntity>>> call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_club);
        ButterKnife.bind(this);
        initTopBar();
        initListView();

        progressDialog = DialogUtils.createProgressDialog(this, null);
        retrofit = HttpUtils.createRetrofit(this, ClubApi.class);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                requestSearch(s.toString());
            }
        });

    }

    private void initTopBar() {
        showTopBar();
        getTopBar().setTitle(R.string.join_to_club);
    }

    private void initListView() {
        addListViewHead();
        adapter = new SearchClubAdapter(this, new ArrayList<SearchClubEntity>());
        resultLv.setAdapter(adapter);
        resultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                if (obj instanceof SearchClubEntity) {
                    SearchClubEntity clubEntity = (SearchClubEntity) obj;
                    startActivity(ClubDetailActivity.createActivity(JoinClubActivity.this, clubEntity.getId()));
                }
            }
        });
    }

    private void addListViewHead() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_search_club_head, null);
        resultLv.addHeaderView(view);
    }

    private void requestSearch(String key) {
//        progressDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppCommonInfo.userid);
        map.put("key", key);
        RequestBody body = JsonUtils.createJsonRequestBody(map);
        if (call != null) {
            call.cancel();
        }
        call = retrofit.searchClub(body);
        call.enqueue(searchCallback);
    }

    private Callback<BaseResponseEntity<List<SearchClubEntity>>> searchCallback = new Callback<BaseResponseEntity<List<SearchClubEntity>>>() {
        @Override
        public void onResponse(Call<BaseResponseEntity<List<SearchClubEntity>>> call, Response<BaseResponseEntity<List<SearchClubEntity>>> response) {
//            progressDialog.dismiss();
            BaseResponseEntity<List<SearchClubEntity>> body = response.body();
            if (body != null && body.isSuccessed()) {
                List<SearchClubEntity> data = body.getData();
                if (data != null && data.size() > 0) {
                    resultLv.setVisibility(View.VISIBLE);
                    adapter.clear();
                    adapter.addAll(data);
                } else {
                    resultLv.setVisibility(View.GONE);
                }
            } else {
                ToastUtils.showLong(R.string.search_fauiler);
            }
        }

        @Override
        public void onFailure(Call<BaseResponseEntity<List<SearchClubEntity>>> call, Throwable t) {
//            progressDialog.dismiss();
            ToastUtils.showLong(t.getMessage());
        }
    };
}
