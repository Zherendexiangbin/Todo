package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.dto.TaskCategoryDto;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.TaskCategoryVo;
import net.onest.time.api.vo.TaskVo;

import java.util.List;

public class TaskCategoryApi {
    private final static String PREFIX = "/category";

    // 新增清单分类
    private final static String ADD_TASK_CATEGORY = "/";
    // 删除清单分类
    private final static String DELETE_TASK_CATEGORY = "/";
    // 修改清单分类
    private final static String UPDATE_TASK_CATEGORY = "/";
    // 查询所有清单集合
    private final static String GET_ALL = "/";
    // 查询一个清单的所有任务
    private final static String GET_ALL_TASKS = "/";


    public static TaskCategoryVo addTaskCategory(TaskCategoryDto TaskCategoryDto) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + ADD_TASK_CATEGORY)
                .post(TaskCategoryDto)
                .buildAndSend(TaskCategoryVo.class);
    }

    public static void deleteTaskCategory(Long categoryId) {
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + PREFIX + DELETE_TASK_CATEGORY + categoryId)
            .delete()
            .buildAndSend();
    }

    public static TaskCategoryVo updateTaskCategory(TaskCategoryDto TaskCategoryDto) {
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + UPDATE_TASK_CATEGORY)
                .put(TaskCategoryDto)
                .buildAndSend(TaskCategoryVo.class);
    }

    public static List<TaskCategoryVo> getAll(){
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + GET_ALL)
                .get()
                .buildAndSend(new TypeToken<List<TaskCategoryVo>>(){});
    }

    public static List<TaskVo> getAllTasks(Long categoryId){
        return RequestUtil.builder()
                .url(ServerConstant.HTTP_ADDRESS + PREFIX + GET_ALL_TASKS + categoryId)
                .get()
                .buildAndSend(new TypeToken<List<TaskVo>>(){});
    }

}
