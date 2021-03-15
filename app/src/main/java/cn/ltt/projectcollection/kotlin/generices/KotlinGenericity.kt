package cn.ltt.projectcollection.kotlin

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import cn.ltt.projectcollection.utils.ApplicationUtils
import com.google.gson.Gson
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KProperty


/**
 * ============================================================
 * @author   李桐桐
 * date    2021年03月08日
 * desc    描述
 * ============================================================
 **/


//region + 简单泛型
//fun main() {
//    val max = maxOf("Hello", "World")
//    println(max)
//    val list = List.Cons<Double>(1.0, List.Nil)
//}
//
//sealed class List<out T> {
//    object  Nil: List<Nothing>() {
//        //...
//    }
//    data class Cons<E> (val head: E, val tail:List<E>): List<E>() {
//        //...
//    }
//}

//endregion

//region + 添加多个约束
//
//fun main() {
//
//}
//
//fun <T> callMax1(a: T, b: T) where T : Comparable<T>, T : () -> Unit {
//    if (a > b) a() else b()
//}
//
//fun <T,R> callMax(a:T, b:T) where T: Comparable<T>, T:()->R {
//    if (a > b) a() else b()
//}
//endregion

//region + 型变--不变


//sealed class List<out T> {
//    object  Nil: List<Nothing>()
//    data class Cons<T>(val head: T, val tail:List<T>): List<T>() {
//    }
//}
//fun main() {
//    val list = List.Cons(1.0, List.Nil)
//}

//endregion

//region + 协变
//
//interface Book
//
//interface  EduBook:Book
//
//class BookStore<out T: Book> {
//    fun getBook(): T {
//        TODO()
//    }
//}
//
//fun main() {
//    val eduBookStore:BookStore<EduBook> = BookStore<EduBook>()
//    val bookStore: BookStore<Book> = eduBookStore
//
//    val book : Book = bookStore.getBook()
//    val eduBook: EduBook = eduBookStore.getBook()
//}

//endregion

//region + 逆变
//
//open class Car
//
//class Audi : Car()
//
//class Factory<in T: Car> {
//    fun put(t: T) {
//
//    }
//}
//
//fun main() {
//    val factory:Factory<Car> = Factory<Car>()
//    val audiFactory:Factory<Audi> = factory
//
//    val car = Car()
//    val audi = Audi()
//
//    factory.put(car)
//    factory.put(audi)
//
////    audiFactory.put(car)
//    audiFactory.put(audi)
//}

//endregion

//region + 星投影

//fun main() {
//    val queryMap: QueryMap<*, *> = QueryMap<String, Int>()
//    queryMap.getKey()
//    queryMap.getValue()
//
//    val f: Function<*,*> = Function<Number, Any>()
//    if(f is Function<*, *>){}
//    val hashMap = HashMap<String, List<*>>()
//}
//
//class QueryMap<out K: CharSequence, out V : Any> {
//    fun getKey(): K = TODO()
//    fun getValue(): V = TODO()
//}
//
//class Function<in P1, in P2> {
//    fun invoke(p1: P1, p2: P2) = Unit
//}

//endregion

//region + 泛型实现原理

//fun main() {
//// genericMethod("str")
//    val gson = Gson()
//    val garen : Person = gson.fromJson("""{"name":"Garen", "age":20}""")
//    val ashe = gson.fromJson<Person>("""{"name":"Ashe", "age":18}""")
//
//    println(garen)
//    println(ashe)
//}
//
//data class Person(val name:String, val age:Int)
//
//inline fun <reified T> Gson.fromJson(json:String): T = fromJson(json, T::class.java)
//
//fun <T : Comparable<T>> maxOf(a: T, b: T): T {
//    return if (a > b) a else b
//}
//
//inline fun<reified T> genericMethod(t: T) {
////    val t = T()
//    val ts = Array<T>(3){TODO()}
//    val jclass = T::class.java
//    val list = ArrayList<T>()
//}

//endregion

//region + 模拟 self type
//
//typealias OnConfirm = () -> Unit
//typealias OnCancel = () -> Unit
//
//private val EmptyFunction = {}
//
//open class Dialog(val title: String, val content: String)
//
//class ConfirmDialog(title: String, content: String,
//                    val onConfirm: OnConfirm,
//                    val onCancel: OnCancel) : Dialog(title, content)
//
//interface SelfType<Self> {
//    val self: Self
//        get() = this as Self
//}
//
//
//open class DialogBuilder<Self : DialogBuilder<Self>> : SelfType<Self> {
//    protected var title = ""
//    protected var content = ""
//
//    fun title(title: String): Self {
//        this.title = title
//        return self
//    }
//
//    fun content(content: String): Self {
//        this.content = content
//        return self
//    }
//
//    open fun build() = Dialog(this.title, this.content)
//}
//
//class ConfirmDialogBuilder : DialogBuilder<ConfirmDialogBuilder>() {
//    private var onConfirm: OnConfirm = EmptyFunction
//    private var onCancel: OnCancel = EmptyFunction
//
//    fun onConfirm(onConfirm: OnConfirm): ConfirmDialogBuilder {
//        this.onConfirm = onConfirm
//        return this
//    }
//
//    fun onCancel(onCancel: OnCancel): ConfirmDialogBuilder {
//        this.onCancel = onCancel
//        return this
//    }
//
//    override fun build() = ConfirmDialog(title, content, onConfirm, onCancel)
//}
//
//fun main() {
//    val confirmDialog = ConfirmDialogBuilder()
//            .title("提交弹窗")
//            .onCancel {
//                println("点击取消")
//            }
//            .content("确定提交吗？")
//            .onConfirm {
//                println("点击确定")
//            }
//            .build()
//
//    confirmDialog.onConfirm()
//}
//endregion

//region + 通过代理实现model注入方式一

//abstract class AbsModel {
//    init {
//        Models.run { register() }
//    }
//}
//
//class DatabaseModel: AbsModel() {
//    fun query(sql: String): Int = 0
//}
//
//class NetworkModel: AbsModel() {
//    fun get(url:String) = """{"code":0}"""
//}
//
//object Models {
//    private val modelMap = ConcurrentHashMap<Class<out AbsModel>, AbsModel>()
//
//    fun <T: AbsModel> KClass<T>.get(): T {
//        return modelMap[this.java] as T
//    }
//
//    fun AbsModel.register() {
//        modelMap[this.javaClass] = this
//    }
//}
//
//inline fun <reified T: AbsModel> modelOf(): ModelDelegate<T> {
//    return ModelDelegate(T::class)
//}
//
//class ModelDelegate<T: AbsModel>(val kClass:KClass<T>): ReadOnlyProperty<Any, T> {
//    override fun getValue(thisRef: Any, property: KProperty<*>): T {
//        return Models.run { kClass.get() }
//    }
//}
//
//
//class MainViewModel{
//    val databaseModel by modelOf<DatabaseModel>()
//    val networkModel by modelOf<NetworkModel>()
//}
//
//fun initModels() {
//    DatabaseModel()
//    NetworkModel()
//}
//
//fun main() {
//    initModels()
//    val mainViewModel = MainViewModel()
//    mainViewModel.databaseModel.query("select * from mysql.user").let(::println)
//    mainViewModel.networkModel.get("https://www.imooc.com").let(::println)
//}
//endregion


//region + 通过代理实现model注入方式二

abstract class AbsModel {
    init {
        Models.run { register() }
    }
}

class DatabaseModel: AbsModel() {
    fun query(sql: String): Int = 0
}

class NetworkModel: AbsModel() {
    fun get(url:String) = """{"code":0}"""
}

class PageModel: AbsModel() {
    init {
        Models.run { register("PageModel2") }
    }

    fun enter() {
        println("enter Next Page")
    }
}

object Models {
    private val modelMap = ConcurrentHashMap<String, AbsModel>()

    fun <T: AbsModel> String.get(): T {
        return modelMap[this] as T
    }

    fun AbsModel.register(name: String = this.javaClass.simpleName) {
        modelMap[name] = this

        println(modelMap.values.joinToString())
    }
}

object ModelDelegate{
    operator fun <T: AbsModel> getValue(thisRef: Any, property: KProperty<*>): T {
        return Models.run { property.name.capitalize().get() }
    }
}


class MainViewModel{
    val databaseModel: DatabaseModel by ModelDelegate
    val networkModel: NetworkModel by ModelDelegate
    val pageModel: PageModel by ModelDelegate
    val pageModel2: PageModel by ModelDelegate
}

fun initModels() {
    DatabaseModel()
    NetworkModel()
    PageModel()
}

fun main() {
    initModels()
    val mainViewModel = MainViewModel()
    mainViewModel.databaseModel.query("select * from mysql.user").let(::println)
    mainViewModel.networkModel.get("https://www.imooc.com").let(::println)
    mainViewModel.pageModel.enter()
    mainViewModel.pageModel2.enter()
}
//endregion


