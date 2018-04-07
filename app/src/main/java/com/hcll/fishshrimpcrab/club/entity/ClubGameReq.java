package com.hcll.fishshrimpcrab.club.entity;

/**
 * Created by hong on 2018/3/9.
 */
public class ClubGameReq {

    /**
     * user_id : 192934
     * gid : club_101430
     * time : 0
     * direction : 10
     * room_path : 51
     * have_seat : 0
     * room_type : 0
     * num_type : 1
     * score_type : 2
     * game_type : 1
     * page_num : 2
     */

    private int user_id;
    private String gid;
    private long time;
    private int direction;
    private int room_path;
    private int have_seat;
    private int room_type;
    private int num_type;
    private int score_type;
    private int game_type;
    private int page_num = 1;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getRoom_path() {
        return room_path;
    }

    public void setRoom_path(int room_path) {
        this.room_path = room_path;
    }

    public int getHave_seat() {
        return have_seat;
    }

    public void setHave_seat(int have_seat) {
        this.have_seat = have_seat;
    }

    public int getRoom_type() {
        return room_type;
    }

    public void setRoom_type(int room_type) {
        this.room_type = room_type;
    }

    public int getNum_type() {
        return num_type;
    }

    public void setNum_type(int num_type) {
        this.num_type = num_type;
    }

    public int getScore_type() {
        return score_type;
    }

    public void setScore_type(int score_type) {
        this.score_type = score_type;
    }

    public int getGame_type() {
        return game_type;
    }

    public void setGame_type(int game_type) {
        this.game_type = game_type;
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }
}
