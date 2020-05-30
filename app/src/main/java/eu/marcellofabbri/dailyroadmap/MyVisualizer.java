package eu.marcellofabbri.dailyroadmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyVisualizer extends View {
    Paint paint = new Paint();
    float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    float hUnit = screenWidth/(700);
    float vUnit = (screenHeight * (float) 0.70) / 800;
    float leftMargin = hUnit*100;
    float upperMargin = hUnit*45;
    float radius = 20*hUnit;

    public MyVisualizer(Context context) {
        super(context);
    }

    public MyVisualizer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyVisualizer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawStuff(canvas);
    }

    private Paint paintObjectLines() {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(52);
        paintObject.setColor(Color.BLUE);
        paintObject.setTextSize(60);
        return paintObject;
    }

    private Paint paintObjectText() {
        Paint paintObject = new Paint();
        paintObject.setColor(Color.WHITE);
        paintObject.setTextSize(50);
        paintObject.setTextAlign(Paint.Align.CENTER);
        return paintObject;
    }

    public void drawStuff(Canvas canvas) {
        drawTopSection(canvas);
        drawRightSection(canvas);
        drawBottomSection(canvas);
        drawLeftSection(canvas);
    }

    private void drawTopSection(Canvas canvas) {
        canvas.drawLine(leftMargin, upperMargin + 100*hUnit, leftMargin, upperMargin, paintObjectLines());
        for (int i = 0; i < 5; i++) {
            float positionInTheChain = i*100*hUnit;
            canvas.drawLine(leftMargin + positionInTheChain, upperMargin, leftMargin + hUnit*100 + positionInTheChain, upperMargin, paintObjectLines());
        }
        canvas.drawCircle(leftMargin, upperMargin + 100*hUnit, radius, paintObjectLines());
        for (int i = 0; i < 5; i++) {
            float positionInTheChain = i*100*hUnit;
            canvas.drawCircle(leftMargin + positionInTheChain, upperMargin, radius, paintObjectLines());
        }
        canvas.drawText("0", leftMargin, upperMargin + 110*hUnit, paintObjectText());
        for (int i = 0; i < 6; i++) {
            float positionInTheChain = i*100*hUnit;
            canvas.drawText(String.valueOf(i+1), leftMargin + positionInTheChain, upperMargin + 8*vUnit, paintObjectText());
        }
    }

    private void drawRightSection(Canvas canvas) {
        for (int i = 0; i < 7; i++ ) {
            float positionInTheChain = i*100*vUnit;
            canvas.drawLine(leftMargin + 500*hUnit, upperMargin + positionInTheChain, leftMargin + 500*hUnit, upperMargin + 100*vUnit + positionInTheChain, paintObjectLines());
        }
        for (int i = 0; i <7 ; i++ ) {
            float positionInTheChain = i*100*vUnit;
            canvas.drawCircle(leftMargin + 500*hUnit, upperMargin + positionInTheChain, radius, paintObjectLines());
        }
        for (int i = 0; i < 7; i++) {
            float positionInTheChain = i*100*vUnit;
            canvas.drawText(String.valueOf(i+6), leftMargin + 500*hUnit, upperMargin + 8*vUnit + positionInTheChain, paintObjectText());
        }
    }

    private void drawBottomSection(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            float positionInTheChain = i*100*hUnit;
            canvas.drawLine(leftMargin + 500*hUnit - positionInTheChain, upperMargin + 700*vUnit, leftMargin + 400*hUnit - positionInTheChain, upperMargin + 700*vUnit, paintObjectLines());
        }
        for (int i = 0; i < 5; i++) {
            float positionInTheChain = i*100*hUnit;
            canvas.drawCircle(leftMargin + 500*hUnit - positionInTheChain, upperMargin + 700*vUnit, radius, paintObjectLines());
        }
        for (int i = 0; i < 5; i++) {
            float positionInTheChain = i*100*hUnit;
            canvas.drawText(String.valueOf(i+1), leftMargin + 500*hUnit - positionInTheChain, upperMargin + 721*hUnit, paintObjectText());
        }
    }

    private void drawLeftSection(Canvas canvas) {
        for (int i = 0; i <5; i++ ) {
            float positionInTheChain = i*100*vUnit;
            canvas.drawLine(leftMargin, upperMargin + 700*vUnit - positionInTheChain, leftMargin, upperMargin + 600*vUnit - positionInTheChain, paintObjectLines());
        }
        canvas.drawLine(leftMargin, upperMargin + 200*vUnit, leftMargin + 30*vUnit, upperMargin + 100*vUnit, paintObjectLines());

        for (int i = 0; i < 6; i++ ) {
            float positionInTheChain = i*100*hUnit;
            canvas.drawCircle(leftMargin, upperMargin + 700*vUnit - positionInTheChain, radius, paintObjectLines());
        }
        canvas.drawCircle(leftMargin, upperMargin + 200*vUnit, 13*hUnit, paintObjectLines());
        canvas.drawCircle(leftMargin + 30*hUnit, upperMargin + 100*vUnit, radius, paintObjectLines());
        for (int i = 0; i < 6; i++) {
            float positionInTheChain = i*100*hUnit;
            canvas.drawText(String.valueOf(i+6), leftMargin, upperMargin + 708*vUnit - positionInTheChain, paintObjectText());
        }
    }


}
