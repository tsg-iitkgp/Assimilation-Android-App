package assimilation.visdrotech.com.assimilation.utils;

/**
 * Created by defcon on 09/07/18.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import assimilation.visdrotech.com.assimilation.retrofitModels.Complaints;
import assimilation.visdrotech.com.assimilation.retrofitModels.UpcomingEvent;
import assimilation.visdrotech.com.assimilation.retrofitModels.changePassword;
import assimilation.visdrotech.com.assimilation.retrofitModels.checkboxAttendanceStudentList;
import assimilation.visdrotech.com.assimilation.retrofitModels.complaintStatus;
import assimilation.visdrotech.com.assimilation.retrofitModels.createEvent;
import assimilation.visdrotech.com.assimilation.retrofitModels.deleteEvent;
import assimilation.visdrotech.com.assimilation.retrofitModels.loginSuccess;
import assimilation.visdrotech.com.assimilation.retrofitModels.raiseComplaint;
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

    @POST("api/raisecomplaint")
    @FormUrlEncoded
    Call<raiseComplaint> raiseComplaint(@Field("eventUID") String eventUID,
                                        @Field("complaint") String complaint,
                                        @Field("token") String token
                                        );
    @POST("api/allcomplaints")
    @FormUrlEncoded
    Call <Complaints> getAllComplaints(@Field("token") String token);

    @POST("api/changecomplaintstatus")
    @FormUrlEncoded
    Call <complaintStatus> changeComplaintStatus(@Field("token") String token,
                                                 @Field("complaintId") String id);

    @POST("api/markmultipleuserattendace")
    @FormUrlEncoded
    Call <singleStudentAttendance> markMultipleStudentAttendance(@Field("token") String token,
            @Field("eventUid") String eventUid,
            @Field("data") String data);
}