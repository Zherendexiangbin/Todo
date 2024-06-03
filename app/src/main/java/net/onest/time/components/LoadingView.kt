package net.onest.time.components

import android.content.Context
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import net.onest.time.R

class LoadingView(
    val context: Context
) {
    private var loadingView: LoadingPopupView

    init {
        loadingView = XPopup.Builder(context)
            .asLoading()
//            .asCustom(CustomLoadingPopupView())
                as LoadingPopupView
    }

    fun show() {
        loadingView.show()
    }

    fun dismiss() {
        loadingView.dismiss()
    }

    inner class CustomLoadingPopupView : LoadingPopupView(context, R.layout.component_loading) {
        init {

        }

    }
}
