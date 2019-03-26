package com.android.atapps;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import net.doo.snap.ScanbotSDK;
import net.doo.snap.blob.BlobFactory;
import net.doo.snap.blob.BlobManager;
import net.doo.snap.entity.Blob;
import net.doo.snap.util.log.Logger;
import net.doo.snap.util.log.LoggerProvider;

import java.io.IOException;

public class Splash extends AppCompatActivity {

    private final Logger logger = LoggerProvider.getLogger();
    private Uri file;
    private ScanbotSDK scanbotSDK;
    private BlobManager blobManager;
    private BlobFactory blobFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initDependencies();
        downloadMRZTraineddata();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent next = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(next);
            }
        }, 3500);
    }

    private void initDependencies() {
        scanbotSDK = new ScanbotSDK(this);
        blobManager = scanbotSDK.blobManager();
        blobFactory = scanbotSDK.blobFactory();
    }

    private void downloadMRZTraineddata() {
        try {
            final Blob mrzBlob = blobFactory.mrzTraineddataBlob();
            if (!blobManager.isBlobAvailable(mrzBlob)) {
                new DownloadOCRDataTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                return;
            }
        } catch (IOException e) {
            logger.logException(e);
        }
    }

    private boolean checkMRZTraineddata() {
        try {
            final Blob mrzBlob = blobFactory.mrzTraineddataBlob();
            if (blobManager.isBlobAvailable(mrzBlob)) {
                return true;
            }
        } catch (IOException e) {
            logger.logException(e);
        }
        final Toast toast = Toast.makeText(Splash.this, "Please download the OCR data first!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return false;
    }

    private class DownloadOCRDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                blobManager.fetch(blobFactory.mrzTraineddataBlob(), false);
            } catch (IOException e) {
                logger.logException(e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            }
    }
}
