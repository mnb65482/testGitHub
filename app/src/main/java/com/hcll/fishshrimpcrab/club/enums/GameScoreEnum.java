package com.hcll.fishshrimpcrab.club.enums;

/**
 * Created by hong on 2018/3/10.
 */

public enum GameScoreEnum implements GameFilterInterface {
    ten(1, "1-10") {
        @Override
        public String getEnumName() {
            return getName();
        }

        @Override
        public int getEnumId() {
            return getId();
        }
    },
    twenty(2, "20-100") {
        @Override
        public String getEnumName() {
            return getName();
        }

        @Override
        public int getEnumId() {
            return getId();
        }
    },
    hundred(3, "200-500") {
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

    GameScoreEnum(int id, String name) {
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
