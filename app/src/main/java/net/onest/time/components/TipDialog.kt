package net.onest.time.components

import android.app.Activity
import android.content.Context
import com.lxj.xpopup.XPopup
import net.onest.time.R
import net.onest.time.application.TimeApplication

class TipDialog(
    private val activity: Context,
    private val title: String,
    private val content: String
) {

    fun show() {
        XPopup.Builder(activity)
            .asConfirm(
                this.title,
                content.trimIndent(),
                "关闭",
                "确认",
                null,
                null,
                true,
                R.layout.my_confim_popup
            )
            .show()
    }
}