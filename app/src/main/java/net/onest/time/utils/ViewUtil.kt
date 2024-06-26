package net.onest.time.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.core.content.FileProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import net.onest.time.api.utils.RequestUtil
import net.onest.time.api.vo.UserVo
import net.onest.time.constant.SharedPreferencesConstant
import net.onest.time.constant.UserInfoConstant
import java.io.File
import java.time.LocalDate
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * 将View转换成Bitmap
 */
fun View.createBitmap(window: Window, callBack: (Bitmap?, Boolean) -> Unit) {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888, true)

    convertLayoutToBitmap(window, this, bitmap) { copyResult -> //如果成功
        if (copyResult == PixelCopy.SUCCESS) {
            callBack(bitmap, true)
        } else {
            callBack(null, false)
        }
    }

}

private fun convertLayoutToBitmap(
    window: Window, view: View, dest: Bitmap,
    listener: PixelCopy.OnPixelCopyFinishedListener
) {
    //获取layout的位置
    val location = IntArray(2)
    view.getLocationInWindow(location)

    //请求转换
    PixelCopy.request(
        window,
        Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
        dest, listener, Handler(Looper.getMainLooper())
    )
}

fun Bitmap.saveBitmapGallery(): Uri? {
    val context = applicationContext()
    val contentValues = ContentValues()
    var insert: Uri? = null
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "${LocalDate.now()}")

    //返回出一个URI
    insert = context.contentResolver.insert(
        uri,
        contentValues
    ) ?: return null //为空的话 直接失败返回了

    //这个打开了输出流  直接保存图片就好了
    context.contentResolver.openOutputStream(insert).use {
        it ?: return null
        this.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return insert
}

fun Bitmap.saveBitmapCache(
    path: String,
    fileName: String = "${System.currentTimeMillis()}.png"
): Uri {
    var file = File(path)

    if (!file.exists() && !file.mkdirs()) {
        throw RuntimeException("创建目录失败")
    }

    file = File(file, fileName)

    file.outputStream().use {
        this.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }

    return FileProvider.getUriForFile(applicationContext(), "net.onest.time.fileprovider", file)
}

fun Bitmap.drawUserWatermark(): Bitmap {
    val canvas = Canvas(this)

    val paint = Paint()
    paint.color = Color.GRAY
    paint.textSize = 64f

    val json = applicationContext()
        .getSharedPreferences(SharedPreferencesConstant.USER_INFO, Context.MODE_PRIVATE)
        .getString(UserInfoConstant.USER_INFO, "")

    val userVo = RequestUtil.getGson().fromJson(json, UserVo::class.java)
    val username = "@${userVo.userName}"

    canvas.drawText(username, this.width - getPx(username) - 44f, this.height - 44f, paint)

    return this
}

private fun getPx(username: String): Int {
    var sum = 0
    for (c in username) {
        if (checkCountName(c.toString())) {
            sum += 64
        } else {
            sum += 32
        }
    }
    return sum
}

private fun checkCountName(countname: String): Boolean {
    val p: Pattern = Pattern.compile("[\u4e00-\u9fa5]")
    val m: Matcher = p.matcher(countname)
    if (m.find()) {
        return true
    }
    return false
}

fun View.withOnClickInfoDialog(
    title: String = "敬请期待！",
    message: String = "敬请期待！"
) {
    this.setOnClickListener {
        MaterialAlertDialogBuilder(context)
            .setCancelable(true)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("确定") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}