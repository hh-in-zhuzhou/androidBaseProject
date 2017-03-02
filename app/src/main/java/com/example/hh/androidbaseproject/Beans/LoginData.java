package com.example.hh.androidbaseproject.Beans;

import java.util.List;

/**
 * Created by hh on 16/8/2.
 */
public class LoginData extends Beans {
    public List<LoginItemData> record;

    public static class LoginItemData {
        public Integer 序号;
        public String 客户名称;
        public String 客户代码;
        public String 客服人员;
        public String 用户状态;
        public String 权限;
        public Double 透支额度;
        public Double 剩余金额;
        public String 城市码;
        public String 区域码;
        public String 会员级别;

        public String 密码;
    }
}
