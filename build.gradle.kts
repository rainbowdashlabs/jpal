plugins {
    java
    `maven-publish`
    `java-library`
    id("de.chojo.publishdata") version "1.0.8"
    id("org.cadixdev.licenser") version "0.6.1"
}

group = "de.chojo"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    api("org.slf4j", "slf4j-api", "1.7.36")
    api("org.jetbrains", "annotations", "23.0.0")
    api("com.google.code.findbugs", "jsr305", "3.0.2")

    // unit testing
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter", "junit-jupiter")
}

license {
    header(rootProject.file("HEADER.txt"))
    include("**/*.java")
}

publishData {
    useEldoNexusRepos(true)
    publishComponent("java")
}

publishing {
    publications.create<MavenPublication>("maven") {
        publishData.configurePublication(this)
    }

    repositories {
        maven {
            authentication {
                credentials(PasswordCredentials::class) {
                    username = System.getenv("NEXUS_USERNAME")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }

            name = "EldoNexus"
            setUrl(publishData.getRepository())
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()

    toolchain{
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    compileJava {
        options.encoding = org.gradle.internal.impldep.org.apache.commons.lang.CharEncoding.UTF_8
    }

    javadoc {
        val options = options as StandardJavadocDocletOptions
        options.encoding = org.gradle.internal.impldep.org.apache.commons.lang.CharEncoding.UTF_8
        options.links(
            "https://javadoc.io/doc/com.google.code.findbugs/jsr305/latest/",
            "https://javadoc.io/doc/org.jetbrains/annotations/latest/",
            "https://docs.oracle.com/en/java/javase/${java.toolchain.languageVersion.get().asInt()}/docs/api/"
        )
    }
}
