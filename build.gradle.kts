import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    kotlin("multiplatform") version "1.8.20"
}

repositories {
    mavenCentral()
}

kotlin {
    wasm {
        binaries.executable()
        nodejs()
    }
}

rootProject.tasks.withType(KotlinNpmInstallTask::class.java) {
    // necessary to ignore non-supported Node modules
    args += "--ignore-engines"
}
rootProject.plugins.withType(NodeJsRootPlugin::class.java) {
    val extension = rootProject.the<NodeJsRootExtension>()
    extension.nodeVersion = "20.0.0-v8-canary2023022187dda913b0"
    extension.nodeDownloadBaseUrl = "https://nodejs.org/download/v8-canary"
}