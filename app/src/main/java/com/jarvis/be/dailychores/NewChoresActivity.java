package com.jarvis.be.dailychores;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.jarvis.be.dailychores.com.jarvis.be.dailychores.model.EventDbHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class NewChoresActivity extends ActionBarActivity implements View.OnClickListener{
    Switch recurToggle;
    RelativeLayout timeLayout, addressLayout, calendarLayout;
    FrameLayout timeBorder;
    TextView addressView, textViewDate, textViewTime, textViewCalendar;
    EditText eventTitle;
    static final int PLACE_PICKER_REQUEST = 1;
    boolean isItRecurring = false;
    long calendarId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isItRecurring = false;
        setContentView(R.layout.activity_new_chores);
        timeLayout = (RelativeLayout) findViewById(R.id.timelayout);
        timeBorder = (FrameLayout) findViewById(R.id.border0);
        recurToggle = (Switch) findViewById(R.id.switch1);
        recurToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    timeLayout.setVisibility(RelativeLayout.GONE);
                    timeBorder.setVisibility(FrameLayout.GONE);
                    isItRecurring = true;
                } else {
                    // The toggle is disabled
                    isItRecurring = false;
                    timeLayout.setVisibility(RelativeLayout.VISIBLE);
                    timeBorder.setVisibility(FrameLayout.VISIBLE);
                }
            }
        });

        addressLayout = (RelativeLayout) findViewById(R.id.AddressLayout);
        addressLayout.setOnClickListener(this);
        addressView = (TextView) findViewById(R.id.textViewAddress);

        //Date and time logic
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy");
        String formattedDate = df.format(c.getTime());
        df = new SimpleDateFormat("HH:mm");
        String formattedTime = df.format(c.getTime());

        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewTime.setText(formattedTime);
        textViewDate.setText(formattedDate);

        //Calendar Selection logic
        calendarLayout = (RelativeLayout) findViewById(R.id.calendarLayout);
        calendarLayout.setOnClickListener(this);
        textViewCalendar = (TextView) findViewById(R.id.textView3);

        //Event related
        eventTitle = (EditText) findViewById(R.id.editText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_chores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(isItRecurring){

                if(eventTitle.getText().toString().length()>5){
                    if(!addressView.getText().toString().matches("Address")){
                        if(calendarId!=0){
                            EventDbHandler db = new EventDbHandler(getApplicationContext());
                            db.insertChoresToDb(eventTitle.getText().toString(),
                                                addressView.getText().toString(),
                                                textViewDate.getText().toString()+", "+
                                                textViewTime.getText().toString(),
                                                Long.toString(calendarId),
                                                textViewCalendar.getText().toString());
                            finish();
                        }else{
                            new MaterialDialog.Builder(this)
                                    .title("No Calendar Selected")
                                    .content("Please choose your calendar first")
                                    .positiveText("OK")
                                    .negativeText("Not Now")
                                    .cancelable(false)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            loadCalendars();
                                        }
                                    })
                                    .show();
                        }
                    }else{
                        new MaterialDialog.Builder(this)
                                .title("No address Added")
                                .content("Please choose your event address first")
                                .positiveText("OK")
                                .negativeText("Not Now")
                                .cancelable(false)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                                        Context context = getApplicationContext();
                                        try {
                                            startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
                                        } catch (GooglePlayServicesRepairableException e) {
                                            e.printStackTrace();
                                        } catch (GooglePlayServicesNotAvailableException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .show();
                    }
                }else{
                    new MaterialDialog.Builder(this)
                            .title("Chores title too small")
                            .content("Please describe your chores in detail")
                            .cancelable(false)
                            .positiveText("OK")
                            .show();
                }
            }else{
                if(eventTitle.getText().toString().length()>5){
                    if(!addressView.getText().toString().matches("Address")){
                        if(calendarId!=0){
                            //makeCalendarEntry();
                            calendarIntent();
                            //Toast.makeText(getApplicationContext(), "Chores added to calendar", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            new MaterialDialog.Builder(this)
                                    .title("No Calendar Selected")
                                    .content("Please choose your calendar first")
                                    .positiveText("OK")
                                    .negativeText("Not Now")
                                    .cancelable(false)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            loadCalendars();
                                        }
                                    })
                                    .show();
                        }
                    }else{
                        new MaterialDialog.Builder(this)
                                .title("No address Added")
                                .content("Please choose your event address first")
                                .positiveText("OK")
                                .negativeText("Not Now")
                                .cancelable(false)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                                        Context context = getApplicationContext();
                                        try {
                                            startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
                                        } catch (GooglePlayServicesRepairableException e) {
                                            e.printStackTrace();
                                        } catch (GooglePlayServicesNotAvailableException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .show();
                    }
                }else{
                    new MaterialDialog.Builder(this)
                            .title("Chores title too small")
                            .content("Please describe your chores in detail")
                            .cancelable(false)
                            .positiveText("OK")
                            .show();
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.AddressLayout){

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            Context context = getApplicationContext();
            try {
                startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }else if(v.getId() == R.id.calendarLayout){
            loadCalendars();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                addressView.setText(place.getAddress());
            }
        }
    }

    private void loadCalendars(){
        ArrayList<String> calendars = new ArrayList<String>();
        final ArrayList<Long> calendarsId = new ArrayList<Long>();
        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};
        Cursor calCursor =
                getContentResolver().
                        query(CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                CalendarContract.Calendars.VISIBLE + " = 1",
                                null,
                                CalendarContract.Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);
                // ...
                calendars.add(displayName);
                calendarsId.add(id);
            } while (calCursor.moveToNext());
        }
        final String[] array = calendars.toArray(new String[calendars.size()]);
        new MaterialDialog.Builder(this)
                .title("Choose Calendars")
                .items(array)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        textViewCalendar.setText(array[which]);
                        calendarId = calendarsId.get(which);
                    }
                })
                .show();
    }

    private void makeCalendarEntry(){
        long calID = calendarId;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();

        startMillis = beginTime.getTimeInMillis();

        endMillis = beginTime.getTimeInMillis();

        TimeZone tz = TimeZone.getDefault();
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, eventTitle.getText().toString());
        values.put(CalendarContract.Events.DESCRIPTION, "Entry made from Daily chores App");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
        values.put(CalendarContract.Events.EVENT_LOCATION, addressView.getText().toString());
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
    }

    private void calendarIntent() {
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();

        startMillis = beginTime.getTimeInMillis();

        endMillis = beginTime.getTimeInMillis();
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                .putExtra(CalendarContract.Events.TITLE, eventTitle.getText().toString())
                .putExtra(CalendarContract.Events.DESCRIPTION, "Entry made from Daily chores App")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, addressView.getText().toString())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }

}
