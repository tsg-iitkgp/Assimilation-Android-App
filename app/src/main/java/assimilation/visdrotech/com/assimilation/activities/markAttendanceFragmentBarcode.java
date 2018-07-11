package assimilation.visdrotech.com.assimilation.activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

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

public class markAttendanceFragmentBarcode extends Fragment {
    private CodeScanner mCodeScanner;
    private String eventId;
    private SweetAlertDialog pDialog, erroDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.fragment_markattendance_barcode, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        eventId = activity.getIntent().getExtras().getString("eventUID");
        mCodeScanner = new CodeScanner(activity, scannerView);
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        erroDialog.setTitleText("Error!");
        erroDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mCodeScanner.startPreview();
            }
        });
        final restInterface restService = restClient.getClient().create(restInterface.class);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.show();
                        restService.markSingleAttendance(eventId, result.getText(), Boolean.TRUE).enqueue(new Callback<singleStudentAttendance>() {
                            @Override
                            public void onResponse(Call<singleStudentAttendance> call, Response<singleStudentAttendance> response) {
                                pDialog.dismissWithAnimation();
                                if (response.isSuccessful()) {
                                    singleStudentAttendance responseObj = response.body();
                                    if (responseObj.getAttendanceStatus()) {

                                        final SweetAlertDialog successAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                        successAlertDialog.setTitleText("Success!");
                                        successAlertDialog.setContentText("Attendance marked successfully!");
                                        successAlertDialog.setCanceledOnTouchOutside(false);
                                        successAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                              successAlertDialog.dismissWithAnimation();
                                                mCodeScanner.startPreview();
                                                mCodeScanner.startPreview();
                                            }
                                        });
                                        successAlertDialog.show();
                                    }
                                    else {
                                        SweetAlertDialog erroDialog;
                                        erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                        erroDialog.setTitleText("Error!");
                                        erroDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                mCodeScanner.startPreview();
                                            }
                                        });
                                     erroDialog.setContentText("Unable to mark attendance. Please try again!")  ;
                                       erroDialog.show();
                                    }
                                }
                                else{
                                    SweetAlertDialog erroDialog;
                                    erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                    erroDialog.setTitleText("Error!");
                                    erroDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            mCodeScanner.startPreview();
                                        }
                                    });
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
                                erroDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        mCodeScanner.startPreview();
                                    }
                                });
                                erroDialog.setContentText("Problem connecting with the server. Please try again!");
                                erroDialog.show();
                            }
                        });

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Menu 1");
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}

