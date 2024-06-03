package net.onest.time;

import net.onest.time.api.TaskCategoryApi;
import net.onest.time.api.dto.TaskCategoryDto;
import net.onest.time.api.vo.TaskCategoryVo;
import net.onest.time.api.vo.TaskVo;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TaskCategoryApiTest {
    @Test
    public void addTaskCategory() {
        TaskCategoryDto taskCategoryDto = new TaskCategoryDto();
        taskCategoryDto.setCategoryName("你好ya");
        taskCategoryDto.setColor(555);
        TaskCategoryVo taskCategoryVo = TaskCategoryApi.addTaskCategory(taskCategoryDto);
        System.out.println(taskCategoryVo);
    }

    @Test
    public void deleteTaskCategory() {
        Long categoryId = 8L;
        TaskCategoryApi.deleteTaskCategory(categoryId);
    }

    @Test
    public void updateTaskCategory() {
        TaskCategoryDto taskCategoryDto = new TaskCategoryDto();
        taskCategoryDto.setCategoryId(9L);
        taskCategoryDto.setCategoryName("拉楼");
        taskCategoryDto.setColor(988);
        TaskCategoryVo taskCategoryVo = TaskCategoryApi.updateTaskCategory(taskCategoryDto);
        System.out.println(taskCategoryVo);
    }

    @Test
    public void getAll() {
        List<TaskCategoryVo> taskCategoryVos = TaskCategoryApi.getAll();
        System.out.println(taskCategoryVos);
    }

    @Test
    public void getAllTasks() {
        Long categoryId = 9L;
        List<TaskVo> allTasks = TaskCategoryApi.getAllTasks(categoryId);
        System.out.println(allTasks);
    }

    @Test
    public void getAllCategoryAndTasks() {
        Map<TaskCategoryVo, List<TaskVo>> allCategoryAndTasks = TaskCategoryApi.getAllCategoryAndTasks();
        System.out.println(allCategoryAndTasks);
    }
}
