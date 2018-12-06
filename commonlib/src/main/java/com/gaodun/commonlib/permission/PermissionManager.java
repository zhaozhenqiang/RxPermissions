package com.gaodun.commonlib.permission;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.functions.Consumer;


/**
 * Function: 权限管理
 * Author Name: zhaozhenqiang
 * Date: 2018/12/6
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public class PermissionManager {

    private static PermissionManager permissionManager;
    private AppCompatActivity activity;

    public PermissionManager() {

    }

    public static PermissionManager instance() {
        if (permissionManager == null) {
            synchronized (PermissionManager.class) {
                if (permissionManager == null) {
                    permissionManager = new PermissionManager();
                }
            }
        }
        return permissionManager;
    }

    public PermissionManager with(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    public void request(final OnPermissionCallback permissionCallback, final String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.activity != null && permissionCallback != null) {
                RxPermissions rxPermissions = new RxPermissions(this.activity);
                cleanPermissionConfig();
                rxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            permissionCallback.onRequestAllow(permission.name);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            permissionCallback.onRequestRefuse(permission.name);
                        } else {
                            permissionCallback.onRequestNoAsk(permission.name);
                        }
                    }
                });
            }
        }else  if (permissionCallback != null && permissions != null) {
            int length = permissions.length;
            cleanPermissionConfig();
            for (int i=0;i<length;i++) {
                permissionCallback.onRequestAllow(permissions[i]);
            }
        }
    }

    public void cleanPermissionConfig(){
        activity = null;
    }
}
