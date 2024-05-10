package net.onest.time;

import net.onest.time.api.UserApi;
import org.junit.Test;

public class HttpRequestTest {
    @Test
    public void getEmailCodeKeyTest() {
        String emailCodeKey = UserApi.getEmailCodeKey("2808021998@qq.com");
        System.out.println(emailCodeKey);
    }
}
