import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
//    id("io.ktor.plugin") version "2.3.6"
    application
}

repositories {
    mavenCentral()
}

val ktorVer = "2.3.6"

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVer")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVer")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktorVer")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktorVer")
    implementation("io.ktor:ktor-client-core:$ktorVer")
    implementation("io.ktor:ktor-client-cio:$ktorVer")
    implementation("io.ktor:ktor-gson:1.6.4")
    implementation ("org.jsoup:jsoup:1.16.1")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("org.slf4j:slf4j-simple:1.7.9")
    implementation("redis.clients:jedis:4.3.1")
    testImplementation ("org.mockito:mockito-core:2.7.5")
    testImplementation ("org.mockito:mockito-inline:2.13.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}