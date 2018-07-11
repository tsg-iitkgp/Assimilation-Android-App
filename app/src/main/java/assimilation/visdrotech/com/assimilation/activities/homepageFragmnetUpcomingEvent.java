package assimilation.visdrotech.com.assimilation.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import assimilation.visdrotech.com.assimilation.R;
import assimilation.visdrotech.com.assimilation.retrofitModels.EventDatum;
import assimilation.visdrotech.com.assimilation.retrofitModels.UpcomingEvent;
import assimilation.visdrotech.com.assimilation.utils.baseApplicationClass;
import assimilation.visdrotech.com.assimilation.utils.restClient;
import assimilation.visdrotech.com.assimilation.utils.restInterface;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by defcon on 10/07/18.
 */

public class homepageFragmnetUpcomingEvent extends Fragment {
    private String prefName ;
    private String token;
    SweetAlertDialog pDialog;
    private static final String TAG = "FragmnetUpcomingEvent";
    private TableLayout table;
    private Boolean isAttendanceTaker;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_upcoming_event, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.upcoming_event_title));

        prefName =  ((baseApplicationClass) getActivity().getApplication()).PREF_NAME ;
        SharedPreferences prefs = this.getActivity().getSharedPreferences(prefName, MODE_PRIVATE);
        token = prefs.getString("token", "");
        isAttendanceTaker = prefs.getBoolean("isAttendanceTaker", false);
        table = (TableLayout) view.findViewById(R.id.table);
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        restInterface restService = restClient.getClient().create(restInterface.class);
        restService.upcomingEvent(token).enqueue(new Callback<UpcomingEvent>() {
            @Override
            public void onResponse(Call<UpcomingEvent> call, Response<UpcomingEvent> response) {
                pDialog.dismissWithAnimation();
                if (response.isSuccessful()) {
                    List<EventDatum> responseList = response.body().getEventData();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    for (final EventDatum it : responseList) {

                        View tr = inflater.inflate(R.layout.upcoming_event_table_row, null,false);
                        TextView title = (TextView) tr.findViewById(R.id.title);
                        title.setText(it.getTitle());
                        TextView venue = (TextView) tr.findViewById(R.id.venue);
                        venue.setText(it.getVenue());
                        TextView date = (TextView) tr.findViewById(R.id.date);
                        date.setText(it.getDate());
                        TextView time = (TextView) tr.findViewById(R.id.time);
                        time.setText(it.getTime());
                        tr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "Table element clicked");
                                // Create custom dialog object
                                final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_DialogWhenLarge);
                                // Include dialog.xml file
                                dialog.setContentView(R.layout.event_view);
                                // Set dialog title
                                dialog.setTitle("Event Details");
                                TextView title = (TextView) dialog.findViewById(R.id.title);
                                title.setText(it.getTitle());

                                TextView desc = (TextView) dialog.findViewById(R.id.desc);
                                desc.setText(it.getDescription());

                                TextView venue = (TextView) dialog.findViewById(R.id.venue);
                                venue.setText(it.getVenue());

                                TextView date = (TextView) dialog.findViewById(R.id.date);
                                date.setText(it.getDate());

                                TextView time = (TextView) dialog.findViewById(R.id.time);
                                time.setText(it.getTime());

                                TextView createdBy = (TextView) dialog.findViewById(R.id.createdBy);
                                createdBy.setText(it.getCreatedBy());

                                TextView audience = (TextView) dialog.findViewById(R.id.audience);
                                audience.setText(it.getAudience());

                                TextView helpers = (TextView) dialog.findViewById(R.id.helpers);
                                StringBuilder sb = new StringBuilder();
                                for (String s : it.getHelpers()) {
                                    sb.append(s);
                                    sb.append("\n");
                                }
                                helpers.setText(sb.toString());

                                TextView role = (TextView) dialog.findViewById(R.id.role);
                                role.setText(it.getRole());

                                if (!isAttendanceTaker) {
                                    LinearLayout llrole = (LinearLayout) dialog.findViewById(R.id.llRole);
                                    llrole.setVisibility(View.GONE);
                                    LinearLayout lleventButton = (LinearLayout) dialog.findViewById(R.id.lleventButton);
                                    lleventButton.setVisibility(View.GONE);
                                }
                                else {
                                    Button editevent = (Button) dialog.findViewById(R.id.editevent);
                                    Button markAttendance = (Button) dialog.findViewById(R.id.markattendance);
                                    markAttendance.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(getContext(), markAttendanceActivity.class);
                                            i.putExtra("eventUID", it.getUid());
                                            startActivity(i);
                                        }
                                    });
                                    Button deleteEvent = (Button) dialog.findViewById(R.id.deleteevent);
                                    if (it.getDeleteFlag()) {

                                    }else {
                                        deleteEvent.setAlpha(.5f);
                                        deleteEvent.setClickable(false);
                                    }
                                }

                                dialog.show();
                            }
                        });

                        table.addView(tr);
                        Log.d(TAG, it.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<UpcomingEvent> call, Throwable t) {

            }
        });
    }


}
