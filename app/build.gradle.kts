import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.android.build.api.dsl.ApplicationExtension

// Load local.properties if present
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

configure<ApplicationExtension> {
    namespace = "com.awais.jsonlauncher"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.awais.jsonlauncher"
        minSdk = 24
        targetSdk = 36
        versionCode = 14
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // RevenueCat API key (reads from local.properties first, then gradle.properties)
        val revenueCatApiKey = localProperties.getProperty("REVENUECAT_API_KEY")
            ?: project.findProperty("REVENUECAT_API_KEY")?.toString()
            ?: ""

        buildConfigField(
            "String",
            "REVENUECAT_API_KEY",
            "\"$revenueCatApiKey\""
        )
    }

    buildTypes {
        release {
            // Enables code optimizations.
            isMinifyEnabled = true

            // Enables resource shrinking.
            isShrinkResources = true

            ndk {
                debugSymbolLevel = "FULL"
            }

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.37.3")

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)

    //navigation
    implementation("androidx.navigation:navigation-compose:2.9.3")

    //Datastore
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    //Revenue Cat
    implementation("com.revenuecat.purchases:purchases:8.17.1")
    implementation("com.revenuecat.purchases:purchases-ui:8.17.1")
}