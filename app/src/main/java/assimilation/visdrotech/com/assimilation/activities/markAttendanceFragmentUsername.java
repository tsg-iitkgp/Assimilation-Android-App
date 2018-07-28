package assimilation.visdrotech.com.assimilation.activities;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},1);
//            startActivity(intent);
        }
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d("CHECK", "Inresult");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},1);
//            startActivity(intent);
        }
//        if (requestCode == 1) {
//            // BEGIN_INCLUDE(permission_result)
//            // Received permission result for camera permission.
//            Log.i("G", "Received response for Camera permission request.");
//
//            // Check if the only required permission has been granted
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Camera permission has been granted, preview can be displayed
//                Log.i("G", "CAMERA permission has now been granted. Showing preview.");
//
//            } else {
//                SweetAlertDialog erroDialog;
//                erroDialog =  new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
//                erroDialog.setTitleText("Error!");
//                erroDialog.setContentText("Camera Permission is required for smooth functioning of the app.");
//                erroDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},1);
//                        sweetAlertDialog.dismissWithAnimation();
//                    }
//                });
//                erroDialog.setCancelText("Cancel");
//                erroDialog.setConfirmText("Give Permission");
//                erroDialog.show();
//                Log.i("G", "CAMERA permission was NOT granted.");
//
//
//            }
//        }
    }

}
