package vn.example.firebaseapp.login.instagram

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InstagramAccessTokenResponse(
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null,

    @SerializedName("user_id")
    @Expose
    val userId: Long? = null,
)
