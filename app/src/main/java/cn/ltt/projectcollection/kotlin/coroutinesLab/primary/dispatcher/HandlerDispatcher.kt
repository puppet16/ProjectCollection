package cn.ltt.projectcollection.kotlin.coroutinesLab.primary.dispatcher;

import android.os.Handler

object HandlerDispatcher: Dispatcher {
    private val handler = Handler()

    override fun dispatch(block: () -> Unit) {
        handler.post(block)
    }
}