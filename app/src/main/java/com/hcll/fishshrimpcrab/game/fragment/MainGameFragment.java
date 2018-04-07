package com.hcll.fishshrimpcrab.game.fragment;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hcll.fishshrimpcrab.R;
import com.hcll.fishshrimpcrab.base.BaseFragment;
import com.hcll.fishshrimpcrab.club.activity.ClubGameListActivity;
import com.hcll.fishshrimpcrab.club.entity.ClubGameReq;
import com.hcll.fishshrimpcrab.club.widget.GameFilterView;
import com.hcll.fishshrimpcrab.game.adapter.GameFragmentAdapter;
import com.hcll.fishshrimpcrab.game.entity.GameListEntity;
import com.hcll.fishshrimpcrab.game.entity.RoomListReq;
import com.hcll.fishshrimpcrab.game.enums.GameListType;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hong on 2018/2/23.
 */

public class MainGameFragment extends BaseFragment {

    private String TAG = this.getClass().getSimpleName();

    private String[] title = new String[]{"大厅", "俱乐部", "我的"};
    private GameFilterView filterView;
    private GameHallFragment hallFragment;
    private GameClubFragment clubFragment;
    private GameMyFragment myFragment;

    @Override
    public void onInitView() {
        initTopBar();
        initPopup();
        initView();
    }

    private void initTopBar() {
        QMUITopBar topBar = getTopBar();
        TextView title = topBar.setTitle(getString(R.string.tab1));
        title.setTextColor(Color.WHITE);

        QMUIAlphaImageButton leftBackImageButton = topBar.addLeftBackImageButton();
        leftBackImageButton.setImageResource(R.drawable.comm_message_ic);
        leftBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转游戏信息
            }
        });

        final Button rightButton = topBar.addRightTextButton(getString(R.string.game_filter), View.generateViewId());
        rightButton.setTextColor(getResources().getColor(R.color.common_text_color));
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterView.show(rightButton);
            }
        });
    }


    private void initView() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_game_tl);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_game_content_vp);
        hallFragment = new GameHallFragment();
        clubFragment = new GameClubFragment();
        myFragment = new GameMyFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(hallFragment);
        fragments.add(clubFragment);
        fragments.add(myFragment);
        GameFragmentAdapter adapter = new GameFragmentAdapter(getFragmentManager(), fragments, title);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initPopup() {
        filterView = new GameFilterView(getContext());
        filterView.setOnFilterCallBack(new GameFilterView.OnFilterCallBack() {
            @Override
            public void filter(ClubGameReq req) {
                RoomListReq roomListReq = new RoomListReq();
                copyParams(req, roomListReq);
                refreshFragment(roomListReq);
            }
        });
    }

    private void copyParams(Object resourceObj, Object receiveObj) {
        Class<?> aClass = resourceObj.getClass();

        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                Method getMethod = aClass.getMethod("get" + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1));
                Object invoke = getMethod.invoke(resourceObj);
                Method setMethod = receiveObj.getClass().getMethod("set" + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1), field.getType());
                setMethod.invoke(receiveObj, invoke);
            } catch (Exception e) {
                //e
                Log.e(TAG, "copyParams:" + fieldName + " 属性复制失败");
            }
        }
    }

    private void refreshFragment(RoomListReq roomListReq) {
        hallFragment.setRequestParam(roomListReq);
        clubFragment.setRequestParam(roomListReq);
        myFragment.setRequestParam(roomListReq);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_main_game;
    }
}
