package net.onest.time;

import net.onest.time.api.UserApi;
import net.onest.time.api.dto.UserDto;
import org.junit.Test;

public class HttpRequestTest {
    @Test
    public void getEmailCodeKeyTest() {
        String emailCodeKey = UserApi.getEmailCodeKey("2808021998@qq.com");
        System.out.println(emailCodeKey);
    }

    @Test
    public void postFile() {
        UserDto userDto = new UserDto();
        userDto.setEmail("2808021998@qq.com");
        userDto.setPassword("admin");
        String token = UserApi.login(userDto);

        String resp = UserApi.uploadAvatar("/storage/emulated/0/DCIM/Camera/IMG_20240510_075804.jpg");
        System.out.println(resp);
    }
}
