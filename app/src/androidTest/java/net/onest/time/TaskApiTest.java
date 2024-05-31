package net.onest.time;

import net.onest.time.api.TaskApi;
import net.onest.time.api.dto.TaskDto;
import net.onest.time.api.vo.Page;
import net.onest.time.api.vo.TaskVo;

import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskApiTest {

    // 存放到 待办
    @Test
    public void addTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskName("TaskApi测试");
        ArrayList<Integer> estimate = new ArrayList<>();
        estimate.add(2);
        taskDto.setEstimate(estimate);
        taskDto.setClockDuration(30);
        TaskVo taskVo = TaskApi.addTask(taskDto);
        System.out.println(taskVo);
    }

    // 存放到 清单
    @Test
    public void addTaskList() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskName("新增清单分类测试");
        ArrayList<Integer> estimate = new ArrayList<>();
        estimate.add(2);
        taskDto.setType(0);
        taskDto.setEstimate(estimate);
        taskDto.setClockDuration(30);
        taskDto.setCategoryId(9L);
        TaskVo taskVo = TaskApi.addTask(taskDto);
        System.out.println(taskVo);
    }

    @Test
    public void removeTask() {
        Long taskId = 29L;
        TaskApi.removeTask(taskId);
    }

    @Test
    public void updateTask() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(122L);//必传参数
        taskDto.setClockDuration(0);

        ArrayList<Integer> estimate = new ArrayList<>();
        estimate.add(1);
        taskDto.setEstimate(estimate);
//        taskDto.setTaskName("无风不起浪");
        TaskApi.updateTask(taskDto);
    }

    @Test
    public void findTaskPage() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskStatus(0);
        int pageNum = 1;
        int pageSize = 20;
        Page<TaskVo> taskPage = TaskApi.findTaskPage(taskDto, pageNum, pageSize);
        System.out.println(taskPage);
    }

    @Test
    public void findById() {
        Long taskId = 28L;
        TaskVo taskVo = TaskApi.findById(taskId);
        System.out.println(taskVo);
    }

    @Test
    public void findAll() {
        List<TaskVo> taskVoList = TaskApi.findAll();
        System.out.println(taskVoList);
    }

    @Test
    public void complete() {
        Long taskId = 122L;
        TaskApi.complete(taskId);
    }

    @Test
    public void findByCategory() {
        String category = "TaskApi清单";
        List<TaskVo> taskVoList = TaskApi.findByCategory(category);
        System.out.println(taskVoList);
    }

    @Test
    public void findByDay() {
        Long timestamp = 1715588988000L;
        TaskApi.findByDay(timestamp, System.out::println);
        // Assert.assertNotNull(taskVoList);
        // taskVoList.forEach(System.out::println);
//        System.out.println(taskVoList);
    }

    @Test
    public void allByCategory() {
        Map<String, List<TaskVo>> result = TaskApi.allByCategory();
        result.forEach(
                (k, v) -> {
                    System.out.println("类别:" + k + "\n任务:");
                    v.forEach(System.out::println);
                });
    }

    @Test
    public void getTaskDay() {
        Long timestamp = 1714212296000L;
        Map<Long, List<TaskVo>> taskDay = TaskApi.getTaskDay(timestamp);
        System.out.println(taskDay);
    }
}
