package net.onest.time.adapter.account

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.reflect.TypeToken
import net.onest.time.MainActivity
import net.onest.time.api.utils.RequestUtil
import net.onest.time.api.vo.UserVo
import net.onest.time.constant.SharedPreferencesConstant
import net.onest.time.constant.UserInfoConstant
import net.onest.time.databinding.ItemAccountBinding
import net.onest.time.utils.showToast

class AccountAdapter (
    var context: Context,
    var userVoList: MutableList<UserVo>,
    var parentDialog: Dialog
) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {
    private lateinit var binding: ItemAccountBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar = binding.avatar
        val username = binding.username
        val signature = binding.signature
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userVo = userVoList[position]

        holder.username.text = userVo.userName
        holder.signature.text = userVo.signature
        Glide.with(context)
            .load(userVo.avatar)
            .into(holder.avatar)

        // 切换账号
        holder.itemView.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setCancelable(true)
                .setTitle("切换账号")
                .setMessage("是否切换账号到${userVo.userName}？")
                .setPositiveButton("确定") { dialog, which ->
                    dialog.dismiss()
                    parentDialog.dismiss()

                    val sharedPreferences =
                        context.getSharedPreferences(SharedPreferencesConstant.USER_INFO, MODE_PRIVATE)

                    // 获取token
                    val loggedTokens = sharedPreferences.getString(UserInfoConstant.LOGGED_TOKENS, "")!!.run {
                        RequestUtil.getGson().fromJson(this, object : TypeToken<Map<Long, String>>(){})
                    }

                    // 切换token
                    sharedPreferences
                        .edit()
                        .putString(UserInfoConstant.TOKEN, loggedTokens[userVo.userId])
                        .apply()

                    // 退回登录页面
                    (context as Activity).finish()
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                    // 提示
                    "账号已切换至${userVo.userName}".showToast()
                }
                .setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemAccountBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = userVoList.size

}
