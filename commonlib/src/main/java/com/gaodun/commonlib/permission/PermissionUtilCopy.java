package com.gaodun.commonlib.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.gaodun.commonlib.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;


/**
 * Function: 权限管理，需要在需要权限的地方动态申请，对于存储权限需要动态监测，eg：文件操作的中途异常，需要再次请求权限
 * 目前高顿网校涉及危险权限（存储、位置、日历、电话、相机、通讯录）
 * Author Name: 赵振强
 * Date: 2018/12/6
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public class PermissionUtilCopy {

    private static PermissionUtilCopy mPermissionUtil;
    private RxPermissions mRxPermissions;
    private Context mContext;
    //private Fragment mFragment;

    public PermissionUtilCopy() {

    }

    @TargetApi(Build.VERSION_CODES.M)
    public static PermissionUtilCopy instance() {
        if (mPermissionUtil == null) {
            synchronized (PermissionUtilCopy.class) {
                if (mPermissionUtil == null) {
                    mPermissionUtil = new PermissionUtilCopy();
                }
            }
        }
        return mPermissionUtil;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public PermissionUtilCopy with(FragmentActivity activity) {
        mRxPermissions = new RxPermissions(activity);
        mContext = activity;
        return this;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public PermissionUtilCopy with(Fragment fragment) {
        mRxPermissions = new RxPermissions(fragment);
        mContext = fragment.getContext();
        return this;
    }

    /**
     * @param permissionCallback 多个权限请求，会回调多次
     * @param permissions        依次传入请求的权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissions(final OnPermissionSuccessCallback permissionCallback,
                                         final String... permissions) throws IllegalArgumentException{
        if (mRxPermissions == null) {
            throw new IllegalArgumentException("permissionUtil need first call instance() and with()");
        }
        if (permissionCallback == null || permissions == null) {
            throw new IllegalArgumentException("permissionUtil callback and permissions not null");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
/*
            mRxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
                @Override
                public void accept(Permission permission) throws Exception {
                    if (permission.granted) {
                        permissionCallback.onRequestAllow(permission.name);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        String msg = String.format(mContext.getString(R.string.permission_denied), permission.name);

                        Toast.makeText(mContext,msg ,Toast.LENGTH_LONG).show();
                        //permissionCallback.onRequestRefuse(permission.name);
                    } else {
                        String msg = String.format(mContext.getString(R.string.permission_denied_with_setting), permission.name,mContext.getApplicationInfo().loadLabel(mContext.getPackageManager()));

                        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);
                        }
                        //permissionCallback.onRequestNoAsk(permission.name);
                    }
                }
            });
*/

            mRxPermissions.requestEachCombined(permissions).subscribe(new Consumer<Permission>() {
                @Override
                public void accept(Permission permission) throws Exception {
                    if (permission.granted) {
                        permissionCallback.onRequestAllow(permission.name);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        String msg = String.format(mContext.getString(R.string.permission_denied), permission.name);

                        Toast.makeText(mContext,msg ,Toast.LENGTH_LONG).show();
                        //permissionCallback.onRequestRefuse(permission.name);
                    } else {
                        String msg = String.format(mContext.getString(R.string.permission_denied_with_setting), permission.name,mContext.getApplicationInfo().loadLabel(mContext.getPackageManager()));

                        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);
                        }
                        //permissionCallback.onRequestNoAsk(permission.name);
                    }
                }
            });

        } else {
            throw new IllegalArgumentException("permissionUtil call show version>=23");
        }
    }

    public void cleanPermissionConfig(){
        mRxPermissions = null;
    }



/*
    @TargetApi(Build.VERSION_CODES.M)
    public boolean isCameraPermissionOK() {
        boolean ret = true;
        checkPermissions(permissionCallback, Manifest.permission.CAMERA);


        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
            permissionsNeeded.add("CAMERA");
        }

        if (permissionsNeeded.size() > 0) {
            checkPermissions(new RxPermissions(mActivity), permissionCallback, Manifest.permission.CAMERA);
            mActivity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            ret = false;
        }

        return ret;
    }*/
/*
    @TargetApi(Build.VERSION_CODES.M)
    public boolean isStoragePermissionOK() {
        boolean ret = true;

        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Write external storage");
        }

        if (permissionsNeeded.size() > 0) {
            mActivity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            ret = false;
        }

        return ret;
    }
    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean ret = true;
        if (mActivity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            ret = false;
        }
        return ret;
    }*/



    @TargetApi(Build.VERSION_CODES.M)
    public boolean isCameraPermissionOK() {
        return mRxPermissions.isGranted(Manifest.permission.CAMERA);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isStoragePermissionOK() {
        return mRxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isCallPermissionOK() {
        return mRxPermissions.isGranted(Manifest.permission.CALL_PHONE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isLocationPermissionOK() {
        return mRxPermissions.isGranted(Manifest.permission.LOCATION_HARDWARE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isBookPermissionOK() {
        return mRxPermissions.isGranted(Manifest.permission.BODY_SENSORS);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isCalendarPermissionOK() {
        return mRxPermissions.isGranted(Manifest.permission.READ_CALENDAR);
    }

}
