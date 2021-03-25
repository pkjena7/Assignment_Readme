package com.example.assignment_readme;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("json/24.48.0.1")
    Call getPost();
}
