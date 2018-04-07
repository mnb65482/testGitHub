package com.hcll.fishshrimpcrab.club.enums;

/**
 * Created by hong on 2018/3/10.
 */

public enum GameTypeEnums implements GameFilterInterface {
    fxx(51, "鱼虾蟹") {
        @Override
        public String getEnumName() {
            return getName();
        }

        @Override
        public int getEnumId() {
            return getId();
        }
    };

    private int id;
    private String name;

    GameTypeEnums(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
