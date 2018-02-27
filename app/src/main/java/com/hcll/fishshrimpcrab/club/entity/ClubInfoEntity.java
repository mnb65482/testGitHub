package com.hcll.fishshrimpcrab.club.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hong on 2018/2/25.
 */

public class ClubInfoEntity implements Serializable {

    /**
     * idou : 999500
     * list : [{"onlineCount":1,"showId":"club_782261","areaID":"22","name":"第三俱乐部","header":"1.png","id":"club_101401","time":1517987585,"totalCount":100,"type":1,"status":0}]
     */

    private int idou;   //剩余钻石
    private List<ListBean> list;

    public int getIdou() {
        return idou;
    }

    public void setIdou(int idou) {
        this.idou = idou;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * onlineCount : 1
         * showId : club_782261
         * areaID : 22
         * name : 第三俱乐部
         * header : 1.png
         * id : club_101401
         * time : 1517987585
         * totalCount : 100
         * type : 1
         * status : 0
         */

        private int onlineCount;
        private String showId;
        private String areaID;
        private String name;
        private String header;
        private String id;
        private int time;
        private int totalCount;
        private int type;
        private int status;

        public int getOnlineCount() {
            return onlineCount;
        }

        public void setOnlineCount(int onlineCount) {
            this.onlineCount = onlineCount;
        }

        public String getShowId() {
            return showId;
        }

        public void setShowId(String showId) {
            this.showId = showId;
        }

        public String getAreaID() {
            return areaID;
        }

        public void setAreaID(String areaID) {
            this.areaID = areaID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
