package com.hcll.fishshrimpcrab.game.enums;

/**
 * Created by lWX385270 on 2018/3/28.
 */

public enum GameListType {
    hall(1),
    club(2),
    my(3);

    private int id;

    GameListType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
