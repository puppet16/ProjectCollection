package cn.lee.kotlin.annotations.apt.compiler

import com.squareup.kotlinpoet.*

fun main() {

    val personClass = ClassName("cn.lee.kotlin", "Person")
    val file = FileSpec.builder("cn.lee.kotlin", "KotlinPoetTest")
            .addType(TypeSpec.classBuilder("Person")
                    .primaryConstructor(FunSpec.constructorBuilder()
                            .addParameter("name", String::class)
                            .build())
                    .addProperty(PropertySpec.builder("age", Int::class)
                            .initializer("18")
                            .build())
                    .addFunction(FunSpec.builder("printContent")
                            .addStatement("println(%P)", "name= \$name")
                            .build())
                    .build())
            .addFunction(FunSpec.builder("main")
                    .addParameter("args", String::class, KModifier.VARARG)
                    .addStatement("%T(args[0]).printContent()", personClass)
                    .build())
            .build()

    file.writeTo(System.out)

}