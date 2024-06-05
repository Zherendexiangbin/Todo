package net.onest.time.navigation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import net.onest.time.MainActivity
import net.onest.time.R
import net.onest.time.api.UserApi
import net.onest.time.constant.SharedPreferencesConstant
import net.onest.time.databinding.PersonFragmentBinding
import net.onest.time.navigation.activity.AccountActivity
import net.onest.time.navigation.activity.PersonEditActivity
import net.onest.time.utils.applicationContext
import net.onest.time.utils.withOnClickInfoDialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class PersonFragment : Fragment() {
    private lateinit var view: View
    private var userAvatar: ImageView? = null
    private var userEdit: LinearLayout? = null
    private var userName: TextView? = null
    private var userId: TextView? = null
    private var userCreateAt: TextView? = null
    private var userTotalDay: TextView? = null
    private var userTodayComplete: TextView? = null
    private var userTotalComplete: TextView? = null
    private var change: Button? = null
    private var exit: Button? = null

    private lateinit var binding: PersonFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PersonFragmentBinding.inflate(inflater, container, false)
        view = binding.root

        findViewById()
        initData()
        setListeners()
        return view
    }

    private fun setListeners() {
        //点击进入用户信息编辑页面
        userEdit!!.setOnClickListener { view: View? ->
            val intent = Intent(context, PersonEditActivity::class.java)
            startActivityForResult(intent, INTENT_CODE)
        }

        //切换账号:
        change!!.setOnClickListener {
            val intent = Intent(requireContext(), AccountActivity::class.java)
            requireContext().startActivity(intent)
        }
        //退出登录:
        exit!!.setOnClickListener {
            removeToken()
            requireActivity().finish()
            val intent = Intent()
            intent.setClass(requireContext(), MainActivity::class.java)
            requireContext().startActivity(intent)
        }

        // 设置弹窗
        binding.accountAndSecurity.withOnClickInfoDialog()
        binding.bindPhone.withOnClickInfoDialog()

        binding.privacy.withOnClickInfoDialog()
        binding.general.withOnClickInfoDialog()
        binding.accessibility.withOnClickInfoDialog()

        binding.privacyPolicySummary.withOnClickInfoDialog()
        binding.commonProblem.withOnClickInfoDialog()
        binding.feedback.withOnClickInfoDialog()
        binding.aboutUs.withOnClickInfoDialog()
    }

    /**
     * 删除token
     */
    private fun removeToken() {
        val preferences = applicationContext()
            .getSharedPreferences(SharedPreferencesConstant.USER_INFO, Context.MODE_PRIVATE)
        preferences.edit()
            .remove("token")
            .apply()
    }

    //处理页面跳转返回结果
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initData()
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        //获取用户信息
        val userVo = UserApi.getUserInfo()

        //用户头像
        Glide.with(requireContext())
            .load(userVo.avatar)
            .into(userAvatar!!)
        //用户昵称
        userName!!.text = userVo.userName
        //用户Id
        userId!!.text = "UID：" + userVo.userId

        //用户创建时间及应用使用时间
        @SuppressLint("SimpleDateFormat") val format =
            SimpleDateFormat("yyyy年MM月dd日").format(userVo.createdAt)
        userCreateAt!!.text = format

        val localDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy年M月d日")
        val specifiedDate =
            LocalDate.parse(userCreateAt!!.text.toString().trim { it <= ' ' }, formatter)
        userTotalDay!!.text =
            ChronoUnit.DAYS.between(specifiedDate, localDate).toString()

        //用户今日完成日程数及累计完整日程数
        userTodayComplete!!.text = "今日完成日程：" + 3
        userTotalComplete!!.text = "累计完成日程：" + 1144
    }

    private fun findViewById() {
        userAvatar = view.findViewById(R.id.user_avatar)
        userEdit = view.findViewById(R.id.user_edit)
        userName = view.findViewById(R.id.user_name)
        userId = view.findViewById(R.id.user_id)
        userCreateAt = view.findViewById(R.id.user_create_time)
        userTotalDay = view.findViewById(R.id.user_total_day)
        userTodayComplete = view.findViewById(R.id.user_today_complete)
        userTotalComplete = view.findViewById(R.id.user_total_complete)

        change = view.findViewById(R.id.btn_change)
        exit = view.findViewById(R.id.btn_exit)
    }

    companion object {
        private const val INTENT_CODE = 1
    }
}
