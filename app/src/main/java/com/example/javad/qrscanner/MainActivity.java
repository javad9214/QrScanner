package com.example.javad.qrscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    CodeScannerView scannerView ;
    public static final String TAG = "===>" ;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToCameraAccepted = false;
    private String [] permissions = {Manifest.permission.CAMERA };
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    boolean CamStart = false ;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CAMERA_PERMISSION:
                permissionToCameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                Log.i(TAG, "onRequestPermissionsResult: ");
                startCamera();
                break;
        }
        if (!permissionToCameraAccepted) finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerView = findViewById(R.id.scanner_view);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_PERMISSION);

    }


    private void startCamera (){

        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this , result.getText(), Toast.LENGTH_SHORT).show();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText()));
                        startActivity(browserIntent);
                    }
                });
            }
        });

        CamStart = true ;
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        mCodeScanner.startPreview();

    }



    @Override
    protected void onResume() {
        super.onResume();
        if (CamStart)
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if (CamStart)
        mCodeScanner.releaseResources();
        super.onPause();
    }


}
