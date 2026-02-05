package com.example.diakronhmi;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Arrays;

import okio.ByteString;

public class QRActivity extends AppCompatActivity {

    // Donde se muestra código QR
    ImageView qrImg;

    // Botón volver
     Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qractivity);

        // Asigna interfaz a objetos
        qrImg = findViewById(R.id.qrImg);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Anclar la pantalla al iniciar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            startLockTask();
        }
        // Ocultar UI
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        // Evitar que la pantalla se duerma
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        // Create QR Code
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            // Get byteArrayPayload by extra
            byte[] byteArrayPayload = getIntent().getByteArrayExtra("byteArrayPayload");

            // Create string of QR payload (Base64 to achieve the smaller size possible)
            // 80 Bytes ->  107 Base64 Char
            String qrPayload = Base64.encodeToString(
                    byteArrayPayload,
                    Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE
            );

            Bitmap bitmap = barcodeEncoder.encodeBitmap(qrPayload, BarcodeFormat.QR_CODE, 1000, 1000);
            ImageView imageViewQrCode = (ImageView) findViewById(R.id.qrImg);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (Exception e){
            e.printStackTrace();
        }


        // Destroy activity after 60 secs
        Toast.makeText(this, "Deleting Activity in 60 secs", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 30000);
    }
}