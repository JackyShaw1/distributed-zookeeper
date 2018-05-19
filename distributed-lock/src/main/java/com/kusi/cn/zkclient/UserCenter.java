package com.kusi.cn.zkclient;

import java.io.Serializable;

public class UserCenter implements Serializable {
    private String mc_id;//机器id
    private String mc_name;//机器名称

    public String getMc_id() {
        return mc_id;
    }

    public void setMc_id(String mc_id) {
        this.mc_id = mc_id;
    }

    public String getMc_name() {
        return mc_name;
    }

    public void setMc_name(String mc_name) {
        this.mc_name = mc_name;
    }
}
