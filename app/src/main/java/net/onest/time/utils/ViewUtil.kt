package net.onest.time.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.time.LocalDate
import kotlin.math.log

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

fun Bitmap.saveBitmapCache(path: String): Uri {
    var file = File(path)

    if (!file.exists() && !file.mkdirs()) {
        throw RuntimeException("创建目录失败")
    }

    file = File(file, "${LocalDate.now()}.jpg")

    file.outputStream().use {
        this.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }

    return FileProvider.getUriForFile(applicationContext(), "net.onest.time.fileprovider", file)
}
