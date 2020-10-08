package eu.marcellofabbri.dailyroadmap.view.activityHelpers;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import eu.marcellofabbri.dailyroadmap.utils.CustomColors;
import eu.marcellofabbri.dailyroadmap.R;
import eu.marcellofabbri.dailyroadmap.model.Event;
import eu.marcellofabbri.dailyroadmap.utils.EntityFieldConverter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class VerticalTrackPainter extends View {

    EntityFieldConverter converter = new EntityFieldConverter();
    float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    float hUnit = (screenWidth / (700));
    float hMinuteUnit = ((100 * hUnit) / 60);
    float vUnit = ((screenHeight * (float) 0.878 * (float) 0.85) / 900);
    float vMinuteUnit = ((100 * vUnit) / 60);
    float leftMargin = hUnit * 100;
    float upperMargin = hUnit * 56;
    int[] trackDefaultColors = new int[]{ContextCompat.getColor(getContext(), R.color.trackDefaultColor0), ContextCompat.getColor(getContext(), R.color.trackDefaultColor1)};
    int backgroundTrackDefaultColor = ContextCompat.getColor(getContext(), R.color.backgroundDefaultColor1);
    int whatColorPosition = 1;
    int trackDefaultColor = trackDefaultColors[whatColorPosition];
    CustomColors myColors = new CustomColors();
    Integer[] colors = new Integer[]{
            ContextCompat.getColor(getContext(), myColors.getRed()),
            ContextCompat.getColor(getContext(), myColors.getBlue()),
            ContextCompat.getColor(getContext(), myColors.getAmber()),
            ContextCompat.getColor(getContext(), myColors.getGreen()),
            ContextCompat.getColor(getContext(), myColors.getOrange()),
            ContextCompat.getColor(getContext(), myColors.getBlack())
    };
    ArrayList<MyPoint> points = new ArrayList<MyPoint>();
    HashMap<String, MyPoint> map;
    List<Event> events;
    boolean isToday = true;

    public VerticalTrackPainter(Context context, List<Event> events, boolean isToday) {
        super(context);
        this.events = events;
        this.isToday = isToday;
    }

    public VerticalTrackPainter(Context context, @Nullable AttributeSet attrs, List<Event> events) {
        super(context, attrs);
        this.events = events;
    }

    public VerticalTrackPainter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, List<Event> events) {
        super(context, attrs, defStyleAttr);
        this.events = events;
    }

    public void setToday(boolean bool) {
        isToday = bool;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (leftMargin*2);
        int height = (int) (100*vUnit*26);
        setMeasuredDimension(width, height);
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawStuff(canvas);
    }

    private void createPoints(Canvas canvas) {
        for (int i = 0; i < 1440; i++) {
            MyPoint point = new MyPoint(leftMargin, upperMargin + i * vMinuteUnit, i);
            points.add(point);
        }
    }


    protected Paint paintObjectLines() {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(45);
        paintObject.setColor(trackDefaultColor);
        paintObject.setTextSize(60);
        return paintObject;
    }

    protected Paint paintObjectLinesBackground() {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(61);
        paintObject.setColor(backgroundTrackDefaultColor);
        paintObject.setStrokeCap(Paint.Cap.ROUND);
        paintObject.setTextSize(60);
        return paintObject;
    }

    protected Paint paintObjectLines12() {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(39);
        paintObject.setColor(trackDefaultColor);
        paintObject.setTextSize(60);
        return paintObject;
    }

    protected Paint paintObjectLines12Background() {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(65);
        paintObject.setColor(backgroundTrackDefaultColor);
        paintObject.setStrokeCap(Paint.Cap.ROUND);
        paintObject.setTextSize(60);
        return paintObject;
    }

    private Paint paintObjectTest(Integer i) {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(52);
        paintObject.setStrokeCap(Paint.Cap.ROUND);
        paintObject.setColor(i);
        paintObject.setTextSize(60);
        return paintObject;
    }

    private Paint paintObjectHourNumbers() {
        Paint paintObject = new Paint();
        paintObject.setTextSize(55);
        paintObject.setColor(R.color.daytimeBackgroundDarker);
        return paintObject;
    }

    private Paint paintObjectCircles() {
        Paint paintObject = new Paint();
        paintObject.setColor(Color.WHITE);
        paintObject.setStyle(Paint.Style.FILL);
        return paintObject;
    }

    private Paint paintObjectNotches() {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(25);
        paintObject.setColor(trackDefaultColor);
        return paintObject;
    }

    private Paint paintObjectNotchesBackground() {
        Paint paintObject = new Paint();
        paintObject.setStrokeWidth(32);
        paintObject.setColor(Color.DKGRAY);
        return paintObject;
    }

    public void drawStuff(Canvas canvas) {
        createPoints(canvas);
        timesToPointsMapper();
        if (isToday) { drawNotches(canvas, paintObjectNotchesBackground(), getIndexOfCurrentPoint()); }
        drawBlueprintTrack(canvas);
        writeHourNumbers(canvas);
        drawNotches(canvas, paintObjectNotches(), 1439);
        if (events != null) {
            for (int i = 0; i < events.size(); i++) {
                drawEvent(events.get(i), canvas, i);
            }
        }
        if (isToday) { drawPin(canvas); }
    }

    private void drawBlueprintTrack(Canvas canvas) {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String nowString = formatter.format(now);
        MyPoint nowPoint = map.get(nowString);
        int nowPointIndex = nowPoint.index;

        //DRAW BACKGROUND CURRENT-TIME TRACK (IF DAY IS TODAY)
        if (isToday) {
            if (nowPointIndex < 1380) {
                for (int i = 0; i < nowPointIndex; i++) {
                    canvas.drawPoint(points.get(i).x, points.get(i).y, paintObjectLinesBackground());
                }
            } else {
                for (int i = 0; i < 1380; i++) {
                    canvas.drawPoint(points.get(i).x, points.get(i).y, paintObjectLinesBackground());
                }
                for (int i = 1380; i < nowPointIndex; i++) {
                    canvas.drawPoint(points.get(i).x, points.get(i).y, paintObjectLinesBackground());
                }
            }
        }

        //DRAW NORMAL BLUEPRINT TRACK OVER IT
        for (int i = 0; i < 1440; i++) {
            canvas.drawPoint(points.get(i).x, points.get(i).y, paintObjectLines());
        }
    }

    private void writeHourNumbers(Canvas canvas) {
        for (int i = 0; i < 1440; i += 60) {
            int hour = i / 60;
            String hourString = String.valueOf(hour);
            canvas.drawText(hourString, points.get(i).x + 30 * hUnit, points.get(i).y + 10 * vUnit, paintObjectHourNumbers());
        }
    }

    private int getIndexOfCurrentPoint() {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String nowString = formatter.format(now);
        MyPoint nowPoint = map.get(nowString);
        int nowPointIndex = nowPoint.index;
        return nowPointIndex;
    }

    private void drawNotches(Canvas canvas, Paint paint, int nowPointIndex) {
        for (int i = 60; i < 1440; i += 60) {
            if (i <= nowPointIndex) { canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i).x + 24 * hUnit, points.get(i).y, paint); }
        }
        if (nowPointIndex >= 0) { canvas.drawCircle(points.get(0).x, points.get(0).y, 19 * hUnit, paint); }
        if (nowPointIndex >= 1439) { canvas.drawCircle(points.get(1439).x, points.get(1439).y, 19 * hUnit, paint); }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void timesToPointsMapper() {
        HashMap<String, MyPoint> myMap = new HashMap<String, MyPoint>();
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < 1440; i++) {
            String timePoint = sdf.format(new Date(60000 * i));
            myMap.put(timePoint, points.get(i));
        }
        map = myMap;
    }

    private void drawPin(Canvas canvas) {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String nowString = formatter.format(now);
        MyPoint nowPoint = map.get(nowString);
        Bitmap pin = BitmapFactory.decodeResource(getResources(), R.drawable.inclined_kyte_halo_horizontal);
        canvas.drawBitmap(pin, nowPoint.x - (pin.getWidth() / 2), nowPoint.y - (pin.getHeight() / 2), paintObjectNotches());
    }

    private void drawEvent(Event event, Canvas canvas, int color) {
        String startTime = converter.extractTime(event.getStartTime());
        String finishTime = converter.extractTime(event.getFinishTime());
        int startTimeIndex = (map.get(startTime)).index;
        int finishTimeIndex = (map.get(finishTime)).index;
        int colorNumber = color - (6 * (int) (color / 6));

        for (int i = startTimeIndex; i < finishTimeIndex; i++) {
            canvas.drawPoint(points.get(i).x, points.get(i).y, paintObjectTest(colors[colorNumber]));
            canvas.drawCircle(points.get(startTimeIndex).x, points.get(startTimeIndex).y, 10, paintObjectCircles());
            canvas.drawCircle(points.get(finishTimeIndex).x, points.get(finishTimeIndex).y, 10, paintObjectCircles());
        }
    }


    public static class MyPoint extends Point {
        private float x;
        private float y;
        private int index;

        MyPoint(float x, float y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }
    }
}
