package com.gaodun.commonlib.app;

import android.app.Application;

/**
 * Function:BaseApplication
 * Author Name: freedie
 * Date: 2018/12/6 1:54 PM
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public class BaseApplication extends Application {

    private static BaseApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

}
