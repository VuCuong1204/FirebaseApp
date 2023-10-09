package vn.example.firebaseapp.realtime

data class UserInfo(
    var id: String? = null,
    var content: String? = null
)

fun mockData(size: Int = 20): List<UserInfo> {
    val list = mutableListOf<UserInfo>()
    repeat(size) {
        list.add(UserInfo("$it", "Hello tôi là $it "))
    }

    return list
}
