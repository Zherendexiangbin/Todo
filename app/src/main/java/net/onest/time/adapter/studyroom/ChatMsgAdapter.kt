package net.onest.time.adapter.studyroom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.onest.time.api.vo.MessageVo
import net.onest.time.databinding.ItemChatMessageLeftImageBinding
import net.onest.time.databinding.ItemChatMessageLeftTextBinding
import net.onest.time.databinding.ItemChatMessageRightImageBinding
import net.onest.time.databinding.ItemChatMessageRightTextBinding

class ChatMsgAdapter(
    private val context: Context,
    private val msgList: List<MessageVo>,
    private val userId: Long
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var leftTextBinding: ItemChatMessageLeftTextBinding
    private lateinit var leftImageBinding: ItemChatMessageLeftImageBinding
    private lateinit var rightTextBinding: ItemChatMessageRightTextBinding
    private lateinit var rightImageBinding: ItemChatMessageRightImageBinding

    // ViewHolder
    inner class LeftTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username = leftTextBinding.leftUserName
        val message = leftTextBinding.leftText
        val avatar = leftTextBinding.imgLeftHeader
    }

    inner class LeftImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username = leftImageBinding.leftUserName
        val image = leftImageBinding.leftImage
        val avatar = leftImageBinding.imgLeftHeader
    }

    inner class RightTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username = rightTextBinding.rightUserName
        val message = rightTextBinding.rightText
        val avatar = rightTextBinding.imgRightHeader
    }

    inner class RightImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username = rightImageBinding.rightUserName
        val image = rightImageBinding.rightImage
        val avatar = rightImageBinding.imgRightHeader
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LEFT_TEXT -> {
                leftTextBinding = ItemChatMessageLeftTextBinding.inflate(LayoutInflater.from(context), parent, false)
                return LeftTextViewHolder(leftTextBinding.root)
            }
            LEFT_IMAGE -> {
                leftImageBinding = ItemChatMessageLeftImageBinding.inflate(LayoutInflater.from(context), parent, false)
                return LeftImageViewHolder(leftImageBinding.root)
            }
            RIGHT_TEXT -> {
                rightTextBinding = ItemChatMessageRightTextBinding.inflate(LayoutInflater.from(context), parent, false)
                return RightTextViewHolder(rightTextBinding.root)
            }
            RIGHT_IMAGE -> {
                rightImageBinding = ItemChatMessageRightImageBinding.inflate(LayoutInflater.from(context), parent, false)
                return RightImageViewHolder(rightImageBinding.root)
            }

            // 不会执行到的代码
            else -> LeftTextViewHolder(leftTextBinding.root)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgList[position]
        when (holder) {
            is LeftTextViewHolder -> {
                holder.username.text = msg.fromUserName
                Glide.with(context)
                    .load(msg.fromUserAvatar)
                    .into(holder.avatar)
                holder.message.text = msg.content
            }
            is LeftImageViewHolder -> {
                holder.username.text = msg.fromUserName
                Glide.with(context)
                    .load(msg.fromUserAvatar)
                    .into(holder.avatar)
                Glide.with(context)
                    .load(msg.content)
                    .into(holder.image)
            }
            is RightTextViewHolder -> {
                holder.username.text = msg.fromUserName
                Glide.with(context)
                    .load(msg.fromUserAvatar)
                    .into(holder.avatar)
                holder.message.text = msg.content
            }
            is RightImageViewHolder -> {
                holder.username.text = msg.fromUserName
                Glide.with(context)
                    .load(msg.fromUserAvatar)
                    .into(holder.avatar)
                Glide.with(context)
                    .load(msg.content)
                    .into(holder.image)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val messageVo = msgList[position]

        return if (messageVo.fromUserId == userId) {
            if (messageVo.type == 0) {
                RIGHT_TEXT
            } else {
                RIGHT_IMAGE
            }
        } else {
            if (messageVo.type == 0) {
                LEFT_TEXT
            } else {
                LEFT_IMAGE
            }
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    companion object {
        val LEFT_TEXT = 0
        val LEFT_IMAGE = 1
        val RIGHT_TEXT = 2
        val RIGHT_IMAGE = 3
    }
}
