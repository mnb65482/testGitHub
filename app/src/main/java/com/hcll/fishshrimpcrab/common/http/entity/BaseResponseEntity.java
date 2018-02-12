package com.hcll.fishshrimpcrab.common.http.entity;

/**
 * Created by hong on 2018/2/10.
 */

public class BaseResponseEntity<T> {
    private int status;
    private String msg;
    private T data;


    /**
     * 是否成功
     *
     * @return
     */
    public boolean isSuccessed() {
        if (0 == status) {
            return true;
        } else {
            return false;
        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
