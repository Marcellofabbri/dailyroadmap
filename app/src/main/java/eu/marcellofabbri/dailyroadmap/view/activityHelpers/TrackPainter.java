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
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

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
public class TrackPainter extends View {

    EntityFieldConverter converter = new EntityFieldConverter();
    float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    float screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    float hUnit = (screenWidth / (700));
    float hMinuteUnit = ((100 * hUnit) / 60);
    float vUnit = ((screenHeight * (float)0.878 * (float)0.85) / 900);
    float vMinuteUnit = ((100 * vUnit) / 60);
    float leftMargin = hUnit * 100;
    float upperMargin = hUnit * 56;
    int[] trackDefaultColors = new int[] {ContextCompat.getColor(getContext(), R.color.trackDefaultColor0), ContextCompat.getColor(getContext(), R.color.trackDefaultColor1)};
    int whatColorPosition = 1;
    int trackDefaultColor = trackDefaultColors[whatColorPosition];
    CustomColors myColors = new CustomColors();
    Integer[] colors = new Integer[] {
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

    public TrackPainter(Context context, List<Event> events) {
        super(context);
        this.events = events;
    }

    public TrackPainter(Context context, @Nullable AttributeSet attrs, List<Event> events) {
        super(context, attrs);
        this.events = events;
    }

    public TrackPainter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, List<Event> events) {
        super(context, attrs, defStyleAttr);
        this.events = events;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawStuff(canvas);
    }

    private void createPoints(Canvas canvas) {

        // from 00 to 01 (indexes from 0 to 59)
        for (int i = 0; i < 60; i++) {
            MyPoint point = new MyPoint(leftMargin, (upperMargin + 100 * vUnit - i*vMinuteUnit), i);
            points.add(point);
        }
        // from 01 to 06 (indexes from 60 to 359)
        for (int i = 0; i < 300; i++) {
            MyPoint point = new MyPoint(leftMargin + i*hMinuteUnit, upperMargin, i+60);
            points.add(point);
        }
        // from 06 to 13 (indexes from 360 to 779)
        for (int i = 0; i < 420; i++) {
            MyPoint point = new MyPoint(leftMargin + 500*hUnit, upperMargin + i*vMinuteUnit, i+360);
            points.add(point);
        }
        // from 13 to 18 (indexes from 780 to 1079)
        for (int i = 0; i < 300; i++) {
            MyPoint point = new MyPoint(leftMargin + 500*hUnit - i*hMinuteUnit, upperMargin + 700*vUnit, i+780);
            points.add(point);
        }
        // from 18 to 23 (indexes from 1080 to 1379)
        for (int i = 0; i < 300; i++) {
            MyPoint point = new MyPoint(leftMargin, upperMargin + 700*vUnit - i*vMinuteUnit, i+1080);
            points.add(point);
        }
        // from 23 to 24 (indexes from 1380 to 1440)
        for (int i = 0; i < 60; i++) {
            MyPoint point = new MyPoint(leftMargin + (i*hUnit)/2, upperMargin + 200*vUnit - i*vMinuteUnit, i+1380);
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
        paintObject.setStrokeWidth(51);
        paintObject.setColor(Color.BLACK);
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
        paintObject.setStrokeWidth(45);
        paintObject.setColor(Color.BLACK);
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

    public void drawStuff(Canvas canvas) {
        createPoints(canvas);
        timesToPointsMapper();
        drawBlueprintTrack(canvas);
        writeHourNumbers(canvas);
        drawNotches(canvas);
        if (events != null) {
            for (int i = 0; i < events.size(); i++) {
                drawEvent(events.get(i), canvas, i);
            }
        }
        drawPin(canvas);
    }

    private void drawBlueprintTrack(Canvas canvas) {
        for (int i = 0; i < 1380; i++) {
            canvas.drawPoint(points.get(i).x, points.get(i).y, paintObjectLinesBackground());
        }
        for (int i = 1380; i < 1440; i++) {
            canvas.drawPoint(points.get(i).x, points.get(i).y, paintObjectLines12Background());
        }
        for (int i = 0; i < 1380; i++) {
            canvas.drawPoint(points.get(i).x, points.get(i).y, paintObjectLines());
        }
        for (int i = 1380; i < 1440; i++) {
            canvas.drawPoint(points.get(i).x, points.get(i).y, paintObjectLines12());
        }
    }

    private void writeHourNumbers(Canvas canvas) {
        canvas.drawText("1", points.get(60).x - 35*hUnit, points.get(60).y - 25*hUnit, paintObjectHourNumbers());
        for (int i = 120; i < 360; i+=60) {
            String hourString = String.valueOf(i/60);
            canvas.drawText(hourString, points.get(i).x - 8*hUnit, points.get(i).y - 30*hUnit, paintObjectHourNumbers());
        }
        canvas.drawText("6", points.get(360).x + 14*hUnit, points.get(360).y - 20*hUnit, paintObjectHourNumbers());
        for (int i = 420; i < 780; i+=60) {
            int hour = i/60;
            String hourString = String.valueOf(hour);
            canvas.drawText(hourString, points.get(i).x + 30*hUnit, points.get(i).y + 10*vUnit, paintObjectHourNumbers());
        }
        canvas.drawText("1", points.get(780).x + 16*hUnit, points.get(780).y + 45*hUnit, paintObjectHourNumbers());
        for (int i = 840; i < 1080; i+=60) {
            int hour = i/60 - 12;
            String hourString = String.valueOf(hour);
            canvas.drawText(hourString, points.get(i).x - 8*hUnit, points.get(i).y + 55*hUnit, paintObjectHourNumbers());
        }
        canvas.drawText("6", points.get(1080).x - 30*hUnit, points.get(1080).y + 45*hUnit, paintObjectHourNumbers());
        for (int i = 1140; i < 1440; i+=60) {
            int hour = i/60 - 12;
            String hourString = hour < 10 ? "  " + hour : String.valueOf(hour);
            canvas.drawText(hourString, points.get(i).x - 60*vUnit, points.get(i).y + 10*hUnit, paintObjectHourNumbers());
        }
        canvas.drawText("00", points.get(0).x - 80*vUnit, points.get(0).y + 10*hUnit, paintObjectHourNumbers());
//        canvas.drawCircle(points.get(1439).x, points.get(1439).y, 19*hUnit, paintObjectNotches());
    }

    private void drawNotches(Canvas canvas) {
        canvas.drawLine(points.get(60).x, points.get(60).y, points.get(60).x - 15*hUnit, points.get(60).y - 15*hUnit, paintObjectNotches());
        for (int i = 120; i < 360; i+=60) {
            canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i).x, points.get(i).y - 24*hUnit, paintObjectNotches());
        }
        canvas.drawLine(points.get(360).x, points.get(360).y, points.get(360).x +15*hUnit, points.get(360).y - 15*hUnit, paintObjectNotches());
        for (int i = 420; i < 780; i+=60) {
            canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i).x + 24*hUnit, points.get(i).y, paintObjectNotches());
        }
        canvas.drawLine(points.get(780).x, points.get(780).y, points.get(780).x +15*hUnit, points.get(780).y + 15*hUnit, paintObjectNotches());
        for (int i = 840; i < 1080; i+=60) {
            canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i).x, points.get(i).y + 24*hUnit, paintObjectNotches());
        }
        canvas.drawLine(points.get(1080).x, points.get(1080).y, points.get(1080).x - 15*hUnit, points.get(1080).y + 15*hUnit, paintObjectNotches());
        for (int i = 1140; i < 1440; i+=60) {
            canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i).x - 24*vUnit, points.get(i).y, paintObjectNotches());
        }
        canvas.drawCircle(points.get(0).x, points.get(0).y, 19*hUnit, paintObjectNotches());
        canvas.drawCircle(points.get(1439).x, points.get(1439).y, 19*hUnit, paintObjectNotches());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void timesToPointsMapper() {
        HashMap<String, MyPoint> myMap = new HashMap<String, MyPoint>();
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < 1440; i++) {
            String timePoint = sdf.format(new Date(60000*i));
            myMap.put(timePoint, points.get(i));
        }
        map = myMap;
    }

    private void drawPin(Canvas canvas) {
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String nowString = formatter.format(now);
        MyPoint nowPoint = map.get(nowString);
        Bitmap pin = BitmapFactory.decodeResource(getResources(),R.drawable.gbg_pin);
        canvas.drawBitmap(pin, nowPoint.x, nowPoint.y - pin.getHeight(), paintObjectNotches());
    }

    private void drawEvent(Event event, Canvas canvas, int color) {
        String startTime = converter.extractTime(event.getStartTime());
        String finishTime = converter.extractTime(event.getFinishTime());
        int startTimeIndex = (map.get(startTime)).index;
        int finishTimeIndex = (map.get(finishTime)).index;
        int colorNumber = color - (6 * (int)(color/6));

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
