package net.onest.time;

import net.onest.time.api.UserApi;
import net.onest.time.api.dto.UserDto;

import org.junit.Test;

public class UserApiTest {

    @Test
    public void getEmailCodeKeyTest() {
        String emailCodeKey = UserApi.getEmailCodeKey("2808021998@qq.com");
        System.out.println(emailCodeKey);
    }


    @Test
    public void login() {
        UserDto userDto = new UserDto();
        userDto.setEmail("1241250055@qq.com");
        userDto.setPassword("123456");
        String token = UserApi.login(userDto);
        System.out.println(token);
    }
}
