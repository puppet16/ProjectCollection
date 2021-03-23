import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("org.jetbrains.kotlin.plugin.allopen") version "1.4.21"
    id ("org.jetbrains.kotlin.plugin.noarg") version "1.4.21"
    kotlin("kapt")

//    kotlin("jvm") version "1.4.30" // or kotlin("multiplatform") or any other kotlin plugin
    kotlin("plugin.serialization") version "1.4.30"
}

noArg {
    invokeInitializers = true
    annotations("cn.ltt.projectcollection.kotlin.DataClassAnnotation")
}

allOpen {
    annotations("cn.ltt.projectcollection.kotlin.DataClassAnnotation")
}



android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.1")

    defaultConfig {
        applicationId = "cn.ltt.projectcollection"
        minSdkVersion(16)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation( "androidx.constraintlayout:constraintlayout:2.0.2")
    testImplementation( "junit:junit:4.12")
    androidTestImplementation( "androidx.test.ext:junit:1.1.2")
    androidTestImplementation( "androidx.test.espresso:espresso-core:3.3.0")

    //Gson
    implementation ("com.google.code.gson:gson:2.8.6")
    //EventBus
    implementation ("org.greenrobot:eventbus:3.1.1")

    implementation ("com.jakewharton:butterknife:10.2.3")
    annotationProcessor( "com.jakewharton:butterknife-compiler:10.2.3")
    implementation( "androidx.core:core-ktx:+")
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    implementation("com.squareup.retrofit2:retrofit:2.6.2")
    implementation("com.squareup.retrofit2:converter-gson:2.6.2")


    implementation("com.google.code.gson:gson:2.8.1")

    implementation("com.squareup.moshi:moshi:1.11.0")
    implementation( "com.squareup.moshi:moshi-kotlin:1.11.0") // for KotlinJsonAdapterFactory
    kapt( "com.squareup.moshi:moshi-kotlin-codegen:1.11.0") // for generated Json Adapter

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation( "org.jetbrains.kotlin:kotlin-reflect")
    implementation(project( ":plugin_common"))

}
repositories {
    mavenCentral()
}