package in.botlabs.voicebot.Retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retroClient {

    private static final String ROOT_URL = "https://newsapi.org/v2/";
//    private static final String ROOT_URL = "http://192.168.43.132:3000/api/appclients/";

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static API getApiService() {
        return getRetrofitInstance().create(API.class);
    }
}