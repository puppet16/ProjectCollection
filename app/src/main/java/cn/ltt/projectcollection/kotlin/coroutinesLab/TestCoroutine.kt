package cn.ltt.projectcollection.kotlin.coroutinesLab

import android.os.Handler
import android.util.Log
import cn.ltt.projectcollection.utils.LogUtils
import cn.ltt.projectcollection.utils.log
import kotlin.concurrent.thread
import kotlin.coroutines.*

/**
 * ============================================================
 * @author   李桐桐
 * date    2021年07月07日
 * desc    描述
 * ============================================================
 **/

suspend fun coroutine1() = suspendCoroutine<Unit>{
    thread {
        it.resume(Unit)
    }

}

 fun main() {
    suspend {
        coroutine1()
    }.startCoroutine(object: Continuation<Unit> {
        override val context = EmptyCoroutineContext

        override fun resumeWith(result: Result<Unit>) {
            log("Coroutine End with $result")
        }
    })
}

