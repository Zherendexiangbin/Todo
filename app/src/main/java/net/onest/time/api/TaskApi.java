package net.onest.time.api;

import com.google.gson.reflect.TypeToken;

import net.onest.time.api.dto.TaskDto;
import net.onest.time.api.utils.RequestUtil;
import net.onest.time.api.vo.Page;
import net.onest.time.api.vo.TaskVo;

import java.util.List;

public class TaskApi {
    private final static String PREFIX = "/task";

    // 添加一项任务
    private final static String ADD_TASK = "/";

    // 删除一项任务
    private final static String REMOVE_TASK = "/";

    // 修改一项任务
    private final static String UPDATE_TASK = "/";

    // 分页查询任务
    private final static String FIND_TASK_PAGE = "/";

    // 查询一个任务
    private final static String fIND_BY_ID = "/";

    // 查询用户的全部任务
    private final static String fIND_ALL = "/all";

    // 完成任务，同步番茄钟与任务数据
    private final static String COMPLETE = "/complete";

    // 查询一个类别的任务
    private final static String FIND_BY_CATEGORY = "/findByCategory";

    // 查询某一天的任务
    private final static String FIND_BY_DAY = "/findByDay";


    // 添加一项任务
    public static TaskVo addTask(TaskDto taskDto){
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + ADD_TASK)
                .post(taskDto)
                .buildAndSend(TaskVo.class);
    }

    // 删除一项任务
    public static void removeTask(Long taskId){
        RequestUtil.builder()
            .url(ServerConstant.ADDRESS + PREFIX + REMOVE_TASK)
            .delete()
            .buildAndSend();
    }

    // 修改一项任务
    public static TaskVo updateTask(TaskDto taskDto){
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + UPDATE_TASK)
                .delete()
                .buildAndSend(TaskVo.class);
    }

    // 分页查询任务
    public static Page<TaskVo> findTaskPage(TaskDto taskDto, int pageNum, int pageSize){
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + FIND_TASK_PAGE +
                        RequestUtil.parseParams(taskDto,
                                "pageNum", pageNum,
                                "pageSize", pageSize))
                .get()
                .buildAndSend(new TypeToken<Page<TaskVo>>(){});
    }

    public static TaskVo findById(Long taskId){
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + fIND_BY_ID + taskId)
                .get()
                .buildAndSend(TaskVo.class);
    }

    public static List<TaskVo> findAll(){
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + fIND_ALL)
                .get()
                .buildAndSend(new TypeToken<List<TaskVo>>(){});
    }

    public static void complete(Long taskId){
         RequestUtil.builder()
            .url(ServerConstant.ADDRESS + PREFIX + COMPLETE + "/" + taskId)
            .put()
            .buildAndSend();
    }

    public static List<TaskVo> findByCategory(String category){
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + FIND_BY_CATEGORY + "/" + category)
                .get()
                .buildAndSend(new TypeToken<List<TaskVo>>(){});
    }

    //时间戳以 毫秒 为单位
    public static List<TaskVo> findByDay(Long timestamp){
        return RequestUtil.builder()
                .url(ServerConstant.ADDRESS + PREFIX + FIND_BY_DAY + "?timestamp=" + timestamp)
                .get()
                .buildAndSend(new TypeToken<List<TaskVo>>(){});
    }
}
