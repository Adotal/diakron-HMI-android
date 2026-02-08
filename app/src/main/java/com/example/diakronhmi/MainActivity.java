package com.example.diakronhmi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.diakron.ui.CircularFillView;
import com.diakron.websocket.MyWebSocketListener;
import com.diakron.websocket.WebSocketInterface;

public class MainActivity extends AppCompatActivity implements WebSocketInterface {

    CircularFillView metalCircle, plasticCircle, paperCircle, glassCircle;
    TextView tvTitle, metalText, plasticText, paperText, glassText;
    ImageView metalImg, plasticImg, paperImg, glassImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        // Asignar interfaz a objetos
        tvTitle = findViewById(R.id.txtTitle);

        // Obtener contenedor de indicadores
        LinearLayout container = findViewById(R.id.indicatorContainer);

        // Obtener circulo y etiqueta de porcentaje de cada tipo de mateiral
        // Metal
        View metalView = container.getChildAt(0);
        metalCircle = metalView.findViewById(R.id.circleView);
        metalText = metalView.findViewById(R.id.txtPercent);
        metalImg = metalView.findViewById(R.id.imgIcon);
        // Plastic
        View plasticView = container.getChildAt(1);
        plasticCircle = plasticView.findViewById(R.id.circleView);
        plasticText = plasticView.findViewById(R.id.txtPercent);
        plasticImg = plasticView.findViewById(R.id.imgIcon);
        // Paper
        View paperView = container.getChildAt(2);
        paperCircle = paperView.findViewById(R.id.circleView);
        paperText = paperView.findViewById(R.id.txtPercent);
        paperImg = paperView.findViewById(R.id.imgIcon);
        // Glass
        View glassView = container.getChildAt(3);
        glassCircle = glassView.findViewById(R.id.circleView);
        glassText = glassView.findViewById(R.id.txtPercent);
        glassImg = glassView.findViewById(R.id.imgIcon);


        // Variabla drawable para establecer imágenes a cada medidor
        Drawable materiaIcon;

        // Imagen de metal
        materiaIcon = getResources().getDrawable(R.drawable.ic_metal_can);
        metalImg.setImageDrawable(materiaIcon);
        // Imagen de plastic
        materiaIcon = getResources().getDrawable(R.drawable.ic_plastic_bottle);
        plasticImg.setImageDrawable(materiaIcon);
        // Imagen de paper
        materiaIcon = getResources().getDrawable(R.drawable.ic_paper_sheet);
        paperImg.setImageDrawable(materiaIcon);
        // Imagen de glass
        materiaIcon = getResources().getDrawable(R.drawable.ic_glass_cup);
        glassImg.setImageDrawable(materiaIcon);

        // Variables de porcentaje de llenado
        int metalPercent = 10;
        int plasticPercent = 30;
        int paperPercent = 40;
        int glassPercent = 60;

        // Establecer nivel de llenado en círuclo y TextView
        metalCircle.setProgress(metalPercent);
        metalText.setText(metalPercent + " %");
        plasticCircle.setProgress(plasticPercent);
        plasticText.setText(plasticPercent + " %");
        paperCircle.setProgress(paperPercent);
        paperText.setText(paperPercent + " %");
        glassCircle.setProgress(glassPercent);
        glassText.setText(glassPercent + " %");

        // Desactivar pantalla anclada manteniendo presionado TextView
        tvTitle.setOnLongClickListener(v -> {
            stopLockTask();
            return true;
        });

        // Create WebSocket
        MyWebSocketListener.getInstance().setActivity(this);
        // Connect WebSocket
        MyWebSocketListener.getInstance().connect();


        // Foto manual
        Button btnPhoto = findViewById(R.id.btnManualPhoto);


        btnPhoto.setOnClickListener(v -> {
            // INICIAR ACTIVITY DE QR Y MANDA ORDEN A ESP32 POR WEBSOCKET
            MyWebSocketListener.getInstance().sendMessage("CAPT");
            Toast.makeText(this, "Orden capturar enviada", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ask for fill levels to update them
        MyWebSocketListener.getInstance().sendMessage("FL");


    }

    // --------------WEBSOCKET INTERFACE OVERRIDE METHODS---------------
    // All must be run with runOnUiThread
    @Override
    public void onMessageReceived(String string) {
        runOnUiThread((() -> {
            Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
        }));

    }

    @Override
    public void onQRPayloadReceived(byte[] byteArrayPayload) {
        runOnUiThread((() -> {
            // Start new intent and send qrPayload by putExtra
            Intent toQRActivity = new Intent(this, QRActivity.class);
            toQRActivity.putExtra("byteArrayPayload", byteArrayPayload);
            startActivity(toQRActivity);
        }));
    }

    @Override
    public void onFillLevelsReceived(byte[] byteArrayPayload) {
        runOnUiThread((() -> {

            Toast.makeText(this, "Received Fill Levels", Toast.LENGTH_SHORT).show();

            int[] fillLevels = new int[4];
            // Receiving fill levels of trash bins in order: Metal, Plastic, CardPaper, Glass
            // [i+2] because indexes 0 and 1 are F and L respectively
            for(int i = 0; i < 4; ++i) {
                fillLevels[i] = byteArrayPayload[i+2] & 0xFF;
                Log.e("FILL:", "["+i+"]: " +fillLevels[i] + " ");
            }

            metalCircle.setProgress(fillLevels[0]);
            metalText.setText(fillLevels[0] + " %");
            plasticCircle.setProgress(fillLevels[1]);
            plasticText.setText(fillLevels[1] + " %");
            paperCircle.setProgress(fillLevels[2]);2
            paperText.setText(fillLevels[2] + " %");
            glassCircle.setProgress(fillLevels[3]);
            glassText.setText(fillLevels[3] + " %");

        }));

    }

    @Override
    public void onConnectionStatus(Boolean connected) {
        runOnUiThread((() -> {

//            if(connected == true)
//                Toast.makeText(this, "CONNECTED ESP32", Toast.LENGTH_SHORT).show();
        }));
    }
}