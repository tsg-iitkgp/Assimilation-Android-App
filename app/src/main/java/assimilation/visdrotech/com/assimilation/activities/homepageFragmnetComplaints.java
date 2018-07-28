package assimilation.visdrotech.com.assimilation.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

import assimilation.visdrotech.com.assimilation.R;
import assimilation.visdrotech.com.assimilation.retrofitModels.Complaints;
import assimilation.visdrotech.com.assimilation.retrofitModels.EventDatum;
import assimilation.visdrotech.com.assimilation.retrofitModels.UpcomingEvent;
import assimilation.visdrotech.com.assimilation.retrofitModels.complaintStatus;
import assimilation.visdrotech.com.assimilation.retrofitModels.complaintsDatum;
import assimilation.visdrotech.com.assimilation.retrofitModels.deleteEvent;
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

public class homepageFragmnetComplaints extends Fragment {
    private String prefName ;
    private String token;
    SweetAlertDialog pDialog,pDialogDeleteEvent;
    private static final String TAG = "FragmnetAllComplaints";
    private TableLayout table;
    private Boolean isAttendanceTaker;
    private SwipeRefreshLayout swipeLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_all_complaints, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.allcomplaints_title));
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                renderData(view);
            }
        });
        renderData(view);
    }

    public void renderData(final View view){
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
        final restInterface restService = restClient.getClient().create(restInterface.class);
        restService.getAllComplaints(token).enqueue(new Callback<Complaints>() {
            @Override
            public void onResponse(Call<Complaints> call, Response<Complaints> response) {
                pDialog.dismissWithAnimation();
                swipeLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    Log.d(TAG, "Success response");
                    if (response.body().getStatus()){

                    List<complaintsDatum> responseList = response.body().getData();
                    LayoutInflater inflater = LayoutInflater.from(getContext());

                    cleanTable(table);
                    for (final complaintsDatum it : responseList) {

                        View tr = inflater.inflate(R.layout.all_complaints_table_row, null, false);
                        TextView title = (TextView) tr.findViewById(R.id.student);
                        title.setText(it.getComplaintBy());
                        TextView venue = (TextView) tr.findViewById(R.id.title);
                        venue.setText(it.getEventTitle());
                        TextView date = (TextView) tr.findViewById(R.id.date);
                        date.setText(it.getComplaintDateTime());
                        tr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "Table element clicked");
                                // Create custom dialog object
                                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_DialogWhenLarge);
                                // Include dialog.xml file
                                dialog.setContentView(R.layout.complaint_view);
                                // Set dialog title
                                dialog.setTitle("Complaint Details");
                                TextView title = (TextView) dialog.findViewById(R.id.title);
                                title.setText(it.getEventTitle());

                                TextView desc = (TextView) dialog.findViewById(R.id.student);
                                desc.setText(it.getComplaintBy());

                                TextView venue = (TextView) dialog.findViewById(R.id.complaint);
                                venue.setText(it.getComplaintMessage());

                                TextView date = (TextView) dialog.findViewById(R.id.complaintdate);
                                date.setText(it.getComplaintDateTime());

                                TextView time = (TextView) dialog.findViewById(R.id.venue);
                                time.setText(it.getEventVenue());

                                TextView createdBy = (TextView) dialog.findViewById(R.id.eventdate);
                                createdBy.setText(it.getEventDate());

                                TextView audience = (TextView) dialog.findViewById(R.id.createdBy);
                                audience.setText(it.getEventCreatedBy());

                                TextView helpers = (TextView) dialog.findViewById(R.id.helpers);
                                StringBuilder sb = new StringBuilder();
                                for (String s : it.getEventHelpers()) {
                                    sb.append(s);
                                    sb.append("\n");
                                }
                                helpers.setText(sb.toString());

                                Button markResolved = (Button) dialog.findViewById(R.id.complaintButton);
                                    if (!(it.getComplaintResolutionStatus())) {

                                    markResolved.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText("Are you sure?")
                                                        .setContentText("Do you really want to mark this complaint resolved?")
                                                        .setConfirmText("Yes,mark it!")
                                                        .setCancelText("Cancel")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();
                                                                pDialogDeleteEvent = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                                                                pDialogDeleteEvent.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                                pDialogDeleteEvent.setTitleText("Loading");
                                                                pDialogDeleteEvent.setCancelable(false);
                                                                pDialogDeleteEvent.show();
                                                                restService.changeComplaintStatus(token, it.getComplaintId()).enqueue(new Callback<complaintStatus>() {
                                                                    @Override
                                                                    public void onResponse(Call<complaintStatus> call, Response<complaintStatus> response) {
                                                                        pDialogDeleteEvent.dismissWithAnimation();
                                                                        if (response.isSuccessful()) {
                                                                            complaintStatus obj = response.body();
                                                                            if (obj.getcomplaintStatus()) {
                                                                                final SweetAlertDialog successAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                                                                successAlertDialog.setTitleText("Success!");
                                                                                successAlertDialog.setContentText("Complaint marked resolved successfully!");
                                                                                successAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                                    @Override
                                                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                                        sweetAlertDialog.dismissWithAnimation();
                                                                                        dialog.dismiss();
                                                                                        renderData(view);
                                                                                    }
                                                                                });
                                                                                successAlertDialog.show();
                                                                            } else {
                                                                                SweetAlertDialog erroDialog;
                                                                                erroDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                                                                erroDialog.setTitleText("Error!");
                                                                                erroDialog.setContentText("Unable to change complaint status. Please try again!");
                                                                                erroDialog.show();
                                                                            }
                                                                        } else {
                                                                            SweetAlertDialog erroDialog;
                                                                            erroDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                                                            erroDialog.setTitleText("Error!");
                                                                            erroDialog.setContentText("Unable to change complaint status. Please try again!");
                                                                            erroDialog.show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<complaintStatus> call, Throwable t) {
                                                                        pDialogDeleteEvent.dismissWithAnimation();
                                                                        SweetAlertDialog erroDialog;
                                                                        erroDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                                                        erroDialog.setTitleText("Error!");
                                                                        erroDialog.setContentText("Unable to change complaint status. Please try again!");
                                                                        erroDialog.show();
                                                                    }
                                                                });

                                                            }
                                                        })
                                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .show();


                                            }
                                        });

                                    } else {
                                        markResolved.setAlpha(.5f);
                                        markResolved.setClickable(false);
                                    }


                                dialog.show();
                            }
                        });

                        table.addView(tr);

                    }
                }
                else {
                SweetAlertDialog erroDialog;
                erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                erroDialog.setTitleText("Error!");
                erroDialog.setContentText("Unable to refresh data. Please try again!")  ;
                erroDialog.show();
                    }
                }
                else {
                    SweetAlertDialog erroDialog;
                    erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    erroDialog.setTitleText("Error!");
                    erroDialog.setContentText("Unable to refresh data. Please try again!")  ;
                    erroDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Complaints> call, Throwable t) {
                swipeLayout.setRefreshing(false);
                Log.d(TAG, "In on fail");
                SweetAlertDialog erroDialog;
                erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                erroDialog.setTitleText("Error!");
                erroDialog.setContentText("Unable to refresh data. Please try again!")  ;
                erroDialog.show();
            }
        });
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }
}
