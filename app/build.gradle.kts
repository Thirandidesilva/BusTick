

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.proj"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proj"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.navigation:navigation-fragment:2.7.3")
    implementation("androidx.navigation:navigation-ui:2.7.3")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation(libs.play.services.cast.framework)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}