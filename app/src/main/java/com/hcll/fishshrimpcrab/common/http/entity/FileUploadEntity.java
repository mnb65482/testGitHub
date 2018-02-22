package com.hcll.fishshrimpcrab.common.http.entity;

/**
 * Created by hong on 2018/2/19.
 */

public class FileUploadEntity {
    private String pic_name;
    private String file_ip;
    private String file_port;

    public String getFile_ip() {
        return file_ip;
    }

    public void setFile_ip(String file_ip) {
        this.file_ip = file_ip;
    }

    public String getFile_port() {
        return file_port;
    }

    public void setFile_port(String file_port) {
        this.file_port = file_port;
    }

    public String getPic_name() {
        return pic_name;
    }

    public void setPic_name(String pic_name) {
        this.pic_name = pic_name;
    }
}
