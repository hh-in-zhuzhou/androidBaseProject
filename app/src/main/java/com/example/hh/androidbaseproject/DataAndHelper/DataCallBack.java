package com.example.hh.androidbaseproject.DataAndHelper;

import com.example.hh.androidbaseproject.Beans.Beans;

/**
 * Created by hh on 16/8/3.
 */
public interface DataCallBack <T extends Beans> {
    void onEnd(T beans);
}
