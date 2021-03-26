package cn.ltt.projectcollection.kotlin.annotationlab

import kotlin.jvm.Throws
import kotlin.reflect.jvm.internal.impl.load.java.UtilsKt

/**
 * ============================================================
 * @author   李桐桐
 * date    2021年03月23日
 * desc    注解示例
 * ============================================================
 **/

//region + 注解概念

//@Retention(AnnotationRetention.RUNTIME)
//@Target(AnnotationTarget.CLASS)
//annotation class State(val status:String)
//
//
//fun main() {
//    val map: Map<Any,Any> = emptyMap()
//
//    @Suppress("UNCHECKED_CAST")
//    map as Map<String, Any>
//
//}

//endregion

//region + 注解--java虚拟机相关

//class Person(@JvmField var name: String, var age: Int)
//
//
//var kotlinField: Int = 2
//    //修改属性的set方法名
//    @JvmName("setJavaField")
//    set(value) {
//        field = value
//    }
//    //修改属性的get方法名
//    @JvmName("getJavaField")
//    get() {
//        return field
//    }
//
////修改普通的方法名
//fun kotlinFunction() {
//}
//
//fun main() {
//    kotlinFunction()
//}

//class ClassA {
//    companion object {
//        @JvmStatic
//        var nameDefault: String = "Lee"
//        @JvmStatic
//        fun getName(): String = "name:$nameDefault"
//    }
//}

//@Throws(IllegalAccessException::class)
//fun hello(){
//
//}
//endregion

