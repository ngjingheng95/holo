package com.google.ar.sceneform.samples.chromakeyvideo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayApi {

    String BASE_URL = "https://pixabay.com/api/";

    @GET("videos/")
    Call<PixabayVideoRequestInfo> getVideoResults(@Query("key") String key,
                                             @Query("q") String q,
                                             @Query("safesearch") boolean safesearch,
                                             @Query("page") int page,
                                             @Query("per_page") int perPage);

}
