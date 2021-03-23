package cn.ltt.kotlin.plugin2

import cn.ltt.kotlin.plugincommon.Plugin

class PluginImpl2: Plugin {
    override fun start() {
        println("Plugin2: Start")
    }

    override fun stop() {
        println("Plugin2: Stop")
    }
}
