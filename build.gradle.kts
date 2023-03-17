buildscript {
    extra.apply {
        set("kotlin_version", "1.7.20")
    }

    val kotlinVersion = extra.get("kotlin_version") as String

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:11.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
    }
}

plugins {
    id("com.android.application") version ("7.3.0") apply (false)
    id("com.android.library") version ("7.3.0") apply (false)
    id("org.jetbrains.kotlin.android") version ("1.8.0") apply (false)
    id("com.google.dagger.hilt.android") version ("2.44") apply (false)
    id("org.jlleitschuh.gradle.ktlint") version ("11.0.0")
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        filter {
            exclude("**/*.md")
        }
        debug.set(true)
        disabledRules.set(setOf("final-newline"))
    }
}

task<Copy>("installGitHook") {
    from(File(rootProject.rootDir, "pre-commit"))
    into { File(rootProject.rootDir, ".git/hooks") }
    fileMode = "0777".toInt()
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

tasks.register(":presentation:preBuild") {
    dependsOn("installGitHook")
}
