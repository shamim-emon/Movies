plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "bd.emon.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    val rxjava_version = "3.1.6"
    val rxandroid_version = "3.0.2"
    val room_version = "2.5.0"
    val retrofit_version = "2.9.0"
    val gson_version = "2.9.1"
    val androidx_datastore_version = "1.0.0"
    val junit_version = "4.13.2"
    val androidx_arch_core_testing_version = "2.1.0"
    val mockito_version = "2.23.4"

    implementation("androidx.datastore:datastore-preferences-core:$androidx_datastore_version")
    implementation("androidx.datastore:datastore-preferences-rxjava3:$androidx_datastore_version")
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:adapter-rxjava3:$retrofit_version")
    implementation("com.google.code.gson:gson:$gson_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("io.reactivex.rxjava3:rxjava:$rxjava_version")
    implementation("io.reactivex.rxjava3:rxandroid:$rxandroid_version")
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-rxjava3:$room_version")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation(project(":domain"))
    testImplementation("junit:junit:$junit_version")
    testImplementation("androidx.arch.core:core-testing:$androidx_arch_core_testing_version")
    testImplementation("org.mockito:mockito-core:$mockito_version")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}