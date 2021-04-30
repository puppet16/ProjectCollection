package cn.ltt.projectcollection.kotlin.annotationlab

import cn.lee.kotlin.annotations.apt.ModelMap
//
//fun main() {
//    val person = Person("Lee", 18)
//    val human = person.toMap().toHuman()
//    println(person)
//    println(human)
//}
//
////fun Human.toMap() = mapOf("name" to name, "age" to age)
////fun <V> Map<String, V>.toHuman() = Human(this["name"] as String , this["age"] as Int)
//
//@ModelMap
//data class Human(val name: String, val age :Int)
//
//@ModelMap
//data class Person(val name: String, val age :Int, val address:String = "china")