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

val testRuntimeClasspath = sourceSets.test.get().runtimeClasspath

tasks.register<JavaExec>("runTestSuite") {
    group = "verification"
    description = "Runs the plain-Java DominionCore test suite."
    dependsOn(tasks.testClasses)
    classpath = testRuntimeClasspath
    mainClass.set("dev.dominioncore.TestSuite")
    isIgnoreExitValue = true

    doLast {
        val result = executionResult.get()
        if (result.exitValue != 0) {
            val report = layout.buildDirectory.file("reports/testsuite.log").get().asFile
            if (report.exists()) {
                println("----- DominionCore TestSuite report (${report.absolutePath}) -----")
                println(report.readText())
                println("----- end TestSuite report -----")
            } else {
                println("TestSuite report file was not created: ${report.absolutePath}")
            }
            throw GradleException("TestSuite failed with exit code ${result.exitValue}. See ${report.absolutePath} for the exact failing test and stacktrace.")
        }
    }
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

tasks.named("test") {
    dependsOn(tasks.named("runTestSuite"))
}

tasks.named("check") {
    dependsOn(tasks.named("runTestSuite"))
}
