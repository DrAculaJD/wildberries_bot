plugins {
    id("java")
    id("application")
    id("checkstyle")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation ("com.fasterxml.jackson.core:jackson-core:2.15.0-rc2")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.0.1")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("main.Main")
}
