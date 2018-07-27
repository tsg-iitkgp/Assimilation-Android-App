package assimilation.visdrotech.com.assimilation.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import assimilation.visdrotech.com.assimilation.R;
import assimilation.visdrotech.com.assimilation.utils.baseApplicationClass;
import assimilation.visdrotech.com.assimilation.utils.restClient;
import assimilation.visdrotech.com.assimilation.utils.restInterface;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class raiseComplaint extends AppCompatActivity {
    private TextInputEditText eventTitle, eventDate, complaint;
    private String prefName =  ((baseApplicationClass) this.getApplication()).PREF_NAME ;
    private Button submit;
    private String eventUID, authToken;
    private SweetAlertDialog pDialog;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_complaint);
        setTitle("Raise Complaint");
        initialiseVariables();
//        ctx = raiseComplaint.this.;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String complaintText = complaint.getText().toString();
                if (complaintText.equals("") || complaintText.length() == 0){
                    Toast.makeText(getApplicationContext(), "Please enter the complaint text!",
                            Toast.LENGTH_SHORT).show();
                }

                else {
                    pDialog = new SweetAlertDialog(raiseComplaint.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Loading");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    restInterface restService = restClient.getClient().create(restInterface.class);
                    restService.raiseComplaint(eventUID,complaintText, authToken).enqueue(new Callback<assimilation.visdrotech.com.assimilation.retrofitModels.raiseComplaint>() {
                        @Override
                        public void onResponse(Call<assimilation.visdrotech.com.assimilation.retrofitModels.raiseComplaint> call, Response<assimilation.visdrotech.com.assimilation.retrofitModels.raiseComplaint> response) {
                            pDialog.dismissWithAnimation();
                            if (response.isSuccessful()){
                                assimilation.visdrotech.com.assimilation.retrofitModels.raiseComplaint obj = response.body();
                                if (obj.getcomplaintStatus()) {
                                final SweetAlertDialog successAlertDialog = new SweetAlertDialog(raiseComplaint.this, SweetAlertDialog.SUCCESS_TYPE);
                                successAlertDialog.setTitleText("Success!");
                                successAlertDialog.setContentText("Complaint raised successfully!");
                                successAlertDialog.show();
                                complaint.getText().clear();
                                }
                                else {
                                    SweetAlertDialog erroDialog;
                                    erroDialog =  new SweetAlertDialog(raiseComplaint.this, SweetAlertDialog.ERROR_TYPE);
                                    erroDialog.setTitleText("Error!");
                                    erroDialog.setContentText("Unable to rasie complaint. Please try again!")  ;
                                    erroDialog.show();
                                }
                            }
                            else {
                                SweetAlertDialog erroDialog;
                                erroDialog =  new SweetAlertDialog(raiseComplaint.this, SweetAlertDialog.ERROR_TYPE);
                                erroDialog.setTitleText("Error!");
                                erroDialog.setContentText("Unable to rasie complaint. Please try again!")  ;
                                erroDialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<assimilation.visdrotech.com.assimilation.retrofitModels.raiseComplaint> call, Throwable t) {
                            pDialog.dismissWithAnimation();
                            SweetAlertDialog erroDialog;
                            erroDialog =  new SweetAlertDialog(raiseComplaint.this, SweetAlertDialog.ERROR_TYPE);
                            erroDialog.setTitleText("Error!");
                            erroDialog.setContentText("Unable to rasie complaint. Please try again!")  ;
                            erroDialog.show();
                        }
                    });
                }
            }
        });


    }

    private void initialiseVariables(){
        eventTitle = (TextInputEditText) findViewById(R.id.eventTitle);
        eventDate = (TextInputEditText) findViewById(R.id.eventDate);
        complaint = (TextInputEditText) findViewById(R.id.complaint);
        submit = (Button) findViewById(R.id.submit);
        eventTitle.setText(this.getIntent().getExtras().getString("eventTitle"));
        eventDate.setText(this.getIntent().getExtras().getString("eventDate"));
        eventUID = this.getIntent().getExtras().getString("eventUID");

        SharedPreferences prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        authToken = prefs.getString("token", "");
    }
}
