plugins {
    id("io.github.turansky.kfc.library")
    id("io.github.turansky.kfc.maven-central-publish")
}

kotlin {
    js()
    jvm()
    iosArm32()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
}
