package eu.marcellofabbri.dailyroadmap;

import android.support.v4.media.MediaMetadataCompat;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Calendar;

import eu.marcellofabbri.dailyroadmap.view.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    public SimpleDateFormat slashesFormat = new SimpleDateFormat("dd/MM/yy");
    public java.util.Date today = Date.from(Instant.now());

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void currentDateIsShown() {
        String expectedText = slashesFormat.format(today);
        onView(withId(R.id.header_date)).check(matches(withText(expectedText)));
    }

    @Test
    public void leftArrowDecrementsDateByOneDay() {
        String yesterday = slashesFormat.format(Date.from(OffsetDateTime.now().minusDays(1).toInstant()));
        String twoDaysAgo = slashesFormat.format(Date.from(OffsetDateTime.now().minusDays(2).toInstant()));
        onView(withId(R.id.left_button)).perform(click());
        onView(withId(R.id.header_date)).check(matches(withText(yesterday)));
        onView(withId(R.id.left_button)).perform(click());
        onView(withId(R.id.header_date)).check(matches(withText(twoDaysAgo)));
    }

    @Test
    public void rightArrowIncrementsDateByOneDay() {
        String tomorrow = slashesFormat.format(Date.from(OffsetDateTime.now().plusDays(1).toInstant()));
        String twoDaysLater = slashesFormat.format(Date.from(OffsetDateTime.now().plusDays(2).toInstant()));
        onView(withId(R.id.right_button)).perform(click());
        onView(withId(R.id.header_date)).check(matches(withText(tomorrow)));
        onView(withId(R.id.right_button)).perform(click());
        onView(withId(R.id.header_date)).check(matches(withText(twoDaysLater)));
    }

    @Test
    public void TodayButtonResetsDateToToday() {
        String todayString = slashesFormat.format(today);
        onView(withId(R.id.right_button)).perform(click());
        onView(withId(R.id.right_button)).perform(click());
        onView(withId(R.id.today_button)).perform(click());
        onView(withId(R.id.header_date)).check(matches(withText(todayString)));
    }

}
