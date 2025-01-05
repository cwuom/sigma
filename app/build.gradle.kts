plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "moe.sigma"
    compileSdk = 34

    defaultConfig {
        applicationId = "moe.sigma"
        minSdk = 29
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "?"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

    androidResources {
        additionalParameters += arrayOf(
            "--allow-reserved-package-id",
            "--package-id", "0x45"
        )
    }
}

dependencies {
    compileOnly(libs.androidx.annotation)
    // Xposed API 89
    compileOnly(libs.xposed)
}
