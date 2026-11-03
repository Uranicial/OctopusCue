// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    用于构建 Android 应用（APK）
    alias(libs.plugins.android.application) apply false
//    为 Android 模块提供 Kotlin 编译支持
    alias(libs.plugins.kotlin.android) apply false
//    配置 Jetpack Compose 编译器插件
    alias(libs.plugins.compose.compiler) apply false
//    提供 Kotlin 符号处理能力，后续 Room 可以使用
    alias(libs.plugins.ksp) apply false
//    引用定义的 Kotlin/JVM 插件。后面 :core:model 就会使用它编译纯 Kotlin 代码
    alias(libs.plugins.kotlin.jvm) apply false
//    引用 Android Library 插件。后面创建 :data:local 等 Android 库模块时会使用
    alias(libs.plugins.android.library) apply false
}