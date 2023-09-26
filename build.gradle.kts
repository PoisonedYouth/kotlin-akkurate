plugins {
    kotlin("jvm") version "1.9.10"
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

group = "com.poisonedyouth.akkurate"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.arrow-kt:arrow-core-jvm:1.2.1")

    implementation("dev.nesk.akkurate:akkurate-core:0.2.0")
    implementation("dev.nesk.akkurate:akkurate-ksp-plugin:0.2.0")
    ksp("dev.nesk.akkurate:akkurate-ksp-plugin:0.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.7.2")
    implementation("io.kotest.extensions:kotest-assertions-arrow:1.3.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}