plugins {
    alias(libs.plugins.android.application)
    id("kotlin-parcelize") // Week 8 Slide 14: Plugin Parcelize
}

android {
    namespace = "com.bpjs.cinelog"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.bpjs.cinelog"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.recyclerview)
    implementation(libs.picasso)
    implementation(libs.volley)

    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:2.1.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}