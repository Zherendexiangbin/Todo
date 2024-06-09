package net.onest.time

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.textfield.TextInputEditText
import net.onest.time.api.UserApi
import net.onest.time.api.dto.UserDto
import net.onest.time.api.utils.RequestUtil
import net.onest.time.api.vo.UserVo
import net.onest.time.constant.SharedPreferencesConstant
import net.onest.time.constant.UserInfoConstant
import net.onest.time.navigation.activity.NavigationActivity
import net.onest.time.navigation.activity.RegisterActivity
import net.onest.time.navigation.activity.ResetPasswordActivity
import net.onest.time.utils.StringUtil.isEmail
import net.onest.time.utils.StringUtil.isPhone

class MainActivity : AppCompatActivity() {
    private var logo: ImageView? = null
    private var loginUser: TextInputEditText? = null
    private var loginPassword: TextInputEditText? = null
    private var forgetPassword: Button? = null
    private var btnLogin: Button? = null
    private var btnRegister: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        findViews()

        Glide.with(this@MainActivity)
            .load(R.mipmap.logo2)
            .transition(DrawableTransitionOptions.withCrossFade(2000))
            .into(logo!!)

        autoLogin()

        loginUser?.setText("2808021998@qq.com")
        loginPassword?.setText("123456")
//        loginUser!!.setText("212296944@qq.com")
//        loginPassword!!.setText("123456")

        setListeners()
        setAnimator()
    }

    /**
     * 通过token自动登录
     */
    private fun autoLogin() {
        var userInfo: UserVo? = null
        try {
            userInfo = UserApi.getUserInfo()
        } catch (e: Exception) {
            // 自动登录失败
        }

        if (userInfo != null) {
            application
                .applicationContext
                .getSharedPreferences(SharedPreferencesConstant.USER_INFO, MODE_PRIVATE)
                .edit()
                .putString(UserInfoConstant.USER_INFO, RequestUtil.getGson().toJson(userInfo))
                .apply()

            loginSuccess()
        }
    }

    private fun setListeners() {

        //登录
        btnLogin!!.setOnClickListener { view: View? ->
            val account = loginUser!!.text.toString().trim()
            val password = loginPassword!!.text.toString().trim()
            if (account.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "不可留空，请重新输入！", Toast.LENGTH_SHORT).show()
            } else {
                val userDto = UserDto()
                userDto.email = account
                userDto.password = password
                var token: String? = null
                try {
                    token = UserApi.login(userDto)
                } catch (e: Exception) {
                    Log.e(TAG, e.message!!)
                }
                if (token.isNullOrEmpty()) {
                    Toast.makeText(this, "账号密码不正确", Toast.LENGTH_SHORT).show()
                } else {
                    loginSuccess()
                }
            }
        }

        //忘记密码
        forgetPassword!!.setOnClickListener { view: View? ->
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        //注册
        btnRegister!!.setOnClickListener { view: View? ->
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        // 前端校验手机号邮箱
        loginUser!!.onFocusChangeListener = OnFocusChangeListener { v: View?, hasFocus: Boolean ->
            if (!hasFocus) {
                val principal = loginUser!!.text.toString()
                if (principal.isEmpty() || (!isEmail(principal) && !isPhone(principal))) {
                    loginUser!!.error = "请输入正确的手机号或邮箱！"
                } else {
                    loginUser!!.error = null
                }
            }
        }

        // 前端校验密码为空
        loginPassword!!.onFocusChangeListener =
            OnFocusChangeListener { v: View?, hasFocus: Boolean ->
                if (!hasFocus) {
                    val credential = loginPassword!!.text.toString()
                    if (credential.isEmpty()) {
                        loginPassword!!.error = "请输入密码！"
                    } else {
                        loginPassword!!.error = null
                    }
                }
            }
    }

    private fun findViews() {
        logo = findViewById(R.id.login_logo)
        loginUser = findViewById(R.id.edt_login_user)
        loginPassword = findViewById(R.id.edt_login_password)
        forgetPassword = findViewById(R.id.btn_forgetPassword)
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)
    }

    //部件淡出动画效果
    @SuppressLint("ObjectAnimatorBinding")
    private fun setAnimator() {
        // 获取ImageView控件
        // 创建一个透明度动画使其从完全透明（alpha为0）变为完全不透明（alpha为1）
        val alphaAnimator2 = ObjectAnimator.ofFloat(btnLogin, "alpha", 0f, 1f)
        val alphaAnimator3 = ObjectAnimator.ofFloat(btnRegister, "alpha", 0f, 1f)
        // 设置动画时长
        alphaAnimator2.setDuration(2000)
        alphaAnimator3.setDuration(2500)
        // 启动动画
        alphaAnimator2.start()
        alphaAnimator3.start()
    }

    private fun loginSuccess() {
//        Toast.makeText(this, "登录成功，欢迎来到时光！", Toast.LENGTH_SHORT).show();
        val intent = Intent()
        intent.setClass(this@MainActivity, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}