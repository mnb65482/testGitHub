package com.hcll.fishshrimpcrab.game.entity;

import com.hcll.fishshrimpcrab.base.inter.PageParams;

import java.util.List;

/**
 * Created by lWX385270 on 2018/3/28.
 */

public class GameListEntity implements PageParams<GameListEntity.RoomsBean> {

    /**
     * rooms : [{"is_start":0,"create_time":1517912825,"reside_time":10800,"init_chip":100,"control":1,"source_type":0,"pause":0,"idou":20,"game_status":0,"room_name":"2018第一局","player_count":4,"creator_id":192934,"id":"324959","room_people":0,"onwer_head":"0001100199991929431520323708110","room_master":"123456","room_type":0,"play_type":0}]
     * pageSize : 1
     * nowPage : 1
     * totalpage : 1
     */

    private int pageSize;
    private int nowPage;
    private int totalpage;
    private List<RoomsBean> rooms;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNowPage() {
        return nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public List<RoomsBean> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomsBean> rooms) {
        this.rooms = rooms;
    }

    @Override
    public boolean hasMore() {
        return totalpage > nowPage;
    }

    @Override
    public List<RoomsBean> getList() {
        return getRooms();
    }

    public static class RoomsBean {
        /**
         * is_start : 0
         * create_time : 1517912825
         * reside_time : 10800
         * init_chip : 100
         * control : 1
         * source_type : 0
         * pause : 0
         * idou : 20
         * game_status : 0
         * room_name : 2018第一局
         * player_count : 4
         * creator_id : 192934
         * id : 324959
         * room_people : 0
         * onwer_head : 0001100199991929431520323708110
         * room_master : 123456
         * room_type : 0
         * play_type : 0
         */

        private int is_start;
        private long create_time;
        private long reside_time;
        private int init_chip;
        private int control;
        private int source_type;
        private int pause;
        private int idou;
        private int game_status;
        private String room_name;
        private int player_count;
        private int creator_id;
        private String id;
        private int room_people;
        private String onwer_head;
        private String room_master;
        private int room_type;
        private int play_type;

        public int getIs_start() {
            return is_start;
        }

        public void setIs_start(int is_start) {
            this.is_start = is_start;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public long getReside_time() {
            return reside_time;
        }

        public void setReside_time(long reside_time) {
            this.reside_time = reside_time;
        }

        public int getInit_chip() {
            return init_chip;
        }

        public void setInit_chip(int init_chip) {
            this.init_chip = init_chip;
        }

        public int getControl() {
            return control;
        }

        public void setControl(int control) {
            this.control = control;
        }

        public int getSource_type() {
            return source_type;
        }

        public void setSource_type(int source_type) {
            this.source_type = source_type;
        }

        public int getPause() {
            return pause;
        }

        public void setPause(int pause) {
            this.pause = pause;
        }

        public int getIdou() {
            return idou;
        }

        public void setIdou(int idou) {
            this.idou = idou;
        }

        public int getGame_status() {
            return game_status;
        }

        public void setGame_status(int game_status) {
            this.game_status = game_status;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public int getPlayer_count() {
            return player_count;
        }

        public void setPlayer_count(int player_count) {
            this.player_count = player_count;
        }

        public int getCreator_id() {
            return creator_id;
        }

        public void setCreator_id(int creator_id) {
            this.creator_id = creator_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getRoom_people() {
            return room_people;
        }

        public void setRoom_people(int room_people) {
            this.room_people = room_people;
        }

        public String getOnwer_head() {
            return onwer_head;
        }

        public void setOnwer_head(String onwer_head) {
            this.onwer_head = onwer_head;
        }

        public String getRoom_master() {
            return room_master;
        }

        public void setRoom_master(String room_master) {
            this.room_master = room_master;
        }

        public int getRoom_type() {
            return room_type;
        }

        public void setRoom_type(int room_type) {
            this.room_type = room_type;
        }

        public int getPlay_type() {
            return play_type;
        }

        public void setPlay_type(int play_type) {
            this.play_type = play_type;
        }
    }
}
