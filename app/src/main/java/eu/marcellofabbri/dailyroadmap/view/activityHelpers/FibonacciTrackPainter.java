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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
  List<Event> amMiddayEvents = new ArrayList<>();
  List<Event> pmMiddayEvents = new ArrayList<>();
  double OUTER_ARC_COEFFICIENT = 0.120;
  double INNER_ARC_COEFFICIENT = 0.240;

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
    long midnight = getDayBeginning();

    super.onDraw(canvas);
    //DRAW OUTER GREY ARC
    canvas.drawArc(createOval(0.120), -90F, 360F, true, paintObject(Color.LTGRAY));

    //DRAW AFTERNOON EVENT ARCS
    drawAfternoon(canvas, afternoonEvents, midnight);

    //DRAW AFTERNOON PORTIONS OF MIDDAY EVENTS
    drawPortionOfMiddayEvents(canvas, pmMiddayEvents, midnight, true);

    //DRAW WHITE THIN ARC BETWEEN OUTER AND INNER GREY ARCS
    canvas.drawArc(createOval(0.220), -90F, 360F, true, paintObject(Color.WHITE));

    //DRAW INNER GREY ACR
    canvas.drawArc(createOval(0.240), -90F, 360F, true, paintObject(Color.LTGRAY));

    //DRAW MORNING PORTIONS OF MIDDAY EVENTS
    drawPortionOfMiddayEvents(canvas, amMiddayEvents, midnight, false);

    //DRAW MORNING EVENT ARCS
    drawMorning(canvas, morningEvents, midnight);

    //DRAW CENTRAL WHITE CIRCLE
    canvas.drawArc(createOval(0.360), -90F, 360F, true, paintObject(Color.WHITE));

    //DRAW NEEDLE
    if (isToday) { drawNeedle(canvas); }

    //DRAW NUMBERS
    drawNumbers(canvas);

  }

  private Paint paintObject(int color) {
    Paint paint = new Paint();
    paint.setColor(color);
    paint.setStrokeWidth(10);
    paint.setTextSize(40);
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

  private void drawNonMiddayEvent(Canvas canvas, Event event, long midnight, double arcCoefficient, int color) {
    long start = event.getStartTime().toEpochSecond()*1000;
    long finish = event.getFinishTime().toEpochSecond()*1000;
    long startDegree = (start - midnight)/120000;
    float startAngle = -90F + startDegree;
    long finishDegree = finish - start;
    float sweepAngle = finishDegree/120000;
    canvas.drawArc(createOval(arcCoefficient), startAngle, sweepAngle, true, paintObject(color));
  }

  private void drawMorning(Canvas canvas, List<Event> morningEvents, long midnight) {
    int i = 0;
    for (Event event : morningEvents) {
      int color = i > 5 ? i % 6 : i;
      drawNonMiddayEvent(canvas, event, midnight, INNER_ARC_COEFFICIENT, colors[color]);
      i++;
    }
  }

  private void drawAfternoon(Canvas canvas, List<Event> afternoonEvents, long midnight) {
    int startingIndex = events.size() - afternoonEvents.size();
    int firstColorIndex = startingIndex % 6;
    int i = firstColorIndex;
    for (Event event : afternoonEvents) {
      int color = i > 5 ? i % 6 : i;
      drawNonMiddayEvent(canvas, event, midnight, OUTER_ARC_COEFFICIENT, colors[color]);
      i++;
    }
  }

  private void drawPortionOfMiddayEvents(Canvas canvas, List<Event> splitMiddayEvents, long midnight, boolean pm) {
    double arcCoefficient = pm ? OUTER_ARC_COEFFICIENT : INNER_ARC_COEFFICIENT;
    int startingIndex = morningEvents.size();
    int firstColorIndex = startingIndex % 6;
    int i = firstColorIndex;
    for (Event event : splitMiddayEvents) {
      int color = i > 5 ? (i % 6) : i;
      drawNonMiddayEvent(canvas, event, midnight, arcCoefficient, colors[color]);
      i++;
    }
  }

  private void drawNeedle(Canvas canvas) {
    Date now = new Date();
    int needleLength = getNeedleLength(now);

    Paint needlePaint = paintObject(getContext().getColor(R.color.daytimeBackgroundDarker));
    double needleAngle = getNeedleAngle(now);
    double convertedAngleForAndroid = needleAngle - 90;
    double angleRadians = (Math.PI / 180.0) * convertedAngleForAndroid;
    float startX = width/2;
    float startY = (height / ratio)/2 - (height * 0.05F);
    double finishX = startX + Math.cos(angleRadians) * needleLength;
    double finishY = startY + Math.sin(angleRadians) * needleLength;
    canvas.drawLine(startX, startY, (float) finishX, (float) finishY, needlePaint);
  }

  private int getNeedleLength(Date now) {
    Date current = now;
    Date midday = new Date(getDayBeginning() + 43200000);
    double longLength = (width / 2) - (OUTER_ARC_COEFFICIENT * width);
    double shortLength = (width / 2) - (INNER_ARC_COEFFICIENT * width);
    if (current.after(midday)) {
      return (int) longLength;
    } else {
      return (int) shortLength;
    }
  }

  private double getNeedleAngle(Date date) {
    Calendar now = Calendar.getInstance();
    now.setTime(date);
    int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
    int hour = hourOfDay > 11 ? hourOfDay - 12 : hourOfDay;
    int minutes = now.get(Calendar.MINUTE);
    int minutesSince12 = minutes + (hour * 60);
    double angle = minutesSince12 / 2;
    return angle;
  }

  private void drawNumbers(Canvas canvas) {
    float startX = width/2 - width*0.01F;
    float startY = (height/ratio)/2 - (height*0.043F);
    double length = (width / 2) - (0.140 * width);
    for (int i = 1; i <= 12; i++) {
      float angleRadians = (float) (Math.PI / 180.0) * (30*i - 90);
      double x = startX + Math.cos(angleRadians) * (float) length;
      double y = startY + Math.sin(angleRadians) * (float) length;
      float doubleDigitBias = i > 9 ? (width*0.01F) : 0;

//      Paint paint = paintObject(Color.WHITE);
//      paint.setStrokeWidth(50);
//      canvas.drawPoint((float) x + width*0.012F, (float) y - height*0.007F, paint);

      canvas.drawText(String.valueOf(i), (float) x - doubleDigitBias, (float) y, paintObject(Color.BLACK));
    }
  }

  private void splitMiddayEvents(List<Event> middayEvents, long midnight) {
    long middayMillis = midnight + 43200000;
    Instant middayInstant = Instant.ofEpochMilli(middayMillis);
    OffsetDateTime middayDate = OffsetDateTime.ofInstant(middayInstant, ZoneId.systemDefault());
    for (Event event : middayEvents) {
      Event morningPortion = new Event(event.getDescription(), event.getStartTime(), middayDate, event.getStartUnix(), event.getIcon());
      amMiddayEvents.add(morningPortion);
      Event afternoonPortion = new Event(event.getDescription(), middayDate, event.getFinishTime(), middayMillis, event.getIcon());
      pmMiddayEvents.add(afternoonPortion);
    }
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
    splitMiddayEvents(middayEvents, getDayBeginning());
  }

  private long getDayBeginning() {
    long millis = 0;
    if (events.size() > 0) {
      OffsetDateTime eventOffsetDateTime = events.get(0).getStartTime();
      long eventMillis = eventOffsetDateTime.toEpochSecond() * 1000;
      Calendar today = myCalendar;
      today.setTimeInMillis(eventMillis);
      today.set(Calendar.HOUR_OF_DAY, 0);
      today.set(Calendar.MINUTE, 0);
      today.set(Calendar.SECOND, 0);
      millis = today.getTime().getTime();
    }
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
