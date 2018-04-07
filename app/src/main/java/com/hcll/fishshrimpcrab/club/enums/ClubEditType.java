package com.hcll.fishshrimpcrab.club.enums;


/**
 * Created by hong on 2018/3/4.
 */

public enum ClubEditType {

    ClubName("name"),
    ClubRegion("area"),
    ClubDesc("desc");

    private String id;

    ClubEditType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
