package com.example

import com.example.db.LogEntity
import java.io.File
import java.util.regex.Pattern

data class StoryCard(
    val id: String,
    val icon: String,
    val title: String,
    val details: String,
    val relativeTime: String,
    val rawLogsCount: Int,
    val timestamp: Long
)

object LogAggregator {

    private val FILE_PATTERN = Pattern.compile("(?i)\\b[\\w-]+\\.(kt|txt|json|xml|pdf|png|jpg|html|css|js|md|properties|gradle|pro|db)\\b")

    fun generateStoryCards(rawLogs: List<LogEntity>, mode: String): List<StoryCard> {
        val logs = rawLogs.take(50).sortedBy { it.timestamp }

        val groups = mutableListOf<MutableList<LogEntity>>()
        
        for (log in logs) {
            val lastGroup = groups.lastOrNull()
            if (lastGroup == null) {
                groups.add(mutableListOf(log))
            } else {
                val lastLogInGroup = lastGroup.last()
                val timeDifference = Math.abs(log.timestamp - lastLogInGroup.timestamp)
                
                val currentFile = extractFileName(log)
                val lastFile = extractFileName(lastGroup.first())
                
                val belongsToSameFile = currentFile != null && lastFile != null && currentFile.equals(lastFile, ignoreCase = true)
                val belongsToSameTypeAndNearTime = log.type == lastLogInGroup.type && timeDifference < 15000 // 15 seconds
                
                if (belongsToSameFile || belongsToSameTypeAndNearTime) {
                    lastGroup.add(log)
                } else {
                    groups.add(mutableListOf(log))
                }
            }
        }

        val storyCards = groups.map { group ->
            createStoryCardFromGroup(group, mode)
        }

        return storyCards.reversed()
    }

    private fun extractFileName(log: LogEntity): String? {
        var matcher = FILE_PATTERN.matcher(log.message)
        if (matcher.find()) {
            return matcher.group()
        }
        val details = log.details ?: ""
        matcher = FILE_PATTERN.matcher(details)
        if (matcher.find()) {
            return matcher.group()
        }
        
        val pathKeywords = listOf("المسار:", "تعديل: ", "تم إنشاء ")
        for (kw in pathKeywords) {
            if (log.message.contains(kw)) {
                val temp = log.message.substringAfter(kw).trim().split(" ").firstOrNull()?.trim()
                if (!temp.isNullOrBlank() && (temp.contains("/") || temp.contains("."))) {
                    return File(temp).name
                }
            }
        }
        return null
    }

    private fun createStoryCardFromGroup(group: List<LogEntity>, mode: String): StoryCard {
        val firstLog = group.first()
        val latestLog = group.last()
        val count = group.size
        
        val type = firstLog.type
        val fileName = extractFileName(firstLog)
        
        val icon = when {
            type == "builder" || fileName?.endsWith(".kt") == true -> "💻"
            fileName?.endsWith(".pdf") == true -> "📄"
            fileName?.endsWith(".json") == true || fileName?.endsWith(".xml") == true -> "⚙️"
            type == "clipboard_service" || type == "clipboard_monitor" -> "📋"
            type == "smart_capture" -> "📥"
            type == "gemini" -> "🧠"
            type == "executor" -> "🛡️"
            else -> "📝"
        }

        val relativeTime = getRelativeTime(latestLog.timestamp)

        val (title, details) = when (mode) {
            "developer" -> {
                val displayFile = fileName ?: "مستند برمجى"
                val act = when {
                    count > 1 -> "عمليات برمجية متعددة على [$displayFile] ($count أحداث)"
                    type == "builder" -> "إنشاء / تحديث الملف [$displayFile] عبر حزمة البناء"
                    type == "smart_capture" -> "التقاط ذكي للملف [$displayFile] بنجاح"
                    type == "executor" -> "تنفيذ أمر نظام على [$displayFile]"
                    else -> "تعديل المكون البنيوي [$displayFile]"
                }
                val detailsText = if (count > 1) {
                    "تسلسل العمليات: " + group.joinToString(" ➔ ") { it.message }
                } else {
                    firstLog.details ?: firstLog.message
                }
                Pair(act, detailsText)
            }
            "academic" -> {
                val displayFile = fileName?.substringBefore(".") ?: "النواة المعرفية"
                val act = when {
                    count > 1 -> "بروتوكول التحقق والمزامنة الشامل للمنشأة ($displayFile)"
                    type == "builder" -> "هيكلة صياغة المستخرجات البرمجية وتكاملها في ($displayFile)"
                    type == "smart_capture" -> "مرحلة استخلاص المعرفة والتحليل الإحصائي لـ ($displayFile)"
                    type == "executor" -> "تفعيل موجه التحكم الذاتي والتحقق الثنائي لـ ($displayFile)"
                    else -> "مزامنة البيانات الكودية والتطبيقات الهيكلية لـ ($displayFile)"
                }
                val detailsText = "تنفيذ دورة رصد الأنشطة الدورية ومطابقتها مع خوارزميات المنصة بنجاح."
                Pair(act, detailsText)
            }
            "user" -> {
                val act = when {
                    count > 1 -> "تم تنظيم وتعديل عدة ملفات في مشروعك الذكي بنجاح"
                    type == "builder" -> "تم حفظ وبناء تعديلات برمجية هامة لتطبيقك"
                    type == "smart_capture" -> "التقاط وحفظ نصوص هامة تلقائياً للرجوع إليها"
                    type == "executor" -> "تم تشغيل الأوامر المطلوبة بنجاح وتهيئة بيئة العمل"
                    else -> "تمت مزامنة البيانات وحفظها بأمان"
                }
                val detailsText = "تجري العمليات بسلاسة وأمان بدون التسبب في أي تباطؤ بجهازك."
                Pair(act, detailsText)
            }
            else -> {
                Pair(firstLog.message, firstLog.details ?: "")
            }
        }

        return StoryCard(
            id = group.map { it.id }.joinToString("-"),
            icon = icon,
            title = title,
            details = details,
            relativeTime = relativeTime,
            rawLogsCount = count,
            timestamp = latestLog.timestamp
        )
    }

    private fun getRelativeTime(timestamp: Long): String {
        val diff = System.currentTimeMillis() - timestamp
        return when {
            diff < 60_000 -> "الآن"
            diff < 3600_000 -> "${diff / 60_000} دقيقة"
            diff < 86400_000 -> "${diff / 3600_000} ساعة"
            else -> "${diff / 86400_000} يوم"
        }
    }
}
