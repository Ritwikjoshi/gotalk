package in.botlabs.voicebot;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public static final String API_KEY = "AIzaSyArFxSvF2kmdhAM2iFXFadTARD0GDkV8I4";
    public String VIDEO_ID;
    YouTubePlayerView youtubePlayerView;

    SharedPreferences shared_Details;
    SharedPreferences.Editor edit_Details;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        youtubePlayerView = findViewById(R.id.youtubetri);

        shared_Details = getSharedPreferences("Details", Context.MODE_PRIVATE);
        edit_Details = shared_Details.edit();
        VIDEO_ID = shared_Details.getString("videoUrl","");

        youtubePlayerView.initialize(API_KEY,VideoActivity.this);


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        //start buffering
        if(!b)
        {
            youTubePlayer.cueVideo(VIDEO_ID);

        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener()
    {

        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };
}
