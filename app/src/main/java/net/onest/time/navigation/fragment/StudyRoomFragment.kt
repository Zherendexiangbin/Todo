package net.onest.time.navigation.fragment

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore
import android.util.Pair
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.wynsbin.vciv.VerificationCodeInputView
import com.wynsbin.vciv.VerificationCodeInputView.OnInputListener
import net.onest.time.R
import net.onest.time.adapter.ranking.RankingAdapter
import net.onest.time.adapter.studyroom.ApplicationItemAdapter
import net.onest.time.api.RoomApi
import net.onest.time.api.UserApi
import net.onest.time.api.dto.RoomDto
import net.onest.time.api.vo.RoomVo
import net.onest.time.api.vo.UserVo
import net.onest.time.navigation.activity.FindStudyRoomActivity
import net.onest.time.navigation.activity.StudyRoomChatActivity
import net.onest.time.utils.getUserInfoFromLocal
import net.onest.time.utils.showToast
import java.util.concurrent.TimeUnit

class StudyRoomFragment : Fragment() {
    private lateinit var notInRoom: TextView
    private var roomDto: RoomDto? = null
    private var roomVo: RoomVo? = null
    private lateinit var userVo: UserVo
    private lateinit var view: View
    private lateinit var roomAvatar: ImageView
    private lateinit var roomChat: ImageView
    private lateinit var userRefresh: ImageView
    private lateinit var roomName: TextView
    private lateinit var roomManager: TextView
    private var rankingAdapter: RankingAdapter? = null
    private var addMenu: RoomCodePopWindow? = null // 创建、加入自习室弹框
    private var menuPopWindow: MenuPopWindow? = null // 管理员查看自习室信息
    private var userVos: MutableList<UserVo> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private lateinit var btnMenu: Button
    private lateinit var btnAdd: Button
    private var isMaster: Boolean? = null // 是否为房间创建者

    // 调用相册
    private var avatarUri: Uri? = null
    private lateinit var popRoomAvatar: ImageView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.study_room_fragment, container, false)
        isMaster = false
        userVo = getUserInfoFromLocal()!!
        userVo.avatar = userVo.avatar

        // 根据加入自习室的方式判断是否为管理员
        findViewById(view)
        setListeners()

        try {
            roomVo = RoomApi.getRoomInfo()
            if (roomVo != null) {
                userVos.clear()
                userVos = RoomApi.listUsers(roomVo!!.roomId)

                Glide.with(requireContext())
                    .load(roomVo!!.roomAvatar)
                    .into(roomAvatar)
                roomName.text = roomVo!!.roomName
                btnMenu.visibility = View.VISIBLE
                roomManager.visibility = View.VISIBLE

                rankingAdapter!!.rankingList = userVos
                rankingAdapter!!.notifyDataSetChanged()

                if (roomVo!!.userId == userVo.getUserId()) {
                    this.isMaster = true
                    roomManager.text = "管理员"
                    btnMenu.visibility = View.VISIBLE
                    btnAdd.hint = "dissolution"
                    btnAdd.setBackgroundResource(R.mipmap.quit)
                } else {
                    isMaster = false
                    roomManager.text = "成员"
                    btnMenu.visibility = View.GONE
                    btnAdd.hint = "quit"
                    btnAdd.setBackgroundResource(R.mipmap.quit)
                }
            }
        } catch (e: Exception) {

        }
        renderNotInRoom()

        return view
    }

    private fun renderNotInRoom() {
        if (roomVo == null) {
            notInRoom.visibility = View.VISIBLE
        } else {
            notInRoom.visibility = View.INVISIBLE
        }
    }

    private fun setListeners() {
        // 跳转至聊天
        roomChat.setOnClickListener { view1: View? ->
            if (roomVo == null) {
                "还未加入自习室哦".showToast()
                return@setOnClickListener
            }

            val intent = Intent(activity, StudyRoomChatActivity::class.java)
            intent.putExtra("room", roomVo)
            startActivity(intent)
        }

        // 刷新自习室信息
        userRefresh.setOnClickListener { view1: View ->
            val animator: ViewPropertyAnimator = view1.animate()
                .rotation(360f)
            animator.setDuration(1000)
                .withEndAction(Runnable {
                    animator.rotation(0f)
                    animator.setDuration(0)
                })

            if (roomVo != null) {
                userVos = RoomApi.listUsers(roomVo!!.roomId)

                Glide.with(requireContext())
                    .load(roomVo!!.roomAvatar)
                    .into((roomAvatar))

                roomName.text = roomVo!!.roomName
                // roomManager.setText("管理员：" + userVo.getUserName());
            }
            rankingAdapter!!.rankingList = userVos
            rankingAdapter!!.notifyDataSetChanged()
        }

        btnAdd.setOnClickListener { view: View? ->
            if ((btnAdd.hint == "add")) {
                addMenu = RoomCodePopWindow(context)
                addMenu!!.showAtLocation(
                    requireActivity().window.decorView,
                    Gravity.TOP,
                    0,
                    0
                )
            } else if ((btnAdd.getHint() == "dissolution")) {
                // 解散自习室
                XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .asConfirm("", "确定解散自习室吗？") {
                        btnAdd.setBackgroundResource(R.mipmap.add3)
                        btnAdd.setHint("add")

                        RoomApi.deleteRoom(roomVo!!.getRoomId())
                        userVos.clear()

                        rankingAdapter!!.rankingList = userVos
                        rankingAdapter!!.notifyDataSetChanged()

                        roomAvatar.setImageResource(R.mipmap.logo)
                        roomName.text = "时光自习室"
                        isMaster = false
                        roomManager.visibility = View.GONE
                        btnMenu.visibility = View.GONE
                        Toast.makeText(context, "解散成功！", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            } else {
                // 退出自习室
                XPopup.Builder(getContext())
                    .dismissOnTouchOutside(false)
                    .asConfirm("", "确定退出自习室吗？") {
                        btnAdd.setBackgroundResource(R.mipmap.add3)
                        btnAdd.setHint("add")

                        RoomApi.userExit(roomVo!!.getRoomId())
                        userVos.clear()
                        rankingAdapter!!.rankingList = userVos
                        rankingAdapter!!.notifyDataSetChanged()

                        roomName.text = "时光自习室"
                        isMaster = false
                        roomManager.visibility = View.GONE
                        btnMenu.visibility = View.GONE
                        Toast.makeText(context, "退出成功！", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
        }

        // 管理员 菜单查看邀请码、房间加入申请
        btnMenu.setOnClickListener { view: View? ->
            menuPopWindow = MenuPopWindow(context)
            menuPopWindow!!.showAtLocation(
                requireActivity().window.decorView,
                Gravity.BOTTOM,
                0,
                0
            )
        }
    }

    private fun findViewById(view: View) {
        notInRoom = view.findViewById(R.id.txt_not_in_room)
        roomAvatar = view.findViewById(R.id.room_avatar)
        roomName = view.findViewById(R.id.room_name)
        roomManager = view.findViewById(R.id.room_manager)
        roomManager.visibility = View.GONE

        roomChat = view.findViewById(R.id.room_chat)
        userRefresh = view.findViewById(R.id.user_refresh)

        recyclerView = view.findViewById(R.id.user_list)
        val manager: RecyclerView.LayoutManager = LinearLayoutManager(
            context
        )
        recyclerView?.setLayoutManager(manager)
        rankingAdapter = RankingAdapter(requireContext(), (userVos)!!)
        recyclerView?.setAdapter(rankingAdapter)

        btnMenu = view.findViewById(R.id.btn_room_menu)
        btnMenu?.visibility = View.GONE

        btnAdd = view.findViewById(R.id.btn_add)
        btnAdd?.setHint("add")
    }


    // 房间号、邀请码输入
    private inner class RoomCodePopWindow(context: Context?) : PopupWindow() {
        private lateinit var createRoom: RadioButton
        private lateinit var joinRoom: RadioButton
        private lateinit var setName: EditText
        private lateinit var idInputView: VerificationCodeInputView
        private lateinit var llRoomAvatar: LinearLayout
        private lateinit var llRoomCode: LinearLayout
        private lateinit var setRoomAvatar: TextView
        private lateinit var findRoom: TextView
        private lateinit var btnCreate: Button

        init {
            // 设置view
            val inflater = LayoutInflater.from(context)
            val view1 = inflater.inflate((R.layout.activity_create_room_pop), null)
            contentView = view1
            initView(view1) // 获取控件
            // activity的contentView的宽度
            val width =
                (context as Activity?)!!.findViewById<View>(android.R.id.content).width
            // 其他设置
            setWidth(width) // 必须设置宽度
            height = dp2px(280f) // 必须设置高度
            isFocusable = true // 是否获取焦点
            isOutsideTouchable = true // 是否可以通过点击屏幕外关闭
        }

        override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
            super.showAtLocation(parent, gravity, x, y)
            // 加入动画
            ObjectAnimator.ofFloat(contentView, "translationY", -height.toFloat(), 0f)
                .setDuration(200).start()
        }

        /**
         * Value of dp to value of px.
         *
         * @param dpValue The value of dp.
         * @return value of px
         */
        fun dp2px(dpValue: Float): Int {
            val scale = Resources.getSystem().displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        private fun initView(view: View) {
            // 获取控件
            createRoom = view.findViewById(R.id.create_room)
            joinRoom = view.findViewById(R.id.join_room)
            setName = view.findViewById(R.id.edit_roomName)
            idInputView = view.findViewById(R.id.id_input)
            llRoomAvatar = view.findViewById(R.id.room_avatar_set)
            llRoomCode = view.findViewById(R.id.ll_room_code)
            popRoomAvatar = view.findViewById(R.id.pop_room_avatar)
            setRoomAvatar = view.findViewById(R.id.set_room_avatar)
            findRoom = view.findViewById(R.id.find_room)
            btnCreate = view.findViewById(R.id.btn_create)

            Glide.with(requireContext())
                .load(userVo.avatar)
                .into(popRoomAvatar)

            joinRoom.setChecked(false)
            llRoomCode.visibility = View.GONE
            setName.visibility = View.VISIBLE
            llRoomAvatar.visibility = View.VISIBLE
            findRoom.visibility = View.GONE
            createRoom.setBackgroundResource(R.drawable.button_wheat)
            joinRoom.setBackgroundResource(R.drawable.button_white)

            createRoom.setOnClickListener { view1: View? ->
                joinRoom.setChecked(false)
                llRoomCode.visibility = View.GONE
                findRoom.visibility = View.GONE
                setName.visibility = View.VISIBLE
                llRoomAvatar.visibility = View.VISIBLE
                createRoom.setBackgroundResource(R.drawable.button_wheat)
                joinRoom.setBackgroundResource(R.drawable.button_white)
            }

            joinRoom.setOnClickListener { view1: View? ->
                createRoom.setChecked(false)
                setName.visibility = View.GONE
                llRoomAvatar.visibility = View.GONE
                findRoom.setVisibility(View.VISIBLE)
                llRoomCode.setVisibility(View.VISIBLE)
                createRoom.setBackgroundResource(R.drawable.button_white)
                joinRoom.setBackgroundResource(R.drawable.button_wheat)
            }

            // 创建自习室
            btnCreate.setOnClickListener(View.OnClickListener { view1: View? ->
                XPopup.Builder(
                    context
                )
                    .dismissOnTouchOutside(false)
                    .asConfirm("创建自习室", setName.getText().toString(),
                        OnConfirmListener {
                            roomDto = RoomDto()
                            // 设置自习室名称
                            if ((setName.getText().toString() == "")) {
                                roomName!!.text = "时光自习室"
                                roomDto!!.roomName = "时光自习室"
                            } else {
                                roomName!!.text = setName.getText().toString()
                                roomDto!!.roomName = setName.getText().toString()
                            }
                            // 设置自习室管理员
                            roomManager!!.visibility = View.VISIBLE
                            roomManager!!.text = "管理员"

                            // 设置自习室头像
                            Glide.with((context)!!)
                                .load(userVo.avatar)
                                .into((roomAvatar)!!)
                            roomDto!!.roomAvatar = userVo.avatar

                            isMaster = true
                            btnMenu!!.visibility = View.VISIBLE
                            btnAdd!!.setBackgroundResource(R.mipmap.quit)
                            btnAdd!!.hint = "dissolution"

                            // 创建自习室，获取自习室信息
                            roomVo = RoomApi.createRoom(roomDto)

                            dismiss()
                            Toast.makeText(context, "创建成功！", Toast.LENGTH_SHORT).show()
                        })
                    .show()
            })

            // 设置自习室头像
            setRoomAvatar.setOnClickListener { view1: View? ->
                // 点击修改头像
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE)
            }

            // 查找自习室加入(页面跳转)
            findRoom.setOnClickListener { view1: View? ->
                val intent = Intent(context, FindStudyRoomActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE)
            }

            // 根据邀请码加入自习室
            idInputView.setOnInputListener(object : OnInputListener {
                override fun onComplete(code: String) {
                    XPopup.Builder(context)
                        .dismissOnTouchOutside(false)
                        .asConfirm(
                            "确认加入", "是否加入自习室？"
                        ) { // 向自习室管理员/创建者提交加入申请
                            RoomApi.acceptInvitation(code)

                            // 获取加入的自习室信息
                            roomVo = RoomApi.getRoomInfo()
                            renderNotInRoom()

                            Glide.with((context)!!)
                                .load(roomVo?.roomAvatar)
                                .into((roomAvatar))
                            roomName.text = roomVo?.roomName
                            roomManager.text = "成员"

                            isMaster = false
                            btnMenu.visibility = View.GONE
                            btnAdd.setBackgroundResource(R.mipmap.quit)
                            btnAdd.hint = "quit"
                            dismiss()
                            Toast.makeText(context, "加入成功！", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                }

                override fun onInput() {
                }
            })
        }
    }

    // 查看邀请码、加入房间申请
    private inner class MenuPopWindow(context: Context?) : PopupWindow() {
        private var applicationCode: TextView? = null
        private var refresh: ImageView? = null
        private var applicationList: RecyclerView? = null
        private var applicationItemAdapter: ApplicationItemAdapter? = null
        private var userVos2: List<UserVo>? = ArrayList()

        init {
            // 设置view
            val inflater = LayoutInflater.from(context)
            val view2 = inflater.inflate((R.layout.activity_room_menu), null)
            contentView = view2
            initView(view2) // 获取控件、初始化控件
            // activity的contentView的宽度
            val width =
                (context as Activity?)!!.findViewById<View>(android.R.id.content).width
            // 其他设置
            setWidth(width) // 必须设置宽度
            height = dp2px(300f) // 必须设置高度
            isFocusable = false // 是否获取焦点
            isOutsideTouchable = true // 是否可以通过点击屏幕外关闭
        }

        override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
            super.showAtLocation(parent, gravity, x, y)
            // 加入动画
            ObjectAnimator.ofFloat(contentView, "translationY", height.toFloat(), 0f)
                .setDuration(200).start()
        }

        /**
         * Value of dp to value of px.
         *
         * @param dpValue The value of dp.
         * @return value of px
         */
        fun dp2px(dpValue: Float): Int {
            val scale = Resources.getSystem().displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        private fun initView(view2: View) {
            applicationCode = view2.findViewById(R.id.application_code)
            refresh = view2.findViewById(R.id.refresh)
            applicationList = view2.findViewById(R.id.application_list)

            // 获取自习室邀请码
            applicationCode?.setText(RoomApi.generateInvitationCode(roomVo!!.roomId))

            // 刷新获取房间申请列表
            refresh?.setOnClickListener(View.OnClickListener { view1: View? ->
                userVos2 = RoomApi.findRequests(roomVo!!.roomId)
                if (userVos2 != null) {
                    applicationItemAdapter!!.updateData(userVos2)
                }
            })

            // 绑定数据
            val manager: RecyclerView.LayoutManager = LinearLayoutManager(
                context
            )
            if (userVos2 != null) {
                // 绑定适配器
                applicationItemAdapter = ApplicationItemAdapter(roomVo!!.roomId, context, userVos2)
            }
            applicationList?.setLayoutManager(manager)
            applicationList?.setAdapter(applicationItemAdapter)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 调用本地相册
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            try {
                popRoomAvatar!!.setImageURI(selectedImageUri)
                avatarUri = selectedImageUri
                userVo.avatar = UserApi.uploadAvatar(getPathFromUri(context, selectedImageUri))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (((requestCode == REQUEST_CODE) && resultCode == INTENT_CODE) && data != null) {
            // 处理页面跳转结果
            addMenu!!.dismiss()
            afterRequest()
        }
    }


    // 获取图片路径
    private fun getImagePath(avatarUri: Uri): Pair<String, String> {
        val cursor = requireContext().contentResolver
            .query(
                avatarUri,
                arrayOf(
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.MIME_TYPE
                ),
                null,
                null,
                null
            )!!
        cursor.moveToFirst()
        return Pair(cursor.getString(0), cursor.getString(1))
    }

    // 图片URI转String
    fun getPathFromUri(context: Context?, uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri!!, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(columnIndex)
            cursor.close()
            return path
        } else {
            return null
        }
    }

    fun afterRequest() {
        object : CountDownTimer((15 * 1000).toLong(), 1000) {
            override fun onTick(l: Long) {
                val seconds = TimeUnit.MILLISECONDS.toSeconds(l).toInt()
                // 查看管理员是否同意加入请求
                try {
                    roomVo = RoomApi.getRoomInfo()
                    renderNotInRoom()

                    if (roomVo != null) {
                        afterManager()
                        cancel()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "等待管理员同意：" + seconds + "s", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFinish() {
                try {
                    roomVo = RoomApi.getRoomInfo()
                    renderNotInRoom()

                    if (roomVo != null) {
                        afterManager()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "管理员未同意", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    // 管理员同意加入请求
    private fun afterManager() {
        userVos = RoomApi.listUsers(roomVo!!.roomId)

        Glide.with(requireContext())
            .load(roomVo!!.roomAvatar)
            .into(roomAvatar!!)
        roomName!!.text = roomVo!!.roomName
        roomManager!!.text = "成员"

        isMaster = false
        btnMenu!!.visibility = View.GONE
        btnAdd!!.setBackgroundResource(R.mipmap.quit)
        btnAdd!!.hint = "quit"

        Toast.makeText(context, "加入成功", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_CODE = 1
        private const val INTENT_CODE = 1

        private const val PICK_IMAGE = 1
    }
}
