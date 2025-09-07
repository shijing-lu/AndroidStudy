package com.example.xlogapplacation

import android.app.Application
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator

object MultiFileLogger {
    fun initMultiFileLogging(app: Application) {
        val basePath = "${app.getExternalFilesDir(null)?.absolutePath}/logs/"

        // 调试日志文件打印器
        val debugPrinter = FilePrinter.Builder("$basePath/debug/")
            .fileNameGenerator(DateFileNameGenerator())
            .build()

        // 错误日志文件打印器
        val errorPrinter = FilePrinter.Builder("$basePath/error/")
            .fileNameGenerator(DateFileNameGenerator())
            .build()

        // 创建多个配置
        val debugConfig = LogConfiguration.Builder()
            .logLevel(LogLevel.DEBUG)
            .tag("DEBUG")
            .build()

        val errorConfig = LogConfiguration.Builder()
            .logLevel(LogLevel.ERROR)
            .tag("ERROR")
            .build()


    }


    fun logDebug(message: String) {
        XLog.d(message)
    }

    fun logError(message: String) {
        XLog.e(message)
    }
}