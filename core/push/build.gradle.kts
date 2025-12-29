plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "social.firefly.core.push"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:accounts"))

//    implementation(libs.unifiedPush.androidConnector)
//    implementation(libs.unifiedPush.androidConnector.ui)
    implementation(libs.jakewharton.timber)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}
