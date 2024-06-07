package net.onest.time.utils

import android.app.Activity
import net.onest.time.api.utils.RequestUtil
import net.onest.time.api.vo.UserVo
import net.onest.time.constant.SharedPreferencesConstant
import net.onest.time.constant.UserInfoConstant

class SharedPreferencesUtil {
}

fun getUserInfoFromLocal() = applicationContext()
        .getSharedPreferences(SharedPreferencesConstant.USER_INFO, Activity.MODE_PRIVATE)
        .getString(UserInfoConstant.USER_INFO, "")
        .run {
            if (isNullOrEmpty()) {
                null
            } else {
                RequestUtil.getGson().fromJson(this, UserVo::class.java)
            }
        }