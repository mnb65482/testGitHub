package com.hcll.fishshrimpcrab.club.entity;

/**
 * Created by hong on 2018/3/5.
 */

public class UserInfoEntity {

    /**
     * nick : 123456
     * idou : 0
     * head_pic_name : null
     * chip : 1000
     * modify_nick_num : 1
     * person_sign : 哈哈哈
     * sex : 1
     * game_cnt : 0
     * randomNum : 79435777786601
     */

    private String nick;
    private int idou;
    private String head_pic_name;
    private int chip;
    private int modify_nick_num;
    private String person_sign;
    private int sex;
    private int game_cnt;
    private String randomNum;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getIdou() {
        return idou;
    }

    public void setIdou(int idou) {
        this.idou = idou;
    }

    public String getHead_pic_name() {
        return head_pic_name;
    }

    public void setHead_pic_name(String head_pic_name) {
        this.head_pic_name = head_pic_name;
    }

    public int getChip() {
        return chip;
    }

    public void setChip(int chip) {
        this.chip = chip;
    }

    public int getModify_nick_num() {
        return modify_nick_num;
    }

    public void setModify_nick_num(int modify_nick_num) {
        this.modify_nick_num = modify_nick_num;
    }

    public String getPerson_sign() {
        return person_sign;
    }

    public void setPerson_sign(String person_sign) {
        this.person_sign = person_sign;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getGame_cnt() {
        return game_cnt;
    }

    public void setGame_cnt(int game_cnt) {
        this.game_cnt = game_cnt;
    }

    public String getRandomNum() {
        return randomNum;
    }

    public void setRandomNum(String randomNum) {
        this.randomNum = randomNum;
    }
}
