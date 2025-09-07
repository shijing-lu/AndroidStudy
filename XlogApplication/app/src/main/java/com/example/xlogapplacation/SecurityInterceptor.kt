package com.example.xlogapplacation

import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogItem
import com.elvishew.xlog.interceptor.BlacklistTagsFilterInterceptor
import com.elvishew.xlog.interceptor.Interceptor

class SecurityInterceptor : Interceptor {
    private val sensitiveKeywords = arrayOf("password", "token", "auth", "creditcard")

    override fun intercept(log: LogItem): LogItem? {
        val message = log.msg

        // 过滤敏感信息
        for (keyword in sensitiveKeywords) {
            if (message.contains(keyword, ignoreCase = true)) {
                return null // 丢弃包含敏感信息的日志
            }
        }

        return log
    }
}

// 使用拦截器
val config = LogConfiguration.Builder()
    .addInterceptor(SecurityInterceptor())
    .addInterceptor(BlacklistTagsFilterInterceptor("blacklist1", "blacklist2")) // 黑名单过滤[5](@ref)
    .build()