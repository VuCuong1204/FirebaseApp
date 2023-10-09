package vn.example.firebaseapp.login.instagram

import android.content.Context
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.example.firebaseapp.login.ILoginState

class InstagramLogin(val context: Context, val listener: ILoginState) {

    private val TAG = InstagramLogin::class.java.simpleName
    private val clientId = "1969202373451157"
    private val clientSecret = "44186f39a8b1b0db74c82d09a6c33b2b"
    private val redirectUri = "https://www.company.com/"
    val authorizationUrl = "https://api.instagram.com/oauth/authorize" +
            "?client_id=${clientId}" +
            "&redirect_uri=${redirectUri}" +
            "&scope=user_profile,user_media" +
            "&response_type=code"
    private val urlAuthorization = "https://api.instagram.com/"
    private val urlGraph = "https://graph.instagram.com/"

    private fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun exchangeAuthorizationCodeForToken(code: String) {

        val apiService = getRetrofit(urlAuthorization).create(InstagramApiService::class.java)

        val call = apiService.getAccessToken(
            clientId,
            clientSecret,
            "authorization_code",
            code,
            redirectUri
        )

        call.enqueue(object : Callback<InstagramAccessTokenResponse> {
            override fun onResponse(
                call: Call<InstagramAccessTokenResponse>,
                response: Response<InstagramAccessTokenResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.accessToken != null && response.body()?.userId != null) {
                        getUserInfo(response.body()?.accessToken!!, response.body()?.userId!!)
                    }
                } else {
                    Toast.makeText(context, "access_token_failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<InstagramAccessTokenResponse>, t: Throwable) {
                Toast.makeText(context, "access_token_error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onFailure access_token_error: ${t.message}")
            }
        })
    }

    private fun getUserInfo(accessToken: String, id: Long) {
        val apiService = getRetrofit(urlGraph).create(InstagramApiService::class.java)

        val call = apiService.getUserInfo(
            id,
            listOf("id", "username"),
            accessToken
        )

        call.enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                if (response.isSuccessful) {
                    listener.onSuccessInfo(response.body()?.id, response.body()?.username)
                } else {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Toast.makeText(context, "error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "error: ${t.message}")
            }
        })
    }
}
