package com.jarvis.be.dailychores.recyclerview;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jarvis.be.dailychores.MainActivity;
import com.jarvis.be.dailychores.R;
import com.jarvis.be.dailychores.com.jarvis.be.dailychores.model.ChoresModel;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by parimal on 07-04-2015.
 */
public class RowController extends RecyclerView.ViewHolder
        implements View.OnClickListener{
    MainActivity mActivity;
    TextView titleTv, addressTv, calendarTv;
    long _calId;
    String _title, _address;
    public RowController(MainActivity mActivity, View row)
    {
        super(row);
        this.mActivity = mActivity;
        Typeface typefaceLight = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface typefaceThin = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Roboto-Thin.ttf"); //rcondensedlight.ttf
        titleTv = (TextView) row.findViewById(R.id.textView4);
        addressTv = (TextView) row.findViewById(R.id.textView6);
        calendarTv = (TextView) row.findViewById(R.id.textView5);
        titleTv.setTypeface(typefaceLight);
        calendarTv.setTypeface(typefaceThin);
        addressTv.setTypeface(typefaceThin);
        row.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        new MaterialDialog.Builder(mActivity)
                .title(titleTv.getText().toString())
                .content("Are you sure you want to make the entry for this chores")
                .positiveText("Yes")
                .negativeText("Not Sure")
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        makeCalendarEntry();
                        //calendarIntent();
                        Toast.makeText(mActivity, "Chores added to calendar", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
        //calendarIntent();
    }



    public void bindModel(ChoresModel tempChoresModel) {
        titleTv.setText(tempChoresModel.getTitle());
        calendarTv.setText("Google Calendar: "+tempChoresModel.getCalName());
        addressTv.setText(tempChoresModel.getAddress());
        _calId = Long.parseLong(tempChoresModel.getCalId());
        _address = tempChoresModel.getAddress();
        _title = tempChoresModel.getTitle();
    }


    private void makeCalendarEntry(){
        long calID = _calId;
        long startMillis;
        Calendar beginTime = Calendar.getInstance();

        startMillis = beginTime.getTimeInMillis();

        TimeZone tz = TimeZone.getDefault();
        ContentResolver cr = mActivity.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, startMillis);
        values.put(CalendarContract.Events.TITLE, _title);
        values.put(CalendarContract.Events.DESCRIPTION, "Entry made from Daily chores App");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
        values.put(CalendarContract.Events.EVENT_LOCATION, _address);
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
                .putExtra(CalendarContract.Events.TITLE, _title)
                .putExtra(CalendarContract.Events.DESCRIPTION, "Entry made from Daily chores App")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, _address)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        mActivity.startActivity(intent);
    }
}
