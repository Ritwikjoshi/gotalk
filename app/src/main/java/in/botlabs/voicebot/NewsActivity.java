package in.botlabs.voicebot;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import in.botlabs.voicebot.Adapters.GlideApp;

public class NewsActivity extends AppCompatActivity {

    ImageView newsImage;
    TextView name, description;

    SharedPreferences shared_Details;
    SharedPreferences.Editor edit_Details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        description = findViewById(R.id.newsDescription);
        name = findViewById(R.id.newsName);
        newsImage = findViewById(R.id.newsImage);

        shared_Details = getSharedPreferences("Details", Context.MODE_PRIVATE);
        edit_Details = shared_Details.edit();

        description.setText(shared_Details.getString("newsDescription",""));

        GlideApp.with(newsImage.getContext())
                .load(shared_Details.getString("newsImage",""))
                .into(newsImage);

        name.setText(shared_Details.getString("newsName",""));


    }
}
