package com.gaodun.commonlib.permission;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liujing on 2018/12/11.
 */

public class OnPermissionCallbackIm implements OnPermissionCallback{

    private Context mContext;

    //允许
    public void onRequestAllow(String permissionName){
        return;
    }

    //拒绝
    public void onRequestRefuse(String permissionName){
        Toast.makeText(mContext,"You need to grant access to "+permissionName ,Toast.LENGTH_SHORT).show();
    }

    //不在询问
    public void onRequestNoAsk(String permissionName){

    }
}