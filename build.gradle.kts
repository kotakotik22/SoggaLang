import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// <:badelephant:877990622190006283>

plugins {
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    application
}

group = "com.kotakotik"
version = "1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("com.kotakotik.sogga.MainKt")
    applicationName = "sogga"
}

//tasks.jar {
//    manifest {
//        attributes(
//            "Main-Class" to "com.kotakotik.sogga.MainKt"
//        )
//    }
//}
//
//tasks.register<Zip>("zip") {
//    dependsOn(tasks.jar)
//    val filename = "sogga${project.version}.zip"
//    delete(files("${buildDir}/libs/${filename}"))
//
//    archiveFileName.set(filename)
//    destinationDirectory.set(layout.buildDirectory.dir("dist"))
//
//    val jarname = "${archiveBaseName.get()}-${project.version}.jar"
//
//    from("${buildDir}/libs/${jarname}")
//    from(layout.projectDirectory.file("sogga.cmd"))
//    filter {
//        return@filter it.replace("%jar%", jarname)
//    }
//}