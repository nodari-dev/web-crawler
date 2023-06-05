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
    kotlin("jvm") version "1.7.21"
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
//    jooqGenerator("org.postgresql:postgresql:$postgresVersion")
//    api("org.jooq:jooq:$jooqVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("org.slf4j:slf4j-simple:1.7.9")
    implementation("com.zaxxer:HikariCP:5.0.1")

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
//
//flyway {
//    url = dbUrl
//    schemas = arrayOf(dbSchema)
//    user = dbUser
//    password = dbPassword
//    baselineVersion = "-1"
//    table = "flyway_schema_history"
//    cleanDisabled = false
//    sqlMigrationPrefix = "V"
//    sqlMigrationSeparator = "__"
//    sqlMigrationSuffixes = arrayOf(".sql")
//    locations = arrayOf("database/migration")
//    outOfOrder = false
//    baselineOnMigrate = true
//}
//jooq {
//    version.set(jooqVersion)
//    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
//
//    configurations {
//        create("main") {
//            generateSchemaSourceOnCompilation.set(true)
//
//            jooqConfiguration.apply {
////                logging = Logging.INFO
//                jdbc.apply {
//                    driver = "org.postgresql.Driver"
//                    url = dbUrl
//                    user = dbUser
//                    password = dbPassword
//                }
//                generator.apply {
//                    name = "org.jooq.codegen.KotlinGenerator"
//                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
//                    database.apply {
//                        name = "org.jooq.meta.postgres.PostgresDatabase"
//                        inputSchema = dbSchema
//                        excludes = "key_vault|flyway_schema_history"
//                    }
//                    generate.apply {
//                        isDeprecated = false
//                        isRecords = true
//                        isRelations = true
//                        isPojos = true
//                        isPojosEqualsAndHashCode = true
//                    }
//                    target.apply {
//                        packageName = "jooq.generated"
//                    }
//                }
//            }
//        }
//    }
//}