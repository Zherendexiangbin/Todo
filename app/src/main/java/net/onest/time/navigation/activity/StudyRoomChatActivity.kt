package net.onest.time.navigation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.FileUtils
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import net.onest.time.R
import net.onest.time.adapter.studyroom.ChatMsgAdapter
import net.onest.time.api.ChatApi
import net.onest.time.api.RoomApi
import net.onest.time.api.StatisticApi
import net.onest.time.api.UserApi
import net.onest.time.api.dto.MessageDto
import net.onest.time.api.utils.MessageListener
import net.onest.time.api.utils.RequestUtil
import net.onest.time.api.vo.MessageVo
import net.onest.time.api.vo.Page
import net.onest.time.api.vo.RoomVo
import net.onest.time.api.vo.UserVo
import net.onest.time.components.CheckInDialog
import net.onest.time.constant.SharedPreferencesConstant
import net.onest.time.constant.UserInfoConstant
import net.onest.time.databinding.ActivityStudyRoomChatBinding
import net.onest.time.entity.CheckIn
import net.onest.time.utils.createBitmap
import net.onest.time.utils.saveBitmapCache
import net.onest.time.utils.saveBitmapGallery
import net.onest.time.utils.showToast
import okhttp3.WebSocket
import java.io.File
import java.io.FileOutputStream

class StudyRoomChatActivity : AppCompatActivity() {
    private var userVo: UserVo? = null
    private lateinit var roomVo: RoomVo
    private var btnBack: Button? = null
    private var btnSend: Button? = null
    private var roomName: TextView? = null
    private var messageListener: MessageListener? = null
    private var historyMessagesList: Page<MessageVo>? = Page()
    private var pageNum = 1
    private var messagesList: MutableList<MessageVo> = ArrayList()
    private var chatMsgAdapter: ChatMsgAdapter? = null
    private var messagesView: RecyclerView? = null
    private var editMessage: EditText? = null
    private var smartRefreshLayout: SmartRefreshLayout? = null
    private var checkIn: Button? = null
    private var tools: LinearLayout? = null

    private lateinit var binding: ActivityStudyRoomChatBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("Range")
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        (it.data?.extras?.get("data") as Bitmap).run {
            val path = "${this@StudyRoomChatActivity.externalCacheDir}/room/images"
            val fileName = "${System.currentTimeMillis()}.png"
            File(path).apply {
                if (!exists() && !mkdirs()) {
                    "创建文件失败".showToast()
                }
            }

            this.saveBitmapCache(path, fileName)
            sendImage("$path/$fileName")
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it.data?.data?.run {
            val path = "${this@StudyRoomChatActivity.externalCacheDir}/room/images"
            val filePath = copyFileFromURI(this, path)
            sendImage(filePath)
        }
    }

    private fun sendImage(file: String) {
        val url = UserApi.uploadAvatar(file)
        val messageDto = MessageDto()
        messageDto.toRoomId = roomVo.roomId
        messageDto.content = url
        messageDto.type = 1

        ChatApi.sendMessage(messageDto)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyRoomChatBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        findView()
        initData()
        setListeners()
        setKeyboardListener()

        // 设置自习室名字
        roomName!!.text = roomVo.roomName

        // 连接
        ChatApi.connectRoom(
            roomVo.roomId,
            object : MessageListener(messagesList) {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    runOnUiThread {
                        chatMsgAdapter!!.notifyItemInserted(messagesList.size)
                    }
                }
            }
        )

        val action = intent.action  // action
        val type = intent.type      // 类型

        if (Intent.ACTION_SEND == action && type != null) {
            val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
            //如果是媒体类型需要从数据库获取路径
            val path = "${this@StudyRoomChatActivity.externalCacheDir}/room/images"
            val filePath = copyFileFromURI(uri!!, path)
            sendImage(filePath)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun copyFileFromURI(uri: Uri, path: String): String {
        val contentResolver = this@StudyRoomChatActivity.contentResolver
        val fileName = "${System.currentTimeMillis()}.png"

        File(path).apply {
            if (!exists() && !mkdirs()) {
                "创建文件失败".showToast()
            }
        }

        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream("$path/$fileName")
        inputStream?.run {
            FileUtils.copy(inputStream, outputStream)
        }
        return "$path/$fileName"
    }

    private fun initData() {
        // 获取用户信息
        val userInfoJson = getSharedPreferences(SharedPreferencesConstant.USER_INFO, MODE_PRIVATE)
            .getString(UserInfoConstant.USER_INFO, "")
        userVo = RequestUtil.getGson().fromJson(userInfoJson, UserVo::class.java)

        // 获取自习室信息
        try {
            roomVo = (intent.getSerializableExtra("room") as RoomVo?) ?: RoomApi.getRoomInfo()
        } catch (e: RuntimeException) {
            "还未加入自习室哦".showToast()
        }

        try {
            historyMessagesList =
                ChatApi.findRoomMessagePage(1, 10, roomVo.roomId, System.currentTimeMillis())
            messagesList = if (historyMessagesList != null) {
                historyMessagesList!!.records
            } else {
                ArrayList()
            }
        } catch (e: Exception) {
            e.message!!.showToast()
        }
        messageListener = MessageListener(messagesList)
        // 绑定适配器
        val layoutManager = LinearLayoutManager(this)
        messagesView!!.layoutManager = layoutManager
        chatMsgAdapter = ChatMsgAdapter(this, messagesList, userVo!!.userId)
        messagesView!!.adapter = chatMsgAdapter

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setListeners() {
        editMessage!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    binding.btnAdd.visibility = View.VISIBLE
                    binding.btnSend.visibility = View.GONE
                } else {
                    binding.btnAdd.visibility = View.GONE
                    binding.btnSend.visibility = View.VISIBLE
                }
            }
        })

        // 打卡
        binding.checkIn.setOnClickListener {
            val statistic = StatisticApi.statistic()
            val checkInData = CheckIn(
                userVo!!.userName,
                statistic.tomatoTimes,
                statistic.tomatoDuration.toDouble(),
                statistic.ratioByDurationOfDay
            )
            val checkInDialog = CheckInDialog(this, checkInData)
            checkInDialog.setSendImageConsumer {
                val path = "${this@StudyRoomChatActivity.externalCacheDir}/room/images"
                val fileName = "${System.currentTimeMillis()}.png"
                it.saveBitmapCache(path, fileName)
                    .run {
                        sendImage("$path/$fileName")
                    }
            }
        }

        // 拍照发送
        binding.camera.setOnClickListener {
            // 打开相册发送图片
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            cameraLauncher.launch(intent)
        }

        // 发送图片
        binding.image.setOnClickListener {
            // 打开相册发送图片
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")

            imagePickerLauncher.launch(intent)
        }

        // 添加按钮 显示工具
        binding.btnAdd.setOnClickListener {
            if (binding.tools.visibility == View.VISIBLE) {
                binding.tools.visibility = View.GONE
            } else {
                binding.tools.visibility = View.VISIBLE
            }
        }

        // 返回自习室页面
        btnBack!!.setOnClickListener { view: View? ->
            finish()
        }

        // 发送文本消息
        btnSend!!.setOnClickListener { view: View? ->
            if (editMessage!!.text.toString().isEmpty()) {
                "发送消息不可为空".showToast()
            } else {
                val messageDto = MessageDto()
                messageDto.toRoomId = roomVo.roomId
                messageDto.content = editMessage!!.text.toString()
                messageDto.type = 0

                ChatApi.sendMessage(messageDto)

                editMessage!!.setText("")
            }
        }

        smartRefreshLayout!!.setOnRefreshListener { refreshLayout: RefreshLayout ->
            pageNum += 1
            getMessages(Position.FIRST, false)
            refreshLayout.finishRefresh(500, true, false)
        }
    }

    private fun findView() {
        btnBack = findViewById(R.id.btn_back)
        roomName = findViewById(R.id.room_name)

        smartRefreshLayout = findViewById(R.id.smartRefresher)
        messagesView = findViewById(R.id.chat_message)
        editMessage = findViewById(R.id.chat_edit)
        btnSend = findViewById(R.id.btn_send)

        checkIn = findViewById(R.id.check_in)
        tools = findViewById(R.id.tools)
        tools!!.visibility = View.GONE
    }

    // 下拉刷新获取消息
    private fun getMessages(position: Position, scrollToEnd: Boolean) {
        try {
            val newMessages = ChatApi.findRoomMessagePage(
                pageNum,
                10,
                roomVo.roomId,
                System.currentTimeMillis()
            )
            if (newMessages != null) {
                val messages = newMessages.records
                for (i in messages.indices.reversed()) {
                    addAndRefreshMsgList(messages[i], position, scrollToEnd)
                }
            } else {
                Toast.makeText(this, "没有其他消息了", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
        }
    }

    /**
     * @param messageVo   将要插入的消息
     * @param position    插入的位置
     * @param scrollToEnd 是否滚动到最后一条消息
     */
    private fun addAndRefreshMsgList(
        messageVo: MessageVo,
        position: Position,
        scrollToEnd: Boolean
    ) {
        runOnUiThread {
            if (position == Position.FIRST) {
                messagesList.add(0, messageVo)
                // 当有新消息时，刷新RecyclerView中的显示
                chatMsgAdapter!!.notifyItemInserted(0)
            } else if (position == Position.LAST) {
                messagesList.add(messageVo)
                // 当有新消息时，刷新RecyclerView中的显示
                chatMsgAdapter!!.notifyItemInserted(messagesList.size - 1)
            }
            if (scrollToEnd) {
                // 将RecyclerView定位到最后一行
                messagesView!!.scrollToPosition(messagesList.size - 1)
            }
        }
    }

    private enum class Position {
        FIRST,
        LAST
    }

    // 监听键盘是否弹出
    private fun setKeyboardListener() {
        val contentView = findViewById<View>(android.R.id.content)
        contentView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            contentView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = contentView.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            if (keypadHeight > screenHeight * 0.15) {
                // 键盘弹出
                messagesView!!.scrollToPosition(messagesList.size - 1)
            }
        }
    }
}
