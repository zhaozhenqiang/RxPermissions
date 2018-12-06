package com.tbruyelle.rxpermissions2.sample;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.gaodun.commonlib.permission.OnPermissionCallback;
import com.gaodun.commonlib.permission.PermissionManager;

import java.io.IOException;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxPermissionsSample";

    private Camera camera;
    private SurfaceView surfaceView;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        surfaceView = findViewById(R.id.surfaceView);

        findViewById(R.id.enableCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionManager.instance().with(MainActivity.this).request(new OnPermissionCallback() {
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

                    @Override
                    public void onRequestRefuse(String permissionName) {
                        Toast.makeText(MainActivity.this,
                                "Denied permission without ask never again",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRequestNoAsk(String permissionName) {
                        Toast.makeText(MainActivity.this,
                                "Permission denied, can't enable the camera",
                                Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.CAMERA);
            }
        });

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