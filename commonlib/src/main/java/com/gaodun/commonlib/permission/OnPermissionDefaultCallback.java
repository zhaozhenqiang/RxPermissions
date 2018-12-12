package com.gaodun.commonlib.permission;

/**
 * Function: 申请权限默认回调，仅成功回调，失败与不再询问默认实现
 * Author Name: 赵振强
 * Date: 2018/12/6
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public interface OnPermissionDefaultCallback {
    //允许
    void onRequestAllow(String permissionName);
}
