import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

// Secrets are read from local.properties (gitignored) or an env var for CI,
// never hardcoded in tracked source. See local.properties.example.
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}
val nytApiKey: String =
    (localProperties.getProperty("NYT_API_KEY") ?: System.getenv("NYT_API_KEY") ?: "")
        .also {
            if (it.isBlank()) {
                logger.warn("NYT_API_KEY is not set. Add it to local.properties (see local.properties.example) or set the NYT_API_KEY environment variable.")
            }
        }

val generatedSecretsDir = layout.buildDirectory.dir("generated/secrets").get().asFile
generatedSecretsDir.resolve("org/rks369/news/secrets").apply {
    mkdirs()
    resolve("Secrets.kt").writeText(
        """
        package org.rks369.news.secrets

        internal object Secrets {
            const val NYT_API_KEY: String = "$nytApiKey"
        }
        """.trimIndent()
    )
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    js {
        browser()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    
    android {
       namespace = "org.rks369.news.app.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
       withDeviceTestBuilder {
           sourceSetTreeName = "test"
       }.configure {
           instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
       }
    }
    
    sourceSets {
        commonMain.configure {
            kotlin.srcDir(generatedSecretsDir)
        }
        commonMain.dependencies {
            api(project(":core"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        // multiplatform-settings-no-arg publishes for android/ios/jvm/js but not wasmJs.
        // Rather than restructuring the default source set hierarchy, each of those four
        // source sets just also compiles the one shared KeyValueStore.kt file directly.
        val settingsSharedDir = "src/settingsShared/kotlin"

        androidMain.configure { kotlin.srcDir(settingsSharedDir) }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.multiplatform.settings.no.arg)
        }

        iosMain.configure { kotlin.srcDir(settingsSharedDir) }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.multiplatform.settings.no.arg)
        }

        jvmMain.configure { kotlin.srcDir(settingsSharedDir) }
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.multiplatform.settings.no.arg)
        }

        jsMain.configure { kotlin.srcDir(settingsSharedDir) }
        jsMain.dependencies {
            implementation(libs.wrappers.browser)
            implementation(libs.multiplatform.settings.no.arg)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}