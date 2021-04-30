package cn.ltt.projectcollection.kotlin.coroutinesLab.common

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * ============================================================
 * @author   李桐桐
 * date    2021年04月30日
 * desc    描述
 * ============================================================
 **/
//网络请求API
interface GitHubApi {
    @GET("users/{login}")
    fun getUserCallback(@Path("login") login: String): Call<User>

    @GET("users/{login}")
    suspend fun getUserSuspend(@Path("login") login: String): User
}

//构建retrofit网络请求实例
val githubApi by lazy {
    val retrofit = retrofit2.Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(Interceptor {
                it.proceed(it.request()).apply {
                    println("request: $code")
                }
            }).build())
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    retrofit.create(GitHubApi::class.java)
}


//网络接口请求返回数据
data class User(val id: String, val name: String, val url: String)

//请求结果处理函数
fun showUser(user:User) {
    println(user)
}
//请求结果处理函数
fun showError(t: Throwable) {
    t.printStackTrace(System.out)
}