package net.onest.time.api;

import android.content.Context;
import android.content.SharedPreferences;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.dto.UserDto;
import net.onest.time.api.vo.UserVo;
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

    // 修改用户个人信息
    private final static String MODIFY_USER_INFO = "/modifyUserInfo";

    // 获取用户信息
    private final static String GET_USER_INFO = "/getUserInfo";

    // 用户注销
    private final static String LOGOUT = "/logout";

    public static String getEmailCodeKey(String email) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + GET_EMAIL_CODE_KEY + "?email=" + email)
                .get()
                .buildAndSend(String.class);
    }

    public static String login(UserDto userDto) {
        String token = RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + LOGIN)
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

    /*
     * 注册
     * 参数 整个 UserDto对象所有参数
     */
    public static void register(UserDto userDto) {
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + REGISTER)
            .post(userDto)
            .buildAndSend();
    }

    // 修改密码/忘记密码
    public static void modifyPassword(UserDto userDto){
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + MODIFY_PASSWORD)
            .put(userDto)
            .buildAndSend(String.class);
    }

    // 上传头像
    public static String uploadAvatar(String avatar) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + UPLOAD_AVATAR)
                .postFile(avatar)
                .buildAndSend(String.class);
    }


    // 修改头像
    public static void modifyAvatar(String avatar){
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + MODIFY_AVATAR + "?avatar=" + avatar)
            .put(avatar)
            .buildAndSend(String.class);
    }

    // 修改用户名
    public static void modifyUserName(String userName){
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + MODIFY_USER_NAME + "?userName=" + userName)
            .put(userName)
            .buildAndSend(String.class);
    }

    // 修改个性签名
    public static void modifySignature(String signature){
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + MODIFY_SIGNATURE + "?signature=" + signature)
            .put(signature)
            .buildAndSend(String.class);
    }

    // 修改用户个人信息
    public static UserVo modifyUserInfo(UserDto userDto){
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + MODIFY_USER_INFO)
                .put(userDto)
                .buildAndSend(UserVo.class);
    }

    // 获取用户信息
    public static UserVo getUserInfo(){
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + GET_USER_INFO)
                .get()
                .buildAndSend(UserVo.class);
    }

    // 获取其他用户信息
    public static UserVo getUserInfo(Long userId){
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + GET_USER_INFO + "/" + userId)
                .get()
                .buildAndSend(UserVo.class);
    }

    // 用户注销
    public static void logout(){
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + LOGOUT)
            .get()
            .buildAndSend(String.class);
    }
}
