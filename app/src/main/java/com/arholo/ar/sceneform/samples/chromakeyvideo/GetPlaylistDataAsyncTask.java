package com.arholo.ar.sceneform.samples.chromakeyvideo;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;

import java.io.IOException;

public class GetPlaylistDataAsyncTask extends AsyncTask<String, Void, PlaylistItemListResponse> {
    private static final String YOUTUBE_PLAYLIST_PART = "snippet";
    private static final String YOUTUBE_PLAYLIST_FIELDS = "items(snippet(thumbnails(medium(url))), snippet(resourceId(videoId)))";

    private YouTube mYouTubeDataApi;

    public GetPlaylistDataAsyncTask(YouTube api) {
        mYouTubeDataApi = api;
    }

    @Override
    protected PlaylistItemListResponse doInBackground(String... params) {
        final String playlistId = params[0];

        PlaylistItemListResponse playlistItemListResponse;
        try {
            playlistItemListResponse = mYouTubeDataApi.playlistItems()
                    .list(YOUTUBE_PLAYLIST_PART)
                    .setPlaylistId(playlistId)
                    .setMaxResults((long) 50)
                    .setFields(YOUTUBE_PLAYLIST_FIELDS)
                    .setKey("AIzaSyCK7mhqGP1qPjLuqGNAg36IuWGCHQbD62s")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return playlistItemListResponse;
    }

//    @Override
//    protected PlaylistListResponse doInBackground(String[]... params) {
//
//        final String[] playlistIds = params[0];
//
//        PlaylistListResponse playlistListResponse;
//        try {
//            playlistListResponse = mYouTubeDataApi.playlists()
//                    .list(YOUTUBE_PLAYLIST_PART)
//                    .setId(TextUtils.join(",", playlistIds))
//                    .setFields(YOUTUBE_PLAYLIST_FIELDS)
//                    .setKey("AIzaSyCK7mhqGP1qPjLuqGNAg36IuWGCHQbD62s") //Here you will have to provide the keys
//                    .execute();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        return playlistListResponse;
//    }
}
