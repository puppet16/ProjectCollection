package cn.ltt.projectcollection.kotlin.kotlinreflection

import android.view.MotionEvent
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

/**
 * ============================================================
 * @author   李桐桐
 * date    2021年03月15日
 * desc    kotlin反射示例
 * ============================================================
 **/

//region + 反射说明

//@ExperimentalStdlibApi
//fun main() {
////    var cls: KClass<String> = String::class
////    cls.java.kotlin
////    val property = cls.declaredMemberProperties.firstOrNull()
////    cls.declaredMemberExtensionFunctions
//
//    val mapCls = Map::class
//    println(mapCls)
//    val mapType = typeOf<Map<out String, Int>>()
//    mapType.arguments.forEach {
//        println(it)
//    }
//
//}

//endregion

//region + 反射实例--获取泛型实参

//class Person(val name: String, val age: String)
//
//interface Api {
//    fun getUsers(): List<Person>
//}
//
//abstract class SuperType<T> {
//    val typeParameter by lazy {
//        this::class.supertypes.first().arguments.first().type!!
//    }
//    val typeParameterJava by lazy {
//        this.javaClass.genericSuperclass.safeAs<ParameterizedType>()!!.actualTypeArguments.first()
//    }
//}
//
//class SubType : SuperType<String>()
//
//
//fun main() {
//    Api::class.declaredMemberFunctions.first { it.name == "getUsers" }
//            .returnType.arguments.forEach {
//                println(it)
//            }
//
//    Api::getUsers.returnType.arguments.forEach { println(it) }
//    Api::class.java.getDeclaredMethod("getUsers")
//            .genericReturnType.safeAs<ParameterizedType>()?.actualTypeArguments?.forEach { println(it) }
//}
//
//fun<T> Any.safeAs(): T? {
//    return this as? T
//}
//
//fun main() {
//
//    val subType = SubType()
//    subType.typeParameter.let(::println)
//    subType.typeParameterJava.let(::println)
//}
//endregion

//region + 反射实例--为数据类实现深复制

//data class Person(val name:String = "Lee", val age:Int = 18, val group: Group)
//
//data class Group(val id:Int, val name:String, val location:String)
//fun main() {
//    val person = Person(group = Group(1,"student","beijing"))
////    val copiedPerson = person.copy()
////    println(person === copiedPerson)
////    println(person.name === copiedPerson.name)
////    println(person.group === copiedPerson.group)
//
//    val deepCopiedPerson = person.deepCopy()
//    println(person === deepCopiedPerson)
//    println(person.name === deepCopiedPerson.name)
//    println(person.group === deepCopiedPerson.group)
//    println(deepCopiedPerson)
//}
//
////数据类的深拷贝扩展函数
//fun <T : Any> T.deepCopy(): T {
//    if(!this::class.isData){
//        return this
//    }
//
//    return this::class.primaryConstructor!!.let {
//        primaryConstructor ->
//        primaryConstructor.parameters.map { parameter ->
//            val value = (this::class as KClass<T>).memberProperties.first { it.name == parameter.name }
//                    .get(this)
//            if((parameter.type.classifier as? KClass<*>)?.isData == true){
//                parameter to value?.deepCopy()
//            } else {
//                parameter to value
//            }
//        }.toMap()
//                .let(primaryConstructor::callBy)
//    }
//}

//endregion

//region + 反射实例--Model 映射
//data class Human(val name: String, val avatarUrl: String)
//
//data class Person(
//        var id: Int,
//        var name: String,
//        var avatarUrl: String,
//        var smallUrl: String,
//        var detailUrl: String
//)
//
//fun main() {
//    val person = Person(
//            0,
//            "Lee",
//            "https://avatars2.githubusercontent.com/u/30511713?v=4",
//            "https://api.github.com/users/Lee",
//            "https://puppet16.github.io/"
//    )
//
//    val human: Person = person.mapAs()
//    println(human)
//
//    val personMap = mapOf(
//            "id" to 0,
//            "name" to "Lee",
//            "avatarUrl" to "https://api.github.com/users/Lee",
//            "smallUrl" to "https://api.github.com/users/Lee",
//            "detailUrl" to "https://api.github.com/users/Lee"
//    )
//
//    val personFromMap: Person = personMap.mapAs()
//    println(personFromMap)
//}
//
//
//inline fun <reified From : Any, reified To : Any> From.mapAs(): To {
//    return From::class.memberProperties.map { it.name to it.get(this) }
//            .toMap().mapAs()
//}
//
//inline fun <reified To : Any> Map<String, Any?>.mapAs(): To {
//    return To::class.primaryConstructor!!.let {
//        it.parameters.map {
//            parameter ->
//            parameter to (this[parameter.name] ?: if(parameter.type.isMarkedNullable) null
//            else throw IllegalArgumentException("${parameter.name} is required but missing."))
//        }.toMap()
//                .let(it::callBy)
//    }
//}

//endregion



//region + 可释放引用对象的不可空类型

class ReleasableNotNull<T: Any>: ReadWriteProperty<Any, T> {
    private var value: T? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("Not initialized or released already.")
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }

    fun isInitialized() = value != null

    fun release() {
        value = null
    }
}

inline val KProperty0<*>.isInitialized: Boolean
    get() {
        isAccessible = true
        return (this.getDelegate() as? ReleasableNotNull<*>)?.isInitialized()
                ?: throw IllegalAccessException("Delegate is not an instance of ReleasableNotNull or is null.")
    }

fun KProperty0<*>.release() {
    isAccessible = true
    (this.getDelegate() as? ReleasableNotNull<*>)?.release()
            ?: throw IllegalAccessException("Delegate is not an instance of ReleasableNotNull or is null.")
}
class Bitmap(val width: Int, val height: Int)

class Activity {
    private var bitmap by ReleasableNotNull<Bitmap>()

    fun onCreate(){
        println(this::bitmap.isInitialized)
        bitmap = Bitmap(1920, 1080)
        println(::bitmap.isInitialized)
    }

    fun onDestroy(){
        println(::bitmap.isInitialized)
        ::bitmap.release()
        println(::bitmap.isInitialized)
    }
}

fun main() {
    val activity = Activity()
    activity.onCreate()
    activity.onDestroy()
}

//endregion