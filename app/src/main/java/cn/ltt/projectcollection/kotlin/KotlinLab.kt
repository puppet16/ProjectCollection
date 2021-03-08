package cn.ltt.projectcollection.kotlin

import kotlin.reflect.jvm.internal.impl.descriptors.annotations.KotlinRetention

/**
 * ============================================================
 * @author   李桐桐
 * date    12/17/20
 * desc    描述
 * ============================================================
 **/

//region + 之前的代码

//region + 统计取斐波那契数列前11位所需时间
//fun main() {
//    cost {
//        val fibonacciNext = fibonacci()
//        for (i in 0.. 10) {
//            println(fibonacciNext())
//        }
//    }
//}
//
////实现斐波那契数列求值，
//fun fibonacci(): () -> Long {
//    var first = 0L
//    var second = 1L
//    return {
//        val next = first + second
//        val current = first
//        first = second
//        second = next
//        current
//    }
//}
//
//inline fun cost(block: () -> Unit) {
//    val start = System.currentTimeMillis()
//    block()
//    println("${System.currentTimeMillis() - start} ms")
//}
//endregion

//region + 内联函数

//fun main() {
//    val ints = intArrayOf(1,2,3,4)
//    ints.forEach {
//        println("Hello $it")
//    }
//    for (element in ints) {
//        println("Hello $element")
//    }
//
//    cost {
//        println("hello")
//    }
//}

//endregion

//region + 内联函数的return
//fun main() {
//    val ints = intArrayOf(1,2,3,4)
//    ints.forEach {
//        if (it == 3) return@forEach
//        println("element: $it")
//    }
//
//    for (element in ints) {
//        if (element == 3) continue
//        println("element: $element")
//    }
//}
// endregion

// region + illegal non-local return

//inline fun Runnable(crossinline block: () -> Unit): Runnable {
//    return object : Runnable {
//        override fun run() {
//            block()
//        }
//    }
//}

// endregion

// region + 内联属性
//var pocket: Double = 0.0
//var monkey: Double
//    inline get() = pocket
//    inline set(value) {
//        pocket = value
//    }
//
//fun main() {
//    monkey = 5.0
//}

//endregion

//region + let、run、also、apply、use
//class Person(var name: String, var age: Int)
//
//fun main() {
//    val person = Person("Lee", 18)
//    person.let(::println)
//    person.run(::println)
//
//    val person2 = person.also {
//        it.name = "Lee2"
//    }
//
//    val person3 = person.apply {
//        name = "Lee3"
//    }
//
//    with(person) {
//        name = "Lee4"
//    }
//
//    File("build.gradle.kts").inputStream().reader().buffered()
//            .use {
//                println(it.readLines())
//            }
//}

//endregion

//region + 遍历集合

//fun main() {
////    for (i in 1.. 10) {
////        println(i)
////    }
//
//    val ints = intArrayOf(1,2,3,3,4,5)
////    for (e in ints) {
////        println(e)
////    }
//    ints.forEach {
//        println(it)
//    }
//}

//endregion

//region + 集合变换

//fun main() {
//    val list = arrayListOf<Int>(1, 2, 3, 4)
//    val list2 = list.map { it * 2 }//list.filter { it % 2 == 0 }
//    println(list.joinToString())
//    println(list2.joinToString())
//    val list3 = list.filter {
//            println("filter:$it")
//            it % 2 == 0
//        }.map {
//            println("map:$it")
//            it * 2
//        }
//    println(list3.joinToString())

//    list.flatMap {
//        0 until it
//    }.joinToString().let(::println)

//    println(list.fold(2) {
//        acc, i ->
//        println("acc:$acc, i:$i")
//        acc + i
//    })

//    val array = arrayListOf<String>("x", "y")
//    list.zip(array){
//        a, b ->
//        println("a:$a, b:$b")
//        arrayListOf(a, b)
//    }.joinToString().let(::println)
//}

//endregion

//region + SAM

//fun main() {

//    val executor = Executors.newCachedThreadPool()
//    executor.submit(object : Runnable {
//        override fun run() {
//            println("run in runnable")
//        }
//    })
//
//
//
//    fun Runnable(block: () -> Unit): Runnable {
//        return object : Runnable {
//            override fun run() {
//                println("run in runnable")
//            }
//        }
//    }
//    val executor = Executor() { println("run") }
//}

//endregion

//region + SAM演示
//fun main() {
//
//    //报错
////    submit {
////
////    }
//    submitRunnable { println("ok") }
//
//}
//
//fun submitRunnable(runnable: Runnable) {
//    runnable.run()
//}
//
//typealias FunctionX = () -> Unit
//
//fun submit(invokable: Invokable) {
//    invokable.invoke()
//}
//
//fun submit2(lambda: () -> Unit) {
//    lambda()
//}
//
//fun submit3(lambda: FunctionX) {
//
//}
//
//interface Invokable {
//    fun invoke()
//}


//endregion

//region + SAM疑问
//fun main() {
//    val manager = CallbackManager()
//
////    val onCallback = CallbackManager.ViewCallback {viewId: Int ->
////        println("onChanged$viewId")
////    }
//
//    val onCallback = object : CallbackManager.ViewCallback {
//        override fun onViewCallback(viewId: Int) {
//            println("onChanged$viewId")
//        }
//    }
//
//    manager.registerCallback(onCallback)
//
//    manager.registerCallback(object: CallbackManager.ViewCallback {
//        override fun onViewCallback(viewId: Int) {
//            onCallback.onViewCallback(viewId)
//        }
//    })
//
//    manager.unregisterCallback(onCallback)
//
//    manager.unregisterCallback(object: CallbackManager.ViewCallback {
//        override fun onViewCallback(viewId: Int) {
//            onCallback.onViewCallback(viewId)
//        }
//    })
//}


//endregion

//region + 统计字符个数
//
//fun main() {
//    File("build.gradle.kts").readText()//读文件，返回一个String
//            .toCharArray()//将String 转为char[]
//            .filterNot { it.isWhitespace() }//过滤空格
//            .groupBy { it }//按每个char字符分组
//            .map {
//                it.key to it.value.size
//            }.let(::println)
//}

//endregion

//region + html DSL

//fun main() {
//    val htmlContent = html {
//        head {
//            "meta" { "charset"("UTF-8") }
//        }
//        body {
//            "div" {
//                "style"(
//                        """
//                    width: 200px;
//                    height: 200px;
//                    line-height: 200px;
//                    background-color: #C9394A;
//                    text-align: center
//                    """.trimIndent()
//                )
//                "span" {
//                    "style"(
//                            """
//                        color: white;
//                        font-family: Microsoft YaHei
//                        """.trimIndent()
//                    )
//                    +"Hello HTML DSL!!"
//                }
//            }
//        }
//    }.render()
//
//    File("Kotlin_html_DSL.html").writeText(htmlContent)
//}
//
//interface Node {
//    fun render(): String
//}
//
//class StringNode(val content: String) : Node {
//    override fun render(): String {
//        return content
//    }
//}
//
////节点类
//class BlockNode(val name: String) : Node {
//    //节点的子节点
//    val children = ArrayList<Node>()
//
//    //节点的属性
//    val properties = HashMap<String, Any>()
//
//    override fun render(): String {
//        //<html props>XXX</html>
//        return """
//            <$name ${properties.map { "${it.key}='${it.value}'" }.joinToString(" ")}>
//                ${children.joinToString(""){it.render()}}
//            </$name>
//        """.trimIndent()
//    }
//
//    //因为这个函数需要两个receiver:String,和BlockNode，将其定义到BlockNode类里就自动有了该receiver
//    operator fun String.invoke(block: BlockNode.() -> Unit): BlockNode {
//        val node = BlockNode(this)
//        node.block()
//        this@BlockNode.children += node
//        return node
//    }
//
//    operator fun String.invoke(value: Any) {
//        this@BlockNode.properties[this] = value
//    }
//
//    operator fun String.unaryPlus() {
//        this@BlockNode.children += StringNode(this)
//    }
//}
//
//fun html(block: BlockNode.() -> Unit): BlockNode {
//    val html = BlockNode("html")
//    html.block()
//    return html
//}
//
////为顶级函数head添加一个receiver,使之成为扩展函数
//fun BlockNode.head(block: BlockNode.() -> Unit): BlockNode {
//    val head = BlockNode("head")
//    head.block()
//    this.children += head
//    return head
//}
//
//fun BlockNode.body(block: BlockNode.() -> Unit): BlockNode {
//    val body = BlockNode("body")
//    body.block()
//    this.children += body
//    return body
//}


//endregion

//region + 构造器基本写法

//
//abstract class Animal(var name: String) {
//}
//
//fun main() {
//    val person = Person(name = "Lee")
//    val str = String()
//    val str1 = String(charArrayOf('1','2'))
//}
//fun String(ints: IntArray): String {
//    return ints.contentToString()
//}
//
//class Person  constructor(var age: Int = 18,name: String){
//    constructor():this(20, "unknown")
//}
//

//endregion

//region + 属性的延迟初始化

//private lateinit var person: Person
//
//fun main() {
//    println(::person.isInitialized)
//    person = Person("Lee", 18)
//    println(::person.isInitialized)
//
//}
// class Person(var name: String, private var age:Int)

//private val mTvName by lazy {
//    findViewById<TextView>(R.id.tv_name)
//}
//endregion

//region + 接口代理

//fun main() {
//
//}
//interface TextWatcher {
//
//    fun beforeTextChanged()
//
//    fun onTextChanged()
//
//    fun afterTextChanged();
//}
//
//class TextWatcherImpl : TextWatcher {
//    override fun beforeTextChanged() {
//        println("beforeTextChanged")
//    }
//
//    override fun onTextChanged() {
//        println("onTextChanged")
//    }
//
//    override fun afterTextChanged() {
//        println("afterTextChanged")
//    }
//}
//
//class TextWatcherImplWrapper(val watcher: TextWatcher) : TextWatcher by watcher {
//
//    override fun afterTextChanged() {
//        watcher.afterTextChanged()
//        println("wrapper")
//    }
//}

//endregion

//region + 属性代理

//class Person(var name: String) {
//    //代理getter
//    val firstName by lazy { name.split(" ")[0] }
//    var secondName: String by Delegates.observable(name.split(" ")[1]) { property, oldValue, newValue ->
//        println("value changed from $oldValue -> $newValue")
//    }
//}
//
//fun main() {
//    val person = Person("Bruce Lee")
//    person.secondName = "Jones"
//
//    println("Person:{name=${person.name},firstName=${person.firstName},secondName=${person.secondName}")
//    person.name = "Jon Smith"
//    println("Person:{name=${person.name},firstName=${person.firstName},secondName=${person.secondName}")
//}


//endregion

//region + 自定义属性代理

//fun main() {
//    var str:String by ProxyX("321")
//    println(str)
//    str = "123"
//    println(str)
//}
//
//class ProxyX(private var initialValue: String) {
//    private var value = initialValue
//
//    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
//        return "$value-Proxy"
//    }
//
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
//        this.value = this.value + value
//    }
//}
//endregion

//region + 自定义属性代理读取Config.properties

//class PropertiesDelegate(private val path: String, private val defaultValue: String = "") {
//    private lateinit var url: URL
//
//    private val properties: Properties by lazy {
//        val prop = Properties()
//        url = try {
//            javaClass.getResourceAsStream(path).use {
//                prop.load(it)
//            }
//            javaClass.getResource(path)!!
//        } catch (e: Exception) {
//            try {
//                ClassLoader.getSystemClassLoader().getResourceAsStream(path).use {
//                    prop.load(it)
//                }
//                ClassLoader.getSystemClassLoader().getResource(path)!!
//            } catch (e: Exception) {
//                FileInputStream(path).use {
//                    prop.load(it)
//                }
//                URL("file://${File(path).canonicalPath}")
//            }
//        }
//        prop
//    }
//
//    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
//        return properties.getProperty(property.name, defaultValue)
//    }
//
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
//        properties.setProperty(property.name, value)
//        File(url.toURI()).outputStream().use {
//            properties.store(it, "Hello")
//        }
//    }
//}
//
//abstract class AbsProperties(path: String) {
//    protected val prop = PropertiesDelegate(path)
//}
//
//class Config: AbsProperties("Config.properties") {
//    var author by prop
//    var version by prop
//    var desc by prop
//}
//
//fun main() {
//    val config = Config()
//    println(config.author)
//    println(config.version)
//    config.version = "1.1.0"
//    println(config.desc)
//    println(config.version)
//}

//endregion

//region + 单例

//object Singleton {
//    @JvmField var value: Int = 26
//    init {
//        value *= 2
//    }
//}
//
//fun main() {
//    println("value${Singleton.value}")
//}

//endregion

//region + 内部类

//class Outer {
//    inner class Inner
//    class StaticInner
//}
//
//fun main() {
//    val inner = Outer().Inner()
//    val innerObject = OuterObject.InnerObject
//    val btn: Button = Button(context)
//    btn.post { object: Runnable, Closeable {
//        override fun run() {
//            TODO("Not yet implemented")
//        }
//
//        override fun close() {
//
//        }
//
//    }}
//
//}
//
//object OuterObject {
//    object InnerObject
//}
//


//endregion

//region + 数据类
//
//class Person(var name: String, var age: Int)
//
//@DataClassAnnotation
//data class Computer(val id: Long, val name: String, val brand:Person) {
//    init{
//        println(name)
//    }
//}
//
//fun main() {
////    val computer = Computer(31L, "mackBook Pro", Person("Apple", 45))
//    val computer = Computer::class.java.newInstance()
//}
//


//endregion

//region + 枚举

//enum class State : Runnable {
//    START {
//        override fun run() {
//            println("For START")
//        }
//    },
//    FINISHED {
//        override fun run() {
//            println("For FINISHED")
//        }
//    }
//}
//
//enum class Status {
//    NEW, RUNNABLE, RUNNING, BLOCKED, DEAD
//}
//
//fun State.next(): State {
//    return State.values().let(fun(it: Array<State>): State {
//        val nextOrdinal = (ordinal + 1) % it.size
//        return it[nextOrdinal]
//    })
//}
//
//fun main() {
////    println(State.START.name)
////    println(State.START.ordinal)
////    println(State.FINISHED.name)
////    println(State.FINISHED.ordinal)
////    println(State.values().contentToString())
////
////    val state = State.START
////    val value = when(state) {
////        State.START -> {0}
////        State.FINISHED -> {1}
////    }
////    if (state <= State.FINISHED) {
////        println("yes")
////    }
//
//    val statusRange = Status.NEW .. Status.BLOCKED
//
//    val status = Status.RUNNING
//
//    println(status in statusRange)
//
//
//}

//endregion

//region + 密封

//fun main() {
//   val player = Player()
//    player.play("The Nights")
//}
//sealed class PlayerState
//
//object Idle : PlayerState()
//
//class Playing(val song: String): PlayerState() {
//    fun start(){}
//    fun stop(){}
//}
//
//class Error(val errorInfo: String):PlayerState() {
//    fun recover() {}
//}
//
//class Player {
//    var state: PlayerState = Idle
//
//    fun play(song: String) {
//        this.state = when(val state = this.state) {
//            Idle -> {
//                Playing(song).also(Playing::start)
//            }
//            is Playing -> {
//                state.stop()
//                Playing(song).also(Playing::start)
//            }
//            is Error -> {
//                state.recover()
//                Playing(song).also(Playing::start)
//            }
//        }
//    }
//
//}

//endregion


//region + 内联类

//inline class BoxInt(val value: Int) : Comparable<Int> {
//    operator fun inc(): BoxInt {
//        return BoxInt(value + 1)
//    }
//
//    override fun compareTo(other: Int): Int {
//        return value.compareTo(other)
//    }
//}
//
//fun main() {
//    var boxInt = BoxInt(5)
//    val newValue = boxInt.value * 200
//    println(newValue)
//    boxInt++
//    println(boxInt)
//
//    println(State.Start)
//}

//inline class State(val ordinal:Int) {
//    companion object {
//        val Start = State(0)
//        val Running = State(1)
//        val Finished = State(2)
//    }
//    fun values() = arrayOf(Start, Finished)
//    val name: String
//        get() {
//            return when (ordinal) {
//                0 -> {
//                    "Start"
//                }
//                1 -> {
//                    "Running"
//                }
//                2 -> {
//                    "Finished"
//                }
//                else -> {
//                    "other"
//                }
//            }
//        }
//}
//
//fun main() {
//    setState(State.Running)
//}
//
//fun setState(state:State) {
//    println("setState-State:$state")
//}


//endregion

//region + 数据类Json序列化
//fun main() {
//    val jsonStr = """{"name":"Lee","age":20}"""
//    val person = Person("Lee", 18)
//
//
//    //Gson
//    println("Gson")
//    val gson = Gson()
//    println(gson.toJson(person))
//    println(gson.fromJson(jsonStr, Person::class.java))
//    println()
//
//
//    //Moshi
//    println("Moshi")
//    val moshi = Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
//            .build()
//    val jsonAdapter = moshi.adapter(Person::class.java)
//    println(jsonAdapter.toJson(person))
//    println(jsonAdapter.fromJson(jsonStr))
//    println()
//
//
//    println("Kotlinx.serialization")
//
//    val data = PersonFroSerialize("Lee", 18)
//    println(Json.encodeToString(PersonFroSerialize.serializer(),data))
//    println(Json.decodeFromString(PersonFroSerialize.serializer(),jsonStr))
//}
//
//
//data class Person(val name: String, val age: Int)
//
//@Serializable
//data class PersonFroSerialize(val name: String, val age: Int)

//endregion

//region + 带默认参数的序列化
//fun main() {
//    val jsonStr = """{"name":"Lee","age":18}"""
//    val person = PersonWithInits("Lee", 18)


    //Gson
//    println("Gson")
//    val gson = Gson()
//    println(gson.toJson(PersonWithInits("Lee", 18)))
//    val person = gson.fromJson(jsonStr, PersonWithInits::class.java)
//    println(person)
//    println(person.firstName)


    //Moshi
//    val moshi = Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
//            .build()
//    val jsonAdapter = moshi.adapter(PersonWithDefaults::class.java)
//    println(jsonAdapter.toJson(person))
//    println(jsonAdapter.fromJson(jsonStr))
//    println()
//
//
//    println("Kotlinx.serialization")
//
//    val data = PersonWithDefaults("Lee")
//    println(Json.encodeToString(PersonWithDefaults.serializer(),PersonWithDefaults("Lee")))
//    println(Json.decodeFromString(PersonWithDefaults.serializer(),jsonStr))
//
//    println(Json.encodeToJsonElement(PersonWithDefaults("Lee")))
//}

//@JsonClass(generateAdapter = true)
//@Serializable
//data class PersonWithDefaults(val name: String, val age: Int = 18)
//
//@Serializable
//data class PersonFroSerialize(val name: String, val age: Int)

//@DataClassAnnotation
//data class PersonWithInits(val name: String, val age:Int) {
//    init {
//        println("PersonWithInits#init()")
//    }
//    val firstName by lazy {
//        name.split(" ")[0]
//    }
//}




//endregion

//region + 带init块的序列化
//fun main() {
//    val jsonStr = """{"name":"Lee", "age":18}"""
//
//    println(Json.encodeToString(PersonWithInits.serializer(), PersonWithInits("Lee", 18)))
//    val person = Json.decodeFromString(PersonWithInits.serializer(), jsonStr)
//    println(person)
//    println(person.firstName)
////
////    //方法一
////    val moshi = Moshi.Builder()
//////            .add(KotlinJsonAdapterFactory())
////            .build()
////    val jsonAdapter = moshi.adapter(PersonWithInitsAnnotation::class.java)
////    println(jsonAdapter.toJson(PersonWithInitsAnnotation("Lee", 18)))
////    val person = jsonAdapter.fromJson(jsonStr)
////    println(person)
////    println(person?.firstName)
////    //方法二
////    val moshi2 = Moshi.Builder()
////            .add(KotlinJsonAdapterFactory())
////            .build()
////    val jsonAdapter2 = moshi2.adapter(PersonWithInits::class.java)
////    println(jsonAdapter2.toJson(PersonWithInits("Lee", 18)))
////
////    val person2 = jsonAdapter2.fromJson(jsonStr)
////    println(person2)
////    println(person2?.firstName)
//
//}
//
//@JsonClass(generateAdapter = true)
//data class PersonWithInitsAnnotation(val name: String, val age:Int) {
//    init {
//        println("PersonWithInits#init()")
//    }
//    val firstName by lazy {
//        name.split(" ")[0]
//    }
//}
//@Serializable
//data class PersonWithInits(val name: String, val age:Int) {
//    init {
//        println("PersonWithInits#init()")
//    }
//    val firstName by lazy {
//        name.split(" ")[0]
//    }
//}
//

//endregion
//endregion

//region + 递归整型列表

fun main() {
    //[0,1,2,3]
    //实现方式一
//    val list = IntList.Cons(0, IntList.Cons(1, IntList.Cons(2, IntList.Cons(3, IntList.Nil))))
    val list = intListOf(0,1,2,3)
    println(list)
    println(list.joinToString('-'))
    println(list.sum())

    val (first, second, third) = list
    println("$first, $second, $third")
}

//实现嵌套列表
fun intListOf(vararg ints: Int): IntList {
    return when(ints.size) {
        0 -> IntList.Nil
        else -> {
            IntList.Cons(
                    ints[0],
                    intListOf(*(ints.slice(1 until ints.size).toIntArray()))
            )
        }
    }
}

//扩展方法--求和
fun IntList.sum(): Int {
    return when (this) {
        IntList.Nil -> 0
        is IntList.Cons -> {
            return head + tail.sum()
        }
    }
}

operator fun IntList.component1(): Int? {
    return when(this) {
        IntList.Nil -> null
        is IntList.Cons -> {
            head
        }
    }
}
operator fun IntList.component2(): Int? {
    return when(this) {
        IntList.Nil -> null
        is IntList.Cons -> {
            tail.component1()
        }
    }
}

operator fun IntList.component3(): Int? {
    return when(this) {
        IntList.Nil -> null
        is IntList.Cons -> {
            tail.component2()
        }
    }
}
sealed class IntList {
    //链的最后一个
    object  Nil: IntList() {
        override fun toString(): String {
            return "Nil"
        }
    }
    data class Cons(val head: Int, val tail:IntList): IntList() {
        override fun toString(): String {
            return "$head, $tail"
        }
    }

    fun joinToString(sep: Char = ','):String {
        return when(this) {
            Nil -> {
                "Nil"
            }
            is Cons -> {
                "${head}$sep${tail.joinToString(sep)}"
            }
        }
    }
}

//endregion     了