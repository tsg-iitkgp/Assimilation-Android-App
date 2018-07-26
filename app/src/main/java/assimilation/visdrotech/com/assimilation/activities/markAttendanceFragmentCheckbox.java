package assimilation.visdrotech.com.assimilation.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

import assimilation.visdrotech.com.assimilation.R;
import assimilation.visdrotech.com.assimilation.retrofitModels.AttendanceList;
import assimilation.visdrotech.com.assimilation.retrofitModels.checkboxAttendanceStudentList;
import assimilation.visdrotech.com.assimilation.utils.restClient;
import assimilation.visdrotech.com.assimilation.utils.restInterface;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by defcon on 12/07/18.
 */

public class markAttendanceFragmentCheckbox extends Fragment {
    private restInterface restService;
    private LinearLayout rootView;
    private Button submitButton ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_markattendance_checkbox, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = (LinearLayout) view.findViewById(R.id.root) ;
        final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        final Activity activity = getActivity();
        restService = restClient.getClient().create(restInterface.class);
        restService.getAllStudentsAttendanceList(activity.getIntent().getExtras().getString("eventUID")).enqueue(new Callback<checkboxAttendanceStudentList>() {
            @Override
            public void onResponse(Call<checkboxAttendanceStudentList> call, Response<checkboxAttendanceStudentList> response) {
                if (response.isSuccessful()) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    checkboxAttendanceStudentList responseObj = response.body();
//                    Log.d("BOOLTAG", )
                    Log.d("TAG",responseObj.toString() );
                    for(AttendanceList item : responseObj.getAttendanceList()){
                        View stud = inflater.inflate(R.layout.markattendance_checkbox_row, null,false);
                        TextView name = (TextView) stud.findViewById(R.id.name);
                        name.setText(item.getName());

                        TextView roll = (TextView) stud.findViewById(R.id.roll);
                        roll.setText(item.getRoll());

                        CheckBox attendanceStatusCheckBox = (CheckBox) stud.findViewById(R.id.status);
                        Boolean attendanceStatus = item.getAttendanceStatus();
                        if (attendanceStatus){
                            attendanceStatusCheckBox.setChecked(true);
                        }
                        else {
                            attendanceStatusCheckBox.setChecked(false);
                        }
                        rootView.addView(stud);
                        pDialog.dismissWithAnimation();
                    }

                }

            }

            @Override
            public void onFailure(Call<checkboxAttendanceStudentList> call, Throwable t) {
                pDialog.dismissWithAnimation();
            }
        });


        submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
