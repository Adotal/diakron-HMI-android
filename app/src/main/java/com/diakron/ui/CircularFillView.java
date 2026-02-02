package com.diakron.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

//Clase medidor, tiene una barra de progreso circular y una imagen al centro.

public class CircularFillView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private RectF rect;
    private int progress = 0;

    public CircularFillView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(10f);
        backgroundPaint.setColor(0xFF000000);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(10f);
        progressPaint.setColor(0xFF2ECC71); // Verde
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float padding = 10f;
        rect = new RectF(
                padding,
                padding,
                getWidth() - padding,
                getHeight() - padding
        );

        // Círculo base
        canvas.drawOval(rect, backgroundPaint);

        // Arco de progreso (inicio arriba, sentido horario)
        float sweepAngle = (360f * progress) / 100f;
        canvas.drawArc(rect, -90f, sweepAngle, false, progressPaint);
    }

    public void setProgress(int percent) {
        // Validacion
        if (percent < 0) percent = 0;
        if (percent > 100) percent = 100;

        // Establece progreso
        this.progress = percent;
        invalidate();
    }
}
