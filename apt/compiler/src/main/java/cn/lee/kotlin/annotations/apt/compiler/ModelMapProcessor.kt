package cn.lee.kotlin.annotations.apt.compiler

import com.bennyhuo.aptutils.types.asKotlinTypeName
import com.bennyhuo.aptutils.types.packageName
import com.bennyhuo.aptutils.types.simpleName
import com.bennyhuo.aptutils.utils.writeToFile
import cn.lee.kotlin.annotations.apt.ModelMap
import com.bennyhuo.aptutils.AptContext
import com.squareup.kotlinpoet.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

@SupportedAnnotationTypes("cn.lee.kotlin.annotations.apt.ModelMap")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ModelMapProcessor: AbstractProcessor() {

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        AptContext.init(processingEnv)
        val personClass = ClassName("cn.lee.kotlin", "Person")
        val file = FileSpec.builder("cn.lee.kotlin", "KotlinPoetTest")
                .addType(TypeSpec.classBuilder("Person")
                        .primaryConstructor(FunSpec.constructorBuilder()
                                .addParameter("name", String::class)
                                .build())
                        .addProperty(PropertySpec.builder("name", String::class)
                                .initializer("name")
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
        file.writeToFile()
    }

    //fun Sample.toMap() = mapOf("a" to a, "b" to b)
//fun <V> Map<String, V>.toSample() = Sample(this["a"] as Int, this["b"] as String)
    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(ModelMap::class.java)
            .forEach {
                element ->
                element.enclosedElements.filterIsInstance<ExecutableElement>()
                    .firstOrNull { it.simpleName() == "<init>" }
                    ?.let {
                        val typeElement = element as TypeElement
                        FileSpec.builder(typeElement.packageName(), "${typeElement.simpleName()}\$\$ModelMap")
                            .addFunction(
                                FunSpec.builder("toMap")
                                    .receiver(typeElement.asType().asKotlinTypeName())
                                    .addStatement("return mapOf(${it.parameters.joinToString {""""${it.simpleName()}" to ${it.simpleName()}""" }})")
                                    .build()
                            )
                            .addFunction(
                                FunSpec.builder("to${typeElement.simpleName()}")
                                    .addTypeVariable(TypeVariableName("V"))
                                    .receiver(MAP.parameterizedBy(STRING, TypeVariableName("V")))
                                    .addStatement(
                                        "return ${typeElement.simpleName()}(${it.parameters.joinToString{ """this["${it.simpleName()}"] as %T """ } })",
                                        *it.parameters.map { it.asType().asKotlinTypeName() }.toTypedArray()
                                    )
                                    .build()
                            )
                            .build().writeToFile()
                    }
            }
        return true
    }
}