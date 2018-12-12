package com.gaodun.commonlib.util;

import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gaodun.commonlib.app.BaseApplication;

/**
 * Function: Toast相关工具类
 * Author Name: yinmenglei
 * Date: 2018/12/7 13:39
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public class ToastUtil {

    private static ToastUtil mToastUtil;
    private Toast mToast;

    /**
     * 获取ToastUtil实例
     */
    public static ToastUtil getInstance() {
        if (mToastUtil == null) {
            mToastUtil = new ToastUtil();
        }
        return mToastUtil;
    }

    private static Handler mHandler = new Handler();

    private Runnable r = new Runnable() {
        public void run() {
            if (mToast != null) {
                mToast.cancel();
            }
        }
    };

    public void show(int resId) {
        String msg = BaseApplication.getInstance().getResources().getString(resId);
        show(msg);
    }

    public void show(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        mHandler.removeCallbacks(r);

        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }

        mToast.setGravity(Gravity.CENTER, 0, 0);
        mHandler.postDelayed(r, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 带图片的Toast
     *
     * @param resId：资源文字
     * @param resImage：资源图片
     */
    public void showToastWithImg(int resId, int resImage) {
        String msg = BaseApplication.getInstance().getResources().getString(resId);
        showToastWithImg(msg, resImage);
    }

    /**
     * 带图片的Toast
     *
     * @param msg：提示文字
     * @param resImage：资源图片
     */
    public void showToastWithImg(String msg, int resImage) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (resImage == 0) {
            show(msg);
            return;
        }
        mHandler.removeCallbacks(r);

        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getInstance(), msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }

        mToast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) mToast.getView();
        ImageView imageView = new ImageView(BaseApplication.getInstance());
        imageView.setImageResource(resImage);
        toastView.addView(imageView, 0);
        mToast.show();
    }

}
