package eu.marcellofabbri.dailyroadmap;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class EventPainterContainer extends RelativeLayout {
    public void setTrackPainter(TrackPainter trackPainter) {
        this.trackPainter = trackPainter;
    }

    private TrackPainter trackPainter;

    public EventPainterContainer(Context context) {
        super(context);
    }

    public EventPainterContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventPainterContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
