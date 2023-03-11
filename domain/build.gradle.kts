plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "bd.emon.domain"
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
}

dependencies {

    val rxjava_version = "3.1.6"
    val rxandroid_version = "3.0.2"
    val androidx_datastore_version = "1.0.0"
    val room_version = "2.5.0"

    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-rxjava3:$room_version")
    implementation("androidx.datastore:datastore-preferences:$androidx_datastore_version")
    implementation("androidx.datastore:datastore-preferences-rxjava3:$androidx_datastore_version")
    implementation("io.reactivex.rxjava3:rxjava:$rxjava_version")
    implementation("io.reactivex.rxjava3:rxandroid:$rxandroid_version")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}