package com.example.xlogapplacation

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elvishew.xlog.BuildConfig
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import com.example.xlogapplacation.ui.theme.XlogApplicationTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initXLog()
        // 不同级别的日志记录
        XLog.v("Verbose message") // 冗余信息
        XLog.d("Debug message") // 调试信息
        XLog.i("Info message") // 信息消息[1](@ref)
        XLog.w("Warning message") // 警告信息
        XLog.e("Error message") // 错误信息[1](@ref)

        // 带 TAG 的日志
        XLog.tag(TAG).d("Activity created")

        // 带异常信息的日志
        try {
            val result = 10 / 0
        } catch (e: Exception) {
            XLog.e("Division error occurred", e)
        }

        // 格式化字符串日志
        XLog.d("User %s is %d years old", "John", 30)

        // 初始化日志工具
        LogUtils.init(applicationContext, BuildConfig.DEBUG)

        // 记录不同的日志类型
        LogUtils.d("MainActivity", "Activity created")
        LogUtils.i("MainActivity", "User opened main screen")

        // 模拟网络请求日志
        logMockNetworkRequest()

        // 模拟用户行为日志
        logUserBehavior()

        // 记录 JSON 数据
        logJsonResponse()

        // 性能监控
        val startTime = System.currentTimeMillis()
        performHeavyOperation()
        val endTime = System.currentTimeMillis()
        LogUtils.logPerformance("HeavyOperation", startTime, endTime)
    }

    private fun logMockNetworkRequest() {
        val mockUrl = "https://api.example.com/user/123"
        val method = "GET"
        val duration = 356L

        LogUtils.logNetworkRequest(mockUrl, method, duration)
    }

    private fun logUserBehavior() {
        LogUtils.logUserAction("MainActivity", "ButtonClick", "Login button clicked")
    }

    private fun logJsonResponse() {
        val jsonResponse = "{\"user\":{\"id\":123,\"name\":\"John Doe\",\"email\":\"john@example.com\"}}"
        LogUtils.json(jsonResponse)
    }

    private fun performHeavyOperation() {
        // 模拟耗时操作
        try {
            Thread.sleep(500)
        } catch (e: InterruptedException) {
            LogUtils.e("MainActivity", "Heavy operation interrupted", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        // 确保所有日志都写入文件
//        XLog.flush()
    }
    private fun initXLog() {
        // 创建日志配置
        val config = LogConfiguration.Builder()
            .logLevel(LogLevel.ALL) // 设置日志级别，低于该级别的日志将不会被打印[5](@ref)
            .tag("MY_APP") // 默认 TAG
            .enableThreadInfo() // 启用线程信息
            .enableStackTrace(2) // 启用堆栈跟踪，深度为 2
            .enableBorder() // 启用边框
            .build()

        // 创建控制台打印器 (输出到 Android Studio Logcat)
        val androidPrinter: Printer = AndroidPrinter(true)

        // 创建文件打印器 (输出到文件)
        val logFolder = "${getExternalFilesDir(null)?.absolutePath}/xlog/"
        val filePrinter = FilePrinter.Builder(logFolder) // 指定保存日志文件的路径[5](@ref)
            .fileNameGenerator(DateFileNameGenerator()) // 按日期生成文件名
            .backupStrategy(NeverBackupStrategy()) // 不备份
            .cleanStrategy(FileLastModifiedCleanStrategy(7 * 24 * 60 * 60 * 1000)) // 保留7天
            .build()

        // 初始化 XLog
//        XLog.init(config, androidPrinter, filePrinter)
XLog.init();

        XLog.i("XLog initialized successfully")
    }
}

object JsonXmlLogger {
    fun logJsonData() {
        val jsonData = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}"
        XLog.json(jsonData) // 自动格式化 JSON[2](@ref)
    }

    fun logXmlData() {
        val xmlData = "<user><name>John</name><age>30</age></user>"
        XLog.xml(xmlData)
    }
}

