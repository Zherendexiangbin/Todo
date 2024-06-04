package net.onest.time.navigation.activity

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

class AccountActivity : AppCompatActivity() {
    private val TAG = javaClass.name

    private lateinit var binding: ActivityAccountBinding
    private var userVoList = ArrayList<UserVo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        val users =
            application.getSharedPreferences(SharedPreferencesConstant.USER_INFO, MODE_PRIVATE)
                .getString(UserInfoConstant.LOGGED_USERS, "")

        if (users!!.isNotBlank()) {
             userVoList = RequestUtil.getGson().fromJson(users, object : TypeToken<ArrayList<UserVo>>(){})
        }

        binding.accountList.apply {
            layoutManager = LinearLayoutManager(this@AccountActivity)
            adapter = AccountAdapter(this@AccountActivity, userVoList)
        }
    }
}