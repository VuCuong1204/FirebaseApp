package vn.example.firebaseapp.login.instagram

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("username")
    @Expose
    val username: String? = null,
)
