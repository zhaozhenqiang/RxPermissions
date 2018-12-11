package com.gaodun.commonlib.permission;

/**
 * Function: 申请权限回调
 * Author Name: zhaozhenqiang
 * Date: 2018/12/6
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public interface OnPermissionSuccessCallback {
    //允许
    void onRequestAllow(String permissionName);
}
