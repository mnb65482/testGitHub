package com.hcll.fishshrimpcrab.login.activity;

import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class SexSelectActivity extends BaseTransparentActivity {

    public static final String EXTRA_SEX_SELECT = "sexSelect";
    public static final int MAN = 0;
    public static final int WOMAN = 1;
    public static Map<Integer, String> map = new HashMap<>();

    static {
        map.put(MAN, "男");
        map.put(WOMAN, "女");
    }

    @Override
    protected String getFirstBtnName() {
        return map.get(MAN);
    }

    @Override
    protected String getSecondBtnName() {
        return map.get(WOMAN);
    }

    @Override
    protected void firstBtnClick() {
        Intent data = new Intent();
        data.putExtra(EXTRA_SEX_SELECT, MAN);
        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    protected void secondBtnClick() {
        Intent data = new Intent();
        data.putExtra(EXTRA_SEX_SELECT, WOMAN);
        setResult(RESULT_OK, data);
        finish();
    }
}
