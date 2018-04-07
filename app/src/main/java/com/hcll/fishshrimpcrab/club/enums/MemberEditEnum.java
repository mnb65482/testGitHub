package com.hcll.fishshrimpcrab.club.enums;

import android.support.annotation.ColorRes;

import com.hcll.fishshrimpcrab.R;

/**
 * Created by hong on 2018/3/5.
 */

public enum MemberEditEnum {

    Creator(1, "创建者", R.color.app_color_blue, 0, 0),
    Manager(4, "取消管理员", R.color.comm_red, 2, 2),
    Member(2, "设为管理员", R.color.common_text_color, 1, 4);

    private int id;
    private String desc;
    private
    @ColorRes
    int colorId;
    private int ctrl;
    private int changeType;

    MemberEditEnum(int id, String desc, int colorId, int ctrl, int changeType) {
        this.id = id;
        this.desc = desc;
        this.colorId = colorId;
        this.ctrl = ctrl;
        this.changeType = changeType;
    }

    public static String getDescById(int id) {
        for (MemberEditEnum anEnum : values()) {
            if (anEnum.id == id) {
                return anEnum.desc;
            }
        }
        return "";
    }

    public static int getColorById(int id) {
        for (MemberEditEnum anEnum : values()) {
            if (anEnum.id == id) {
                return anEnum.colorId;
            }
        }
        return 0;
    }

    public static int getControlCodeById(int id) {
        for (MemberEditEnum anEnum : values()) {
            if (anEnum.id == id) {
                return anEnum.ctrl;
            }
        }
        return 0;
    }

    public static int getChangeTypeById(int id) {
        for (MemberEditEnum anEnum : values()) {
            if (anEnum.id == id) {
                return anEnum.changeType;
            }
        }
        return 0;
    }


}
