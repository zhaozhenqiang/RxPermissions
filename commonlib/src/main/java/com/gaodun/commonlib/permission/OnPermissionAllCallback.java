package com.gaodun.commonlib.permission;

/**
 * Function: 申请权限三种方法的回调
 * Author Name: 赵振强
 * Date: 2018/12/6
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public interface OnPermissionAllCallback extends OnPermissionDefaultCallback {
    //允许
    void onRequestAllow(String permissionName);

    //拒绝
    void onRequestRefuse(String permissionName);

    //不再询问
    void onRequestNoAsk(String permissionName);
}
