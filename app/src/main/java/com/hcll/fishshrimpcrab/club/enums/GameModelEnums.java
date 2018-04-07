package com.hcll.fishshrimpcrab.club.enums;

/**
 * Created by hong on 2018/3/10.
 */

public enum GameModelEnums implements GameFilterInterface {
    standard(1, "标准") {
        @Override
        public String getEnumName() {
            return getName();
        }

        @Override
        public int getEnumId() {
            return getId();
        }
    },

    eat(2, "吃骰") {
        @Override
        public String getEnumName() {
            return getName();
        }

        @Override
        public int getEnumId() {
            return getId();
        }
    },
    move(3, "挪注") {
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

    GameModelEnums(int id, String name) {
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
