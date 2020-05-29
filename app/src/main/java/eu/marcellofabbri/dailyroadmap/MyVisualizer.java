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
    float unit = screenWidth/(700);
    float leftMargin = unit*100;
    float upperMargin = unit*60;

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

    private Paint paintObjectBlueprintLines() {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(30);
        paintObject.setColor(Color.BLUE);
        return paintObject;
    }

    private Paint paintObjectBlueprintPoints() {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(50);
        paintObject.setColor(Color.BLUE);
        return paintObject;
    }

    public void drawStuff(Canvas canvas) {
        drawTopSection(canvas);
        drawRightSection(canvas);
        drawBottomSection(canvas);
        drawLeftSection(canvas);
    }

    private void drawTopSection(Canvas canvas) {
        canvas.drawLine(leftMargin, upperMargin + 100*unit, leftMargin, upperMargin, paintObjectBlueprintLines());
        for (int i = 0; i < 5; i++) {
            float positionInTheChain = i*100*unit;
            canvas.drawLine(leftMargin + positionInTheChain, upperMargin, leftMargin + unit*100 + positionInTheChain, upperMargin, paintObjectBlueprintLines());
        }
        canvas.drawCircle(leftMargin, upperMargin + 100*unit, 13*unit, paintObjectBlueprintLines());
        for (int i = 0; i < 5; i++) {
            float positionInTheChain = i*100*unit;
            canvas.drawCircle(leftMargin + positionInTheChain, upperMargin, 13*unit, paintObjectBlueprintLines());
        }
    }

    private void drawRightSection(Canvas canvas) {
        for (int i = 0; i <7; i++ ) {
            float positionInTheChain = i*100*unit;
            canvas.drawLine(leftMargin + 500*unit, upperMargin + positionInTheChain, leftMargin + 500*unit, upperMargin + 100*unit + positionInTheChain, paintObjectBlueprintLines());
        }
        for (int i = 0; i <7; i++ ) {
            float positionInTheChain = i*100*unit;
            canvas.drawCircle(leftMargin + 500*unit, upperMargin + positionInTheChain, 13*unit, paintObjectBlueprintLines());
        }
    }

    private void drawBottomSection(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            float positionInTheChain = i*100*unit;
            canvas.drawLine(leftMargin + 500*unit - positionInTheChain, upperMargin + 700*unit, leftMargin + 400*unit - positionInTheChain, upperMargin + 700*unit, paintObjectBlueprintLines());
        }
        for (int i = 0; i < 5; i++) {
            float positionInTheChain = i*100*unit;
            canvas.drawCircle(leftMargin + 500*unit - positionInTheChain, upperMargin + 700*unit, 13*unit, paintObjectBlueprintLines());
        }
    }

    private void drawLeftSection(Canvas canvas) {
        for (int i = 0; i <5; i++ ) {
            float positionInTheChain = i*100*unit;
            canvas.drawLine(leftMargin, upperMargin + 700*unit - positionInTheChain, leftMargin, upperMargin + 600*unit - positionInTheChain, paintObjectBlueprintLines());
        }
        canvas.drawLine(leftMargin, upperMargin + 200*unit, leftMargin + 30*unit, upperMargin + 100*unit, paintObjectBlueprintLines());

        for (int i = 0; i <5; i++ ) {
            float positionInTheChain = i*100*unit;
            canvas.drawCircle(leftMargin, upperMargin + 700*unit - positionInTheChain, 13*unit, paintObjectBlueprintLines());
        }
        canvas.drawCircle(leftMargin, upperMargin + 200*unit, 13*unit, paintObjectBlueprintLines());
        canvas.drawCircle(leftMargin + 30*unit, upperMargin + 100*unit, 13*unit, paintObjectBlueprintLines());
    }

}
