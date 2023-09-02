import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.example"
version = "1.0-SNAPSHOT"

val dbUrl = "jdbc:postgresql://localhost:5432/postgres"
val dbUser = "admin"
val dbPassword = "admin_password"
val dbSchema = "my"

val postgresVersion = "42.5.1"
val jooqVersion = "3.15.3"

plugins {
    kotlin("jvm") version "1.9.10"
    application
    id("org.flywaydb.flyway") version "9.11.0"
//    id("nu.studer.jooq") version "6.0.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.jsoup:jsoup:1.16.1")
    implementation("org.flywaydb:flyway-core:9.11.0")
    implementation("org.postgresql:postgresql:$postgresVersion")
//    api("org.jooq:jooq:$jooqVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("org.slf4j:slf4j-simple:1.7.9")
    implementation("com.zaxxer:HikariCP:5.0.1")
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