package cn.ltt.projectcollection.kotlin.coroutinesLab

import cn.ltt.projectcollection.utils.log
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine


/**
 * ============================================================
 * @author   李桐桐
 * date    2021年07月07日
 * desc    描述
 * ============================================================
 **/
//suspend fun main() {
//
//}
//fun main() {
//
//}

//fun main1(continuation: Continuation<Unit>): Any? {
//    return Unit;
//}
//
//fun main() {
//    runSuspend(::main1 as suspend () -> Unit)
//}
//
//
///**
// * Wrapper for `suspend fun main` and `@Test suspend fun testXXX` functions.
// */
//@SinceKotlin("1.3")
//internal fun runSuspend(block: suspend () -> Unit) {
//    val run = RunSuspend()
//    block.startCoroutine(run)
//    run.await()
//}
//
//private class RunSuspend : Continuation<Unit> {
//    override val context: CoroutineContext
//        get() = EmptyCoroutineContext
//
//    var result: Result<Unit>? = null
//
//    override fun resumeWith(result: Result<Unit>) = synchronized(this) {
//        this.result = result
//        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN") (this as Object).notifyAll()
//    }
//
//    fun await() = synchronized(this) {
//        while (true) {
//            when (val result = this.result) {
//                null -> @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN") (this as Object).wait()
//                else -> {
//                    result.getOrThrow() // throw up failure
//                    return
//                }
//            }
//        }
//    }
//}

//    fun main(args: Array<String>) = runBlocking<Unit> {
//        launch { // 默认继承 parent coroutine 的 CoroutineDispatcher，指定运行在 main 线程
//            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
//            delay(100)
//            println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
//        }
//        launch(Dispatchers.Unconfined) {
//            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
//            delay(100)
//            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
//        }
//    }


//fun main() = runBlocking<Unit> { // start main coroutine
//    GlobalScope.launch { // launch a new coroutine in background and continue
//        delay(1000L)
//        log("World!")
//    }
//    log("Hello,") // main coroutine continues here immediately
//    delay(2000L)      // delaying for 2 seconds to keep JVM alive
//}


//fun main() = runBlocking { // this: CoroutineScope
//    launch {
//        delay(200L)
//        log("Task from runBlocking")
//    }
//
//    coroutineScope { // 创建一个协程作用域
//        launch {
//            delay(500L)
//            log("Task from nested launch")
//        }
//
//        delay(100L)
//        log("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
//    }
//
//    log("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
//}

fun main() = runBlocking {
    repeat(100_000) { // 启动大量的协程
        launch {
            delay(5000L)
            print(".")
        }
    }
}