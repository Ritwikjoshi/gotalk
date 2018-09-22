package in.botlabs.voicebot.Retrofit;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.botlabs.voicebot.Model.ResultModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API {

    @GET("json?")
    Call<ResultModel> getNearestPlaces(@Query("key") String key,
                                  @Query("sensor") String sensor,
                                  @Query("location") String location,
                                  @Query("radius") String radius,
                                  @Query("keyword") String keyword);
}