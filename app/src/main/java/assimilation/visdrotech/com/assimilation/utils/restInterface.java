package assimilation.visdrotech.com.assimilation.utils;

/**
 * Created by defcon on 09/07/18.
 */

import assimilation.visdrotech.com.assimilation.retrofitModels.loginSuccess;
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
                             @Field("devxiceId") String deviceId
                             );

//    @GET("movie/{id}")
//    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}