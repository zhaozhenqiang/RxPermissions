package com.gaodun.commonlib.permission;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;


/**
 * Function: 权限管理，需要在需要权限的地方动态申请，对于存储权限需要动态监测，eg：文件操作的中途异常，需要再次请求权限
 * Author Name: zhaozhenqiang
 * Date: 2018/12/6
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public class PermissionUtils {

    /**
     * @param activity
     * @param permissionCallback 多个权限请求，会回调多次
     * @param permissions        依次传入请求的权限
     */
    public static void permissions(FragmentActivity activity,
                                   final OnPermissionCallback permissionCallback,
                                   final String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity != null && permissionCallback != null) {
                new RxPermissions(activity)
                        .requestEach(permissions)
                        .subscribe(new Consumer<Permission>() {
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
            for (int i=0;i<length;i++) {
                permissionCallback.onRequestAllow(permissions[i]);
            }
        }
    }

    /**
     * @param fragment
     * @param permissionCallback 多个权限请求，会回调多次
     * @param permissions        依次传入请求的权限
     */
    public static void permissions(Fragment fragment,
                                   final OnPermissionCallback permissionCallback,
                                   final String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fragment != null && permissionCallback != null) {
                new RxPermissions(fragment).requestEach(permissions).subscribe(new Consumer<Permission>() {
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
        } else if (permissionCallback != null && permissions != null) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                permissionCallback.onRequestAllow(permissions[i]);
            }
        }
    }


}
