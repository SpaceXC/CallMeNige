import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "cn.spacexc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("com.github.ajalt.clikt:clikt:4.4.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.twitter4j:twitter4j-core:4.1.2")
    implementation("com.github.scribejava:scribejava-apis:8.3.3")
    implementation("io.github.redouane59.twitter:twittered:2.23")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "CallMeNigger"
            packageVersion = "1.0.0"
        }
    }
}
