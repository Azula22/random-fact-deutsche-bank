
plugins {
    id ("org.jetbrains.kotlin.jvm") version libs.versions.kotlin.version
    id("io.ktor.plugin") version libs.versions.ktor.version
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
    testImplementation("io.ktor:ktor-server-test-host:${libs.versions.ktor.version}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${libs.versions.kotlin.version}")
}
