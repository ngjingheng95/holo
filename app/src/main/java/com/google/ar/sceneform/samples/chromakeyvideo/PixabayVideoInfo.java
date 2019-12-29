package com.google.ar.sceneform.samples.chromakeyvideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
* GET Request on Pixabay API
* -> Retrieve PixabayVideoRequestInfo (total, totalHits)
* -> get individual video results, PixabayVideoInfo, with their attributes eg id, pageURL etc
* -> PixabayVideo (large/medium/small/tiny)
* -> Attributes of PixabayVideo from PixabayBaseVideo (eg url, width etc)
 */

public class PixabayVideoInfo {
    @SerializedName("picture_id")
    @Expose
    private String pictureId;
    @SerializedName("videos")
    @Expose
    private PixabayVideo videos;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("downloads")
    @Expose
    private int downloads;
    @SerializedName("likes")
    @Expose
    private int likes;
    @SerializedName("favorites")
    @Expose
    private int favorites;
    @SerializedName("duration")
    @Expose
    private int duration;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("views")
    @Expose
    private int views;
    @SerializedName("comments")
    @Expose
    private int comments;
    @SerializedName("userImageURL")
    @Expose
    private String userImageURL;
    @SerializedName("pageURL")
    @Expose
    private String pageURL;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("user")
    @Expose
    private String user;

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public PixabayVideo getVideos() {
        return videos;
    }

    public void setVideos(PixabayVideo videos) {
        this.videos = videos;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
