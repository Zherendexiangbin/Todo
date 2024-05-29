package net.onest.time;

import net.onest.time.utils.StringUtil;

import static org.junit.Assert.*;
import static net.onest.time.utils.StringUtil.*;
import org.junit.Test;

public class StringUtilTest {
    @Test
    public void emailTest() {
        String email1 = "2808021998@qq.com";
        String email2 = "2808021998qq.com";

        assertTrue(isEmail(email1));
        assertFalse(isEmail(email2));
    }

    @Test
    public void phoneTest() {
        String phone1 = "19216814416";
        String phone2 = "1921681441";

        assertTrue(isPhone(phone1));
        assertFalse(isPhone(phone2));
    }
}