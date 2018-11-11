import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

androidExtensions {
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "com.vitusortner.patterns"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
        getByName("androidTest").java.srcDirs("src/androidTest/kotlin")
    }
}

dependencies {
    val lifecycleVersion = "2.0.0"
    val coroutinesVersion = "1.0.0-RC1"
    val moshiVersion = "2.4.0"

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))


    implementation("androidx.appcompat:appcompat:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha2")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    implementation("com.squareup.retrofit2:retrofit:$moshiVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$moshiVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$moshiVersion")

    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    implementation("com.squareup.moshi:moshi-kotlin:1.7.0")
    implementation("com.serjltt.moshi:moshi-lazy-adapters:2.2")

    implementation("com.squareup.picasso:picasso:2.71828")

    testImplementation("junit:junit:4.12")
    testImplementation("androidx.arch.core:core-testing:$lifecycleVersion")
    testImplementation("io.mockk:mockk:1.8.12.kotlin13")
    testImplementation("org.amshove.kluent:kluent-android:1.42")

    androidTestImplementation("androidx.test:runner:1.1.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0")
}
