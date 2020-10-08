package eu.marcellofabbri.dailyroadmap.view.activityHelpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import eu.marcellofabbri.dailyroadmap.R;
import eu.marcellofabbri.dailyroadmap.model.Event;
import eu.marcellofabbri.dailyroadmap.utils.CustomColors;
import eu.marcellofabbri.dailyroadmap.utils.EntityFieldConverter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FibonacciTrackPainter extends View {

  EntityFieldConverter converter = new EntityFieldConverter();
  float height = (float) (Resources.getSystem().getDisplayMetrics().heightPixels);
  float width = Resources.getSystem().getDisplayMetrics().widthPixels;
  float ratio = height/width;
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

  List<Event> events;
  boolean isToday = true;
  Calendar myCalendar;
  List<Event> morningEvents = new ArrayList<>();
  List<Event> afternoonEvents = new ArrayList<>();
  List<Event> middayEvents = new ArrayList<>();

  public FibonacciTrackPainter(Context context, List<Event> events, boolean isToday, Calendar myCalendar) {
    super(context);
    this.events = events;
    this.isToday = isToday;
    this.myCalendar = myCalendar;
    sortEvents();
  }

  public FibonacciTrackPainter(Context context, @Nullable AttributeSet attrs, List<Event> events) {
    super(context, attrs);
    this.events = events;
  }

  public FibonacciTrackPainter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, List<Event> events) {
    super(context, attrs, defStyleAttr);
    this.events = events;
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawArc(createOval(0.120), -90F, 360F, true, paintObject(Color.LTGRAY));
    canvas.drawArc(createOval(0.220), -90F, 360F, true, paintObject(Color.WHITE));
    canvas.drawArc(createOval(0.240), -90F, 360F, true, paintObject(Color.LTGRAY));

    //canvas.drawArc(createOval(0.240), 90F, 30F, true, paintObject(Color.RED));
    if (morningEvents.size() > 0) {
      drawMorningEvent(canvas, morningEvents.get(0));
    }


    canvas.drawArc(createOval(0.360), -90F, 360F, true, paintObject(Color.WHITE));


  }

  private Paint paintObject(int color) {
    Paint paint = new Paint();
    paint.setColor(color);
    return paint;
  }

  private RectF createOval(double coefficient) {
    float margin = (float) coefficient;
    float verticalBias = height * 0.05F;
    RectF oval = new RectF(
            (width * margin),
            (height * margin / ratio) - verticalBias,
            (width - width*margin),
            ((height - height*margin) / ratio) - verticalBias
    );
    return oval;
  }

  private void drawMorningEvent(Canvas canvas, Event event) {
    long start = event.getStartTime().toEpochSecond()*1000;
    long finish = event.getFinishTime().toEpochSecond()*1000;
    long startDegree = (start - getDayBeginning())/120000;
    float startAngle = -90F + startDegree;
    long finishDegree = finish - start;
    float sweepAngle = finishDegree/120000;
    canvas.drawArc(createOval(0.240), startAngle, sweepAngle, true, paintObject(Color.RED));
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private void sortEvents() {
    for (Event event : events) {
      int startHour = event.getStartTime().getHour();
      int finishHour = event.getFinishTime().getHour();
      if (startHour < 12 && finishHour < 12) {
        morningEvents.add(event);
      } else if (startHour < 12 && finishHour >= 12) {
        middayEvents.add(event);
      } else {
        afternoonEvents.add(event);
      }
    }
  }

  private long getDayBeginning() {
    Calendar today = myCalendar;
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    long millis = today.getTime().getTime();
    System.out.println(millis);
    return millis;
  }

//  @RequiresApi(api = Build.VERSION_CODES.O)
//  public void drawStuff(Canvas canvas) {
//    if (events != null) {
//      for (int i = 0; i < events.size(); i++) {
//        drawEvent(events.get(i), canvas, i);
//      }
//    }
//    if (isToday) { drawPin(canvas); }
//  }


}
