package vn.example.firebaseapp.realtime

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vn.example.firebaseapp.R

class ChatActivity : AppCompatActivity() {

    private lateinit var rvRoot: RecyclerView
    private lateinit var edtContent: EditText
    private lateinit var ivSend: ImageView

    private val chatAdapter by lazy {
        ChatAdapter()
    }

    private lateinit var myRef: DatabaseReference

    private val dataList = mutableListOf<UserInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)

        rvRoot = findViewById(R.id.rvChatRoot)
        edtContent = findViewById(R.id.edtChatContent)
        ivSend = findViewById(R.id.ivChatSend)

        myRef = FirebaseDatabase.getInstance().reference
        addHeader()
        addAdapter()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()

                // Đọc danh sách đối tượng từ dataSnapshot và thêm vào danh sách userList
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(UserInfo::class.java)
                    if (user != null) {
                        dataList.add(user)
                    }
                }

                chatAdapter.submitList(dataList)
                if (dataList.lastIndex > 0) {
                    rvRoot.smoothScrollToPosition(dataList.lastIndex)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Đọc dữ liệu thất bại
                println("Failed to read value: ${error.toException()}")
            }
        })
    }

    private fun addHeader() {
        ivSend.setOnClickListener {
            val content = edtContent.text.toString().trim()
            edtContent.setText("")
            val key = "${AppConfig.userId}${dataList.size + 1}"
            sendData(UserInfo(key, AppConfig.userId, content))
        }
    }

    private fun addAdapter() {
        chatAdapter.dataList = dataList
        rvRoot.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity, VERTICAL, false)
            adapter = chatAdapter
        }
    }

    private fun sendData(userInfo: UserInfo) {
        val userId = myRef.push().key
        if (userId != null) {
            // Đặt giá trị đối tượng người dùng vào Firebase Realtime Database
            myRef.child(userId).setValue(userInfo)
                .addOnSuccessListener {
                    // Ghi dữ liệu thành công
                    println("Data saved successfully!")
                }
                .addOnFailureListener {
                    // Ghi dữ liệu thất bại
                    println("Failed to save data: ${it.message}")
                }
        }
    }
}
