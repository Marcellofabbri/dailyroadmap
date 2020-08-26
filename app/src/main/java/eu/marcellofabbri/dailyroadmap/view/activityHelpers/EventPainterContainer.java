package eu.marcellofabbri.dailyroadmap.view.activityHelpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class EventPainterContainer extends RelativeLayout {
    public void setVerticalTrackPainter(VerticalTrackPainter verticalTrackPainter) {
        this.verticalTrackPainter = verticalTrackPainter;
    }

    private VerticalTrackPainter verticalTrackPainter;

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
