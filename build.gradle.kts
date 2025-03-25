
plugins {
    id("io.ktor.plugin") version libs.versions.ktor.version
    kotlin("jvm") version libs.versions.kotlin.version
    kotlin("plugin.serialization") version libs.versions.kotlin.version
}

group = "com.random.fact"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:${libs.versions.ktor.version}")
    implementation("io.ktor:ktor-server-netty:${libs.versions.ktor.version}")
    implementation("ch.qos.logback:logback-classic:${libs.versions.logback.version.get()}")
    implementation("io.ktor:ktor-server-config-yaml:${libs.versions.ktor.version}")
    implementation("io.ktor:ktor-server-content-negotiation:${libs.versions.ktor.version}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${libs.versions.ktor.version}")
    implementation("io.ktor:ktor-server-logging:${libs.versions.ktor.version}")

    //client
    implementation("io.ktor:ktor-client-content-negotiation:${libs.versions.ktor.version}")
    implementation("io.ktor:ktor-client-core:${libs.versions.ktor.version}")
    implementation("io.ktor:ktor-client-cio:${libs.versions.ktor.version}")
    implementation("io.ktor:ktor-client-logging:${libs.versions.ktor.version}")

    testImplementation("io.ktor:ktor-server-test-host:${libs.versions.ktor.version}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${libs.versions.kotlin.version}")
}
