package apps.igor.movies.api;

import com.google.gson.GsonBuilder;

import apps.igor.movies.constants.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient retrofitClient;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MOVIEDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getRetrofitClient(){
        if(retrofitClient == null)
            retrofitClient = new RetrofitClient();

        return retrofitClient;
    }
    public API getAPI(){
        return retrofit.create(API.class);
    }
}
