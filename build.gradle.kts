import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.20"
    id("fabric-loom") version "1.9-SNAPSHOT"
    id("maven-publish")
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}


repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
    maven("https://maven.parchmentmc.org")

    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }

    maven("https://cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }

    maven { url = uri("https://maven.architectury.dev/") }
    maven { url = uri("https://maven.shedaniel.me/") }
    maven {
        url = uri("https://mvn.devos.one/snapshots/")
    }

    maven("https://maven.ladysnake.org/releases") {
        name = "Ladysnake Mods"
    }
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.layered() {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.21:2024.07.28@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    modImplementation("maven.modrinth:chunky:dPliWter")
    modCompileOnly("maven.modrinth:flashback:0.15.0")

    modImplementation("maven.modrinth:sodium:mc1.21.1-0.6.5-fabric")
    modImplementation("maven.modrinth:iris:1.8.1+1.21.1-fabric")
    //modImplementation(files("libs/DistantHorizons-fabric-2.3.0-a-dev-1.21.1.jar"))

    modImplementation("maven.modrinth:ninja-armor:1.3.0+1.21")
    modImplementation("maven.modrinth:bamboo-combat:1.21-1.0.5")
    modImplementation("maven.modrinth:travelersbackpack:1.21.1-10.1.3")

    val ccaVersion = "6.1.1"

    modImplementation("maven.modrinth:polymorph:AGMyBSJE")
    modImplementation("maven.modrinth:stellaris:1.2.0")
    modApi("curse.maven:reborncore-237903:5776056")
    modApi("curse.maven:techreborn-233564:5776057")
    modApi("dev.architectury:architectury-fabric:13.0.8")
    modApi("teamreborn:energy:4.1.0")
    modApi("me.shedaniel.cloth:cloth-config-fabric:15.0.140")
    modImplementation("org.ladysnake.cardinal-components-api:cardinal-components-base:$ccaVersion")
    modImplementation("org.ladysnake.cardinal-components-api:cardinal-components-block:$ccaVersion")
    modImplementation("org.ladysnake.cardinal-components-api:cardinal-components-entity:$ccaVersion")

    val plModules = listOf("base", "client_events")
    for (module in plModules) {
        modImplementation("io.github.fabricators_of_create.Porting-Lib:$module:3.1.0-beta.47+1.21.1")!!
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version"),
            "loader_version" to project.property("loader_version"),
            "kotlin_loader_version" to project.property("kotlin_loader_version")
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}
