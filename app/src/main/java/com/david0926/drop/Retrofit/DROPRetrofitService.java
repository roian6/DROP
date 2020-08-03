package com.david0926.drop.Retrofit;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DROPRetrofitService {

    @Multipart
    @POST("v1/user")
    Call<ResponseBody> CreateUser(
//            @Body RegisterModel rm
            @Part("userid") RequestBody userid,
            @Part("password") RequestBody password,
            @Part("name") RequestBody name,
            @Part("fcmtoken") RequestBody fcmtoken,
            @Part MultipartBody.Part photo
    );

    @Multipart
    @PATCH("v1/user")
    Call<ResponseBody> UpdateUser(
            @Header("x-access-token") String token,
            @Part("keyword") List<RequestBody> keyword
    );

    @POST("v1/auth")
    Call<ResponseBody> Login(
            @Body LoginModel lm
    );

    @Multipart
    @POST("v1/group")
    Call<ResponseBody> CreateGroup(
            @Header("x-access-token") String token,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part photo
    );

    @Multipart
    @POST("v1/post")
    Call<ResponseBody> CreatePost(
            @Header("x-access-token") String token,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("type") RequestBody type,
            @Part("time") RequestBody time,
            @Part("place") RequestBody place,
            @Part("reward") RequestBody reward,
            @Part("group") RequestBody groupid,
            @Part MultipartBody.Part photo
    );

    @Multipart
    @PATCH("v1/post/{postid}")
    Call<ResponseBody> UpdatePost(
            @Header("x-access-token") String token,
            @Path("postid") String postid,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("type") RequestBody type,
            @Part("time") RequestBody time,
            @Part("place") RequestBody place,
            @Part("reward") RequestBody reward,
            @Part("group") RequestBody groupid,
            @Part MultipartBody.Part photo
    );

    @DELETE("v1/post/{postid}")
    Call<ResponseBody> DeletePost(
            @Header("x-access-token") String token,
            @Path("postid") String postid
    );

    @Multipart
    @POST("v1/post/comment")
    Call<ResponseBody> addComment(
            @Header("x-access-token") String token,
            @Part("postid") RequestBody title,
            @Part("content") RequestBody description,
            @Part("isImportant") RequestBody type
    );

    @DELETE("v1/post/comment/{postid}/{commentindex}")
    Call<ResponseBody> DeleteComment(
            @Header("x-access-token") String token,
            @Path("postid") String postid,
            @Path("commentindex") String commentindex
    );

    @GET("v1/post")
    Call<ResponseBody> getPosts(
            @Header("x-access-token") String token,
            @Query("group") String groupid,
            @Query("length") int length
    );

    @GET("v1/post/notification")
    Call<ResponseBody> getNotification(
            @Header("x-access-token") String token
    );

    @GET("v1/post/{postid}")
    Call<ResponseBody> getPost(
            @Header("x-access-token") String token,
            @Path("postid") String postid
    );

    @POST("v1/post/{postid}/resolve")
    Call<ResponseBody> setPostSolved(
            @Header("x-access-token") String token,
            @Path("postid") String postid
    );

    @GET("v1/post")
    Call<ResponseBody> getPosts(
            @Header("x-access-token") String token,
            @Query("length") int length
    );

    @GET("v1/post")
    Call<ResponseBody> getPosts(
            @Header("x-access-token") String token,
            @Query("length") int length,
            @Query("search") String keyword
    );

    @GET("v1/group")
    Call<ResponseBody> getGroups(
            @Header("x-access-token") String token
    );

    @Multipart
    @POST("v1/group/join")
    Call<ResponseBody> JoinGroup(
            @Header("x-access-token") String token,
            @Part("group") RequestBody groupid
    );

    @GET("v1/group/user")
    Call<ResponseBody> MyGroups(
            @Header("x-access-token") String token
    );

    @GET("v1/user")
    Call<ResponseBody> MyInfo(
            @Header("x-access-token") String token
    );

    @POST("v1/auth")
    Call<ResponseBody> getToken(
            @Body LoginModel lm
    );


}
