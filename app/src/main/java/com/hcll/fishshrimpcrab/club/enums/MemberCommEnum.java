package com.hcll.fishshrimpcrab.club.enums;

import android.support.annotation.ColorRes;

import com.hcll.fishshrimpcrab.R;

/**
 * Created by hong on 2018/3/5.
 */

public enum MemberCommEnum {
    Creator(1, "创建者", R.color.app_color_blue),
    //    CommMemberEnum(2, "", 0),
    Manager(4, "管理员", R.color.common_text_color);

    private int id;
    private String desc;
    private
    @ColorRes
    int colorId;

    MemberCommEnum(int id, String desc, int colorId) {
        this.id = id;
        this.desc = desc;
        this.colorId = colorId;
    }

    public static String getDescById(int id) {
        for (MemberCommEnum anEnum : values()) {
            if (anEnum.id == id) {
                return anEnum.desc;
            }
        }
        return "";
    }

    public static int getColorById(int id) {
        for (MemberCommEnum anEnum : values()) {
            if (anEnum.id == id) {
                return anEnum.colorId;
            }
        }
        return 0;
    }
}
