val logback_version: String by project
plugins {
    application
    kotlin("jvm") version "1.5.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.10"
}

group = "org.telegram.bot"
version = "0.0.1"
application {
    mainClass.set("org.telegram.bot.ApplicationKt")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("se.akerfeldt:okhttp-signpost:1.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.2.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.2.1")
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.4")
    implementation("com.google.firebase:firebase-admin:7.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
}

tasks.create("stage") {
    dependsOn("installDist")
}
