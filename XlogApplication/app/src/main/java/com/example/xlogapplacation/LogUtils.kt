package com.example.xlogapplacation

import android.content.Context
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator

object LogUtils {
    private const val MAX_LOG_FILE_SIZE = 50 * 1024 * 1024L // 50MB[5](@ref)

    // 初始化日志系统
    fun init(context: Context, isDebug: Boolean) {
        val level = if (isDebug) LogLevel.ALL else LogLevel.INFO

        val config = LogConfiguration.Builder()
            .logLevel(level)
            .tag("APP")
            .enableThreadInfo()
            .enableStackTrace(2)
            .addInterceptor(SecurityInterceptor())
            .build()

        val logPath = "${context.getExternalFilesDir(null)?.absolutePath}/logs/"

        val filePrinter = FilePrinter.Builder(logPath)
            .fileNameGenerator(DateFileNameGenerator())
            .backupStrategy(FileSizeBackupStrategy(MAX_LOG_FILE_SIZE))
            .cleanStrategy(FileLastModifiedCleanStrategy(10 * 24 * 60 * 60 * 1000)) // 10天
            .build()

        XLog.init(config, AndroidPrinter(), filePrinter)

        // 初始化崩溃处理
        CrashHandler.init(context)
    }

    // 不同场景的日志方法
    fun d(tag: String, message: String) {
        XLog.tag(tag).d(message)
    }

    fun i(tag: String, message: String) {
        XLog.tag(tag).i(message)
    }

    fun w(tag: String, message: String) {
        XLog.tag(tag).w(message)
    }

    fun e(tag: String, message: String) {
        XLog.tag(tag).e(message)
    }

    fun e(tag: String, message: String, tr: Throwable) {
        XLog.tag(tag).e(message, tr)
    }

    fun json(json: String) {
        XLog.json(json)
    }

    fun xml(xml: String) {
        XLog.xml(xml)
    }

    // 网络请求日志
    fun logNetworkRequest(url: String, method: String, duration: Long) {
        XLog.i("Network: $method $url | Duration: ${duration}ms")
    }

    // 用户行为日志
    fun logUserAction(activity: String, action: String, details: String) {
        XLog.i("UserAction: $activity | $action | $details")
    }

    // 性能监控日志
    fun logPerformance(taskName: String, startTime: Long, endTime: Long) {
        val duration = endTime - startTime
        XLog.d("Performance: $taskName took ${duration}ms")
    }
}