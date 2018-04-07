package com.hcll.fishshrimpcrab.club.enums;

/**
 * Created by hong on 2018/3/10.
 */

public enum GamePeopleEnum implements GameFilterInterface {
    four(1, "2-4") {
        @Override
        public String getEnumName() {
            return getName();
        }

        @Override
        public int getEnumId() {
            return getId();
        }
    },
    seven(2, "5-7") {
        @Override
        public String getEnumName() {
            return getName();
        }

        @Override
        public int getEnumId() {
            return getId();
        }
    },
    nine(3, "8-9") {
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

    GamePeopleEnum(int id, String name) {
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
