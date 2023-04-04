import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform") version "1.8.20"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
}

kotlin {
    wasm {
        binaries.executable()
        browser {
            commonWebpackConfig {
                devServer = KotlinWebpackConfig.DevServer(
                    open = mapOf(
                        "app" to mapOf(
                            "name" to "google chrome canary", // MacOS-specific name
                            "arguments" to listOf("--js-flags=--experimental-wasm-gc")
                        )
                    ),
                    static = devServer?.static,
                )
            }
        }
    }
    sourceSets {
        val wasmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta-wasm0")
                implementation("org.jetbrains.kotlinx:atomicfu:0.18.5-wasm0")
            }
        }
    }
}
