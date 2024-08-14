import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.ncorti.ktfmt.gradle.tasks.*

plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.versions)
    alias(libs.plugins.ktfmt) apply true
    cleanup
    base
}

allprojects {
    group = PUBLISHING_GROUP
}

val detektFormatting = libs.detekt.formatting

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    detekt {
        config.from(rootProject.files("config/detekt/detekt.yml"))
    }

    dependencies {
        detektPlugins(detektFormatting)
    }
}

tasks {
    withType<DependencyUpdatesTask>().configureEach {
        rejectVersionIf {
            candidate.version.isStableVersion().not()
        }
    }
}

ktfmt {
    // KotlinLang style - 4 space indentation - From kotlinlang.org/docs/coding-conventions.html
    kotlinLangStyle()
}

tasks.register<KtfmtCheckTask>("ktfmtPrecommitCheck") {
    source = project.fileTree(rootDir)
    include("**/*.kt")
}

tasks.register<KtfmtFormatTask>("ktfmtPrecommitFormat") {
    source = project.fileTree(rootDir)
    include("**/*.kt")
}
