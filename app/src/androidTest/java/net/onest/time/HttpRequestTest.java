package net.onest.time;

import net.onest.time.api.RoomApi;
import net.onest.time.api.StatisticApi;
import net.onest.time.api.TaskApi;
import net.onest.time.api.UserApi;
import net.onest.time.api.dto.RoomDto;
import net.onest.time.api.dto.UserDto;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.Page;
import net.onest.time.api.vo.TaskVo;

import org.junit.Test;

public class HttpRequestTest {

    @Test
    public void parseParamsTest() {
        UserDto userDto = new UserDto();
        userDto.setEmail("2808021998@qq.com");
        userDto.setPassword("admin");
        String s = RequestUtil.parseParams(userDto);
        System.out.println(s);
    }

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

    @Test
    public void findTaskPageTest() {
        UserDto userDto = new UserDto();
        userDto.setEmail("2808021998@qq.com");
        userDto.setPassword("admin");
        String token = UserApi.login(userDto);

        Page<TaskVo> taskPage = TaskApi.findTaskPage(null, 1, 20);
        System.out.println(taskPage);
    }

    @Test
    public void complete() {
        UserDto userDto = new UserDto();
        userDto.setEmail("2808021998@qq.com");
        userDto.setPassword("admin");
        String token = UserApi.login(userDto);

        TaskApi.complete(3L);
    }

    @Test
    public void statistic() {
        System.out.println(StatisticApi.statistic());
    }

    @Test
    public void findRooms() {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomName("haha");
        System.out.println(RoomApi.findRooms(roomDto, 1, 20));
    }

    @Test
    public void listUsers() {
        System.out.println(RoomApi.listRooms());
    }

    @Test
    public void generateInvitationCode() {
        System.out.println(RoomApi.generateInvitationCode(111L));
    }
}
