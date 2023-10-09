package vn.example.firebaseapp.login

interface ILoginState {
    fun onSuccess(token: String){}
    fun onSuccessInfo(id: String?, username: String?) {}
}
