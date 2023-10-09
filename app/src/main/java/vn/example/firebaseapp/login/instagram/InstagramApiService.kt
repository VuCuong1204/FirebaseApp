package vn.example.firebaseapp.login.instagram

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InstagramApiService {
    @FormUrlEncoded
    @POST("oauth/access_token")
    fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): Call<InstagramAccessTokenResponse>

    @GET("{user-id}")
    fun getUserInfo(
        @Path("user-id") id: Long,
        @Query("fields") fields: List<String>,
        @Query("access_token") accessToken: String,
    ): Call<UserInfoResponse>
}
