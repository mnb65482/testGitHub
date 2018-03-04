package com.hcll.fishshrimpcrab.club.entity;

import android.support.annotation.DrawableRes;

import com.hcll.fishshrimpcrab.R;

/**
 * Created by hong on 2018/3/5.
 */

public enum GenderEnum {
    boy(0, "男", R.drawable.comm_boy_ic),
    girl(1, "女", R.drawable.comm_girl_ic);

    private int id;
    private String name;
    private
    @DrawableRes
    int drawableId;

    GenderEnum(int drawableId, String name, int id) {
        this.drawableId = drawableId;
        this.id = id;
        this.name = name;
    }

    public static int getDrawablebyId(int id) {
        for (GenderEnum anEnum : values()) {
            if (anEnum.id == id) {
                return anEnum.drawableId;
            }
        }
        return 0;
    }
}
