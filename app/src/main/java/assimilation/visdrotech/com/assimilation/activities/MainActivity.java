package assimilation.visdrotech.com.assimilation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import assimilation.visdrotech.com.assimilation.R;
import assimilation.visdrotech.com.assimilation.retrofitModels.loginSuccess;
import assimilation.visdrotech.com.assimilation.utils.baseApplicationClass;
import assimilation.visdrotech.com.assimilation.utils.restClient;
import assimilation.visdrotech.com.assimilation.utils.restInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String prefName =  ((baseApplicationClass) this.getApplication()).PREF_NAME ;
    private EditText username,password;
    private Button signin;
    private ProgressBar loginProgress;
    private final static String TAG = "mainActivity" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkIfLoginCredentialsAreStored()) {
            Intent i = new Intent(this,homepage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        setContentView(R.layout.activity_main);
        initialiseVariables();



//        SharedPreferences.Editor editor = getSharedPreferences(prefName, MODE_PRIVATE).edit();
//        editor.putString("name", "Elena");
//        editor.putInt("idName", 12);
//        editor.apply();




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signin :
                String uname = username.getText().toString();
                String pass = password.getText().toString();
                if (uname.equals("") || uname.length() == 0){
                    Toast.makeText(this, getString(R.string.no_username),
                            Toast.LENGTH_SHORT).show();
                }
                else if (pass.equals("") || pass.length() == 0){
                    Toast.makeText(this, getString(R.string.no_password),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    sendLoginData(uname,pass);
                }
                break;
        }
    }
    private void sendLoginData(final String uname, String pass) {
        loginProgress.setVisibility(View.VISIBLE);
        restInterface restService = restClient.getClient().create(restInterface.class);
//        Call<loginSuccess> call= restService.login();
        restService.login(uname,pass,"123").enqueue(new Callback<loginSuccess>() {
            @Override
            public void onResponse(Call<loginSuccess> call, Response<loginSuccess> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = getSharedPreferences(prefName, MODE_PRIVATE).edit();
                    loginSuccess responseObject = response.body();
                    editor.putString("name",responseObject.getName());
                    editor.putString("groups",responseObject.getGroups());
                    editor.putBoolean("isLoggedIn",true);
                    editor.putString("token", responseObject.getToken());
                    editor.apply();
                    loginProgress.setVisibility(View.GONE);
                    Intent i = new Intent(getApplicationContext(),homepage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_username),
                            Toast.LENGTH_SHORT).show();
                    Log.d("loginCheck", "Failed" + response.code());
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<loginSuccess> call, Throwable t) {

            }
        });
//        String url = "api/login";
//        RequestParams params = new RequestParams();
//        params.put("username",uname);
//        params.put("password",pass);
//        params.put("deviceId", "123");
//        SharedPreferences.Editor editor = getSharedPreferences(prefName, MODE_PRIVATE).edit();
//        editor.putString("username",uname);
//        editor.putString("password",pass);
//        editor.apply();
//        restClient.post(url, params, new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                Log.d("respcheck", String.valueOf(statusCode));
//                if (statusCode == 200){
//                    SharedPreferences.Editor editor = getSharedPreferences(prefName, MODE_PRIVATE).edit();
//                    try {
//
//                        String name = (String) response.get("name");
//                        editor.putString("name",name);
//                        String groups = (String) response.get("groups");
//                        editor.putString("groups",groups);
//                        editor.putBoolean("isLoggedIn",true);
//                        String authToken = (String) response.get("token");
//                        editor.putString("token", authToken);
//                        editor.apply();
//                        loginProgress.setVisibility(View.GONE);
//                        Intent i = new Intent(getApplicationContext(),homepage.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                Log.d("failRespCheck",String.valueOf(statusCode));
//            }
//        });

    }
    private Boolean checkIfLoginCredentialsAreStored() {
        SharedPreferences prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        Boolean loginStatus = prefs.getBoolean("isLoggedIn", false);
        return loginStatus;
    }


    private void initialiseVariables() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.signin);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        signin.setOnClickListener(this);
    }
}
