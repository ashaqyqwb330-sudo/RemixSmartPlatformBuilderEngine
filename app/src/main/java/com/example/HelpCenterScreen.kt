package com.example

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class GlossaryItem(val term: String, val definition: String, val example: String)
data class FaqItem(val question: String, val answer: String)

@Composable
fun HelpCenterScreen(onNavigateBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("📖 قاموس المصطلحات", "❓ الأسئلة الشائعة")

    val glossaryItems = remember {
        listOf(
            GlossaryItem(
                term = "@builder",
                definition = "بادئة ذكية وموجه أساسي لإنشاء وتشكيل الملفات البرمجية مباشرة من النصوص داخل التطبيق تلقائياً.",
                example = "@builder:file src/main.py\n# اكتب كود Python هنا\n@builder:end"
            ),
            GlossaryItem(
                term = "@executor",
                definition = "بادئة لتشغيل وتنفيذ الأوامر والعمليات البرمجية بشكل مباشر وتراجعي داخل بيئة وهيكل المشروع.",
                example = "@executor:move --path=/Books --new-path=/Library"
            ),
            GlossaryItem(
                term = "@treedoc",
                definition = "بادئة متخصصة لبناء خرائط شجرية تفاعلية بصرية لكافة مجلدات العمل لضمان سهولة الفهم والتوثيق.",
                example = "@treedoc:generate --path=/MyProject"
            ),
            GlossaryItem(
                term = "SmartInbox",
                definition = "المستودع الذكي الافتراضي في التطبيق لتخزين النصوص المؤرشفة الملتقطة تلقائياً، والمنسقة بقوالب HTML فاخرة لتسهيل القراءة وتصفح المعلومات.",
                example = "يتم حفظ جميع النصوص المنسوخة المصنفة كنصوص تعليمية أو مقالات داخل دليل SmartInbox بتصميم السمة المحددة."
            )
        )
    }

    val faqItems = remember {
        listOf(
            FaqItem(
                question = "لماذا يتم تجاهل النصوص القصيرة المنسوخة أحياناً؟",
                answer = "يدرج التطبيق خاصية 'تجاهل النصوص القصيرة' لتجنب تكرار وحفظ القصاصات والكلمات العشوائية المنسوخة مؤقتاً بالخلفية. يمكنك إلغاء تفعيل هذا الخيار من الإعدادات للبدء بنسخ وحفظ كافة البيانات مهما كان حجمها."
            ),
            FaqItem(
                question = "كيف يمكنني مشاركة جذاذات مشروعي البرمجي مع مطور أو صديق؟",
                answer = "يمكنك النقر على زر 'تصدير حزمة بناء ذكية' من شاشات المشروع أو استعمال مخرج الحزمة المتعدد لنسخ كتل الأكواد مصنفة ومهيأة للنسخ كملف واحد متكامل."
            ),
            FaqItem(
                question = "ما فائدة الفقاعة الذهبية العائلة V2 العائمة؟",
                answer = "الفقاعة الذهبية توفر لك سهولة مطلقة ووصول سريع لكافة خدمات الأتمتة المباشرة، وحصد سجل الأحداث، وتجربة الأوامر يدوياً بضغطة زر واحدة من أي مكان على شاشة هاتفك خارج التطبيق."
            )
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SlateBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .background(GlassWhite, RoundedCornerShape(10.dp))
                        .border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                        .testTag("help_center_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "الرجوع",
                        tint = MetallicGold
                    )
                }

                Text(
                    text = "ℹ️ مركز المساعدة والتوثيق",
                    color = BrightGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Tab Selector Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GlassWhite, RoundedCornerShape(12.dp))
                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tabTitles.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                if (isSelected) MetallicGold.copy(alpha = 0.25f) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedTab = index }
                            .testTag("help_tab_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) BrightGold else TextSilver,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            // Tab contents
            Box(modifier = Modifier.weight(1f)) {
                if (selectedTab == 0) {
                    // Glossary Pane
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(glossaryItems) { item ->
                            GlossaryCardComponent(item)
                        }
                    }
                } else {
                    // FAQ Pane
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(faqItems) { item ->
                            FaqCardComponent(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlossaryCardComponent(item: GlossaryItem) {
    var isExpanded by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("glossary_card_${item.term}")
    ) {
        Column(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.term,
                    color = BrightGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextGray,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.definition,
                color = TextSilver,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = GlassBorder.copy(alpha = 0.15f), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "مثال الاستعمال البرمجي:",
                        color = MetallicGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = item.example,
                            color = TextSilver,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FaqCardComponent(item: FaqItem) {
    var isExpanded by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("faq_card_${item.question.take(15)}")
    ) {
        Column(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.question,
                    color = MetallicGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = TextGray,
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = GlassBorder.copy(alpha = 0.15f), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = item.answer,
                        color = TextSilver,
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
