package vn.example.firebaseapp.realtime

import androidx.recyclerview.widget.DiffUtil

class ChatDiffUtilCallBack(
    private val oldItem: List<UserInfo>,
    private val newItem: List<UserInfo>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItem.size

    override fun getNewListSize(): Int = newItem.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition].key == newItem[newItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition] == newItem[newItemPosition]
    }
}
