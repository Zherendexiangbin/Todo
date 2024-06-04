package net.onest.time;

import net.onest.time.api.ServerConstant;
import net.onest.time.api.UserApi;
import net.onest.time.api.dto.UserDto;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.TaskVo;
import net.onest.time.api.vo.UserVo;
import org.junit.Test;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UserApiTest {

    @Test
    public void getEmailCodeKeyTest() {
        String emailCodeKey = UserApi.getEmailCodeKey("luohua_666@163.com");
        System.out.println(emailCodeKey);
    }

    @Test
    public void register() {
        UserDto userDto = new UserDto();
        userDto.setUserName("风华学院");
        userDto.setEmail("luohua_666@163.com");
        userDto.setPassword("123456");
        userDto.setConfirmPassword("123456");
        userDto.setEmailCodeKey("29acae14f94949c9a93705d559c5ba96");
        userDto.setEmailCode("f6f4cc");
        UserApi.register(userDto);
    }

    @Test
    public void login() {
        UserDto userDto = new UserDto();
        userDto.setEmail("212296944@qq.com");
        userDto.setPassword("123456");
        String token = UserApi.login(userDto);
        System.out.println(token);
    }

    @Test
    public void modifyPassword() {
        UserDto userDto = new UserDto();
        userDto.setEmail("luohua_666@163.com");
        userDto.setPassword("123456");
        userDto.setConfirmPassword("123456");
        userDto.setEmailCodeKey("bfb004307c0746288ad228bf7b36b96f");
        userDto.setEmailCode("ebd7c3");
        UserApi.modifyPassword(userDto);
    }

    @Test
    public void uploadAvatar() {
        String avatarPath = "/sdcard/DCIM/cosplay美女 电脑桌 键盘 机房 可爱 小姐姐4k壁纸3840x2160_彼岸图网.jpg";
        System.out.println(UserApi.uploadAvatar(avatarPath));
    }

    @Test
    public void modifyAvatar() {
        String avatar = "http://8.130.17.7:9000/todo-bucket/user/438571a4de144525991955f89ef039fc.jpg";
        UserApi.modifyAvatar(avatar);
    }

    @Test
    public void modifyUserName() {
        String userName = "土狗您吉祥";
        UserApi.modifyUserName(userName);
    }

    @Test
    public void modifySignature() {
        String signature = "落子无悔";
        UserApi.modifySignature(signature);
    }

    @Test
    public void modifyUserInfo() {
        UserDto userDto = new UserDto();
//        userDto.setUserName("风花雪月");
        userDto.setAvatar("http://8.130.17.7:9000/todo-bucket/user/809dc3467e044925851838f6276b4553.jpeg");
        userDto.setSignature("人生几何时");
        UserVo userVo = UserApi.modifyUserInfo(userDto);
        System.out.println(userVo);
    }

    @Test
    public void getUserInfo() {
        UserVo userInfo = UserApi.getUserInfo();
        System.out.println(userInfo);
    }

    @Test
    public void getUserInfoById() {
        Long userId = 25L;
        UserVo userInfo = UserApi.getUserInfo(userId);
        System.out.println(userInfo);
    }

    @Test
    public void logout() {
        UserApi.logout();
    }

    @Test
    public void dateConver() {
        System.out.println(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("+8")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("+8")).getMonth());
    }

    @Test
    public void completableFutureTest() {
        System.out.println("before");
        RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + "/user" + "/getUserInfo")
                .get()
//            .buildAndSendAndConsume<TaskVo>(::println)
                .submit(TaskVo.class , (taskVo) -> System.out.println(taskVo));
        System.out.println("after");

        try { Thread.sleep(500); } catch (InterruptedException e) { throw new RuntimeException(e); }
    }
}
