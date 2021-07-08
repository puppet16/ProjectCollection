package cn.ltt.projectcollection.kotlin.coroutinesLab.primary

import android.util.Log
import cn.ltt.projectcollection.kotlin.coroutinesLab.primary.common.User
import cn.ltt.projectcollection.kotlin.coroutinesLab.primary.common.githubApi
import cn.ltt.projectcollection.kotlin.coroutinesLab.primary.common.showUser
import cn.ltt.projectcollection.kotlin.coroutinesLab.primary.dispatcher.DispatcherContext
import cn.ltt.projectcollection.kotlin.coroutinesLab.primary.dispatcher.HandlerDispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.*

/**
 * ============================================================
 * @author   李桐桐
 * date    2021年04月28日
 * desc    描述
 * ============================================================
 **/

val TAG = "tag"

suspend fun foo(){ }

suspend fun bar(a: Int): String {
    return "Hello"
}

//fun foo(continuation: Continuation<Unit>): Any{}
//
//fun bar(a: Int, continuation: Continuation<String>): Any{
//    return "Hello"
//}

suspend fun getUserSuspend(name: String) = suspendCoroutine<User> { continuation ->
    githubApi.getUserCallback(name).enqueue(object: Callback<User> {
        override fun onFailure(call: Call<User>, t: Throwable) =
                continuation.resumeWithException(t)
        override fun onResponse(call: Call<User>, response: Response<User>) =
                response.takeIf { it.isSuccessful }?.body()?.let(continuation::resume)
                        ?: continuation.resumeWithException(HttpException(response))
    })
}


suspend fun main(){

    val user = getUserSuspend("bennyhuo")
    showUser(user)

    suspend {

    }.createCoroutine(object: Continuation<Unit> {
        override val context = EmptyCoroutineContext

        override fun resumeWith(result: Result<Unit>) {
            Log.d(TAG,"Coroutine End with $result")
        }
    }).resume(Unit)

    suspend {

    }.startCoroutine(object: Continuation<Unit>{
        override val context = EmptyCoroutineContext

        override fun resumeWith(result: Result<Unit>) {
            Log.d(TAG,"Coroutine End with $result")
        }
    })

    suspend {

    }.startCoroutine(object: Continuation<Unit>{
        override val context = DispatcherContext(HandlerDispatcher)

        override fun resumeWith(result: Result<Unit>) {
            Log.d(TAG,"Coroutine End with $result")
        }
    })

    suspend {

    }.startCoroutine(object: Continuation<Unit>{
        override val context = DispatcherContext(HandlerDispatcher)

        override fun resumeWith(result: Result<Unit>) {
            Log.d(TAG,"Coroutine End with $result")
        }
    })
}

suspend fun suspendFunc() = suspendCoroutine<Int> {
    it.resumeWith(Result.success(1))
}