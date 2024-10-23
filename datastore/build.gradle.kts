import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "datastore"
            isStatic = true
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val noWasm by creating {
            dependsOn(commonMain.get())
        }
        wasmJsMain.get().dependsOn(commonMain.get())

        androidMain.get().dependsOn(noWasm)
        val desktopMain by getting
        desktopMain.dependsOn(noWasm)
        iosMain.get().dependsOn(noWasm)

        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
        noWasm.dependencies {
            implementation(libs.datastore.core)
            implementation(libs.datastore.preferences)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        wasmJsMain.dependencies {

        }
    }
}

android {
    namespace = "com.macpry.datastore"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
