package in.botlabs.voicebot.Retrofit;


import in.botlabs.voicebot.Model.NewsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface API {

    @GET("everything?q=indigo&from=2018-08-23&sortBy=publishedAt&apiKey=f54cde78f4bb4475add51d36d8a31aa6")
    Call<NewsModel> getNews();
}