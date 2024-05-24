package net.onest.time.utils

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

object AnimationUtil {
    fun alphaAnimation(
        fromAlpha: Float = .3f,
        toAlpha: Float = 1f,
        duration: Long = 500
    ): AlphaAnimation {
        val alphaAnimation = AlphaAnimation(.3f, 1f)
        alphaAnimation.setDuration(500)
        return alphaAnimation;
    }
}

fun View.withCustomAlphaAnimation(): View {
    this.animation = AnimationUtil.alphaAnimation()
    return this
}

fun View.withCustomTranslateAnimation(): View {
    val translateAnimation = TranslateAnimation(0f, 100f, 0f, 100f)
    translateAnimation.duration = 5000
    this.animation = translateAnimation
    return this
}