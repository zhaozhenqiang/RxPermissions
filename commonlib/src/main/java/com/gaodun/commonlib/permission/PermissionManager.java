package com.gaodun.commonlib.permission;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.gaodun.commonlib.R;
import com.gaodun.commonlib.util.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static android.content.pm.PackageManager.GET_PERMISSIONS;


/**
 * Function: 权限管理，需要在需要权限的地方动态申请，对于存储权限需要动态监测
 * 目前高顿网校涉及危险权限（存储、位置、日历、电话、相机、通讯录）
 * Author Name: 赵振强
 * Date: 2018/12/6
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public class PermissionManager {

    private RxPermissions mRxPermissions;
    private Context mContext;

    @TargetApi(Build.VERSION_CODES.M)
    public PermissionManager(FragmentActivity activity) {
        mRxPermissions = new RxPermissions(activity);
        mContext = activity;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public PermissionManager(Fragment fragment) {
        mRxPermissions = new RxPermissions(fragment);
        mContext = fragment.getContext();
    }

    /**
     * Function: 多个权限请求，多次回调
     *
     * @param permissionCallback 回调
     * @param permissions        依次传入请求的权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissionsByEach(final OnPermissionDefaultCallback permissionCallback,
                                       final String... permissions) throws IllegalArgumentException {
        initIllegalCheck(permissionCallback, permissions);
        Observable<Permission> observable = mRxPermissions.requestEach(permissions);
        subscribe(observable, permissionCallback);
    }

    /**
     * Function: 多个权限请求，统一单次回调
     * 6.0以下会直接回调授权成功
     * @param permissionCallback 回调
     * @param permissions        依次传入请求的权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissionsByOnce(final OnPermissionDefaultCallback permissionCallback,
                                       final String... permissions) throws IllegalArgumentException {
        initIllegalCheck(permissionCallback, permissions);
        Observable<Permission> observable = mRxPermissions.requestEachCombined(permissions);
        subscribe(observable, permissionCallback);
    }

    /**
     * Function: 绑定
     *
     * @param permissionCallback 回调
     * @param observable
     */
    private void subscribe(Observable<Permission> observable, final OnPermissionDefaultCallback
            permissionCallback) {
        observable.subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    permissionCallback.onRequestAllow(permission.name);
                } else if (permission.shouldShowRequestPermissionRationale) {
                    if (permissionCallback instanceof OnPermissionAllCallback) {
                        ((OnPermissionAllCallback) permissionCallback).onRequestRefuse(permission.name);
                    } else {
                        showNoPermission(permission.name);
                    }
                } else {
                    if (permissionCallback instanceof OnPermissionAllCallback) {
                        ((OnPermissionAllCallback) permissionCallback).onRequestNoAsk(permission.name);
                    } else {
                        showNoAlways(permission.name);
                    }
                }
            }
        });
    }

    //资源回收
    public void cleanPermissionUtil() {
        mRxPermissions = null;
    }

    //是否授权
    @TargetApi(Build.VERSION_CODES.M)
    public boolean isGranted(String permission) {
        return mRxPermissions.isGranted(permission);
    }

    // 是否在包中声明
    public boolean isRevoked(String permission) {
        return mRxPermissions.isRevoked(permission);
    }

    // 非法校验
    public void initIllegalCheck(final OnPermissionDefaultCallback permissionCallback,
                                 final String... permissions) {
        if (mRxPermissions == null) {
            throw new IllegalArgumentException("permissionManager need first call instance() and with()");
        }
        if (permissionCallback == null || permissions == null) {
            throw new IllegalArgumentException("permissionManager callback and permissions not null");
        }
    }

    /**
     * Function:无权限提示
     *
     * @param name 权限名
     */
    public void showNoPermission(String name) {
        String msg = String.format(mContext.getString(R.string.permission_denied), name);
        ToastUtil.getInstance().show(msg);
        //Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }

    /**
     * Function:默认拒绝权限提示及设置页面跳转
     *
     * @param name 权限名
     */
    public void showNoAlways(String name) {
        String msg = String.format(mContext.getString(R.string.permission_denied_with_setting), name, mContext.getApplicationInfo().loadLabel(mContext.getPackageManager()));
        ToastUtil.getInstance().show(msg);
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        }
    }

    //mani注册权限
    public String[] getManifestPermissions() {
        PackageManager pm = mContext.getPackageManager();

        try {
            return pm.getPackageInfo(mContext.getPackageName(), GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException var3) {
            return null;
        }
    }

    //版本检查
    public boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }
}
