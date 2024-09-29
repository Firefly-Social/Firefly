plugins {
    id("social.firefly.android.library")
    id("social.firefly.android.library.compose")
}

android {
    namespace = "social.firefly.core.ui.htmlcontent"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:image"))

    implementation(libs.coil)
    implementation(libs.jakewharton.timber)
}
