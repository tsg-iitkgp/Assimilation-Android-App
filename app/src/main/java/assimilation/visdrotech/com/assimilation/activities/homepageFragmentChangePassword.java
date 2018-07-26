package assimilation.visdrotech.com.assimilation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import assimilation.visdrotech.com.assimilation.R;
import assimilation.visdrotech.com.assimilation.retrofitModels.changePassword;
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
 * Created by defcon on 27/07/18.
 */

public class homepageFragmentChangePassword extends Fragment {
    private String prefName,token ;
    private TextInputEditText password,repassword;
    private SweetAlertDialog pDialog;
    private Button submit;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.change_password_title));
        prefName =  ((baseApplicationClass) getActivity().getApplication()).PREF_NAME ;
        SharedPreferences prefs = this.getActivity().getSharedPreferences(prefName, MODE_PRIVATE);

//        String audienceObj = prefs.getString("audience", "");
        token = prefs.getString("token", "");
        initialiseVariable(view);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Please wait!");
                pDialog.setCancelable(false);

                if (pass.equals("") || pass.length() == 0){
                    Toast.makeText(getContext(), getString(R.string.no_password_entered),
                            Toast.LENGTH_SHORT).show();
                }
                else if (repass.equals("") || repass.length() == 0){
                    Toast.makeText(getContext(), getString(R.string.no_repassword_entered),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    pDialog.show();
                    sendChangePasswordRequest(pass,token);
                }
            }
        });

//
//        try {
//            JSONObject mainObject = new JSONObject(audienceObj);
//            stAudienceKey = mainObject.getString("key");
//            audience.setText(mainObject.getString("value"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Sentry.capture(e);
//        }
//        List<String> items = Arrays.asList(audienceObj.split("\\s*,\\s*"));
//        Log.d(TAG, items.get(0) + "|" + items.get(1));

    }

    private void sendChangePasswordRequest(String pass, String token) {
        restInterface restService = restClient.getClient().create(restInterface.class);
        restService.changePassword(token,pass).enqueue(new Callback<changePassword>() {
            @Override
            public void onResponse(Call<changePassword> call, Response<changePassword> response) {
                pDialog.dismissWithAnimation();
                if (response.isSuccessful()){
                    final SweetAlertDialog successAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    successAlertDialog.setTitleText("Success!");
                    successAlertDialog.setContentText("Password Changed successfully!");
                    successAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            successAlertDialog.dismissWithAnimation();
                            Intent i = new Intent(getContext(),homepage.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    });
                    successAlertDialog.show();
                    password.getText().clear();
                    repassword.getText().clear();
                }
                else {
                    SweetAlertDialog erroDialog;
                    erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    erroDialog.setTitleText("Error!");
                    erroDialog.setContentText("Unable to mark attendance. Please try again!")  ;
                    erroDialog.show();
                }

            }

            @Override
            public void onFailure(Call<changePassword> call, Throwable t) {
                pDialog.dismissWithAnimation();
                SweetAlertDialog erroDialog;
                erroDialog =  new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                erroDialog.setTitleText("Error!");
                erroDialog.setContentText("Unable to mark attendance. Please try again!")  ;
                erroDialog.show();
            }
        });
    }

    private void initialiseVariable(View view) {
        password = view.findViewById(R.id.password);
        repassword = view.findViewById(R.id.repassword);
        submit = view.findViewById(R.id.submit);

    }

}
