package com.hcll.fishshrimpcrab.base;


import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.hcll.fishshrimpcrab.R;
import com.qmuiteam.qmui.widget.QMUITopBar;

/**
 * Created by cgspine on 2018/1/7.
 */

public abstract class BaseFragment extends Fragment {

    private View topbarLayout;
    private QMUITopBar topBar;
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parent = LayoutInflater.from(getContext()).inflate(R.layout.activity_base, container, false);
        topbarLayout = parent.findViewById(R.id.base_topbar_layout);
        topBar = (QMUITopBar) parent.findViewById(R.id.base_topbar);
        ViewGroup group = (ViewGroup) parent.findViewById(R.id.container_body);

        contentView = inflater.inflate(getContentView(), null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        group.addView(contentView, lp);

        onInitView();

        return parent;
    }

    /**
     * 标准流程-初始化
     */
    public abstract void onInitView();

    public abstract int getContentView();


    public final View findViewById(int id) {
        return contentView.findViewById(id);
    }

    protected void hideTopBar() {
        topbarLayout.setVisibility(View.GONE);
    }

    protected void showTopBar() {
        topbarLayout.setVisibility(View.VISIBLE);
    }

    protected QMUITopBar getTopBar() {
        return topBar;
    }

    protected View getFragView() {
        return contentView;
    }


    /**
     * 隐藏软键盘
     *
     * @return void [返回类型说明]
     * @throws throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    protected void hideKeyboard(IBinder binder) {
        if (binder == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(
                this.getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binder, 0);
    }


}
