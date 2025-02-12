plugins {
    `java-library`
    // Check for updates with ./gradlew dependencyUpdates
    id("com.github.ben-manes.versions") version "0.46.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.9.3")
    implementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("org.slf4j:slf4j-nop:2.0.7")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
