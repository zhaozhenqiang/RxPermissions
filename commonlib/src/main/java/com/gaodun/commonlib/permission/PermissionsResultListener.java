package com.gaodun.commonlib.permission;

public interface PermissionsResultListener {

    //成功
    void onSuccessful(int[] grantResults);

    //失败
    void onFailure();
}