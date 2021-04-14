//package cn.ltt.projectcollection.kotlin.annotationlab
//
//import java.lang.IllegalArgumentException
//import kotlin.reflect.KClass
//import kotlin.reflect.full.findAnnotation
//import kotlin.reflect.full.memberProperties
//import kotlin.reflect.full.primaryConstructor
//
///**
// * ============================================================
// * @author   李桐桐
// * date    2021年04月01日
// * desc    描述
// * ============================================================
// **/
//
//@Target(AnnotationTarget.VALUE_PARAMETER)
//annotation class FieldName(val name: String)
//
//@Target(AnnotationTarget.CLASS)
//annotation class MappingStrategy(val klass: KClass<out NameStrategy>)
//
//interface NameStrategy {
//    fun mapTo(name: String): String
//}
//
//object UnderScoreToCamel : NameStrategy {
//    // html_url -> htmlUrl
//    override fun mapTo(name: String): String {
//        return name.toCharArray().fold(StringBuilder()) { acc, c ->
//            when (acc.lastOrNull()) {
//                '_' -> acc[acc.lastIndex] = c.toUpperCase()
//                else -> acc.append(c)
//            }
//            acc
//        }.toString()
//    }
//}
//
//object CamelToUnderScore : NameStrategy {
//    override fun mapTo(name: String): String {
//        return name.toCharArray().fold(StringBuilder()) { acc, c ->
//            when {
//                c.isUpperCase() -> acc.append('_').append(c.toLowerCase())
//                else -> acc.append(c)
//            }
//            acc
//        }.toString()
//    }
//}
//
//@MappingStrategy(CamelToUnderScore::class)
//data class Human(val name: String,
//                 val avatarUrl: String,
//                 val detailUrl: String)
//
//data class Person(
//        var id: Int,
//        var name: String,
//        var avatar_url: String,
//        var smallUrl: String,
//        var detail_url: String
//)
//
//fun main() {
////    val person = Person(
////            0,
////            "Lee",
////            "https://avatars2.githubusercontent.com/u/30511713?v=4",
////            "https://api.github.com/users/Lee",
////            "https://github.com/Lee"
////    )
////
////    val human: Human = person.mapAs()
////    println(human)
//
//    val personMap = mapOf(
//            "id" to 0,
//            "name" to "Lee",
//            "avatar_url" to "https://api.github.com/users/Lee",
//            "detail_url" to "https://api.github.com/users/Lee"
//    )
//
//    val humanFromMap: Human = personMap.mapAs()
//    println(humanFromMap)
//}
//
//inline fun <reified From : Any, reified To : Any> From.mapAs(): To {
//    return From::class.memberProperties.map { it.name to it.get(this) }
//            .toMap().mapAs()
//}
//
//inline fun <reified To : Any> Map<String, Any?>.mapAs(): To {
//    return To::class.primaryConstructor!!.let {
//        it.parameters.map { parameter ->
//            parameter to (this[parameter.name]
//                    ?: (parameter.annotations.filterIsInstance<FieldName>().firstOrNull()?.name?.let(this::get))
//                    ?: To::class.findAnnotation<MappingStrategy>()?.klass?.objectInstance?.mapTo(parameter.name!!)?.let(this::get)
//                    ?: if (parameter.type.isMarkedNullable) null
//                    else throw IllegalArgumentException("${parameter.name} is required but missing."))
//        }.toMap()
//                .let(it::callBy)
//    }
//}