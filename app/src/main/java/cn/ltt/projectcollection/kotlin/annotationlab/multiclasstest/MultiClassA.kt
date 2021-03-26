@file:JvmName("Utils")
@file:JvmMultifileClass
package cn.ltt.projectcollection.kotlin.annotationlab.multiclasstest

/**
 * ============================================================
 * @author   李桐桐
 * date    2021年03月25日
 * desc    测试注解JvmMultifileClass
 * ============================================================
 **/


fun getDeviceId() : String = "deviceId"

lateinit var id: String

const val DEVICE_ID_DEFAULT = "123"