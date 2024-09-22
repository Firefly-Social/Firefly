package social.firefly

import org.gradle.api.Project

fun Project.configureSecrets() {
    android {
        buildFeatures {
            buildConfig = true
        }
    }
}
