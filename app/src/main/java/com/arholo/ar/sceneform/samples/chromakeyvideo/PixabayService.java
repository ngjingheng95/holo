package com.arholo.ar.sceneform.samples.chromakeyvideo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PixabayService {

    public static PixabayApi createPixabayService(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .baseUrl(PixabayApi.BASE_URL)
                .build();

        return retrofit.create(PixabayApi.class);
    }

}
