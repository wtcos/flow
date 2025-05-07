repositories {
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    `java-library`
    // Check for updates with ./gradlew dependencyUpdates
    id("com.github.ben-manes.versions") version "0.46.0"
    id("io.deepmedia.tools.deployer") version "0.16.0"
}


dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("org.slf4j:slf4j-nop:2.0.7")
    testImplementation("org.buildobjects:jproc:2.8.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    java {
        withJavadocJar()
        withSourcesJar()
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

deployer {
    // 1. Artifact definition.
    // https://opensource.deepmedia.io/deployer/artifacts
    content {
        component {
            fromJava()
        }
    }

    // 2. Project details.
    // https://opensource.deepmedia.io/deployer/configuration
    projectInfo {
        version = project.version.toString()
        description.set("Flow is a tool to help programmers visualize and understand their workflow.")
        url.set("https://github.com/wethinkcode/flow")
        scm.fromGithub("wethinkcode", "flow")
        license(MIT)
        developer("wtcos", "opensource@wethinkcode.co.za", "WeThinkCode", "https://wethinkcode.co.za")
        groupId.set("za.co.wethinkcode")
    }

    localSpec {

    }

    // 3. Central Portal configuration.
    // https://opensource.deepmedia.io/deployer/repos/central-portal
    centralPortalSpec {
        signing.key.set(secret("SIGNING_KEY"))
        signing.password.set(secret("SIGNING_PASSPHRASE"))
        auth.user.set(secret("UPLOAD_USERNAME"))
        auth.password.set(secret("UPLOAD_PASSWORD"))
    }
}