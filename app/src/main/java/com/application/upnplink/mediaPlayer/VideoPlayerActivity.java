package com.application.upnplink.mediaPlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.application.upnplink.R;

public class VideoPlayerActivity extends Activity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener,View.OnTouchListener {

    private VideoView videoview;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoview = (VideoView)findViewById(R.id.videoview);
        videoview.setOnCompletionListener(this);
        videoview.setOnPreparedListener(this);
        videoview.setOnTouchListener(this);

        ImageButton sendUpnpButton = (ImageButton)findViewById(R.id.sendUpnp);
        sendUpnpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoPlayerActivity.this, "Send upnp", Toast.LENGTH_SHORT).show();
            }
        });
        String url = null;
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("url");
            playFile( url);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        String url = null;
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("url");
            playFile( url);
        }
    }

    private boolean playFile(String url) {
        if (url != null) {
            // Set the media controller buttons
            if (mediaController == null) {
                mediaController = new MediaController(this);
                // Set the videoView that acts as the anchor for the MediaController.
                mediaController.setAnchorView(videoview);
                // Set MediaController for VideoView
                videoview.setMediaController(mediaController);
            }
            videoview.requestFocus();
            videoview.setVideoURI(Uri.parse(url));
            videoview.start();
            return true;
        } else {
            return false;
        }
    }

    public void stopPlaying() {
        videoview.stopPlayback();
        this.finish();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (videoview.isPlaying()) {
            if (videoview.canPause() ) {
                videoview.pause();
                mediaController.show();
            }
        } else {
            videoview.start();
            mediaController.show();
        }

        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
    }

    // When you change direction of phone, this method will be called.
    // It store the state of video (Current position)
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Store current position.
        savedInstanceState.putInt("CurrentPosition", videoview.getCurrentPosition());
        videoview.pause();
    }


    // After rotating the phone. This method is called.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Get saved position.
        int position = savedInstanceState.getInt("CurrentPosition");
        videoview.seekTo(position);
    }

    //Convenience method to show a video
    public static void showRemoteVideo(Context ctx, String url) {
        Intent i = new Intent(ctx, VideoPlayerActivity.class);
        i.putExtra("url", url);
        ctx.startActivity(i);
    }


}
