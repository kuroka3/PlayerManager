import org.gradle.configurationcache.extensions.capitalized

plugins {
    idea
    kotlin("jvm") version "1.7.21"
    id("io.papermc.paperweight.userdev") version "1.5.3"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    paperweight.paperDevBundle("1.20-R0.1-SNAPSHOT")

    implementation("io.github.monun:kommand-api:3.1.6")
    implementation("io.github.monun:tap-api:4.9.3")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
}

extra.apply {
    set("pluginName", project.name.split('-').joinToString("") { it.capitalize() })
    set("packageName", project.name.replace("-", ""))
    set("kotlinVersion", "1.7.21")
    set("paperVersion", "1.7.21".split('.').take(2).joinToString("."))
}

tasks {
    // generate plugin.yml
    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
            expand(extra.properties)
        }
    }

    fun registerJar(
        classifier: String,
        source: Any
    ) = register<Copy>("test${classifier.capitalized()}Jar") {
        from(source)

        val prefix = project.name
        val plugins = rootProject.file(".server/plugins-$classifier")
        val update = File(plugins, "update")
        val regex = Regex("($prefix).*(.jar)")

        from(source)
        into(if (plugins.listFiles { _, it -> it.matches(regex) }?.isNotEmpty() == true) update else plugins)

        doLast {
            update.mkdirs()
            File(update, "RELOAD").delete()
        }
    }

    registerJar("dev", jar)
    registerJar("reobf", reobfJar)
}

idea {
    module {
        excludeDirs.add(file(".server"))
    }
}
