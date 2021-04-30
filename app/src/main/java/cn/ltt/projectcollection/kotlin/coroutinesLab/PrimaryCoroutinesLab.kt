package cn.ltt.projectcollection.kotlin.coroutinesLab

import android.util.Log
import cn.ltt.projectcollection.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * ============================================================
 * @author   李桐桐
 * date    2021年04月15日
 * desc    协程初级使用示例
 * ============================================================
 **/

//
//fun main() {
//
//    val call = githubApi.getUserCallback("puppet16")
//    call.enqueue(object : Callback<User> {
//        override fun onFailure(call: Call<User>, t: Throwable) {
//            showError(t)
//        }
//        override fun onResponse(call: Call<User>, response: Response<User>) {
//            response.body()?.let(::showUser) ?: showError(NullPointerException())
//        }
//    })
//}
//
//
//suspend fun coroutine(){
//    val names = arrayOf("abreslav","udalov", "yole")
//    names.forEach { name ->
//        try {
//            val user = githubApi.getUserSuspend(name)
//            showUser(user)
//        } catch (e: Exception) {
//            showError(e)
//        }
//    }
//}
//
//suspend fun suspendingPrint() = withContext(Dispatchers.IO) {
//    println("Thread: ${Thread.currentThread().name}")
//}
//
//suspend fun coroutineLoop(){
//    val names = arrayOf("abreslav","udalov", "yole")
//    val users = names.map { name ->
//        githubApi.getUserSuspend(name)
//    }
//}
////
