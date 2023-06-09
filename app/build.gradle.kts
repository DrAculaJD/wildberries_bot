plugins {
    id("java")
    id("application")
    id("jacoco")
    id("checkstyle")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.11.1")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation ("com.fasterxml.jackson.core:jackson-core:2.15.0-rc2")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation ("com.github.pengrad:java-telegram-bot-api:6.6.0")
    implementation ("org.telegram:telegrambots:6.7.0")
    implementation ("org.telegram:telegrambots-meta:6.5.0")
    implementation ("org.mariadb.jdbc:mariadb-java-client:2.1.2")
    implementation ("org.postgresql:postgresql:42.6.0")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("com.liferay:com.fasterxml.jackson.databind:2.10.5.1.LIFERAY-PATCHED-1")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("main.Main")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "main.Main"
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}