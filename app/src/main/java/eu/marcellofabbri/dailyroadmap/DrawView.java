package eu.marcellofabbri.dailyroadmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class DrawView extends View {
    private Paint paintStraight = new Paint();
    private Paint paintRound = new Paint();
    public float fiveHundred = 500;
    public float height = (Resources.getSystem().getDisplayMetrics().heightPixels) / fiveHundred;
    public float width = (Resources.getSystem().getDisplayMetrics().widthPixels) / fiveHundred;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initPaintStraight() {
        paintStraight.setColor(Color.RED);
        paintStraight.setStrokeWidth(40);
        paintStraight.setStyle(Paint.Style.FILL);
    }

    private void initPaintRound() {
        paintRound.setColor(Color.RED);
        paintRound.setStrokeWidth(38);
        paintRound.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas canvas) {
        initPaintStraight();
        initPaintRound();
        for (int i = 0; i < 300; i++) {
            canvas.drawPoint((float) (100*width + 2.8*i), 60*height, paintStraight);
        }
        for (float i = 270; i < 360; i+= 1.5) {
            Point point = convertToCartesian(100, i);
            canvas.drawPoint(400*width + point.x, height*81 + point.y, paintRound);
        }
        for (int i = 100; i < 400; i++) {
            canvas.drawPoint(435*width, height*88, paintRound);
        }

    }

    public Point convertToCartesian(int r, float angle) {
        double x = r * Math.cos(Math.toRadians(angle));
        double y = r * Math.sin(Math.toRadians(angle));
        return new Point((int)x, (int)y);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        invalidate();
    }

}
