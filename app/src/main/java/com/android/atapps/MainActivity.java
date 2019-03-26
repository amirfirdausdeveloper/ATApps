package com.android.atapps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import net.doo.snap.ScanbotSDK;
import net.doo.snap.blob.BlobFactory;
import net.doo.snap.blob.BlobManager;
import net.doo.snap.camera.CameraOpenCallback;
import net.doo.snap.camera.ScanbotCameraView;
import net.doo.snap.entity.Blob;
import net.doo.snap.mrzscanner.MRZScanner;
import net.doo.snap.mrzscanner.MRZScannerFrameHandler;
import net.doo.snap.util.log.Logger;
import net.doo.snap.util.log.LoggerProvider;

import java.io.IOException;

import io.scanbot.mrzscanner.model.MRZRecognitionResult;

public class MainActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    private ScanbotCameraView cameraView;
    private TextView resultView;

    boolean flashEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this)); // avoid crash


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                1);



        cameraView = findViewById(R.id.camera);
        cameraView.setCameraOpenCallback(new CameraOpenCallback() {
            @Override
            public void onCameraOpened() {
                cameraView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cameraView.useFlash(flashEnabled);
                        cameraView.continuousFocus();
                    }
                }, 700);
            }
        });

        resultView = findViewById(R.id.result);

        ScanbotSDK scanbotSDK = new ScanbotSDK(this);
        final MRZScanner mrzScanner = scanbotSDK.mrzScanner();
        MRZScannerFrameHandler mrzScannerFrameHandler = MRZScannerFrameHandler.attach(cameraView, mrzScanner);

        mrzScannerFrameHandler.addResultHandler(new MRZScannerFrameHandler.ResultHandler() {
            @Override
            public boolean handleResult(MRZRecognitionResult mrzRecognitionResult) {
                if (mrzRecognitionResult != null && mrzRecognitionResult.recognitionSuccessful) {
                    long a = System.currentTimeMillis();

                    try {
                        startActivity(MRZResultActivity.newIntent(MainActivity.this, mrzRecognitionResult));
                    } finally {
                        long b = System.currentTimeMillis();

                    }
                }
                return false;
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
    }
    @Override
    public void onBackPressed() {
        Intent next = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(next);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


}



