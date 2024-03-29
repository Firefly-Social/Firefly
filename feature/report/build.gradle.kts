plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "social.firefly.feature.report"
}

dependencies {
    implementation(project(":core:repository:mastodon"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:ui:common"))
    implementation(project(":core:ui:htmlcontent"))
    implementation(project(":core:navigation"))
    implementation(project(":core:analytics"))
    implementation(project(":core:usecase:mastodon"))

    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)

    implementation(libs.coil)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.jakewharton.timber)

    implementation(libs.kotlinx.datetime)
}
