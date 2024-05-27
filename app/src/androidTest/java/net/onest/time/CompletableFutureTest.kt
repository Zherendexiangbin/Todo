package net.onest.time

import net.onest.time.api.ServerConstant
import net.onest.time.api.UserApi
import net.onest.time.api.utils.RequestUtil
import net.onest.time.api.vo.TaskVo
import org.junit.Test

class CompletableFutureTest {
    @Test
    fun completableFutureTest() {
        println("before")
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + "/user" + "/getUserInfo")
            .get()
            .buildAndSendAndConsume<TaskVo>(::println)
//            .buildAndSendAndConsume<TaskVo> {
//                println(it)
//            }
        println("after")

        Thread.sleep(500)
    }

    @Test
    fun futureTest() {
        println("before")
        println(UserApi.getUserInfo())
        println("after")

        Thread.sleep(500)
    }
}
