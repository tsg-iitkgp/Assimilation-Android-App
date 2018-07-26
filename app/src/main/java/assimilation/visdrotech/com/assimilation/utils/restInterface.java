package assimilation.visdrotech.com.assimilation.utils;

/**
 * Created by defcon on 09/07/18.
 */

import org.json.JSONObject;

import java.util.ArrayList;

import assimilation.visdrotech.com.assimilation.retrofitModels.UpcomingEvent;
import assimilation.visdrotech.com.assimilation.retrofitModels.changePassword;
import assimilation.visdrotech.com.assimilation.retrofitModels.checkboxAttendanceStudentList;
import assimilation.visdrotech.com.assimilation.retrofitModels.createEvent;
import assimilation.visdrotech.com.assimilation.retrofitModels.deleteEvent;
import assimilation.visdrotech.com.assimilation.retrofitModels.loginSuccess;
import assimilation.visdrotech.com.assimilation.retrofitModels.singleStudentAttendance;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface restInterface {
    @POST("api/login")
    @FormUrlEncoded
    Call<loginSuccess> login(@Field("username") String username,
                             @Field("password") String password,
                             @Field("deviceId") String deviceId
                             );

    @POST("api/createevent")
    @FormUrlEncoded
    Call<createEvent> createEvent(@Field("token") String token,
                                 @Field("title") String title,
                                 @Field("description") String description,
                                 @Field("venue") String venue,
                                 @Field("date") String date,
                                 @Field("time") String time,
                                 @Field("audience") String audience,
                                 @Field("helpers") ArrayList<String> helpers
                       );

    @POST("api/upcomingevent")
    @FormUrlEncoded
    Call<UpcomingEvent> upcomingEvent(@Field("token") String token
    );

    @POST("api/singlestudentattendance")
    @FormUrlEncoded
    Call<singleStudentAttendance> markSingleAttendance(@Field("eventUid") String eventId,
                                                       @Field("username") String username,
                                                       @Field("status") Boolean status
        );


    @POST("api/getstudentattendancelist")
    @FormUrlEncoded
    Call<checkboxAttendanceStudentList> getAllStudentsAttendanceList(@Field("eventUid") String eventUid);

    @POST("api/changepassword")
    @FormUrlEncoded
    Call<changePassword> changePassword(@Field("token") String token,
                                        @Field("password") String password);

    @POST("api/deleteevent")
    @FormUrlEncoded
    Call<deleteEvent> deleteEvent (@Field("token") String token,
                                   @Field("eventUid") String eventUid);

}