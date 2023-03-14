import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "bd.emon.movies"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", "\"" + getApiKey() + "\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets.getByName("main") {
        java.srcDir("src/main/kotlin")
    }
    sourceSets.getByName("test") {
        java.srcDir("src/test/kotlin")
    }
}

dependencies {

    val dagger_version = "2.44"
    val retrofit_version = "2.9.0"
    val gson_version = "2.9.1"
    val androidx_lifecycle_version = "2.5.1"
    val google_material_version = "1.6.1"
    val glide_version = "4.14.2"
    val swipe_refresh_layout_version = "1.1.0"
    val androidx_core_ktx_version = "1.9.0"
    val androidx_appcompat_version = "1.5.1"
    val constraint_layout_version = "2.1.4"
    val androidx_navigation_version = "2.5.3"
    val junit_version = "4.13.2"
    val junit_androidx_version = "1.1.3"
    val androidx_arch_core_testing_version = "2.1.0"
    val mockito_version = "2.23.4"
    val espresso_version = "3.4.0"
    val androidx_datastore_version = "1.0.0"
    val rxjava_version = "3.1.6"
    val rxandroid_version = "3.0.2"
    val room_version = "2.5.0"

    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-rxjava3:$room_version")
    implementation("io.reactivex.rxjava3:rxjava:$rxjava_version")
    implementation("io.reactivex.rxjava3:rxandroid:$rxandroid_version")
    implementation("androidx.datastore:datastore-preferences-core:$androidx_datastore_version")
    implementation("androidx.datastore:datastore-preferences-rxjava3:$androidx_datastore_version")
    implementation("com.google.dagger:hilt-android:$dagger_version")
    kapt("com.google.dagger:hilt-android-compiler:$dagger_version")
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:adapter-rxjava3:$retrofit_version")
    implementation("com.google.code.gson:gson:$gson_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$androidx_lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$androidx_lifecycle_version")
    implementation("com.google.android.material:material:$google_material_version")
    implementation("com.github.bumptech.glide:glide:$glide_version")
    kapt("com.github.bumptech.glide:compiler:$glide_version")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$swipe_refresh_layout_version")
    implementation("androidx.core:core-ktx:$androidx_core_ktx_version")
    implementation("androidx.appcompat:appcompat:$androidx_appcompat_version")
    implementation("androidx.constraintlayout:constraintlayout:$constraint_layout_version")
    implementation("androidx.navigation:navigation-fragment-ktx:$androidx_navigation_version")
    implementation("androidx.navigation:navigation-ui-ktx:$androidx_navigation_version")
    implementation(project(":domain"))
    implementation(project(":data"))
    testImplementation("junit:junit:$junit_version")
    testImplementation("androidx.arch.core:core-testing:$androidx_arch_core_testing_version")
    testImplementation("org.mockito:mockito-core:$mockito_version")
    androidTestImplementation("androidx.test.ext:junit:$junit_androidx_version")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso_version")
}

kapt {
    correctErrorTypes = true
}

fun getApiKey(): String {
    return gradleLocalProperties(rootDir).getProperty("api_key")
}
