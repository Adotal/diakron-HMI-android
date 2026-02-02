package com.example.diakronhmi;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.diakron.ui.CircularFillView;

public class MainActivity extends AppCompatActivity {

    CircularFillView metalCircle, plasticCircle, paperCircle, glassCircle;
    TextView tvvTitle, metalText, plasticText, paperText, glassText;
    ImageView metalImg, plasticImg, paperImg, glassImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Asignar interfaz a objetos
        tvvTitle = findViewById(R.id.txtTitle);

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
        tvvTitle.setOnLongClickListener(v -> {
            stopLockTask();
            return true;
        });

        // Evitar que la pantalla se duerma
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Anclar la pantalla al iniciar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startLockTask();
        }

        // Ocultar UI
        hideSystemUI();

//        handler.post(uiHider);
    }

    private void hideSystemUI() {
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

}