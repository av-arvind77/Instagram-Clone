package com.yellowseed.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import com.yellowseed.application.YellowSeedApplication;

import java.net.URL;
import java.net.URLStreamHandler;

public class Player  {
    private static final Player ourInstance = new Player();
    private final String TAG = Player.class.getSimpleName();

    private SimpleExoPlayer simpleExoPlayer;
    private Context context;
    private com.google.android.exoplayer2.Player.EventListener eventListener = new com.google.android.exoplayer2.Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            Log.e("state", "onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.e("state", "onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.e("state", "onLoadingChanged");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.e("state", "onPlayerStateChanged");

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            Log.e("state", "onRepeatModeChanged");
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            Log.e("state", "onShuffleModeEnabledChanged");
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.e("state", "onPlayerError");
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.e("state", "onPositionDiscontinuity");
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.e("state", "onPlaybackParametersChanged");
        }

        @Override
        public void onSeekProcessed() {
            Log.e("state", "onSeekProcessed");
        }

    };

    private Player() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = getnewExoPlayer();
        }
    }

    public static Player getInstance() {
        return ourInstance;
    }

    private static String getDefaultUserAgent() {
        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version")); // such as 1.1.0
        result.append(" (Linux; U; Android ");
        String version = Build.VERSION.RELEASE; // "1.0" or "3.4b5"
        result.append(version.length() > 0 ? version : "1.0");        // add the model for the release build
        if ("REL".equals(Build.VERSION.CODENAME)) {
            String model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        String id = Build.ID; // "MASTER" or "M4-rc20"
        if (id.length() > 0) {
            result.append(" Build/");
            result.append(id);
        }

        result.append(")");
        return result.toString();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public SimpleExoPlayer getPlayer() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = getnewExoPlayer();
        }
        return simpleExoPlayer;
    }

    public void play(String mrl) {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = getnewExoPlayer();
            simpleExoPlayer.setPlayWhenReady(false);
        }
        Uri uri;
        if (simpleExoPlayer != null && !TextUtils.isEmpty(mrl)) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                URL.setURLStreamHandlerFactory(new java.net.URLStreamHandlerFactory() {
                    public URLStreamHandler createURLStreamHandler(String protocol) {
                        Log.e("SongService", "Asking for stream handler for protocol: '" + protocol + "'");
                        return null;
                    }
                });
            }
            uri = Uri.parse(mrl);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(YellowSeedApplication.getInstance().getApplicationContext(), getDefaultUserAgent());
            DefaultExtractorsFactory defaultExtractorsFactory = new DefaultExtractorsFactory();

            ExtractorMediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, defaultExtractorsFactory
                    , null, null);
            simpleExoPlayer.prepare(audioSource, true, true);
            simpleExoPlayer.addListener(eventListener);
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.setVolume(1f);
        } else {
            simpleExoPlayer.setPlayWhenReady(false);
        }

    }

    public void play(boolean isPlay) {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(isPlay);
        }
    }

    public SimpleExoPlayer getnewExoPlayer() {
        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create the player
        return ExoPlayerFactory.newSimpleInstance(YellowSeedApplication.getInstance().getApplicationContext(), trackSelector);
        //exoPlayer.addListener(this);
    }


    /*public void requestMusicFocus() {
        AudioManager am = (AudioManager) AntAudioApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);

// Request audio focus for playback
        int result = am.requestAudioFocus(this,
// Use the music stream.
                AudioManager.STREAM_MUSIC,
// Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
// other app had stopped playing song now , so u can do u stuff now .
            play(true);
        }
    }
*/

}