package com.example.xlogapplacation

import android.content.Context
import com.elvishew.xlog.XLog

class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    companion object {
        private val instance = CrashHandler()

        fun init(context: Context) {
            instance.defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler(instance)
        }
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        // 记录崩溃信息
        XLog.e("Crash occurred in thread: ${thread.name}", ex)

        // 确保日志被刷新到文件
//        XLog.flush()

        // 调用默认处理器
        defaultHandler?.uncaughtException(thread, ex)
    }
}