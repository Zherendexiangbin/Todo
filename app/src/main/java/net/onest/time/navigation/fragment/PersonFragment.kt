package net.onest.time.navigation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import net.onest.time.AccountSafeActivity
import net.onest.time.MainActivity
import net.onest.time.R
import net.onest.time.api.UserApi
import net.onest.time.components.AccountListDialogBuilder
import net.onest.time.constant.SharedPreferencesConstant
import net.onest.time.databinding.PersonFragmentBinding
import net.onest.time.navigation.activity.AboutAppActivity
import net.onest.time.navigation.activity.PersonEditActivity
import net.onest.time.navigation.activity.PrivacyPolicyActivity
import net.onest.time.utils.applicationContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class PersonFragment : Fragment() {
    private lateinit var view: View
    private var userAvatar: ImageView? = null
    private var userEdit: LinearLayout? = null
    private var userName: TextView? = null
    private var userId: TextView? = null
    private var userCreateAt: TextView? = null
    private var userTotalDay: TextView? = null
    private var userSignature: TextView? = null
    private var change: Button? = null
    private var exit: Button? = null
    private var privacy: TextView? = null
    private var general: TextView? = null
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

    @SuppressLint("UseSwitchCompatOrMaterialCode", "MissingInflatedId")
    private fun setListeners() {
        //点击进入用户信息编辑页面
        userEdit!!.setOnClickListener { view: View? ->
            val intent = Intent(context, PersonEditActivity::class.java)
            startActivityForResult(intent, INTENT_CODE)
        }

        //隐私政策
        privacy!!.setOnClickListener{ view: View? ->
            val intent = Intent(context, PrivacyPolicyActivity::class.java)
            startActivity(intent);
        }

        //切换账号:
        change!!.setOnClickListener {
            AccountListDialogBuilder(requireActivity())
                .show()
        }

        //退出登录:
        exit!!.setOnClickListener {
            removeToken()
            requireActivity().finish()
            val intent = Intent()
            intent.setClass(requireContext(), MainActivity::class.java)
            requireContext().startActivity(intent)
        }

        // 账号与安全
        binding.accountAndSecurity.setOnClickListener {
            val intent = Intent(requireContext(),AccountSafeActivity::class.java)
            requireContext().startActivity(intent)
        }

        //关于时光
        binding.aboutUs.setOnClickListener {
            val intent = Intent(requireContext(), AboutAppActivity::class.java)
            requireContext().startActivity(intent)
        }

//        binding.accessibility.withOnClickInfoDialog("辅助功能")
//        binding.general.withOnClickInfoDialog("通用")
//        binding.commonProblem.withOnClickInfoDialog("常见问题")
//        binding.feedback.withOnClickInfoDialog("意见反馈")
//        binding.aboutUs.withOnClickInfoDialog("关于时光")

        //通用
//        binding.general.setOnClickListener{
//            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val generalView = inflater.inflate(R.layout.activity_general_switch_dialog, null)
//            val remindSwitch = generalView.findViewById<Switch>(R.id.message_remind_switch)
//            val modeSwitch = generalView.findViewById<Switch>(R.id.mode_switch)
//            val screenSwitch = generalView.findViewById<Switch>(R.id.landscape_screen_switch)
//            remindSwitch?.setOnClickListener {
//                if (remindSwitch.isChecked){
//                    Toast.makeText(requireContext(), "已开启", Toast.LENGTH_SHORT).show()
//                }else{
//                    Toast.makeText(requireContext(), "已关闭", Toast.LENGTH_SHORT).show()
//                }
//            }
//
////            modeSwitch?.setOnClickListener{
////                if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
////                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
////                }else{
////                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
////                }
////                recreate(requireActivity())
////            }
//            MaterialAlertDialogBuilder(requireContext())
//                    .setTitle("通用")
//                    .setView(generalView)
//                    .show()
//        }

        //辅助功能
//        binding.accessibility.setOnClickListener{
//            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val accessibilityView = inflater.inflate(R.layout.activity_accessibilty_switch_page, null)
//            MaterialAlertDialogBuilder(requireContext())
//                    .setTitle("辅助功能")
//                    .setView(accessibilityView)
//                    .show()
//        }

        ///常见问题
        binding.commonProblem.setOnClickListener{
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val generalView = inflater.inflate(R.layout.activity_commonproblem_dialog, null)
            val editInfo = generalView.findViewById<TextView>(R.id.edit_info)
            editInfo?.setOnClickListener {
                Toast.makeText(requireContext(), "我的-个人信息编辑", Toast.LENGTH_SHORT).show()
            }
            val joinRoom = generalView.findViewById<TextView>(R.id.join_room)
            joinRoom?.setOnClickListener {
                Toast.makeText(requireContext(), "邀请码或查询申请加入自习室", Toast.LENGTH_SHORT).show()
            }
            MaterialAlertDialogBuilder(requireContext())
                    .setTitle("常见问题")
                    .setView(generalView)
                    .show()
        }

        //意见反馈
//        binding.feedback.setOnClickListener {
//            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val generalView = inflater.inflate(R.layout.activity_feedback_dialog, null)
//            val inputText = generalView.findViewById<TextInputEditText>(R.id.edt_feedback)
//            MaterialAlertDialogBuilder(requireContext())
//                    .setTitle("意见反馈")
//                    .setView(generalView)
//                    .setPositiveButton("确定") { dialog, which ->
//                        Toast.makeText(requireContext(), "反馈已提交", Toast.LENGTH_SHORT).show()
//                        dialog.dismiss()
//                    }
//                    .show()
//        }
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

        //个性签名
        userSignature!!.text = userVo.signature
    }

    private fun findViewById() {
        userAvatar = view.findViewById(R.id.user_avatar)
        userEdit = view.findViewById(R.id.user_edit)
        userName = view.findViewById(R.id.user_name)
        userId = view.findViewById(R.id.user_id)
        userCreateAt = view.findViewById(R.id.user_create_time)
        userTotalDay = view.findViewById(R.id.user_total_day)
        userSignature = view.findViewById(R.id.user_signature)
        change = view.findViewById(R.id.btn_change)
        exit = view.findViewById(R.id.btn_exit)
//        general = view.findViewById(R.id.general)
        privacy = view.findViewById(R.id.privacy_policy_summary);
    }

    companion object {
        private const val INTENT_CODE = 1
    }
}
