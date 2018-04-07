package com.hcll.fishshrimpcrab.club.entity;

import java.util.List;

/**
 * Created by lWX524664 on 2018/3/7.
 */

public class ClubMessageEntity {

    /**
     * hasMore : 0
     * list : [{"title":"加入战队","header":"","content":"","remark":"","time":1233211234567,"clubID":"club_123456","sender":"111111","type":1}]
     * direction : 0
     */

    private int hasMore;
    private int direction;
    private List<ListBean> list;

    public int getHasMore() {
        return hasMore;
    }

    public void setHasMore(int hasMore) {
        this.hasMore = hasMore;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        //通知
        public static final int TYPE_NOTICE = 0;
        //已同意申请
        public static final int TYPE_ACCEPTED = 1;
        //申请加入
        public static final int TYPE_TOACCEPT = 2;
        //同意请求
        public static final int TYPE_ACCEPT_APPLY = 5;
        //（成员）退出
        public static final int TYPE_EXIT_CLUB = 7;
        //（群主）踢出
        public static final int TYPE_REMOVE_CLUB = 8;
        //（群主）解散
        public static final int TYPE_DISMISS_CLUB = 9;
        //设为管理员
        public static final int TYPE_SET_MANAGER = 10;
        //取消管理员
        public static final int TYPE_REMOVE_MANAGER = 11;
        //(群主)拒绝加入战队的申请
        public static final int TYPE_REFUSE_APPLY = 21;
        //（成员）收到加入战队申请被拒通知
        public static final int TYPE_NOTICE_REFUSE = 22;

        /**
         * title : 加入战队
         * header :
         * content :
         * remark :
         * time : 1233211234567
         * clubID : club_123456
         * sender : 111111
         * type : 1
         */

        private String title;
        private String header;
        private String content;
        private String remark;
        private long time;
        private String clubID;
        private String sender;
        private int type;
        private int msgID;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getClubID() {
            return clubID;
        }

        public void setClubID(String clubID) {
            this.clubID = clubID;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getMsgID() {
            return msgID;
        }

        public void setMsgID(int msgID) {
            this.msgID = msgID;
        }
    }
}
