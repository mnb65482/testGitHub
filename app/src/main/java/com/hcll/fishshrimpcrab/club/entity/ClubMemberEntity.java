package com.hcll.fishshrimpcrab.club.entity;

/**
 * Created by hong on 2018/3/5.
 */

public class ClubMemberEntity {

    /**
     * uid : 192934
     * signature : 哈哈哈
     * sex : 1
     * head : 123.png
     * nick : 叫我大王
     * type : 1
     */

    private int uid;
    private String signature;
    private int sex;
    private String head;
    private String nick;
    private int type;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
