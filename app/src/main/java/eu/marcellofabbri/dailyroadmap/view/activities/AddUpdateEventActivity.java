package eu.marcellofabbri.dailyroadmap.view.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import eu.marcellofabbri.dailyroadmap.R;
import eu.marcellofabbri.dailyroadmap.utils.DatePickerPrompter;
import eu.marcellofabbri.dailyroadmap.utils.GridViewAdapter;
import eu.marcellofabbri.dailyroadmap.utils.MyIconsAlertFacilitator;
import eu.marcellofabbri.dailyroadmap.utils.TimePickerPrompter;

public class AddUpdateEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final String EXTRA_ID = "eu.marcellofabbri.dailyroadmap.EXTRA_ID";
    public static final String EXTRA_DESCRIPTION = "eu.marcellofabbri.dailyroadmap.EXTRA_DESCRIPTION";
    public static final String EXTRA_STARTTIME = "eu.marcellofabbri.dailyroadmap.EXTRA_STARTTIME";
    public static final String EXTRA_FINISHTIME = "eu.marcellofabbri.dailyroadmap.EXTRA_FINISHTIME";
    public static final String EXTRA_ICON_RESOURCEID = "eu.marcellofabbri.dailyroadmap.EXTRA_ICON_RESOURCEID";
    public static final String DEFAULT_ICON_RESOURCEID = "2131230885";
    public static final String ORIGINAL_UNIX = "eu.marcellofabbri.dailyroadmap.EXTRA_ORIGINAL_UNIX";

    public EditText etDescription;
    public EditText etStartDate;
    public EditText etFinishDate;
    public EditText etStartTime;
    public EditText etFinishTime;
    public FrameLayout frameIcon;
    public ImageView ivIcon;
    public String iconCode;
    private int DESCRIPTION_LENGTH = 40;
    private int[] backgroundColors = new int[] {R.color.daytimeBackground, R.color.lightGrey};
    private int[] textColors = new int[] {R.color.white, R.color.logoBackgroundHeader};
    private int selectedBackgroundColorPosition = 1;

    private void assignEditTextsToFields() {
        etDescription = findViewById(R.id.edit_text_description);
        etStartDate = findViewById(R.id.edit_text_startDate);
        etFinishDate = findViewById(R.id.edit_text_finishDate);
        etStartTime = findViewById(R.id.edit_text_startTime);
        etFinishTime = findViewById(R.id.edit_text_finishTime);
        frameIcon = findViewById(R.id.frame_icon);
        ivIcon = findViewById(R.id.chosen_icon);
    }

    private void assignColorToTexts() {
        EditText[] editTexts = new EditText[] {etDescription, etStartDate, etFinishDate, etStartTime, etFinishTime};
        for (EditText editText : editTexts) {
            editText.setTextColor(ContextCompat.getColor(getApplicationContext(), textColors[selectedBackgroundColorPosition]));
        }
        TextView iconTv = findViewById(R.id.assing_an_icon_text_view);
        iconTv.setTextColor(ContextCompat.getColor(getApplicationContext(), textColors[selectedBackgroundColorPosition]));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        assignEditTextsToFields();
        assignColorToTexts();
        findViewById(R.id.add_update_activity).setBackgroundColor(ContextCompat.getColor(this, backgroundColors[selectedBackgroundColorPosition]));

        etDescription.addTextChangedListener(createDescriptionTextWatcher());

        etFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddUpdateEventActivity.this, "Cross-day tasks are not supported", Toast.LENGTH_SHORT).show();
            }
        });

        new DatePickerPrompter(etStartDate, etFinishDate).listenForClicks();
        //new DatePickerPrompter(etFinishDate).listenForClicks();
        new TimePickerPrompter(etStartTime, Optional.ofNullable(etFinishTime)).listenForClicks();
        new TimePickerPrompter(etFinishTime, Optional.empty()).listenForClicks();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit task");
            etDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            etStartDate.setText(intent.getStringExtra(EXTRA_STARTTIME).substring(0, 8));
            etFinishDate.setText(intent.getStringExtra(EXTRA_FINISHTIME).substring(0, 8));
            etStartTime.setText(intent.getStringExtra(EXTRA_STARTTIME).substring(8));
            etFinishTime.setText(intent.getStringExtra(EXTRA_FINISHTIME).substring(8));
            iconCode = intent.getStringExtra(EXTRA_ICON_RESOURCEID);
            ivIcon.setBackgroundResource(Integer.parseInt(iconCode));
        } else {
            setTitle("Add task");
            etStartDate.setText(intent.getStringExtra(EXTRA_STARTTIME).substring(0, 8));
            etFinishDate.setText(intent.getStringExtra(EXTRA_STARTTIME).substring(0, 8));
            iconCode = DEFAULT_ICON_RESOURCEID;
        }

        List<String> iconCodes = Arrays.asList(getResources().getStringArray(R.array.icon_codes));
        GridViewAdapter gridViewAdapter = new GridViewAdapter(iconCodes, this);
        MyIconsAlertFacilitator facilitator = new MyIconsAlertFacilitator(gridViewAdapter, this);
        GridView gridView = facilitator.getGridView();
        AlertDialog.Builder builder = facilitator.getBuilder();
        AlertDialog alertDialog = builder.create();

        frameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable chosenDrawable = gridViewAdapter.extractDrawableFromArray(position);
                iconCode = String.valueOf(gridViewAdapter.extractResourceIdFromArray(position));
                ivIcon.setBackground(chosenDrawable);
                alertDialog.dismiss();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveEvent() {
        DateTimeFormatter dtfLong = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter dtfShort = DateTimeFormatter.ofPattern("h:mm a");
        String description = etDescription.getText().toString();
        String startTime = etStartDate.getText().toString() + etStartTime.getText().toString();
        LocalTime start = startTime.length() == 16 ? LocalTime.parse(startTime.substring(8), dtfLong) : LocalTime.parse(startTime.substring(8), dtfShort);
        String finishTime = etFinishDate.getText().toString() + etFinishTime.getText().toString();
        LocalTime finish = finishTime.length() == 16 ? LocalTime.parse(finishTime.substring(8), dtfLong).minusMinutes(1) : LocalTime.parse(finishTime.substring(8), dtfShort).minusMinutes(1);

        if (startTime.trim().isEmpty() || finishTime.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Insert a valid description or input times", Toast.LENGTH_SHORT).show();
            return;
        }
        if (start.isAfter(finish)) {
            Toast.makeText(this, "Finish time must be after start time", Toast.LENGTH_SHORT).show();
            return;
        }


        //to send data back to the activity that started this activity
        Intent data = new Intent();
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_STARTTIME, startTime);
        data.putExtra(EXTRA_FINISHTIME, finishTime);
        data.putExtra(EXTRA_ICON_RESOURCEID, iconCode);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_event_menu, menu);
        return true;
    }

    //to handle clicks on the Menu
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save_event:
                saveEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private TextWatcher createDescriptionTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etDescription.isFocused() && etDescription.getText().toString().trim().length() > DESCRIPTION_LENGTH) {
                    etDescription.setText(s.toString().substring(0, DESCRIPTION_LENGTH));
                    etDescription.setSelection(s.length() - 1);
                    Toast toast = Toast.makeText(AddUpdateEventActivity.this, "40 characters max. The title should be like a bullet point.", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    toastView.setBackgroundColor(getColor(R.color.redToast));
                    toast.show();
                }
            }
        };
    }
}
