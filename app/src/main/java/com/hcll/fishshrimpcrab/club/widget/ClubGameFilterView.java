package com.hcll.fishshrimpcrab.club.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.StringUtils;
import com.hcll.fishshrimpcrab.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by hong on 2018/3/10.
 */

public class ClubGameFilterView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = ClubGameFilterView.class.getSimpleName();

    private String title;
    private int maxItem;
    private SparseIntArray selectArray = new SparseIntArray();
    private SparseArray<View> viewArray = new SparseArray<>();
    private LinearLayout viewGroup;

    public ClubGameFilterView(Context context) {
        this(context, null);
    }

    public ClubGameFilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClubGameFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClubGameFilterView);
        title = typedArray.getString(R.styleable.ClubGameFilterView_game_filter_title);
        maxItem = typedArray.getInt(R.styleable.ClubGameFilterView_game_max_item, 3);
        typedArray.recycle();

        setOrientation(VERTICAL);
        int padding = ConvertUtils.dp2px(8);
        setPadding(padding, padding, padding, padding);
    }

    public void init(List<SelectBean> list) {
        TextView titleTv = new TextView(getContext());
        titleTv.setText(StringUtils.isEmpty(title) ? "" : title);
        titleTv.setTextColor(getContext().getResources().getColor(R.color.qmui_config_color_white));
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        LayoutParams tvParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.bottomMargin = ConvertUtils.dp2px(2);
        tvParams.topMargin = ConvertUtils.dp2px(4);
        addView(titleTv, tvParams);

        viewGroup = new LinearLayout(getContext());
        viewGroup.setOrientation(HORIZONTAL);
        viewGroup.setWeightSum(maxItem);

        if (list == null) return;
        int itemCount = list.size() >= maxItem ? maxItem : list.size();
        for (int i = 0; i < itemCount; i++) {
            CheckBox checkBox = new CheckBox(getContext());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            if (i != itemCount - 1) {
                params.rightMargin = ConvertUtils.dp2px(6);
            }
            checkBox.setBackgroundResource(R.drawable.select_game_filter_radio);
            checkBox.setButtonDrawable(null);
            checkBox.setGravity(Gravity.CENTER);
            int rbpadding = ConvertUtils.dp2px(4);
            checkBox.setPadding(rbpadding, rbpadding, rbpadding, rbpadding);

//            checkBox.setTextColor(getResources().getColor(R.color.game_filter_text_bg));
            checkBox.setTextColor(createColorStateList(getResources().getColor(R.color.qmui_config_color_75_white),
                    getResources().getColor(R.color.common_text_color)));
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            checkBox.setText(list.get(i).getName());
            int viewId = View.generateViewId();
            checkBox.setId(viewId);
            checkBox.setOnClickListener(this);
            selectArray.put(viewId, list.get(i).getId());
            viewArray.put(viewId, checkBox);
            viewGroup.addView(checkBox, params);
        }
        addView(viewGroup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void init(SelectBean... list) {
        init(Arrays.asList(list));
    }


    public int getSelectId() {
        if (viewGroup == null) return 0;

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox box = (CheckBox) view;
                if (box.isChecked()) {
                    return selectArray.get(box.getId());
                }
            }
        }
        return 0;
    }

    public void reset() {
        // TODO: 2018/3/10
    }

    @Override
    public void onClick(View v) {
        resetOthers(v.getId());
    }

    /**
     * 重置其他之前被选中的控件
     *
     * @param id
     */
    private void resetOthers(int id) {
        if (viewGroup == null) return;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof CheckBox && view.getId() != id) {
                CheckBox box = (CheckBox) view;
                box.setChecked(false);
            }
        }
    }

    private ColorStateList createColorStateList(int normal, int check) {
        int[] colors = new int[]{normal, check};
        int[][] states = new int[2][];
        states[0] = new int[]{};
        states[1] = new int[]{android.R.attr.state_checked};
        return new ColorStateList(states, colors);
    }


    public static class SelectBean {
        private String name;
        private int id;

        public SelectBean() {
        }

        public SelectBean(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

