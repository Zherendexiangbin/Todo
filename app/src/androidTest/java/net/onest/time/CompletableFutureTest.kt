package net.onest.time

import net.onest.time.api.ServerConstant
import net.onest.time.api.UserApi
import net.onest.time.api.utils.RequestUtil
import net.onest.time.api.vo.TaskVo
import net.onest.time.api.vo.UserVo
import org.junit.Test

class CompletableFutureTest {
    @Test
    fun completableFutureTest() {
        println("before")
        RequestUtil.builder()
            .url(ServerConstant.HTTP_ADDRESS + "/user" + "/getUserInfo")
            .get()
            .submit(
                UserVo::class.java,
                {
                    println(it)
                }, {
                    System.err.println(it.code)
                    System.err.println(it.message)
                    System.err.println("哈哈哈 出错了")
                })
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
