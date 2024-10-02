plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.4.2"
    id("io.micronaut.aot") version "4.4.2"
    id("org.openapi.generator") version "7.8.0"
}

version = "0.1"
group = "com.github.coolcat2310"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    compileOnly("io.micronaut:micronaut-http-client")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("io.micronaut:micronaut-http-client")
}


application {
    mainClass = "com.github.coolcat2310.Application"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

openApiGenerate {
    generatorName.set("java") // Specify the client language, e.g., java, kotlin, etc.
    inputSpec.set("$rootDir/src/docs/SpaceTradersApiSpec.yaml") // Path to your OpenAPI spec file
    outputDir.set("$rootDir/generated") // Output directory for the generated code
    apiPackage.set("com.github.coolcat2310.api") // Package for the API classes
    modelPackage.set("com.github.coolcat2310.model") // Package for the model classes
    invokerPackage.set("com.github.coolcat2310.invoker") // Package for the invoker classes
    configOptions.set(mapOf(
        "dateLibrary" to "java8" // Additional configuration options
    ))
}

graalvmNative.toolchainDetection = false

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.github.coolcat2310.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading = false
        convertYamlToJava = false
        precomputeOperations = true
        cacheEnvironment = true
        optimizeClassLoading = true
        deduceEnvironment = true
        optimizeNetty = true
        replaceLogbackXml = true
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}


