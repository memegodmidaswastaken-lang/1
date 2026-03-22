import org.gradle.api.tasks.testing.Test

plugins {
    java
}

group = "dev.dominioncore"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val localGradleLib = gradle.gradleHomeDir!!.resolve("lib")

dependencies {
    testImplementation(files(
        localGradleLib.resolve("junit-4.13.2.jar"),
        localGradleLib.resolve("hamcrest-core-1.3.jar")
    ))
}

tasks.register<JavaExec>("runTestSuite") {
    group = "verification"
    description = "Runs the plain-Java DominionCore test suite."
    dependsOn(tasks.testClasses)
    classpath = sourceSets.test.get().output + sourceSets.test.get().runtimeClasspath
    mainClass.set("dev.dominioncore.TestSuite")
}

tasks.register<JavaExec>("runPrototypeServer") {
    group = "application"
    description = "Runs the socket-based prototype server for local client/server testing."
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("dev.dominioncore.server.PrototypeServerMain")
    args(
        (findProperty("port") ?: "5050").toString(),
        (findProperty("saveRoot") ?: "runtime/socket-server").toString()
    )
}

tasks.register<JavaExec>("runPrototypeClient") {
    group = "application"
    description = "Runs the socket-based prototype client for local client/server testing."
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("dev.dominioncore.client.PrototypeClientMain")

    val cliArgs = findProperty("clientArgs")
        ?.toString()
        ?.split(Regex("\\s+"))
        ?.filter { it.isNotBlank() }
        ?: listOf("127.0.0.1", "5050", "player-one")
    args(cliArgs)
}

tasks.register<JavaExec>("runPrototypeTryout") {
    group = "application"
    description = "Runs a scripted end-to-end local tryout of the prototype server/client flow."
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("dev.dominioncore.app.PrototypeTryoutMain")
    args(
        (findProperty("playerId") ?: "player-one").toString(),
        (findProperty("saveRoot") ?: "runtime/tryout").toString()
    )
}

tasks.register<JavaExec>("runDominionCoreApp") {
    group = "application"
    description = "Runs the DominionCore app functional checks and launches the UI when a display is available."
    dependsOn(tasks.classes)
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("dev.dominioncore.DominionCoreApp")
}

tasks.named<Test>("test") {
    description = "Runs the JUnit 4 bridge test that executes the custom DominionCore TestSuite."
    useJUnit()
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    setScanForTestClasses(true)
    include("**/*Test.class", "**/*Tests.class", "**/*TestCase.class")
    filter {
        includeTestsMatching("dev.dominioncore.TestSuiteJUnit4Test")
    }
}
