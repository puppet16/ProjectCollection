package cn.ltt.kotlin.plugin1

import cn.ltt.kotlin.plugincommon.Plugin

class PluginImpl1: Plugin{
    override fun start() {
        println("Plugin1: Start")
        newMethod()
    }

    fun newMethod(){
        println("newMethod called!! 1")
    }

    override fun stop() {
        println("Plugin1: Stop")
    }
}
