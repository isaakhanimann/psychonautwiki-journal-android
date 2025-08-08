// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    id("com.google.dagger.hilt.android") version libs.versions.hiltAndroidCompiler apply false
    id("androidx.room") version libs.versions.roomRuntime apply false
}