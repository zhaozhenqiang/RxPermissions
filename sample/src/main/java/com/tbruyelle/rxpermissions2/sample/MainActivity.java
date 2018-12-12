package com.tbruyelle.rxpermissions2.sample;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.gaodun.commonlib.permission.OnPermissionAllCallback;
import com.gaodun.commonlib.permission.OnPermissionDefaultCallback;
import com.gaodun.commonlib.permission.PermissionManager;

import java.io.IOException;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RxPermissionsSample";

    private Camera camera;
    private SurfaceView surfaceView;
    private Disposable disposable;
    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        surfaceView = findViewById(R.id.surfaceView);
        findViewById(R.id.enableCamera).setOnClickListener(this);
        findViewById(R.id.enableCamera2).setOnClickListener(this);
        findViewById(R.id.enableCamera3).setOnClickListener(this);
        findViewById(R.id.enableCamera4).setOnClickListener(this);
        permissionManager = new PermissionManager(this);
        String[] test = permissionManager.getManifestPermissions();
        for(int i=0;i<test.length;i++) {
            Log.e("manifest:", test[i]);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enableCamera:
                permissionManager.checkPermissionsByEach(
                        new OnPermissionDefaultCallback() {
                            @Override
                            public void onRequestAllow(String permissionName) {
                                if (Manifest.permission.CAMERA.equals(permissionName)) {
                                    releaseCamera();
                                    camera = Camera.open(0);
                                    try {
                                        camera.setPreviewDisplay(surfaceView.getHolder());
                                        camera.startPreview();
                                    } catch (IOException e) {
                                        Log.e(TAG, "Error while trying to display the camera preview", e);
                                    }
                                }
                            }

                        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE);
                break;
            case R.id.enableCamera2:
                permissionManager.checkPermissionsByOnce(
                        new OnPermissionDefaultCallback() {
                            @Override
                            public void onRequestAllow(String permissionName) {
                                if (permissionName.contains(Manifest.permission.CAMERA)) {
                                    releaseCamera();
                                    camera = Camera.open(0);
                                    try {
                                        camera.setPreviewDisplay(surfaceView.getHolder());
                                        camera.startPreview();
                                    } catch (IOException e) {
                                        Log.e(TAG, "Error while trying to display the camera preview", e);
                                    }
                                }
                            }
                        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest
                                .permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.CALL_PHONE);

                break;
            case R.id.enableCamera3:
                permissionManager.checkPermissionsByEach(
                        new OnPermissionAllCallback() {
                            @Override
                            public void onRequestAllow(String permissionName) {
                                if (permissionName.contains(Manifest.permission.CAMERA)) {
                                    releaseCamera();
                                    camera = Camera.open(0);
                                    try {
                                        camera.setPreviewDisplay(surfaceView.getHolder());
                                        camera.startPreview();
                                    } catch (IOException e) {
                                        Log.e(TAG, "Error while trying to display the camera preview", e);
                                    }
                                }
                            }

                            @Override
                            public void onRequestRefuse(String permissionName) {
                                permissionManager.showNoPermission("相关权限");

                            }

                            public void onRequestNoAsk(String
                                                               permissionName) {
                                permissionManager.showNoPermission("相关权限");

                            }

                        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE);
                break;
            case R.id.enableCamera4:
                permissionManager.checkPermissionsByOnce(
                        new OnPermissionAllCallback() {
                            @Override
                            public void onRequestAllow(String permissionName) {
                                if (permissionName.contains(Manifest.permission.CAMERA)) {
                                    releaseCamera();
                                    camera = Camera.open(0);
                                    try {
                                        camera.setPreviewDisplay(surfaceView.getHolder());
                                        camera.startPreview();
                                    } catch (IOException e) {
                                        Log.e(TAG, "Error while trying to display the camera preview", e);
                                    }
                                }
                            }

                            @Override
                            public void onRequestRefuse(String permissionName) {
                                permissionManager.showNoPermission("相关权限");

                            }

                            public void onRequestNoAsk(String permissionName) {
                                permissionManager.showNoPermission("相关权限");

                            }
                        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

}