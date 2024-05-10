package net.onest.time.api;

import android.content.Context;
import android.content.SharedPreferences;
import net.onest.time.api.dto.RequestUtil;
import net.onest.time.api.dto.UserDto;
import net.onest.time.application.TimeApplication;
import net.onest.time.constant.SharedPreferencesConstant;

public class UserApi {
    private final static String PREFIX = "/user";

    // 获取验证码Key
    private final static String GET_EMAIL_CODE_KEY = "/getEmailCodeKey";

    // 注册
    private final static String REGISTER = "/register";

    // 登录
    private final static String LOGIN = "/login";

    // 修改密码/忘记密码
    private final static String MODIFY_PASSWORD = "/modifyPassword";

    // 上传头像
    private final static String UPLOAD_AVATAR = "/uploadAvatar";

    // 修改头像
    private final static String MODIFY_AVATAR = "/modifyAvatar";

    // 修改用户名
    private final static String MODIFY_USER_NAME = "/modifyUserName";

    // 修改个性签名
    private final static String MODIFY_SIGNATURE = "/modifySignature";

    // 获取用户信息
    private final static String GET_USER_INFO = "/getUserInfo";

    // 用户注销
    private final static String LOGOUT = "/logout";

    public static String getEmailCodeKey(String email) {
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + GET_EMAIL_CODE_KEY + "?email=" + email)
                .get()
                .buildAndSend(String.class);
    }

    public static String login(UserDto userDto) {
        String token = RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + LOGIN)
                .post(userDto)
                .buildAndSend(String.class);

        // 将token存入SharedPreferences
        SharedPreferences preferences = TimeApplication
                .getApplication()
                .getApplicationContext()
                .getSharedPreferences(SharedPreferencesConstant.USER_INFO, Context.MODE_PRIVATE);
        preferences.edit()
                .putString("token", token)
                .apply();

        return token;
    }

    public static String uploadAvatar(String avatar) {
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + UPLOAD_AVATAR)
                .postFile(avatar)
                .buildAndSend(String.class);
    }
}
