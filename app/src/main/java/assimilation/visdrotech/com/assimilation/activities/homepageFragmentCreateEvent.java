package assimilation.visdrotech.com.assimilation.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import assimilation.visdrotech.com.assimilation.R;

/**
 * Created by defcon on 25/06/18.
 */

public class homepageFragmentCreateEvent extends Fragment {
    private TextInputEditText date,time;
    private String stDate, stTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final String TAG = "CreateEventFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.create_event_title));
        initialiseVariable(view);
        setListeners();
    }

    private void initialiseVariable(View v){
        date = (TextInputEditText) v.findViewById(R.id.date);
        time = (TextInputEditText) v.findViewById(R.id.time);
        date.setKeyListener(null);
        time.setKeyListener(null);
    }

    private void setListeners() {
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int month, int dayOfMonth) {
                                    stDate = dayOfMonth + "-" + (month + 1) + "-" + year;
                                   date.setText(stDate);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    date.clearFocus();
                }

            }
        });

        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    stTime = hourOfDay + " : " + minute;
                                   time.setText(stTime);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                    time.clearFocus();
                }
            }
        });
    }
}
