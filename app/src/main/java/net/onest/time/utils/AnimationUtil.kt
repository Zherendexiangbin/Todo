package net.onest.time.utils

import android.view.View
import android.view.animation.AlphaAnimation

object AnimationUtil {
    fun alphaAnimation(
        fromAlpha: Float = .3f,
        toAlpha: Float = 1f,
        duration: Long = 500
    ): AlphaAnimation {
        val alphaAnimation = AlphaAnimation(.3f, 1f)
        alphaAnimation.setDuration(5000)
        return alphaAnimation;
    }
}

fun View.withCustomAlphaAnimation(): View {
    this.animation = AnimationUtil.alphaAnimation()
    return this
}