package net.onest.time

import net.onest.time.components.CheckInDialog
import net.onest.time.entity.CheckIn
import net.onest.time.utils.applicationContext
import org.junit.Test
import java.io.File

class CheckInDialogTest {
    @Test
    fun checkInDialogTest() {
//        val checkIn = CheckIn("LingFenglong", 1, 79.2, null!!)
//        CheckInDialog(applicationContext(), checkIn)
//            .show()
    }

    @Test
    fun createFile() {
        val path = "${applicationContext().externalCacheDir?.path}/7788"
        path.run {
            val file = File(this)
            if (file.exists()) {
                println("${this}存在")
            } else {
                println("${this}不存在")
            }

            if (!file.mkdirs()) {
                println("文件创建失败")
            }
        }
    }
}