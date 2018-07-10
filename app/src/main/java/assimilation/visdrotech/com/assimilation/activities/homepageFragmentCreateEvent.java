package assimilation.visdrotech.com.assimilation.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import assimilation.visdrotech.com.assimilation.R;
import assimilation.visdrotech.com.assimilation.retrofitModels.createEvent;
import assimilation.visdrotech.com.assimilation.utils.baseApplicationClass;
import assimilation.visdrotech.com.assimilation.utils.restClient;
import assimilation.visdrotech.com.assimilation.utils.restInterface;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.sentry.Sentry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by defcon on 25/06/18.
 */

public class homepageFragmentCreateEvent extends Fragment {
    private String prefName ;
    private TextInputEditText date,time,audience,helpers,title,description,venue;
    private Button submit;
    private String stDate="", stTime="",helpersText,token;
    private ArrayList<String> selectedHelperUsername = new ArrayList<String>();
    private String stAudienceKey= "";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final String TAG = "CreateEventFragment";
    private ArrayList<String> helperUserNames, helperNameToShowOnEditText;
    private CharSequence[] helperNames;
    private boolean[] checkedHelper;
    private ProgressBar loginProgress;

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
        prefName =  ((baseApplicationClass) getActivity().getApplication()).PREF_NAME ;
        SharedPreferences prefs = this.getActivity().getSharedPreferences(prefName, MODE_PRIVATE);
        String audienceObj = prefs.getString("audience", "");
        token = prefs.getString("token", "");
        helpersText = prefs.getString("helpers", "");
        getActivity().setTitle(getString(R.string.create_event_title));
        initialiseVariable(view);
        setHelpers();
        setListeners();

        try {
            JSONObject mainObject = new JSONObject(audienceObj);
            stAudienceKey = mainObject.getString("key");
            audience.setText(mainObject.getString("value"));
        } catch (JSONException e) {
            e.printStackTrace();
            Sentry.capture(e);
        }
//        List<String> items = Arrays.asList(audienceObj.split("\\s*,\\s*"));
//        Log.d(TAG, items.get(0) + "|" + items.get(1));

    }

    private void initialiseVariable(View v){
        date = (TextInputEditText) v.findViewById(R.id.date);
        time = (TextInputEditText) v.findViewById(R.id.time);
        audience = (TextInputEditText) v.findViewById(R.id.audience);
        helpers = (TextInputEditText) v.findViewById(R.id.helpers);
        title = (TextInputEditText) v.findViewById(R.id.title);
        description = (TextInputEditText) v.findViewById(R.id.description);
        venue = (TextInputEditText) v.findViewById(R.id.venue);
        submit = (Button) v.findViewById(R.id.submitButton);
        loginProgress = (ProgressBar) v.findViewById(R.id.login_progress);
        date.setKeyListener(null);
        time.setKeyListener(null);
        helpers.setKeyListener(null);
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
                                    stDate =String.format("%02d", dayOfMonth) + "-" + String.format("%02d", month + 1) + "-" + year;
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
                                    stTime = String.format("%02d", hourOfDay) + " : " + String.format("%02d", minute) ;
                                   time.setText(stTime);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                    time.clearFocus();
                }
            }
        });
        helpers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d(TAG,"Helper clicked");
                    showHelperMultiSelectDialog();
                    helpers.clearFocus();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleString = title.getText().toString();
                String descriptionString = description.getText().toString();
                String venueString = venue.getText().toString();
                if (titleString.equals("") || titleString.length() == 0){
                    Toast.makeText(getContext(), getString(R.string.create_event_notitle),
                            Toast.LENGTH_SHORT).show();
                }
                else if (descriptionString.equals("") || descriptionString.length() == 0){
                    Toast.makeText(getContext(), getString(R.string.create_event_nodescription),
                            Toast.LENGTH_SHORT).show();
                }
                else if (venueString.equals("") || venueString.length() == 0){
                    Toast.makeText(getContext(), getString(R.string.create_event_novenue),
                            Toast.LENGTH_SHORT).show();
                }
                else if (stDate.equals("") || stDate.length() == 0){
                    Toast.makeText(getContext(), getString(R.string.create_event_nodate),
                            Toast.LENGTH_SHORT).show();
                }
                else if (stTime.equals("") || stTime.length() == 0){
                    Toast.makeText(getContext(), getString(R.string.create_event_notime),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    sendData(titleString,descriptionString,venueString) ;
                }
            }
        });
    }

    private void setHelpers() {

         helperUserNames = new ArrayList<String>();
        helperNameToShowOnEditText = new ArrayList<String>();
        try {
            JSONArray t = new JSONArray(helpersText);
            Log.d(TAG, String.valueOf(t.length()));
            helperNames = new CharSequence[t.length()];
            for (int i=0; i < t.length(); i++ ) {
                JSONObject o = t.getJSONObject(i);
                helperNames[i] = (o.getString("fullname") + " ("  + o.getString("username") +")");
                helperUserNames.add(o.getString("username"));
            }
//            checkSelected.add(Boolean.FALSE);
        } catch (JSONException e) {
            e.printStackTrace();
            Sentry.capture(e);
        }
    }

    protected void showHelperMultiSelectDialog() {

        checkedHelper = new boolean[helperNames.length];

        int count = helperNames.length;

        for(int i = 0; i < count; i++)
            checkedHelper[i] = helperNameToShowOnEditText.contains(helperNames[i].toString());

        DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {


                if(isChecked) {
                    selectedHelperUsername.add(helperUserNames.get(position));
                    helperNameToShowOnEditText.add(helperNames[position].toString());
                    checkedHelper[position] = true;
                }
                else {
                    selectedHelperUsername.remove(helperUserNames.get(position));
                    helperNameToShowOnEditText.remove(helperNames[position].toString());
                    checkedHelper[position] = false;
                }
            helpers.setText( TextUtils.join(", ", helperNameToShowOnEditText));
            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Select Helpers");
        builder.setMultiChoiceItems(helperNames, checkedHelper, coloursDialogListener);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void sendData(String title, String desc, String venue) {
        Log.d("date", stDate);
        Log.d("time", stTime);
        Log.d("audience",stAudienceKey );
        Log.d("helpers", selectedHelperUsername.toString());

        loginProgress.setVisibility(View.VISIBLE);
        restInterface restService = restClient.getClient().create(restInterface.class);
        restService.createEvent(token,title,desc,venue,stDate,stTime,stAudienceKey,selectedHelperUsername).enqueue(new Callback<createEvent>() {
            @Override
            public void onResponse(Call<createEvent> call, Response<createEvent> response) {
                loginProgress.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    createEvent createEventResponseObject = response.body();
                    if (createEventResponseObject.getSuccess()){
                        SweetAlertDialog sd =  new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                        sd.setTitleText("Success!");
                        sd.setContentText(createEventResponseObject.getMessage());
                        sd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                if(fm.getBackStackEntryCount()>0) {
                                    fm.popBackStack();
                                }
                            }
                        });
                        sd.setCanceledOnTouchOutside(false);
                        sd.show();
                    }
                    else {
                        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(createEventResponseObject.getMessage())
                                .show();
                    }

//                        Toast.makeText(getContext(), response.message(),
//                                Toast.LENGTH_SHORT).show();
                }
                else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(response.message())
                            .show();
                }

            }
            @Override
            public void onFailure(Call<createEvent> call, Throwable t) {
                Log.d(TAG, "in on fail");
                Log.d(TAG, t.toString());
                Sentry.capture(t);
                loginProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), getString(R.string.login_fail),
                        Toast.LENGTH_SHORT).show();
            }
        });

//        restService.createEvent(token,title,desc,venue,stDate,stTime,stAudienceKey,selectedHelperUsername).enqueue(new Callback<createEvent>() {
//            @Override
//            public void onResponse(Call<createEvent> call, Response<createEvent> response) {
//                Log.d(TAG,"In onsuccess");
//            }
//
//            @Override
//            public void onFailure(Call<createEvent> call, Throwable t) {
//                Log.d(TAG, "In fail");
//                Log.d(TAG, t.toString());
//            }
//        });

    }

}
