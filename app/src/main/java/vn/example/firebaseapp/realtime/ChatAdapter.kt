package vn.example.firebaseapp.realtime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vn.example.firebaseapp.R

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SENDER_TYPE = 0
        const val RECEIVER_TYPE = 1
    }

    var dataList = listOf<UserInfo>()

    override fun getItemViewType(position: Int): Int {
        val item = dataList[position]
        return if (item.id == AppConfig.userId) {
            SENDER_TYPE
        } else {
            RECEIVER_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENDER_TYPE) {
            val senderView = LayoutInflater.from(parent.context).inflate(R.layout.chat_sender_item, parent, false)
            ChatSendVH(senderView)
        } else {
            val receiverView = LayoutInflater.from(parent.context).inflate(R.layout.chat_receiver_item, parent, false)
            ChatReceiverVH(receiverView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        if (item.id == AppConfig.userId) {
            (holder as? ChatSendVH)?.onBind(item)
        } else {
            (holder as? ChatReceiverVH)?.onBind(item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class ChatSendVH(view: View) : RecyclerView.ViewHolder(view) {

        private var tvContent: TextView = view.findViewById(R.id.tvHomeChatSenderContent)

        fun onBind(data: UserInfo) {
            tvContent.text = data.content
        }
    }

    inner class ChatReceiverVH(view: View) : RecyclerView.ViewHolder(view) {

        private var tvTitle: TextView = view.findViewById(R.id.tvChatReceiverTitle)
        private var tvContent: TextView = view.findViewById(R.id.tvHomeChatReceiverContent)

        fun onBind(data: UserInfo) {
            tvTitle.text = data.id
            tvContent.text = data.content
        }
    }

    fun submitList(newList: List<UserInfo>) {
        val diffCallback = ChatDiffUtilCallBack(dataList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }
}
