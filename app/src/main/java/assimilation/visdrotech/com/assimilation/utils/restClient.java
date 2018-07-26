package assimilation.visdrotech.com.assimilation.utils;

/**
 * Created by defcon on 09/07/18.
 */

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class restClient {

    public static final String BASE_URL = "http://fde77776.ngrok.io/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}