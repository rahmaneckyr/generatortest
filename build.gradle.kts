// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}
tasks.register<Exec>("generateModule") {
    description = "Generate a new module structure"
    group = "custom"

    val moduleName = project.findProperty("moduleName") as? String ?: "Splash"
    val packageName = project.findProperty("packageName") as? String ?: "com.ait.generatortest"
    val commonPackageName = project.findProperty("commonPackageName") as? String ?: ""

    commandLine("bash", "./scripts/generate_module.sh", moduleName, packageName, commonPackageName)
}