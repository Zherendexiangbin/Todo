package net.onest.time.components

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import net.onest.time.adapter.account.AccountAdapter
import net.onest.time.api.utils.RequestUtil
import net.onest.time.api.vo.UserVo
import net.onest.time.constant.SharedPreferencesConstant
import net.onest.time.constant.UserInfoConstant
import net.onest.time.databinding.ActivityAccountBinding
import net.onest.time.utils.applicationContext

class AccountListDialogBuilder(
    var activity: Activity
) : Dialog(activity) {
    private lateinit var binding: ActivityAccountBinding
    private var userVoList = ArrayList<UserVo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding
            .inflate(LayoutInflater.from(activity), null, false)
        setContentView(binding.root)

        val users =
            applicationContext().getSharedPreferences(
                SharedPreferencesConstant.USER_INFO,
                AppCompatActivity.MODE_PRIVATE
            )
                .getString(UserInfoConstant.LOGGED_USERS, "")

        if (users!!.isNotBlank()) {
            userVoList = RequestUtil.getGson().fromJson(users, object : TypeToken<ArrayList<UserVo>>(){})
        }

        binding.accountList.apply {
            layoutManager = LinearLayoutManager(this@AccountListDialogBuilder.activity)
            adapter = AccountAdapter(
                this@AccountListDialogBuilder.activity,
                userVoList,
                this@AccountListDialogBuilder)
        }
    }

    override fun show() {
        val alertDialog = super.show()
        this.window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDimAmount(0.33f)

            val attrs = attributes
            attrs.width = 1000
            attrs.height = 1200
            attributes = attrs
        }
        return alertDialog
    }
}