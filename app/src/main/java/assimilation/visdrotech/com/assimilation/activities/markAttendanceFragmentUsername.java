package assimilation.visdrotech.com.assimilation.activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import assimilation.visdrotech.com.assimilation.R;
import assimilation.visdrotech.com.assimilation.retrofitModels.singleStudentAttendance;
import assimilation.visdrotech.com.assimilation.utils.restClient;
import assimilation.visdrotech.com.assimilation.utils.restInterface;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by defcon on 11/07/18.
 */

public class markAttendanceFragmentUsername extends Fragment {
    private TextInputEditText edittext;
    private Button submitButton;
    private restInterface restService;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_markattendance_username, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        //you can set the title for your toolbar here for different fragments different titles

        edittext = (TextInputEditText) view.findViewById(R.id.username);
        submitButton = (Button) view.findViewById(R.id.submit);
        restService = restClient.getClient().create(restInterface.class);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edittext.getText().toString();
                final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
                if ((username == "") || (username.length() == 0)){
                    pDialog.dismissWithAnimation();
                    SweetAlertDialog erroDialog;
                    erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    erroDialog.setTitleText("Error!");
                    erroDialog.setContentText("Please enter a username.");
                    erroDialog.show();
                }
                else {
                    restService.markSingleAttendance(activity.getIntent().getExtras().getString("eventUID"), username, Boolean.TRUE).enqueue(new Callback<singleStudentAttendance>() {
                        @Override
                        public void onResponse(Call<singleStudentAttendance> call, Response<singleStudentAttendance> response) {
                            pDialog.dismissWithAnimation();
                            if (response.isSuccessful()) {
                                singleStudentAttendance responseObj = response.body();
                                if (responseObj.getAttendanceStatus()) {
                                    final SweetAlertDialog successAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                    successAlertDialog.setTitleText("Success!");
                                    successAlertDialog.setContentText("Attendance marked successfully!");
                                    successAlertDialog.show();
                                }
                                else {
                                    SweetAlertDialog erroDialog;
                                    erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                    erroDialog.setTitleText("Error!");
                                    erroDialog.setContentText("Unable to mark attendance. Please try again!")  ;
                                    erroDialog.show();
                                }
                            }
                            else{
                                SweetAlertDialog erroDialog;
                                erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                erroDialog.setTitleText("Error!");
                                erroDialog.setContentText("Unable to mark attendance. Please try again!");
                                erroDialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<singleStudentAttendance> call, Throwable t) {
                            pDialog.dismissWithAnimation();
                            SweetAlertDialog erroDialog;
                            erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                            erroDialog.setTitleText("Error!");
                            erroDialog.setContentText("Problem connecting with the server. Please try again!");
                            erroDialog.show();
                        }
                    });

                }

            }
        });
    }
}
