package com.tbruyelle.rxpermissions2.sample;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.gaodun.commonlib.permission.OnPermissionSuccessCallback;
import com.gaodun.commonlib.permission.PermissionUtil;

import java.io.IOException;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RxPermissionsSample";

    private Camera camera;
    private SurfaceView surfaceView;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        surfaceView = findViewById(R.id.surfaceView);
        findViewById(R.id.enableCamera).setOnClickListener(this);
        findViewById(R.id.enableCamera2).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.enableCamera) {
            PermissionUtil.instance()
                    .with(MainActivity.this)
                    .checkPermissionsByEach(new OnPermissionSuccessCallback() {
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

                                            }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest
                                    .permission
                                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE
                            , Manifest.permission.CALL_PHONE);
        } else {
            PermissionUtil.instance()
                    .with(MainActivity.this)
                    .checkPermissionsByOnce(new OnPermissionSuccessCallback() {
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
                                            }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest
                                    .permission
                                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE
                            , Manifest.permission.CALL_PHONE);
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