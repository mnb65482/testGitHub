package com.hcll.fishshrimpcrab.club.enums;

/**
 * Created by hong on 2018/3/10.
 */

public enum GameSeatEnums implements GameFilterInterface {
    no(1, "无座") {
        @Override
        public String getEnumName() {
            return getName();
        }

        @Override
        public int getEnumId() {
            return getId();
        }
    },
    yes(2, "有座") {
        @Override
        public String getEnumName() {
            return getName();
        }

        @Override
        public int getEnumId() {
            return getId();
        }
    };
    private String name;
    private int id;

    GameSeatEnums(int id, String name) {
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
