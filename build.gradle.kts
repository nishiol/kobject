import com.palantir.gradle.gitversion.VersionDetails
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm") version "2.1.0"
    id("com.palantir.git-version") version "3.1.0"
    id("com.vanniktech.maven.publish") version "0.30.0"
}

val versionDetails: groovy.lang.Closure<VersionDetails> by extra
val gitVersion = versionDetails().lastTag?.takeIf { it.startsWith("v") }?.substringAfter("v")

group = "ru.nishiol.kobject"
version = gitVersion ?: "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_1_8
        apiVersion = KotlinVersion.KOTLIN_1_8
    }
}

mavenPublishing {
    coordinates(groupId = group.toString(), artifactId = "kobject", version = version.toString())
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    pom {
        name = "KObject"
        description = "KObject implements a dynamic object where fields are defined at runtime."
        url = "https://github.com/nishiol/kobject"
        licenses {
            license {
                name = "MIT License"
                url = "https://github.com/nishiol/kobject/blob/main/LICENSE"
            }
        }
        developers {
            developer {
                id = "nishiol"
                name = "Oleg Shitikov"
                email = "schitikov.ol@gmail.com"
            }
        }
        scm {
            connection = "scm:git:git://github.com/nishiol/kobject.git"
            developerConnection = "scm:git:ssh://github.com/nishiol/kobject.git"
            url = "https://github.com/nishiol/kobject"
        }
    }
}